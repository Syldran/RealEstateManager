package com.ocproject.realestatemanager.data.entities
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ocproject.realestatemanager.core.InterestPoint


@Entity
data class PropertyEntity(
    val interestPoints: List<InterestPoint>,
    val description: String,
    val address: String,
    val town: String,
    val lat: Double,
    val lng: Double,
    val country: String,
    val createdDate: Long,
    val areaCode: Int,
    val surfaceArea: Int,
    val price: Int,
    val soldDate: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
)