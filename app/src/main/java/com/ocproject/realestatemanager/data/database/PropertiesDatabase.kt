package com.ocproject.realestatemanager.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ocproject.realestatemanager.models.PhotoProperty
import com.ocproject.realestatemanager.models.Property


@Database(
    entities = [Property::class, PhotoProperty::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PropertiesDatabase: RoomDatabase() {

    abstract val dao: PropertiesDao
}