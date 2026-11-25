package com.example.coursefinder.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.coursefinder.ui.screens.CourseDetailScreen
import com.example.coursefinder.ui.screens.CourseListScreen
import com.example.coursefinder.ui.screens.FavoritesScreen
import com.example.coursefinder.viewmodel.CourseViewModel

sealed class Screen(val route: String) {
    object List : Screen("list")
    object Favorites : Screen("favorites")
    object Detail : Screen("detail/{courseId}") {
        fun withId(id: Long) = "detail/$id"
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModel: CourseViewModel
) {
    NavHost(navController = navController, startDestination = Screen.List.route) {
        composable(Screen.List.route) {
            CourseListScreen(
                viewModel = viewModel,
                onCourseClick = { courseId ->
                    navController.navigate(Screen.Detail.withId(courseId))
                },
                onFavoritesClick = {
                    navController.navigate(Screen.Favorites.route)
                }
            )
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(
                viewModel = viewModel,
                onCourseClick = { courseId ->
                    navController.navigate(Screen.Detail.withId(courseId))
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("courseId") { type = NavType.LongType })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getLong("courseId") ?: return@composable
            CourseDetailScreen(
                courseId = courseId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
