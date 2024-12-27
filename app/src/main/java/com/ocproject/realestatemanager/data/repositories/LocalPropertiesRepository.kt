package com.ocproject.realestatemanager.data.repositories

import com.ocproject.realestatemanager.data.database.PropertiesDao
import com.ocproject.realestatemanager.data.toPhotoPropertyEntity
import com.ocproject.realestatemanager.data.toProperty
import com.ocproject.realestatemanager.data.toPropertyEntity
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.repositories.PropertiesRepository


class LocalPropertiesRepository(
    private val dao: PropertiesDao,
) : PropertiesRepository {
    override suspend fun upsertProperty(property: Property): Long {
        var propertyToAdd = dao.upsertProperty(property.toPropertyEntity())
        // upsert return -1 for replacing existing data
        if (propertyToAdd < 0L) propertyToAdd = property.id
        dao.deletePicturesOfPropertyByIdProperty(property.id)
        val photosToAdd = property.photoList
        photosToAdd?.forEach {
            dao.upsertPhoto(it.toPhotoPropertyEntity(propertyToAdd))
        }
        return propertyToAdd
    }

    override suspend fun deleteProperty(property: Property) {
        val propertyToDelete = property.toPropertyEntity()
        dao.deleteProperty(propertyToDelete)
    }

    override suspend fun getPropertyList(): List<Property> {
        val properties = dao.getPropertyList().map { it.toProperty() }
        return properties
    }

    override suspend fun getProperty(id: Long): Property {
        val propertyDetails = dao.getPropertyDetail(id).toProperty()
        return propertyDetails
    }
}