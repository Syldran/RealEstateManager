package com.ocproject.realestatemanager.presentation.scene.listdetails

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.google.android.gms.maps.model.LatLng
import com.ocproject.realestatemanager.presentation.navigation.Screen

fun NavGraphBuilder.listDetailsScreen(navController: NavController, currentPosition: LatLng?){
    composable(route = Screen.ListDetailsScreen.route){

        ListDetails(
            onNavigateToAddPropertyScreen = {
                navController.navigate(Screen.AddPropertyScreen.withArgs(it?:0))
            },
            onNavigateToMapOfProperties = {
                navController.navigate(Screen.MapOfPropertiesScreen.route)
            },
            onNavigateToFundingScreen = {
                navController.navigate(Screen.FundingScreen.route)
            },
            currentPosition = currentPosition,
        )

    }
}