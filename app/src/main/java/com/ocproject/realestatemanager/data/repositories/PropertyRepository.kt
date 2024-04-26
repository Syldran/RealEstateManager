package com.ocproject.realestatemanager.data.repositories

import com.ocproject.realestatemanager.data.db.PropertyDao
import com.ocproject.realestatemanager.models.PropertyWithPictures
import com.openclassrooms.realestatemanager.models.PictureOfProperty
import com.openclassrooms.realestatemanager.models.Property
import kotlinx.coroutines.flow.Flow

class PropertyRepository(
    private val dao: PropertyDao,
) {

    suspend fun upsertProperty(property: Property): Long {
        return dao.upsertProperty(property)
    }

    suspend fun upsertPictureOfProperty(pictureOfProperty: PictureOfProperty) {
        return dao.upsertPictureofProperty(pictureOfProperty)
    }

    suspend fun deleteProperty(property: Property) {
        dao.deleteProperty(property)
    }

    fun getPropertyListOrderedByPrice(): Flow<List<PropertyWithPictures>> {
        return dao.getPropertiesOrderedByPrice()
    }

    suspend fun getProperty(id: Int): PropertyWithPictures {
        return dao.getPropertyDetails(id)
    }

}