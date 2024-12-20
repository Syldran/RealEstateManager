package com.ocproject.realestatemanager.presentation.scene.propertylist

import android.util.Range
import com.ocproject.realestatemanager.core.Filter
import com.ocproject.realestatemanager.domain.models.Property


sealed interface PropertyListEvent {
    data object DismissFilter : PropertyListEvent
    data object OpenFilter : PropertyListEvent
    data class DeleteProperty(val property: Property) : PropertyListEvent
    data class SortProperties(val filter: Filter) : PropertyListEvent
    //    data class UpdateFilters(): PropertyListEvent
    data class SetRangePrice(val rangePrice: Range<Float>) : PropertyListEvent
    data class OnSchoolChecked(val value: Boolean) : PropertyListEvent
    data class OnParkChecked(val value: Boolean) : PropertyListEvent
    data class OnShopChecked(val value: Boolean) : PropertyListEvent
    data class OnTransportChecked(val value: Boolean) : PropertyListEvent
}