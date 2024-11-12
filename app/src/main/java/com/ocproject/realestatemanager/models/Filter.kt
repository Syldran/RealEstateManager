package com.ocproject.realestatemanager.models

import android.util.Range

data class Filter(
    val sortType: SortType,
    val orderPrice: Order,
    val orderDate: Order,
    val rangePrice: Range<Int>,
    val rangeDate: Range<Long>,
    val sellingStatus : SellingStatus,

    val tagSchool: Boolean,
    val tagTransport: Boolean,
    val tagShop: Boolean,
    val tagPark: Boolean,
)

enum class SellingStatus {
    ALL,
    PURCHASABLE,
    SOLD,
}

enum class SortType {
    PRICE,
    DATE,
//    TAGS,
//    TOWN,
}

enum class Order{
    ASC,
    DESC,
}

enum class InterestPoint {
    SCHOOL,
    PARK,
    SHOP,
    TRANSPORT,
}