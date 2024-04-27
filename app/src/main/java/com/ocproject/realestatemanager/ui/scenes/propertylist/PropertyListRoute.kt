package com.ocproject.realestatemanager.ui.scenes.propertylist

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ocproject.realestatemanager.ui.Screen

fun NavGraphBuilder.addPropertyListScreen(navController: NavController){
    composable(route = Screen.PropertiesScreen.route) {
        PropertyList(
            onNavigateToAddPropertyScreen = {
                navController.navigate(Screen.AddProperty.withArgs(it?:0))
            },
            onNavigateToDetailsPropertyScreen = {
                navController.navigate(Screen.PropertyDetail.withArgs(it))
            }
        )

    }
}
