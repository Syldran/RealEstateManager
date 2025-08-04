package com.ocproject.realestatemanager.presentation.scene.addproperty

import com.ocproject.realestatemanager.domain.models.PhotoProperty
import com.ocproject.realestatemanager.domain.models.Property

data class AddPropertyState(
    val descriptionError: String? = null,
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
    val photoList : List<PhotoProperty> = emptyList(),
    val newProperty : Property = Property(
        photoList = emptyList(),
        interestPoints = emptyList(),
        description = "",
        address = "",
        town = "",
        lat = 0.0,
        lng = 0.0,
        country = "",
        createdDate = null,
        areaCode = 0,
        surfaceArea = 0,
        price = 0,
        id = 0L,
        sold = -1,
    ),
    val soldState : Boolean = false,
    val latitudeInput: String = "",
    val longitudeInput: String = "",
)
