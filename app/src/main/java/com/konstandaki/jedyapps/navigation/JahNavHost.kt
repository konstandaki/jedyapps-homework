package com.konstandaki.jedyapps.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.konstandaki.jedyapps.presentation.home.HomeScreen
import com.konstandaki.jedyapps.presentation.details.DetailsScreen

@Composable
fun JahNavHost(nav: androidx.navigation.NavHostController = rememberNavController()) {
    NavHost(navController = nav, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                onOpenDetails = { movie -> nav.navigate(Routes.detailsOf(movie)) }
            )
        }
        composable(
            route = Routes.DETAILS,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType },
                navArgument("year") { type = NavType.StringType },
                navArgument("type") { type = NavType.StringType },
                navArgument("poster") { type = NavType.StringType; defaultValue = "" }
            )
        ) {
            DetailsScreen(onBack = { nav.popBackStack() })
        }
    }
}