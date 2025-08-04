package com.ocproject.realestatemanager

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
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
import com.ocproject.realestatemanager.presentation.scene.map.mapOfPropertiesScreen


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun RealEstateManagerApp(
    currentLocation: LatLng?,
    darkTheme: Boolean,
    dynamicColor: Boolean,
) {
    RealEstateManagerTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
    ) {
        val navController = rememberNavController()
        val snackBarHostState = remember { SnackbarHostState() }
        
        // Global snackBar handling
//        val successMessage = stringResource(R.string.property_saved_successfully)
//        val failureMessage = stringResource(R.string.property_save_failed)
//        val photoSuccessMsg = stringResource(R.string.photo_captured_successfully)
//        val photoFailureMsg = stringResource(R.string.capture_failed)
        val snackBarState by GlobalSnackBarManager.snackBarState.collectAsState()
        
        LaunchedEffect(snackBarState) {
            if (snackBarState.isVisible && snackBarState.message != null) {
                snackBarHostState.showSnackbar(
                    message = snackBarState.message!!,
                    duration = SnackbarDuration.Short
                )
            }
        }

        // Main content with SnackBarHost
        androidx.compose.material3.Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Screen.ListDetailsScreen.route
            ) {
                val padding = paddingValues
                listDetailsScreen(navController, currentLocation, snackBarHostState)
                addPropertyScreen(navController)
                mapOfPropertiesScreen(navController, currentLocation, snackBarHostState)
                fundingScreen(navController)
                cameraScreen(navController, snackBarHostState)
            }
        }
    }
}
