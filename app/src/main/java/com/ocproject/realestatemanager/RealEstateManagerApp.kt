package com.ocproject.realestatemanager

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ocproject.realestatemanager.presentation.navigation.Screen
import com.ocproject.realestatemanager.presentation.scene.addproperty.addPropertyScreen
import com.ocproject.realestatemanager.presentation.scene.detailproperty.addPropertyDetailsScreen
import com.ocproject.realestatemanager.presentation.scene.propertylist.addPropertyListScreen
import com.ocproject.realestatemanager.ui.theme.RealestatemanagerTheme
import org.koin.compose.KoinContext

@Composable
fun RealEstateManagerApp(
    darkTheme: Boolean,
    dynamicColor: Boolean,
) {
    RealestatemanagerTheme (
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
    ){
        val navController = rememberNavController()
        KoinContext {
            NavHost(
                navController = navController,
                startDestination = Screen.PropertyListScreen.route
            ){
                addPropertyListScreen(navController)
                addPropertyScreen(navController)
                addPropertyDetailsScreen(navController)
            }
        }
    }
}