package com.ocproject.realestatemanager.domain.models
import android.os.Parcelable
import com.ocproject.realestatemanager.core.InterestPoint
import kotlinx.parcelize.Parcelize

@Parcelize
data class Property(
    val id: Long,
    val photoList: List<PhotoProperty>,
    val interestPoints: List<InterestPoint>,
    val description: String,
    val address: String,
    val town: String,
    val lat: Double,
    val lng: Double,
    val country: String,
    val createdDate: Long?,
    val areaCode: Int,
    val surfaceArea: Int,
    val price: Int,
    val sold: Long,
): Parcelable