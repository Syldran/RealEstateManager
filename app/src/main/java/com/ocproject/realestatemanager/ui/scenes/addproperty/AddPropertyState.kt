package com.ocproject.realestatemanager.ui.scenes.addproperty

import android.graphics.Picture
import com.openclassrooms.realestatemanager.models.InterestPoint
import com.openclassrooms.realestatemanager.models.PictureOfProperty


data class AddPropertyState(
    val type: String = "",
    val price: Int = 0,
    val area: Int = 0,
    val numberOfRooms: Int = 0,
    val description: String = "",
    val address: String = "",
    val interestPoints: List<InterestPoint> = emptyList(),
    val state: String = "",
    val createDate: String = "",
    val soldDate: String = "",
    val agentId: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,

    val isAddingProperty: Boolean = false,
    val picturesList: List<PictureOfProperty> = emptyList(),
    val mainPic: PictureOfProperty ?= null,
)