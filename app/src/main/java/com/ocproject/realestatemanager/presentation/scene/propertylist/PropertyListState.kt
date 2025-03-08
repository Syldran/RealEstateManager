package com.ocproject.realestatemanager.presentation.scene.propertylist


import android.view.Surface
import com.ocproject.realestatemanager.core.Order
import com.ocproject.realestatemanager.core.SellingStatus
import com.ocproject.realestatemanager.core.SortType
import com.ocproject.realestatemanager.core.utils.Range
import com.ocproject.realestatemanager.domain.models.Property

data class PropertyListState(
    val properties: List<Property> = emptyList(),
    val isLoadingProgressBar: Boolean = false,
    val isError: Boolean = false,
    val isFilterSheetOpen: Boolean = false,
    val sortType: SortType = SortType.PRICE,
    val orderPrice: Order = Order.ASC,
    val orderDate: Order = Order.ASC,
    val orderSurface: Order = Order.ASC,
    val rangePrice: Range<Int> = Range<Int>(0, Int.MAX_VALUE),
    val rangeDate: Range<Long> = Range<Long>(0L, Long.MAX_VALUE),
    val rangeSurface: Range<Int> = Range<Int>(0, Int.MAX_VALUE),
    val maxPrice: Int = Int.MAX_VALUE,
    val maxSurface: Int = Int.MAX_VALUE,
    val soldState: SellingStatus = SellingStatus.PURCHASABLE,
    val schoolState: Boolean = false,
    val transportState: Boolean = false,
    val parkState: Boolean = false,
    val shopState: Boolean = false,
)