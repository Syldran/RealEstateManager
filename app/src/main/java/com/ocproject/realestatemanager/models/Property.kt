package com.ocproject.realestatemanager.models
import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity
data class Property(
    val interestPoints: List<InterestPoint>,
    val school : Boolean,
    val shop : Boolean,
    val transport : Boolean,
    val park : Boolean,
    val address: String,
    val town: String,
    val lat: Double,
    val lng: Double,
    val country: String,
    val createdDate: Long?,
    val areaCode: Int?,
    val surfaceArea: Int?,
    val price: Int?,
    val sold: Boolean,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
)