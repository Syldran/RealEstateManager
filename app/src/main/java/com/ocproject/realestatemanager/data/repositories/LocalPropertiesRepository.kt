package com.ocproject.realestatemanager.data.repositories

import com.ocproject.realestatemanager.data.database.PropertiesDao
import com.ocproject.realestatemanager.domain.models.PhotoProperty
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.models.PropertyWithPhotos
import com.ocproject.realestatemanager.domain.repositories.PropertiesRepository
import kotlinx.coroutines.flow.Flow


class LocalPropertiesRepository(
    private val dao: PropertiesDao,
) : PropertiesRepository {
    override suspend fun upsertProperty(property: Property): Long {
        return dao.upsertProperty(property)
    }

    override suspend fun upsertPhotoProperty(photoProperty: PhotoProperty) {
        return dao.upsertPhoto(photoProperty)
    }

    override suspend fun deleteProperty(property: Property) {
        return dao.deleteProperty(property)
    }

    override suspend fun deletePicturesOfPropertyById(idProperty: Long) {
        return dao.deletePicturesOfPropertyById(idProperty)
    }

    override suspend fun getPropertyList(): List<PropertyWithPhotos> {
        return dao.getPropertyList()
    }

    override fun getPropertyListBis(): Flow<List<PropertyWithPhotos>> {
        return dao.getPropertyListBis()
    }

    override suspend fun getProperty(id: Long): PropertyWithPhotos {
        return dao.getPropertyDetail(id)
    }
}