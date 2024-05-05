package com.ocproject.realestatemanager.ui.scenes.propertydetails

import com.openclassrooms.realestatemanager.models.InterestPoint
import com.openclassrooms.realestatemanager.models.PictureOfProperty

data class PropertyDetailsState(
    val id : Int = 0,
    val type: String = "",
    val price: Int = 0,
    val area: Int = 0,
    val numberOfRooms: Int = 0,
    val description: String = "",
    val picturesList: List<PictureOfProperty>? = emptyList(), // to create
    val address: String = "",
    val interestPoints: List<InterestPoint> = emptyList(), // to create
    val state: String = "",
    val createDate: String = "",
    val soldDate: String = "",
    val agentId: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
)