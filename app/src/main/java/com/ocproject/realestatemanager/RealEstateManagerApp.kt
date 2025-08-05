package com.ocproject.realestatemanager

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.LatLng
import com.ocproject.realestatemanager.core.GlobalSnackBarManager
import com.ocproject.realestatemanager.core.ui.theme.RealEstateManagerTheme
import com.ocproject.realestatemanager.presentation.navigation.Screen
import com.ocproject.realestatemanager.presentation.scene.addproperty.addPropertyScreen
import com.ocproject.realestatemanager.presentation.scene.cameraScreen.cameraScreen
import com.ocproject.realestatemanager.presentation.scene.funding.fundingScreen
import com.ocproject.realestatemanager.presentation.scene.listdetails.listDetailsScreen
import com.ocproject.realestatemanager.core.utils.AndroidVersionUtils

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun RealEstateManagerApp(
    currentLocation: LatLng?,
    darkTheme: Boolean,
    dynamicColor: Boolean,
) {
    // Check if Material 3 Adaptive is supported
    if (!AndroidVersionUtils.supportsMaterial3Adaptive()) {
        // Fallback for older Android versions
        RealEstateManagerTheme(
            darkTheme = darkTheme,
            dynamicColor = false, // Disable dynamic color for older versions
        ) {
            val navController = rememberNavController()
            val snackBarHostState = remember { SnackbarHostState() }
            val snackBarState by GlobalSnackBarManager.snackBarState.collectAsState()
            
            LaunchedEffect(snackBarState) {
                if (snackBarState.isVisible && snackBarState.message != null) {
                    snackBarHostState.showSnackbar(
                        message = snackBarState.message!!,
                        duration = SnackbarDuration.Short
                    )
                    GlobalSnackBarManager.hideToast()
                }
            }

            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
            ) { paddingValues ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.ListDetailsScreen.route
                ) {
                    listDetailsScreen(navController, currentLocation, snackBarHostState)
                    addPropertyScreen(navController)
                    fundingScreen(navController)
                    cameraScreen(navController)
                }
            }
        }
        return
    }
    RealEstateManagerTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
    ) {
        val navController = rememberNavController()
        val snackBarHostState = remember { SnackbarHostState() }
        val snackBarState by GlobalSnackBarManager.snackBarState.collectAsState()
        
        LaunchedEffect(snackBarState) {
            if (snackBarState.isVisible && snackBarState.message != null) {
                snackBarHostState.showSnackbar(
                    message = snackBarState.message!!,
                    duration = SnackbarDuration.Short
                )
                // Reset the global snackbar state after showing it
                GlobalSnackBarManager.hideToast()
            }
        }

        // Main content with SnackBarHost
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Screen.ListDetailsScreen.route
            ) {
                val padding = paddingValues
                listDetailsScreen(navController, currentLocation, snackBarHostState)
                addPropertyScreen(navController)
                fundingScreen(navController)
                cameraScreen(navController)
            }
        }
    }
}
