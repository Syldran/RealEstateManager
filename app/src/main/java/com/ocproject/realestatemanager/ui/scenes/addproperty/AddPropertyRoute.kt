package com.ocproject.realestatemanager.ui.scenes.addproperty

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ocproject.realestatemanager.ui.Screen
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf


fun NavGraphBuilder.addPropertyScreen(navController: NavController) {
    composable(
        route = Screen.AddProperty.route+ "/{id}",
        arguments = listOf(
            navArgument("id") {
                type = NavType.IntType
            }
        )
    ) {
            navBackStackEntry ->
        val id = navBackStackEntry.arguments!!.getInt("id")
        AddProperty(
            viewModel = getViewModel(parameters = { parametersOf(id) }),
            onNavigateToPropertiesScreen = {
                navController.navigate(Screen.PropertiesScreen.route)
            }
        )
    }
}