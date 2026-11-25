package com.example.coursefinder.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Отзыв пользователя о курсе. Связан с courses.id внешним ключом
 * с каскадным удалением: при удалении курса удаляются и его отзывы.
 */
@Entity(
    tableName = "reviews",
    foreignKeys = [
        ForeignKey(
            entity = Course::class,
            parentColumns = ["id"],
            childColumns = ["courseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("courseId")]
)
data class Review(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val courseId: Long,
    val authorName: String,
    val rating: Int, // 1..5
    val comment: String,
    val timestamp: Long
)
