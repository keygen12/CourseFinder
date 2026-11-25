package com.example.coursefinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursefinder.data.AppDatabase
import com.example.coursefinder.data.Course
import com.example.coursefinder.data.Review
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Не выбран / "любой" вариант фильтра. */
const val ANY_FILTER = "Любой"

data class FilterState(
    val query: String = "",
    val category: String = ANY_FILTER,
    val level: String = ANY_FILTER,
    val language: String = ANY_FILTER,
    val maxPrice: Int = Int.MAX_VALUE
)

class CourseViewModel(private val db: AppDatabase) : ViewModel() {

    private val dao = db.courseDao()

    private val _filters = MutableStateFlow(FilterState())
    val filters: StateFlow<FilterState> = _filters

    // Полный, неотфильтрованный каталог — единственный источник правды
    // для поиска курса по id (используется на экране деталей), поэтому
    // курс остаётся доступным, даже если он не проходит текущие фильтры
    // или не находится в избранном.
    val allCourses: StateFlow<List<Course>> = dao.getAllCourses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Отфильтрованный список пересчитывается в памяти при каждом
    // изменении каталога или состояния фильтров.
    val filteredCourses: StateFlow<List<Course>> = combine(allCourses, _filters) { courses, f ->
        courses.filter { course ->
            (f.query.isBlank() || course.title.contains(f.query, ignoreCase = true)) &&
                (f.category == ANY_FILTER || course.category == f.category) &&
                (f.level == ANY_FILTER || course.level == f.level) &&
                (f.language == ANY_FILTER || course.language == f.language) &&
                (course.price <= f.maxPrice)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteCourses: StateFlow<List<Course>> = dao.getFavoriteCourses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: StateFlow<List<String>> = allCourses
        .map { list -> list.map { it.category }.distinct().sorted() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateQuery(query: String) {
        _filters.value = _filters.value.copy(query = query)
    }

    fun updateCategory(category: String) {
        _filters.value = _filters.value.copy(category = category)
    }

    fun updateLevel(level: String) {
        _filters.value = _filters.value.copy(level = level)
    }

    fun updateLanguage(language: String) {
        _filters.value = _filters.value.copy(language = language)
    }

    fun updateMaxPrice(maxPrice: Int) {
        _filters.value = _filters.value.copy(maxPrice = maxPrice)
    }

    fun toggleFavorite(course: Course) {
        viewModelScope.launch {
            dao.setFavorite(course.id, !course.isFavorite)
        }
    }

    fun reviewsFor(courseId: Long): StateFlow<List<Review>> =
        dao.getReviewsForCourse(courseId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun averageRatingFor(courseId: Long): StateFlow<Double?> =
        dao.getAverageRating(courseId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun addReview(courseId: Long, authorName: String, rating: Int, comment: String) {
        viewModelScope.launch {
            dao.insertReview(
                Review(
                    courseId = courseId,
                    authorName = authorName.ifBlank { "Аноним" },
                    rating = rating.coerceIn(1, 5),
                    comment = comment.trim(),
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    fun courseById(id: Long): Course? = allCourses.value.find { it.id == id }
}
