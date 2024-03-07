package com.openclassrooms.realestatemanager.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Property(
    val type: String,
    val price: Int,
    val area: Int,
    val numberOfRooms: Int,
    val description: String,
    val address: String,
    val interestPoints: String,
    val state: String,
    val createDate: String,
    val soldDate: String,
    val agentId: String,
    val lat: Double,
    val lng: Double,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)