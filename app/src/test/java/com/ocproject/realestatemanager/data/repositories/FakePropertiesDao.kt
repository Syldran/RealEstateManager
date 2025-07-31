package com.ocproject.realestatemanager.data.repositories

import android.database.Cursor
import com.ocproject.realestatemanager.data.database.PropertiesDao
import com.ocproject.realestatemanager.data.entities.PropertyEntity
import com.ocproject.realestatemanager.data.entities.PhotoPropertyEntity
import com.ocproject.realestatemanager.data.entities.PropertyWithPhotosEntity

class FakePropertiesDao : PropertiesDao {

    private val propertyList = mutableListOf<PropertyEntity>()
    val propertyWithPhotosList = mutableListOf<PropertyWithPhotosEntity>()
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
        propertyWithPhotosList.add(propertyWithPhoto)
        return property.id
    }

    override suspend fun deleteProperty(property: PropertyEntity) {
        propertyList.remove(property)
        propertyWithPhotosList.remove(PropertyWithPhotosEntity(property, null))
    }

    override suspend fun upsertPhoto(photoProperty: PhotoPropertyEntity) {
        photosProperty.add(photoProperty)
    }

    override suspend fun deletePicturesOfPropertyByIdProperty(propertyId: Long) {

    }

    override suspend fun getPropertyList(): List<PropertyWithPhotosEntity> {
        return propertyWithPhotosList
    }

    override suspend fun getPropertyDetail(selectedId: Long): PropertyWithPhotosEntity {
        propertyWithPhotosList.forEach { p ->
            if (p.property.id == selectedId) return p
        }
        return PropertyWithPhotosEntity(
            PropertyEntity(
                emptyList(),
                "",
                "",
                "",
                0.0,
                0.0,
                "",
                0L,
                null,
                0,
                0,
                null,
                -1L
            ),
            null
        )
    }
}