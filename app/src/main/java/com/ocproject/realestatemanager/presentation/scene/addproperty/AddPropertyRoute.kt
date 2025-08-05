package com.ocproject.realestatemanager.presentation.scene.addproperty

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ocproject.realestatemanager.presentation.navigation.Screen
//import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


fun NavGraphBuilder.addPropertyScreen(navController: NavController) {
    composable(
        route = Screen.AddPropertyScreen.route + "/{id}",
        arguments = listOf(
            navArgument("id") {
                type = NavType.LongType
            }
        )
    ) { navBackStackEntry ->
        val id = navBackStackEntry.arguments!!.getLong("id")
        AddPropertyScreen(
            viewModel = koinViewModel(parameters = { parametersOf(id) }),
            onNavigateToListDetails = {
                navController.popBackStack()
            },
            onNavigateToCamera = { onPhotoCaptured ->
                navController.navigate(Screen.CameraScreen.route) {
                    // Pass the callback through navigation
                    popUpTo(Screen.CameraScreen.route) { inclusive = true }
                }
            },
            navController = navController
        )
    }
}