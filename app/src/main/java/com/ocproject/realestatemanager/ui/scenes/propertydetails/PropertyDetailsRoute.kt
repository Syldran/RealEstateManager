package com.ocproject.realestatemanager.ui.scenes.propertydetails

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ocproject.realestatemanager.ui.Screen
import com.ocproject.realestatemanager.ui.scenes.addproperty.AddProperty
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.addPropertyDetailsScreen(navController: NavController) {
    composable(
        route = Screen.PropertyDetail.route + "/{id}",
        arguments = listOf(
            navArgument("id") {
                type = NavType.IntType
            }
        )
    )

    {
            navBackStackEntry ->
        val id = navBackStackEntry.arguments?.getInt("id")
        PropertyDetail(
            viewModel = getViewModel(parameters = { parametersOf(id) }),
            onNavigateToAddPropertyScreen = {
                navController.navigate(Screen.AddProperty.withArgs(it?:0))
            },
        )
    }
}
