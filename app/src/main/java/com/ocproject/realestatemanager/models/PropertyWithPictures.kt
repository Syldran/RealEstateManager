package com.ocproject.realestatemanager.models

import androidx.room.Embedded
import androidx.room.Relation
import com.openclassrooms.realestatemanager.models.PictureOfProperty
import com.openclassrooms.realestatemanager.models.Property

data class PropertyWithPictures(
    @Embedded val property: Property,
    @Relation(
        parentColumn = "id",
        entityColumn = "propertyId",
        entity = PictureOfProperty::class
    )
    val pictureList: List<PictureOfProperty>
)
