package com.ocproject.realestatemanager.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ocproject.realestatemanager.models.PhotoProperty
import com.ocproject.realestatemanager.models.Property
import com.ocproject.realestatemanager.models.PropertyWithPhotos
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertiesDao {

    @Upsert
    suspend fun upsertProperty(property: Property): Long

//    @Upsert
//    suspend fun upsertPropertyWithPhoto(propertyWithPhotos: PropertyWithPhotos): Long

    @Delete
    suspend fun deleteProperty(property: Property)

    @Upsert
    suspend fun upsertPhoto(photoProperty: PhotoProperty)

    @Query("DELETE FROM PhotoProperty WHERE propertyId = :propertyId")
    suspend fun deletePicturesOfPropertyById(propertyId: Long)

    @Transaction
    @Query("SELECT * FROM Property")
    fun getPropertyList(): Flow<List<PropertyWithPhotos>>

    @Transaction
    @Query("SELECT * FROM Property WHERE id = :selectedId")
    suspend fun getPropertyDetail(selectedId: Long): PropertyWithPhotos

}