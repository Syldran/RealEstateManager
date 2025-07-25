package com.ocproject.realestatemanager.presentation.scene.listdetails

import com.ocproject.realestatemanager.core.Filter
import com.ocproject.realestatemanager.core.utils.Range
import com.ocproject.realestatemanager.domain.models.Property

sealed interface ListDetailsEvent {
    data class OnClickPropertyDisplayMode(val map: Boolean) : ListDetailsEvent
    data object DismissFilter : ListDetailsEvent
    data object OpenFilter : ListDetailsEvent
    data class DeleteProperty(val property: Property) : ListDetailsEvent
    data object GetProperties : ListDetailsEvent
    data class GetDetails(val propertyId: Long) : ListDetailsEvent
    data class SetRangeSurface(val rangeSurface: Range<Float>) : ListDetailsEvent
    data class SetRangePrice(val rangePrice: Range<Float>) : ListDetailsEvent
    data class OnSchoolChecked(val value: Boolean) : ListDetailsEvent
    data class OnParkChecked(val value: Boolean) : ListDetailsEvent
    data class OnShopChecked(val value: Boolean) : ListDetailsEvent
    data class OnTransportChecked(val value: Boolean) : ListDetailsEvent
    data class OnAreaCodeChosen(val code: Int?) : ListDetailsEvent
    data class OnDateRangeSelected(val startRange: Long, val endRange: Long) : ListDetailsEvent
    data class OnSoldDateRangeSelected(val startRange: Long, val endRange: Long) : ListDetailsEvent
    data class UpdateSelectedProperty(val property: Property?) : ListDetailsEvent
    data object UpdateSortedProperties : ListDetailsEvent
    data class UpdateProperties(val properties: List<Property>) : ListDetailsEvent
    data class UpdateFilter(val filter: Filter) : ListDetailsEvent
}
