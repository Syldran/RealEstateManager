package com.ocproject.realestatemanager.presentation.scene.propertylist

import android.util.Range
import com.ocproject.realestatemanager.models.Order
import com.ocproject.realestatemanager.models.PropertyWithPhotos
import com.ocproject.realestatemanager.models.SortType

data class PropertyListState(
    val properties: List<PropertyWithPhotos> = emptyList(),
    val isFilterSheetOpen: Boolean = false,
    val sortType: SortType = SortType.PRICE,
    val orderPrice: Order = Order.ASC,
    val orderDate: Order = Order.ASC,
    val rangePrice: Range<Int> = Range<Int>(0, Int.MAX_VALUE),
    val rangeDate: Range<Long> = Range<Long>(0L, Long.MAX_VALUE),
    val maxPrice: Int = Int.MAX_VALUE
)