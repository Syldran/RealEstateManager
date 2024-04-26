package com.ocproject.realestatemanager.ui.scenes.propertylist

import com.ocproject.realestatemanager.models.PropertyWithPictures


data class PropertyListState (
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val properties: List<PropertyWithPictures> = emptyList(),

        val sortType: SortType = SortType.PRICE
){}

enum class SortType {
        PRICE,
}
