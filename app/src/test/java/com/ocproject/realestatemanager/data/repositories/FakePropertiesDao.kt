package com.ocproject.realestatemanager.data.repositories

import android.database.Cursor
import com.ocproject.realestatemanager.data.database.PropertiesDao
import com.ocproject.realestatemanager.domain.models.Property
//import com.ocproject.realestatemanager.models.PhotoProperty
//import com.ocproject.realestatemanager.models.Property
//import com.ocproject.realestatemanager.models.PropertyWithPhotos
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flowOf
//
//class FakePropertiesDao : PropertiesDao {
//
//    private val properties = mutableListOf<Property>()
//    val propertyWithPhotos = mutableListOf<PropertyWithPhotos>()
//    val photosProperty = mutableListOf<PhotoProperty>()
//    override fun getPropertiesWithCursor(): Cursor {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun upsertProperty(property: Property): Long {
//        val propertyWithPhoto = PropertyWithPhotos(property, null)
//        properties.add(property)
//        propertyWithPhotos.add(propertyWithPhoto)
//        return property.id
//    }
//
//    override suspend fun deleteProperty(property: Property) {
//        properties.remove(property)
//        propertyWithPhotos.remove(PropertyWithPhotos(property, null))
//    }
//
//    override suspend fun upsertPhoto(photoProperty: PhotoProperty) {
//        photosProperty.add(photoProperty)
//    }
//
//    override suspend fun deletePicturesOfPropertyById(propertyId: Long) {
//        TODO("Not yet implemented")
//    }
//
//    override fun getPropertyList(): Flow<List<PropertyWithPhotos>> {
//        return flowOf(propertyWithPhotos)
//    }
//
//    override suspend fun getPropertyDetail(selectedId: Long): PropertyWithPhotos {
//        return propertyWithPhotos.get(selectedId.toInt())
//    }
//}