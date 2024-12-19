package com.ocproject.realestatemanager.presentation.scene.propertylist

import android.util.Range
import com.ocproject.realestatemanager.domain.models.Order
import com.ocproject.realestatemanager.domain.models.PropertyWithPhotos
import com.ocproject.realestatemanager.domain.models.SellingStatus
import com.ocproject.realestatemanager.domain.models.SortType

data class PropertyListState(
    val properties: List<PropertyWithPhotos> = emptyList(),
    val isLoadingProgressBar: Boolean = false,
    val isError: Boolean = false,
    val isFilterSheetOpen: Boolean = false,
    val sortType: SortType = SortType.PRICE,
    val orderPrice: Order = Order.ASC,
    val orderDate: Order = Order.ASC,
    val rangePrice: Range<Int> = Range<Int>(0, Int.MAX_VALUE),
    val rangeDate: Range<Long> = Range<Long>(0L, Long.MAX_VALUE),
    val maxPrice: Int = Int.MAX_VALUE,
    val soldState: SellingStatus = SellingStatus.PURCHASABLE,
    val schoolState: Boolean = false,
    val transportState: Boolean = false,
    val parkState: Boolean = false,
    val shopState: Boolean = false,
)