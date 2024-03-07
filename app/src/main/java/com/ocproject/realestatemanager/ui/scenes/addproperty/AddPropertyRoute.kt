package com.ocproject.realestatemanager.ui.scenes.addproperty

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ocproject.realestatemanager.ui.Screen


fun NavGraphBuilder.addPropertyScreen(navController: NavController) {
    composable(route = Screen.AddProperty.route) {
        AddProperty(
            onNavigateToPropertiesScreen = {
                navController.navigate(Screen.PropertiesScreen.route)
            }
        )
    }
}