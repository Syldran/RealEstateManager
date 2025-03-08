package com.ocproject.realestatemanager.presentation.scene.listdetails

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ocproject.realestatemanager.presentation.navigation.Screen

fun NavGraphBuilder.listDetails(navController: NavController){
    composable(route = Screen.ListDetails.route){
        val navOptions = NavOptions.Builder()
            .setRestoreState(true)
            .setPopUpTo(Screen.ListDetails.route, inclusive = false, saveState = true)
            .build()

        ListDetails(
            onNavigateToAddPropertyScreen = {
                navController.navigate(Screen.AddPropertyScreen.withArgs(it?:0))
            },
        )

    }
}