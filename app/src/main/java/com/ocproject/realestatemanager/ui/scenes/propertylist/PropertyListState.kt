package com.ocproject.realestatemanager.ui.scenes.propertylist

import com.ocproject.realestatemanager.models.PropertyWithPictures


data class PropertyListState (
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val properties: List<PropertyWithPictures> = emptyList(),
        val sortType: SortType = SortType.PRICEASC,
        val openFilterState : Boolean = false
){}

enum class SortType {
        PRICEASC,
        PRICEDESC,
        PRICERANGE,
        TAGS,
}
