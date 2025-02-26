package com.ocproject.realestatemanager.presentation.scene.propertydetails

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.presentation.navigation.Screen
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.addPropertyDetailsScreen(navController: NavController) {
    composable(
        route = Screen.PropertyDetailScreen.route + "/{id}",
        arguments = listOf(
            navArgument("id") {
                type = NavType.LongType
            }
        )
    ) { navBackStackEntry ->
        val id = navBackStackEntry.arguments?.getLong("id")
        PropertyDetailScreen(
            propertyId = 0,
            navigateBack = {},
//            viewModel = koinViewModel(parameters = { parametersOf(id) }),
//            onNavigateToAddPropertyScreen = {
//                navController.navigate(Screen.AddPropertyScreen.withArgs(it ?: 0))
//            },
//            onNavigateToPropertyListScreen = {
//                navController.popBackStack()
//            }
        )
    }
}
