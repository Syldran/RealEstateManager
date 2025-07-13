package com.ocproject.realestatemanager.presentation.scene.listdetails

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.ocproject.realestatemanager.core.Filter
import com.ocproject.realestatemanager.core.InterestPoint
import com.ocproject.realestatemanager.core.Order
import com.ocproject.realestatemanager.core.SellingStatus
import com.ocproject.realestatemanager.core.SortType
import com.ocproject.realestatemanager.core.utils.Range
import com.ocproject.realestatemanager.domain.models.Property
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Calendar

data class ListDetailsState(
    val mapMode: Boolean = false,
    val properties: List<Property> = emptyList<Property>(),
    val sortedProperties: List<Property> = emptyList<Property>(),
    val selectedProperty: Property? = null,
    val selectedTags: MutableState<List<InterestPoint>> = mutableStateOf(listOf<InterestPoint>()),
    val isLoadingProgressBar: Boolean = false,
    val isError: Boolean = false,
    val isFilterSheetOpen: Boolean = false,
    val sortType: SortType = SortType.PRICE,
    val orderPrice: Order = Order.ASC,
    val orderDate: Order = Order.ASC,
    val orderSurface: Order = Order.ASC,
    val rangePrice: Range<Int> = Range<Int>(0, Int.MAX_VALUE),
    val rangeDate: Range<Long> = Range<Long>(0L, Calendar.getInstance().timeInMillis),
    val soldRangeDate: Range<Long> = Range<Long>(0L, Calendar.getInstance().timeInMillis),
    val rangeSurface: Range<Int> = Range<Int>(0, Int.MAX_VALUE),
    val maxPrice: Int = Int.MAX_VALUE,
    val maxSurface: Int = Int.MAX_VALUE,
    val soldStatus: SellingStatus = SellingStatus.ALL,
    val schoolTag: Boolean = false,
    val transportTag: Boolean = false,
    val parkTag: Boolean = false,
    val shopTag: Boolean = false,
    val areaCodeList: List<Int> = emptyList(),
    val chosenAreaCode: Int? = null,
)