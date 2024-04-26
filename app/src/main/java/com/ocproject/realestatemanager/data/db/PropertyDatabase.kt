package com.ocproject.realestatemanager.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.openclassrooms.realestatemanager.models.InterestPoint
import com.openclassrooms.realestatemanager.models.PictureOfProperty
import com.openclassrooms.realestatemanager.models.Property

@Database(
    entities = [Property::class, PictureOfProperty::class],
    version = 1
)
abstract class PropertyDatabase: RoomDatabase() {

    abstract val dao: PropertyDao
}