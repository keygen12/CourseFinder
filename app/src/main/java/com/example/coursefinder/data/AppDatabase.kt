package com.example.coursefinder.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Course::class, Review::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun courseDao(): CourseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "coursefinder.db"
                ).build()
                INSTANCE = instance

                // Предзагрузка каталога при первом запуске: если таблица
                // courses пуста, вставляем набор демонстрационных курсов.
                scope.launch(Dispatchers.IO) {
                    val dao = instance.courseDao()
                    if (dao.countCourses() == 0) {
                        dao.insertAll(SeedData.courses)
                    }
                }

                instance
            }
        }
    }
}
