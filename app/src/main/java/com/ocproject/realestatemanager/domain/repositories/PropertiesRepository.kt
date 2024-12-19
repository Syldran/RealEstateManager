package com.ocproject.realestatemanager.domain.repositories

import com.ocproject.realestatemanager.domain.models.PhotoProperty
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.models.PropertyWithPhotos
import kotlinx.coroutines.flow.Flow

interface PropertiesRepository {
    suspend fun upsertProperty(property: Property): Long

    suspend fun upsertPhotoProperty(photoProperty: PhotoProperty)

    suspend fun deleteProperty(property: Property)

    suspend fun deletePicturesOfPropertyById(idProperty: Long)

    suspend fun getPropertyList(): List<PropertyWithPhotos>

    fun getPropertyListBis(): Flow<List<PropertyWithPhotos>>

    suspend fun getProperty(id: Long): PropertyWithPhotos
}