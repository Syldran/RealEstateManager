package com.ocproject.realestatemanager.core

import com.ocproject.realestatemanager.core.utils.Range


data class Filter(
    val sortType: SortType,
    val priceOrder: Order,
    val dateOrder: Order,
    val surfaceOrder: Order,
    val priceRange: Range<Int>,
    val dateRange: Range<Long>,
    val soldDateRange: Range<Long>,
    val surfaceRange: Range<Int>,
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