package com.ocproject.realestatemanager.presentation.scene.cameraScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ocproject.realestatemanager.presentation.navigation.Screen

fun NavGraphBuilder.cameraScreen(
    navController: NavController
) {
    composable(route = Screen.CameraScreen.route){
        CameraScreen(
            onPhotoCaptured = { photoBytes ->
                // Navigate back to add property screen with the photo
                navController.previousBackStackEntry?.savedStateHandle?.set("photo_captured", photoBytes)
                navController.popBackStack()
            },
            navigateBack = {
                navController.popBackStack()
            }
        )
    }
}