package com.openclassrooms.realestatemanager.models

import android.graphics.Picture


data class Property (
    val id: String,
    val type: String,
    val price: Int,
    val area: Int,
    val numberOfRooms: Int,
    val description: String,
    val picturesList: List<PictureOfProperty>, // to create
    val address: String,
    val interestPoints: List<InterestPoint>, // to create
    val state: String,
    val createDate: String,
    val soldDate: String,
    val agentId: String,
    val lat: Double,
    val lng: Double
)