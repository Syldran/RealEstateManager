package com.ocproject.realestatemanager.ui

sealed class Screen(val route:String) {
    data object AddProperty : Screen("add_property_screen")
    data object PropertyDetail : Screen("property_details")
    data object PropertiesScreen : Screen("properties_screen")

    fun withArgs(vararg args: Int) : String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}