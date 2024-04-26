package com.ocproject.realestatemanager.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ocproject.realestatemanager.ui.scenes.propertydetails.PropertyDetail
import com.ocproject.realestatemanager.ui.scenes.addproperty.addPropertyScreen
import com.ocproject.realestatemanager.ui.scenes.propertydetails.PropertyDetailsViewModel
import com.ocproject.realestatemanager.ui.scenes.propertydetails.addPropertyDetailsScreen
import com.ocproject.realestatemanager.ui.scenes.propertylist.addPropertyListScreen
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf


@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.PropertiesScreen.route) {
        /*    composable(route = Screen.PropertiesScreen.route) {
                PropertiesScreen(
                    navController = navController,
                    state = state,
                    onEvent = viewModel::onEvent
                )
            }*/
        addPropertyListScreen(navController)

        addPropertyScreen(navController)

        addPropertyDetailsScreen(navController)
//        composable(
//            route = Screen.PropertyDetail.route + "/{id}",
//            arguments = listOf(
//                navArgument("id") {
//                    type = NavType.IntType
//                }
//            )
//        ) { navBackStackEntry ->
//            val id = navBackStackEntry.arguments!!.getInt("id")
//            PropertyDetail(
//                viewModel = getViewModel(parameters = { parametersOf(id)})
//            )
//        }
    }
}



