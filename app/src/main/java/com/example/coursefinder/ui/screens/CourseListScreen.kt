package com.example.coursefinder.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.coursefinder.data.Course
import com.example.coursefinder.ui.theme.AccentYellow
import com.example.coursefinder.ui.theme.BluePrimary
import com.example.coursefinder.viewmodel.ANY_FILTER
import com.example.coursefinder.viewmodel.CourseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseListScreen(
    viewModel: CourseViewModel,
    onCourseClick: (Long) -> Unit,
    onFavoritesClick: () -> Unit
) {
    val filters by viewModel.filters.collectAsState()
    val courses by viewModel.filteredCourses.collectAsState()
    val categories by viewModel.categories.collectAsState()

    var showFilters by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "CourseFinder",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                actions = {
                    IconButton(onClick = { showFilters = !showFilters }) {
                        Icon(
                            Icons.Default.FilterList,
                            contentDescription = "Фильтры",
                            tint = if (showFilters) AccentYellow else Color.White
                        )
                    }
                    IconButton(onClick = onFavoritesClick) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = "Избранное",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BluePrimary)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Поисковая строка
            OutlinedTextField(
                value = filters.query,
                onValueChange = { viewModel.updateQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Поиск курсов...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Панель фильтров (раскрывается)
            AnimatedVisibility(visible = showFilters) {
                FilterPanel(
                    filters = filters,
                    categories = categories,
                    onCategoryChange = { viewModel.updateCategory(it) },
                    onLevelChange = { viewModel.updateLevel(it) },
                    onLanguageChange = { viewModel.updateLanguage(it) },
                    onMaxPriceChange = { viewModel.updateMaxPrice(it) }
                )
            }

            // Счётчик найденных курсов
            Text(
                text = "Найдено: ${courses.size} курсов",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            if (courses.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Курсы не найдены.\nИзмените параметры поиска.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(courses, key = { it.id }) { course ->
                        CourseCard(
                            course = course,
                            onClick = { onCourseClick(course.id) },
                            onToggleFavorite = { viewModel.toggleFavorite(course) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterPanel(
    filters: com.example.coursefinder.viewmodel.FilterState,
    categories: List<String>,
    onCategoryChange: (String) -> Unit,
    onLevelChange: (String) -> Unit,
    onLanguageChange: (String) -> Unit,
    onMaxPriceChange: (Int) -> Unit
) {
    val levels = listOf(ANY_FILTER, "Начальный", "Средний", "Продвинутый")
    val languages = listOf(ANY_FILTER, "Русский", "Английский")
    val prices = listOf(Int.MAX_VALUE to "Любая", 0 to "Бесплатно", 3000 to "до 3 000 ₸", 5000 to "до 5 000 ₸")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Категория
        FilterRow(
            label = "Категория",
            options = listOf(ANY_FILTER) + categories,
            selected = filters.category,
            onSelect = onCategoryChange
        )
        // Уровень
        FilterRow(
            label = "Уровень",
            options = levels,
            selected = filters.level,
            onSelect = onLevelChange
        )
        // Язык
        FilterRow(
            label = "Язык",
            options = languages,
            selected = filters.language,
            onSelect = onLanguageChange
        )
        // Цена
        Text(
            text = "Цена",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(prices) { (value, label) ->
                val isSelected = filters.maxPrice == value
                FilterChip(
                    selected = isSelected,
                    onClick = { onMaxPriceChange(value) },
                    label = { Text(label) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BluePrimary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }
        Divider(modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
private fun FilterRow(
    label: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold
    )
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(options) { option ->
            FilterChip(
                selected = selected == option,
                onClick = { onSelect(option) },
                label = { Text(option) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = BluePrimary,
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}

@Composable
fun CourseCard(
    course: Course,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Платформа и категория
                Column(modifier = Modifier.weight(1f)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        CategoryChip(text = course.platform, color = BluePrimary)
                        CategoryChip(text = course.category, color = MaterialTheme.colorScheme.secondary)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = course.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                // Кнопка избранного
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (course.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (course.isFavorite) "Удалить из избранного" else "В избранное",
                        tint = if (course.isFavorite) Color(0xFFE53935) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Характеристики курса
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MetaItem(label = course.level)
                MetaItem(label = course.language)
                MetaItem(label = course.format)
                MetaItem(label = "${course.durationHours} ч")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Цена
            Text(
                text = if (course.price == 0) "Бесплатно" else "${course.price} ₸",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (course.price == 0) Color(0xFF16A34A) else BluePrimary
            )
        }
    }
}

@Composable
private fun CategoryChip(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = color
        )
    }
}

@Composable
private fun MetaItem(label: String) {
    Text(
        text = label,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
    )
}
