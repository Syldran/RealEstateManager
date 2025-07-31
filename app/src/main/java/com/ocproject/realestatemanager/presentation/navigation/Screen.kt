package com.ocproject.realestatemanager.presentation.navigation

sealed class Screen(val route:String) {
    data object AddPropertyScreen : Screen("add_property_screen")
    data object ListDetailsScreen: Screen("list_details_screen")
    data object MapOfPropertiesScreen : Screen("map_of_properties")
    data object FundingScreen: Screen("funding_screen")
    data object CameraScreen: Screen("camera_screen")

    fun withArgs(vararg args: Long) : String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/${arg.toInt()}")
            }
        }
    }
}