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
        return dao.upsertPictureOfProperty(pictureOfProperty)
    }

    suspend fun deleteProperty(property: Property) {
        dao.deleteProperty(property)
    }

    suspend fun deletePicturesOfPropertyById(propertyId: Int) {
        dao.deletePicturesOfPropertyById(propertyId)
    }
    fun getPropertyList(): Flow<List<PropertyWithPictures>> {
        return dao.getProperties()
    }

    fun getPropertyListOrderedByPriceAsc(): Flow<List<PropertyWithPictures>> {
        return dao.getPropertiesOrderedByPriceAsc()
    }

    fun getPropertyListOrderedByPriceDesc(): Flow<List<PropertyWithPictures>> {
        return dao.getPropertiesOrderedByPriceDesc()
    }
    suspend fun getProperty(id: Int): PropertyWithPictures {
        return dao.getPropertyDetails(id)
    }

}