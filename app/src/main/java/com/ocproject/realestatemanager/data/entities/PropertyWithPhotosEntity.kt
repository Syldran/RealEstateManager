package com.ocproject.realestatemanager.data.entities
import androidx.room.Embedded
import androidx.room.Relation

data class PropertyWithPhotosEntity(
    @Embedded val property: PropertyEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "propertyId",
    )
        val photoList: List<PhotoPropertyEntity>?
)
