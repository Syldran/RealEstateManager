package com.ocproject.realestatemanager.presentation.scene.cameraScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ocproject.realestatemanager.presentation.navigation.Screen

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.cameraScreen(navController: NavController) {


    composable(route = Screen.CameraScreen.route){
        CameraScreen()
    }
}