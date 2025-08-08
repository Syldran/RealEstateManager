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
import java.util.Calendar

data class ListDetailsState(
    val mapMode: Boolean = false,
    val properties: List<Property> = emptyList(),
    val selectedProperty: Property? = null,
    val isLoadingProgressBar: Boolean = false,
    val isError: Boolean = false,
    val isFilterSheetOpen: Boolean = false,
    val filterState: Filter = Filter(
        sortType = SortType.PRICE,
        priceOrder = Order.ASC,
        dateOrder = Order.ASC,
        surfaceOrder = Order.ASC,
        priceRange = Range(0, Int.MAX_VALUE),
        dateRange = Range(0L, Calendar.getInstance().timeInMillis + 12583060),
        soldDateRange = Range(0L, Calendar.getInstance().timeInMillis + 12583060),
        surfaceRange = Range(0, Int.MAX_VALUE),
        sellingStatus = SellingStatus.ALL,
        tagSchool = false,
        tagPark = false,
        tagShop = false,
        tagTransport = false,
        areaCodeFilter = null,
        typeHousing = null,
        minNbrPhotos = 0,
    ),
    val maxPrice: Int = 0,
    val maxSurface: Int = 0,
    val areaCodeList: List<Int> = emptyList(),
)