package com.ocproject.realestatemanager.repositories

import com.ocproject.realestatemanager.db.PropertyDao
import com.ocproject.realestatemanager.models.PropertyWithPictures
import com.openclassrooms.realestatemanager.models.Property
import kotlinx.coroutines.flow.Flow

class PropertyRepository(
    private val dao: PropertyDao,
) {

    suspend fun upsertProperty(property: Property){
        dao.upsertProperty(property)
    }

    suspend fun deleteProperty(property: Property){
        dao.deleteProperty(property)
    }

    fun getPropertyListOrderedByPrice(): Flow<List<PropertyWithPictures>> {
        return dao.getPropertiesOrderedByPrice()
    }

    suspend fun getProperty(id : Int): PropertyWithPictures {
        return dao.getPropertyDetails(id)
    }

}