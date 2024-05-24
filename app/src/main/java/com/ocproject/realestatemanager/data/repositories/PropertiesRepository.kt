package com.ocproject.realestatemanager.data.repositories

import com.ocproject.realestatemanager.data.database.PropertiesDao
import com.ocproject.realestatemanager.models.PhotoProperty
import com.ocproject.realestatemanager.models.Property
import com.ocproject.realestatemanager.models.PropertyWithPhotos
import kotlinx.coroutines.flow.Flow

class PropertiesRepository(
    private val dao: PropertiesDao,
) {
    suspend fun upsertProperty(property: Property): Long {
        return dao.upsertProperty(property)
    }

    suspend fun upsertPhotoProperty(photoProperty: PhotoProperty) {
        return dao.upsertPhoto(photoProperty)
    }

    suspend fun deleteProperty(property: Property){
        return dao.deleteProperty(property)
    }

    suspend fun deletePicturesOfPropertyById(idProperty: Long){
        return dao.deletePicturesOfPropertyById(idProperty)
    }

    fun getPropertyList(): Flow<List<PropertyWithPhotos>> {
        return dao.getPropertyList()
    }

    suspend fun getProperty(id: Long): PropertyWithPhotos {
        return dao.getPropertyDetail(id)
    }
}