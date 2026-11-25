package com.example.coursefinder.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Курс в каталоге CourseFinder.
 * price = 0 означает бесплатный курс.
 * isFavorite хранится прямо в таблице — для учебного объёма проекта
 * отдельная связывающая таблица избранного не требуется.
 */
@Entity(tableName = "courses")
data class Course(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val platform: String,
    val category: String,
    val level: String,
    val language: String,
    val format: String,
    val price: Int,
    val durationHours: Int,
    val description: String,
    val isFavorite: Boolean = false
)
