package com.ocproject.realestatemanager.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ocproject.realestatemanager.data.entities.PhotoPropertyEntity
import com.ocproject.realestatemanager.data.entities.PropertyEntity


@Database(
    entities = [PropertyEntity::class, PhotoPropertyEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PropertiesDatabase: RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: PropertiesDatabase? = null

        fun getInstance(context: Context): PropertiesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PropertiesDatabase::class.java,
                    "PropertiesDatabase"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
    abstract val dao: PropertiesDao
}