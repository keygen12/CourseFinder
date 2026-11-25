package com.example.coursefinder.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {

    // Поток всех курсов. Поиск и фильтрация выполняются в ViewModel
    // в оперативной памяти: каталог учебного проекта небольшой (десятки
    // записей), поэтому отдельные параметризованные SQL-запросы под
    // каждую комбинацию фильтров не нужны.
    @Query("SELECT * FROM courses ORDER BY title ASC")
    fun getAllCourses(): Flow<List<Course>>

    @Query("SELECT * FROM courses WHERE isFavorite = 1 ORDER BY title ASC")
    fun getFavoriteCourses(): Flow<List<Course>>

    @Query("SELECT COUNT(*) FROM courses")
    suspend fun countCourses(): Int

    @Insert
    suspend fun insertAll(courses: List<Course>)

    @Query("UPDATE courses SET isFavorite = :isFavorite WHERE id = :courseId")
    suspend fun setFavorite(courseId: Long, isFavorite: Boolean)

    @Query("SELECT * FROM reviews WHERE courseId = :courseId ORDER BY timestamp DESC")
    fun getReviewsForCourse(courseId: Long): Flow<List<Review>>

    @Query("SELECT AVG(rating) FROM reviews WHERE courseId = :courseId")
    fun getAverageRating(courseId: Long): Flow<Double?>

    @Insert
    suspend fun insertReview(review: Review)
}
