package com.ocproject.realestatemanager.presentation.scene.map

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.android.gms.maps.model.LatLng
import com.ocproject.realestatemanager.presentation.navigation.Screen

fun NavGraphBuilder.mapOfPropertiesScreen(navController: NavController, currentPosition: LatLng?, globalSnackbarHostState: SnackbarHostState) {
    composable(
        route = Screen.MapOfPropertiesScreen.route,
    ) {

        MapOfProperties(
            currentPosition = currentPosition,
            focusPosition = currentPosition!!,
            globalSnackbarHostState = globalSnackbarHostState
        )

    }
}