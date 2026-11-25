package com.example.coursefinder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.coursefinder.data.Review
import com.example.coursefinder.ui.theme.AccentYellow
import com.example.coursefinder.ui.theme.BluePrimary
import com.example.coursefinder.viewmodel.CourseViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen(
    courseId: Long,
    viewModel: CourseViewModel,
    onBack: () -> Unit
) {
    val course = viewModel.courseById(courseId)
    val reviews by viewModel.reviewsFor(courseId).collectAsState()
    val avgRating by viewModel.averageRatingFor(courseId).collectAsState()

    // Состояние формы добавления отзыва
    var showReviewForm by remember { mutableStateOf(false) }
    var reviewAuthor by remember { mutableStateOf("") }
    var reviewComment by remember { mutableStateOf("") }
    var reviewRating by remember { mutableIntStateOf(5) }

    if (course == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Курс не найден")
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = course.title,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleFavorite(course) }) {
                        Icon(
                            imageVector = if (course.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Избранное",
                            tint = if (course.isFavorite) AccentYellow else Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BluePrimary)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Блок с основной информацией
            item {
                CourseInfoCard(
                    course = course,
                    avgRating = avgRating,
                    reviewCount = reviews.size
                )
            }

            // Кнопка добавления отзыва
            item {
                OutlinedButton(
                    onClick = { showReviewForm = !showReviewForm },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(if (showReviewForm) "Скрыть форму" else "Написать отзыв")
                }
            }

            // Форма добавления отзыва
            if (showReviewForm) {
                item {
                    ReviewForm(
                        author = reviewAuthor,
                        comment = reviewComment,
                        rating = reviewRating,
                        onAuthorChange = { reviewAuthor = it },
                        onCommentChange = { reviewComment = it },
                        onRatingChange = { reviewRating = it },
                        onSubmit = {
                            if (reviewComment.isNotBlank()) {
                                viewModel.addReview(
                                    courseId = courseId,
                                    authorName = reviewAuthor,
                                    rating = reviewRating,
                                    comment = reviewComment
                                )
                                reviewAuthor = ""
                                reviewComment = ""
                                reviewRating = 5
                                showReviewForm = false
                            }
                        }
                    )
                }
            }

            // Заголовок секции отзывов
            item {
                Text(
                    text = "Отзывы (${reviews.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            if (reviews.isEmpty()) {
                item {
                    Text(
                        text = "Отзывов пока нет. Будьте первым!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            } else {
                items(reviews, key = { it.id }) { review ->
                    ReviewCard(review = review)
                }
            }
        }
    }
}

@Composable
private fun CourseInfoCard(
    course: com.example.coursefinder.data.Course,
    avgRating: Double?,
    reviewCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Платформа и категория
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PlatformBadge(text = course.platform, color = BluePrimary)
                PlatformBadge(text = course.category, color = MaterialTheme.colorScheme.secondary)
            }

            Spacer(Modifier.height(12.dp))

            // Название
            Text(
                text = course.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            // Средняя оценка
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = null, tint = AccentYellow, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(4.dp))
                if (avgRating != null) {
                    Text(
                        text = "%.1f".format(avgRating),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = " ($reviewCount отзывов)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                } else {
                    Text(
                        text = "Нет оценок",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            // Характеристики в сетке
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                DetailRow(label = "Уровень", value = course.level)
                DetailRow(label = "Язык", value = course.language)
                DetailRow(label = "Формат", value = course.format)
                DetailRow(label = "Продолжительность", value = "${course.durationHours} часов")
                DetailRow(
                    label = "Стоимость",
                    value = if (course.price == 0) "Бесплатно" else "${course.price} ₸",
                    valueColor = if (course.price == 0) Color(0xFF16A34A) else BluePrimary
                )
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            // Описание
            Text(
                text = "Описание",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = course.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String, valueColor: Color = Color.Unspecified) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = if (valueColor == Color.Unspecified) MaterialTheme.colorScheme.onSurface else valueColor
        )
    }
}

@Composable
private fun PlatformBadge(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.labelMedium, color = color)
    }
}

@Composable
private fun ReviewForm(
    author: String,
    comment: String,
    rating: Int,
    onAuthorChange: (String) -> Unit,
    onCommentChange: (String) -> Unit,
    onRatingChange: (Int) -> Unit,
    onSubmit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Оставить отзыв",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = author,
                onValueChange = onAuthorChange,
                label = { Text("Ваше имя (необязательно)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            // Выбор рейтинга звёздами
            Column {
                Text(
                    text = "Оценка: $rating из 5",
                    style = MaterialTheme.typography.labelMedium
                )
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (i in 1..5) {
                        IconButton(
                            onClick = { onRatingChange(i) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = if (i <= rating) Icons.Default.Star else Icons.Outlined.Star,
                                contentDescription = "$i звезд",
                                tint = if (i <= rating) AccentYellow else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = comment,
                onValueChange = onCommentChange,
                label = { Text("Комментарий") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(10.dp)
            )

            Button(
                onClick = onSubmit,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                enabled = comment.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
            ) {
                Text("Опубликовать отзыв")
            }
        }
    }
}

@Composable
private fun ReviewCard(review: Review) {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val dateString = dateFormat.format(Date(review.timestamp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = review.authorName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = dateString,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
            Spacer(Modifier.height(4.dp))
            // Звёзды
            Row {
                repeat(5) { i ->
                    Icon(
                        imageVector = if (i < review.rating) Icons.Default.Star else Icons.Outlined.Star,
                        contentDescription = null,
                        tint = if (i < review.rating) AccentYellow else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(
                text = review.comment,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
            )
        }
    }
}
