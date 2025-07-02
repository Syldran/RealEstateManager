package com.ocproject.realestatemanager.presentation.scene.propertylist

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ocproject.realestatemanager.presentation.navigation.Screen


fun NavGraphBuilder.propertyListScreen(navController: NavController){
    composable(route = Screen.PropertyListScreen.route) {

        PropertyListScreen(
            onClick = {},
        )
    }
}
