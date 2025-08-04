package com.ocproject.realestatemanager.presentation.scene.listdetails

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.google.android.gms.maps.model.LatLng
import com.ocproject.realestatemanager.presentation.navigation.Screen

fun NavGraphBuilder.listDetailsScreen(navController: NavController, currentPosition: LatLng?, globalSnackbarHostState: SnackbarHostState){
    composable(route = Screen.ListDetailsScreen.route){
        val navOptions = NavOptions.Builder()
            .setRestoreState(true)
            .build()

        ListDetails(
            onNavigateToAddPropertyScreen = {
                navController.navigate(Screen.AddPropertyScreen.withArgs(it?:0), navOptions)
            },
            onNavigateToMapOfProperties = {
                navController.navigate(Screen.MapOfPropertiesScreen.route)
            },
            onNavigateToFundingScreen = {
                navController.navigate(Screen.FundingScreen.route)
            },
            currentPosition = currentPosition,
            globalSnackbarHostState = globalSnackbarHostState
        )

    }
}