package com.konstandaki.jedyapps.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.konstandaki.jedyapps.presentation.details.DetailsScreen
import com.konstandaki.jedyapps.presentation.home.HomeScreen

object Routes {
    const val HOME = "home"
    const val DETAILS = "details/{id}"
    fun details(id: String) = "details/$id"
}

@Composable
fun JahNavHost() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                onOpenDetails = { id -> nav.navigate(Routes.details(id)) }
            )
        }
        composable(
            route = Routes.DETAILS,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id").orEmpty()
            DetailsScreen(
                id = id,
                onBack = { nav.popBackStack() }
            )
        }
    }
}