package com.example.coursefinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.coursefinder.data.AppDatabase
import com.example.coursefinder.ui.AppNavHost
import com.example.coursefinder.ui.theme.CourseFinderTheme
import com.example.coursefinder.viewmodel.CourseViewModel
import kotlinx.coroutines.MainScope

/**
 * Единственная точка входа приложения (single-activity архитектура,
 * типичная для проектов на Jetpack Compose). Вся навигация между
 * экранами (список → детали, список → избранное) реализована поверх
 * Navigation Compose внутри AppNavHost, без создания новых Activity.
 */
class MainActivity : ComponentActivity() {

    // Скоуп уровня приложения для фоновой инициализации базы данных
    // (создание таблиц и предзагрузка каталога курсов).
    private val appScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getInstance(applicationContext, appScope)
        val viewModelFactory = CourseViewModelFactory(database)

        setContent {
            CourseFinderTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val viewModel: CourseViewModel = viewModel(factory = viewModelFactory)
                    val navController = rememberNavController()
                    AppNavHost(navController = navController, viewModel = viewModel)
                }
            }
        }
    }
}

/**
 * CourseViewModel принимает AppDatabase через конструктор, поэтому
 * для его создания нужна собственная фабрика — стандартный Android ViewModel
 * по умолчанию умеет создавать только классы с конструктором без параметров.
 */
class CourseViewModelFactory(
    private val database: AppDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CourseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CourseViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
