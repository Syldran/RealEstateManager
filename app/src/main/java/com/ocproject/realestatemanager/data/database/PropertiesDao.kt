package com.ocproject.realestatemanager.data.database

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ocproject.realestatemanager.domain.models.PhotoProperty
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.models.PropertyWithPhotos
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertiesDao {

    @Query("SELECT * FROM property")
    fun getPropertiesWithCursor(): Cursor

    @Upsert
    suspend fun upsertProperty(property: Property): Long

    @Delete
    suspend fun deleteProperty(property: Property)

    @Upsert
    suspend fun upsertPhoto(photoProperty: PhotoProperty)

    @Query("DELETE FROM PhotoProperty WHERE propertyId = :propertyId")
    suspend fun deletePicturesOfPropertyById(propertyId: Long)

    @Transaction
    @Query("SELECT * FROM Property")
    suspend fun getPropertyList(): List<PropertyWithPhotos>

    @Transaction
    @Query("SELECT * FROM Property")
    fun getPropertyListBis(): Flow<List<PropertyWithPhotos>>

    @Transaction
    @Query("SELECT * FROM Property WHERE id = :selectedId")
    suspend fun getPropertyDetail(selectedId: Long): PropertyWithPhotos

}