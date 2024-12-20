package com.ocproject.realestatemanager.domain.models
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ocproject.realestatemanager.core.InterestPoint


data class Property(
    val photoList: List<PhotoProperty>?,
    val interestPoints: List<InterestPoint>,
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
    val id: Long,
)