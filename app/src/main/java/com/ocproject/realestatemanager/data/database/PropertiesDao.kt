package com.ocproject.realestatemanager.data.database

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ocproject.realestatemanager.data.entities.PhotoPropertyEntity
import com.ocproject.realestatemanager.data.entities.PropertyEntity
import com.ocproject.realestatemanager.data.entities.PropertyWithPhotosEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertiesDao {

    @Query("SELECT * FROM PropertyEntity")
    fun getPropertiesWithCursor(): Cursor

    @Upsert
    suspend fun upsertProperty(property: PropertyEntity): Long

    @Delete
    suspend fun deleteProperty(property: PropertyEntity)

    @Upsert
    suspend fun upsertPhoto(photoProperty: PhotoPropertyEntity)

    @Query("DELETE FROM PhotoPropertyEntity WHERE propertyId = :propertyId")
    suspend fun deletePicturesOfPropertyById(propertyId: Long)

    @Transaction
    @Query("SELECT * FROM PropertyEntity")
    suspend fun getPropertyList(): List<PropertyWithPhotosEntity>

    @Transaction
    @Query("SELECT * FROM PropertyEntity WHERE id = :selectedId")
    suspend fun getPropertyDetail(selectedId: Long): PropertyWithPhotosEntity

}