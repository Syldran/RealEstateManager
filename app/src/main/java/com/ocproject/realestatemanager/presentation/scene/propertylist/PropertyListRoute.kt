package com.ocproject.realestatemanager.presentation.scene.propertylist

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ocproject.realestatemanager.presentation.navigation.Screen


fun NavGraphBuilder.addPropertyListScreen(navController: NavController){
    composable(route = Screen.PropertyListScreen.route) {
        val navOptions = NavOptions.Builder()
            .setRestoreState(true)
            .setPopUpTo(Screen.PropertyListScreen.route, inclusive = false, saveState = true)
            .build()

        PropertyListScreen(
            onNavigateToAddPropertyScreen = {
                navController.navigate(Screen.AddPropertyScreen.withArgs(it?:0))
            },
            onNavigateToPropertyDetailScreen = {
                navController.navigate(Screen.PropertyDetailScreen.withArgs(it), navOptions)
            }
        )
    }
}
