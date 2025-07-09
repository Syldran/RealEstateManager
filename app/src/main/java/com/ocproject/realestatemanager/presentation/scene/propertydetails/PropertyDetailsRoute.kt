package com.ocproject.realestatemanager.presentation.scene.propertydetails

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.presentation.navigation.Screen
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

//fun NavGraphBuilder.propertyDetailsScreen(navController: NavController) {
//    composable(
//        route = Screen.PropertyDetailScreen.route + "/{id}",
//        arguments = listOf(
//            navArgument("id") {
//                type = NavType.LongType
//            }
//        )
//    ) { navBackStackEntry ->
//        val id = navBackStackEntry.arguments?.getLong("id")
//        PropertyDetailScreen(
//            property1 = Property(
//                null,
//                emptyList(),
//                "aaa",
//                "aaa",
//                0.0,
//                0.0,
//                "A",
//                0L,
//                12,
//                12,
//                12,
//                null,
//                0L
//            ),
//            navigateBack = {},
////            viewModel = koinViewModel(parameters = { parametersOf(id) }),
//            onNavigateToAddPropertyScreen = {
//                navController.navigate(Screen.AddPropertyScreen.withArgs(it ?: 0))
//            },
////            onNavigateToPropertyListScreen = {
////                navController.popBackStack()
////            }
//        )
//    }
//}
    //fun NavGraphBuilder.propertyDetailsScreen(navController: NavController) {
    //    composable(
    //        route = Screen.PropertyDetailScreen.route + "/{id}",
    //        arguments = listOf(
    //            navArgument("id") {
    //                type = NavType.LongType
    //            }
    //        )
    //    ) { navBackStackEntry ->
    //        val id = navBackStackEntry.arguments?.getLong("id")
    //        val viewModel = koinViewModel<PropertyDetailsViewModel>(parameters = { parametersOf(id) })
    //
    //        PropertyDetailScreen(
    //            viewModel = viewModel,
    //            propertyId = id ?: 0L,
    //            navigateBack = {
    //                navController.popBackStack()
    //            },
    //            onNavigateToAddPropertyScreen = {
    //                navController.navigate(Screen.AddPropertyScreen.withArgs(it ?: 0))
    //            }
    //        )
    //    }
    //}