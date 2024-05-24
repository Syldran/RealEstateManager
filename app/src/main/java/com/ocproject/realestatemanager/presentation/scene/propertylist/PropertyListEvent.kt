package com.ocproject.realestatemanager.presentation.scene.propertylist

import android.util.Range
import com.ocproject.realestatemanager.models.Filter
import com.ocproject.realestatemanager.models.Property

sealed interface PropertyListEvent {
    data object DismissFilter : PropertyListEvent
    data object OpenFilter : PropertyListEvent
    data class DeleteProperty(val property: Property) : PropertyListEvent
    data class SortProperties(val filter: Filter) : PropertyListEvent
    data class SetRangePrice(val rangePrice: Range<Float>): PropertyListEvent
}

