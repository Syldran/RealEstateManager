package com.ocproject.realestatemanager.presentation.scene.addproperty

import androidx.compose.ui.graphics.ImageBitmap
import com.ocproject.realestatemanager.domain.models.PhotoProperty
import com.ocproject.realestatemanager.domain.models.Property
import java.util.Calendar

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
    val photoList : List<PhotoProperty> = emptyList(),
    val newProperty : Property = Property(
        photoList = emptyList(),
        interestPoints = emptyList(),
        address = "",
        town = "",
        lat = 0.0,
        lng = 0.0,
        country = "",
        createdDate = Calendar.getInstance().timeInMillis,
        areaCode = null,
        surfaceArea = null,
        price = null,
        id = 0L,
        sold = null,
    ),
    val soldState : Boolean = false,
)
