package com.ocproject.realestatemanager.ui.scenes.propertylist

import com.ocproject.realestatemanager.models.PropertyWithPictures
import com.openclassrooms.realestatemanager.models.Property


data class PropertyListState (
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val properties: List<PropertyWithPictures> = emptyList(),

        val sortType: SortType = SortType.PRICE
){}

enum class SortType {
        PRICE,
}
