package com.ocproject.realestatemanager.data.repositories

import android.database.Cursor
import com.ocproject.realestatemanager.data.database.PropertiesDao
import com.ocproject.realestatemanager.data.entities.PropertyEntity
import com.ocproject.realestatemanager.data.entities.PhotoPropertyEntity
import com.ocproject.realestatemanager.data.entities.PropertyWithPhotosEntity

class FakePropertiesDao : PropertiesDao {

    private val propertyList = mutableListOf<PropertyEntity>()
    val propertyWithPhotos = mutableListOf<PropertyWithPhotosEntity>()
    val photosProperty = mutableListOf<PhotoPropertyEntity>()
    override fun getPropertiesWithCursor(): Cursor {
        TODO("Not yet implemented")
    }

    override fun getPropertyWithCursorById(id: Long): Cursor {
        TODO("Not yet implemented")
    }

    override suspend fun upsertProperty(property: PropertyEntity): Long {
        val propertyWithPhoto = PropertyWithPhotosEntity(property, null)
        propertyList.add(property)
        propertyWithPhotos.add(propertyWithPhoto)
        return property.id
    }

    override suspend fun deleteProperty(property: PropertyEntity) {
        propertyList.remove(property)
        propertyWithPhotos.remove(PropertyWithPhotosEntity(property, null))
    }

    override suspend fun upsertPhoto(photoProperty: PhotoPropertyEntity) {
        photosProperty.add(photoProperty)
    }

    override suspend fun deletePicturesOfPropertyByIdProperty(propertyId: Long) {

    }

    override suspend fun getPropertyList(): List<PropertyWithPhotosEntity> {
        return propertyWithPhotos
    }

    override suspend fun getPropertyDetail(selectedId: Long): PropertyWithPhotosEntity {
        return propertyWithPhotos[selectedId.toInt()]
    }
}