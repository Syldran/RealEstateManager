package com.ocproject.realestatemanager.presentation.scene.propertylist

import com.ocproject.realestatemanager.core.Filter
import com.ocproject.realestatemanager.core.utils.Range
import com.ocproject.realestatemanager.domain.models.Property


//sealed interface PropertyListEvent {
//    data object DismissFilter : PropertyListEvent
//    data object OpenFilter : PropertyListEvent
//    data class DeleteDetails(val property: Property) : PropertyListEvent
//    data class GetProperties(val filter: Filter) : PropertyListEvent
//    data class SetRangeSurface(val rangeSurface: Range<Float>) : PropertyListEvent
//    data class SetRangePrice(val rangePrice: Range<Float>) : PropertyListEvent
//    data class OnSchoolChecked(val value: Boolean) : PropertyListEvent
//    data class OnParkChecked(val value: Boolean) : PropertyListEvent
//    data class OnShopChecked(val value: Boolean) : PropertyListEvent
//    data class OnTransportChecked(val value: Boolean) : PropertyListEvent
//}