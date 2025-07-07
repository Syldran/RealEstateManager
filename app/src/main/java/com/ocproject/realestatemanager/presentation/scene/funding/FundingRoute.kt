package com.ocproject.realestatemanager.presentation.scene.funding

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.android.gms.maps.model.LatLng
import com.ocproject.realestatemanager.presentation.navigation.Screen
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetails

fun NavGraphBuilder.fundingScreen(navController: NavController){
    composable(route = Screen.FundingScreen.route){
        FundingScreen(
        )
    }
}