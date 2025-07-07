package com.ocproject.realestatemanager

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.LatLng
import com.ocproject.realestatemanager.presentation.navigation.Screen
import com.ocproject.realestatemanager.presentation.scene.addproperty.addPropertyScreen
import com.ocproject.realestatemanager.core.ui.theme.RealestatemanagerTheme
import com.ocproject.realestatemanager.presentation.scene.cameraScreen.cameraScreen
import com.ocproject.realestatemanager.presentation.scene.funding.fundingScreen
import com.ocproject.realestatemanager.presentation.scene.listdetails.listDetailsScreen
import com.ocproject.realestatemanager.presentation.scene.map.mapOfPropertiesScreen
import org.koin.compose.KoinContext


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun RealEstateManagerApp(
    currentLocation: LatLng?,
    darkTheme: Boolean,
    dynamicColor: Boolean,
) {
    RealestatemanagerTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
    ) {

        val navController = rememberNavController()
        KoinContext {
            NavHost(
                navController = navController,
                startDestination = Screen.ListDetailsScreen.route
            ) {
                listDetailsScreen(navController, currentLocation)
                addPropertyScreen(navController)
                mapOfPropertiesScreen(navController, currentLocation)
                fundingScreen(navController)
                cameraScreen(navController)
            }
        }
    }
}
