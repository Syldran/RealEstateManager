package com.ocproject.realestatemanager.ui.scenes.propertylist

import com.openclassrooms.realestatemanager.models.Property

sealed interface PropertyListEvent {
    data class SortProperties(val sortType: SortType) : PropertyListEvent
    data class DeleteProperty(val property: Property) : PropertyListEvent
}
