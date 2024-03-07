package com.ocproject.realestatemanager.models

import androidx.room.Embedded
import androidx.room.Relation
import com.openclassrooms.realestatemanager.models.PictureOfProperty
import com.openclassrooms.realestatemanager.models.Property

data class PropertyWithPictures(
    @Embedded val property: Property,
    @Relation(
        parentColumn = "id",
        entityColumn = "propertyId"
    )
    val pictureList: List<PictureOfProperty> // <-- This is a one-to-many relationship, since each artist has many albums, hence returning a List here
)
