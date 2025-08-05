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

    override fun getPhotosWithCursor(): Cursor {
        TODO("Not yet implemented")
    }

    override fun getPhotoWithCursorById(id: Long): Cursor {
        TODO("Not yet implemented")
    }

    override fun getPhotosWithCursorByPropertyId(propertyId: Long): Cursor {
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

    override suspend fun upsertPhoto(photoProperty: PhotoPropertyEntity): Long {
        photosProperty.add(photoProperty)
        return photoProperty.id
    }

    override suspend fun deletePhoto(photoProperty: PhotoPropertyEntity) {
        photosProperty.remove(photoProperty)
    }

    override suspend fun deletePhotoById(photoId: Long) {
        photosProperty.removeIf { it.id == photoId }
    }

    override suspend fun deleteAllProperties(): Int {
        val count = propertyList.size
        propertyList.clear()
        propertyWithPhotosList.clear()
        return count
    }

    override suspend fun deleteAllPhotos(): Int {
        val count = photosProperty.size
        photosProperty.clear()
        return count
    }

    override suspend fun getPropertyListPriceASC(
        areaCode: Int?,
        type: String?,
        interestPoints: String?,
        minPhotos: Int,
        minAddedDate: Long,
        maxAddedDate: Long,
        minSoldDate: Long,
        maxSoldDate: Long,
        sellingStatus: Long?,
        minPrice: Int,
        maxPrice: Int,
        minSurface: Int,
        maxSurface: Int
    ): List<PropertyWithPhotosEntity> {
        return propertyWithPhotosList
    }

    override suspend fun deletePicturesOfPropertyByIdProperty(propertyId: Long) {
        photosProperty.removeIf{it.propertyId == propertyId}
    }

    override suspend fun getPropertyListPriceDESC(
        areaCode: Int?,
        type: String?,
        interestPoints: String?,
        minPhotos: Int,
        minAddedDate: Long,
        maxAddedDate: Long,
        minSoldDate: Long,
        maxSoldDate: Long,
        sellingStatus: Long?,
        minPrice: Int,
        maxPrice: Int,
        minSurface: Int,
        maxSurface: Int
    ): List<PropertyWithPhotosEntity> {
        return propertyWithPhotosList
    }

    override suspend fun getPropertyListDateASC(
        areaCode: Int?,
        type: String?,
        interestPoints: String?,
        minPhotos: Int,
        minAddedDate: Long,
        maxAddedDate: Long,
        minSoldDate: Long,
        maxSoldDate: Long,
        sellingStatus: Long?,
        minPrice: Int,
        maxPrice: Int,
        minSurface: Int,
        maxSurface: Int
    ): List<PropertyWithPhotosEntity> {
        return propertyWithPhotosList
    }

    override suspend fun getPropertyListDateDESC(
        areaCode: Int?,
        type: String?,
        interestPoints: String?,
        minPhotos: Int,
        minAddedDate: Long,
        maxAddedDate: Long,
        minSoldDate: Long,
        maxSoldDate: Long,
        sellingStatus: Long?,
        minPrice: Int,
        maxPrice: Int,
        minSurface: Int,
        maxSurface: Int
    ): List<PropertyWithPhotosEntity> {
        return propertyWithPhotosList
    }

    override suspend fun getPropertyListSurfaceASC(
        areaCode: Int?,
        type: String?,
        interestPoints: String?,
        minPhotos: Int,
        minAddedDate: Long,
        maxAddedDate: Long,
        minSoldDate: Long,
        maxSoldDate: Long,
        sellingStatus: Long?,
        minPrice: Int,
        maxPrice: Int,
        minSurface: Int,
        maxSurface: Int
    ): List<PropertyWithPhotosEntity> {
        return propertyWithPhotosList
    }

    override suspend fun getPropertyListSurfaceDESC(
        areaCode: Int?,
        type: String?,
        interestPoints: String?,
        minPhotos: Int,
        minAddedDate: Long,
        maxAddedDate: Long,
        minSoldDate: Long,
        maxSoldDate: Long,
        sellingStatus: Long?,
        minPrice: Int,
        maxPrice: Int,
        minSurface: Int,
        maxSurface: Int
    ): List<PropertyWithPhotosEntity> {

        return propertyWithPhotosList
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
                0,
                0,
                0,
                -1L,
                "",
                0,
                ""
            ),
            null
        )
    }
}