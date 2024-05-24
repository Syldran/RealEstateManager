package com.ocproject.realestatemanager.presentation.scene.addproperty

data class AddPropertyState(
    val addressError: String? = null,
    val townError: String? = null,
    val latError: String? = null,
    val lngError: String? = null,
    val countryError: String? = null,
    val createdDateError: String? = null,
    val areaCodeError: String? = null,
    val surfaceAreaError: String? = null,
    val priceError: String? = null,
    val navToPropertyListScreen: Boolean = false,
)
