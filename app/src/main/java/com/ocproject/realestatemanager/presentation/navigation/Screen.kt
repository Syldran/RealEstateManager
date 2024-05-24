package com.ocproject.realestatemanager.presentation.navigation

sealed class Screen(val route:String) {
    data object AddPropertyScreen : Screen("add_property_screen")
    data object PropertyDetailScreen : Screen("property_detail_screen")
    data object PropertyListScreen : Screen("property_list_screen")

    fun withArgs(vararg args: Long) : String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/${arg.toInt()}")
            }
        }
    }
}