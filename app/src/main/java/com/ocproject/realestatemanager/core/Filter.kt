package com.ocproject.realestatemanager.core

import com.ocproject.realestatemanager.core.utils.Range


data class Filter(
    val sortType: SortType,
    val orderPrice: Order,
    val orderDate: Order,
    val orderSurface: Order,
    val rangePrice: Range<Int>,
    val rangeDate: Range<Long>,
    val soldRangeDate: Range<Long>,
    val rangeSurface: Range<Int>,
    val sellingStatus: SellingStatus,
    val tagSchool: Boolean,
    val tagTransport: Boolean,
    val tagShop: Boolean,
    val tagPark: Boolean,
    val areaCodeFilter: Int?,
    val minNbrPhotos: Int,
)

enum class SellingStatus {
    ALL,
    PURCHASABLE,
    SOLD,
}

enum class SortType {
    PRICE,
    DATE,
    AREA,
//    TAGS,
//    TOWN,
}

enum class Order {
    ASC,
    DESC,
}

enum class InterestPoint {
    SCHOOL,
    PARK,
    SHOP,
    TRANSPORT,
}