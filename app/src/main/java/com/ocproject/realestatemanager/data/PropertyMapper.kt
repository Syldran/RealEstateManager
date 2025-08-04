package com.ocproject.realestatemanager.data

import com.ocproject.realestatemanager.data.entities.PhotoPropertyEntity
import com.ocproject.realestatemanager.data.entities.PropertyEntity
import com.ocproject.realestatemanager.data.entities.PropertyWithPhotosEntity
import com.ocproject.realestatemanager.domain.models.PhotoProperty
import com.ocproject.realestatemanager.domain.models.Property

fun PhotoPropertyEntity.toPhotoProperty(): PhotoProperty {
    return PhotoProperty(
        isMain = this.isMain,
        name = this.name,
        photoBytes = this.photoBytes,
        id = this.id
    )
}

fun PropertyWithPhotosEntity.toProperty(): Property {
    return Property(
        photoList = this.photoList?.map { it.toPhotoProperty() } ?: emptyList(),
        interestPoints = this.property.interestPoints,
        description = this.property.description,
        address = this.property.address,
        town = this.property.town,
        lat = this.property.lat,
        lng = this.property.lng,
        country = this.property.country,
        createdDate = this.property.createdDate,
        areaCode = this.property.areaCode,
        surfaceArea = this.property.surfaceArea,
        price = this.property.price,
        sold = this.property.soldDate,
        id = this.property.id,
    )
}

fun PhotoProperty.toPhotoPropertyEntity(idProperty: Long): PhotoPropertyEntity {
    return PhotoPropertyEntity(
        id = this.id,
        isMain = this.isMain,
        name = this.name,
        photoBytes = this.photoBytes,
        propertyId = idProperty,
    )
}

fun Property.toPropertyEntity(): PropertyEntity {
    return PropertyEntity(
        interestPoints = this.interestPoints,
        description = this.description,
        address = this.address,
        town = this.town,
        lat = this.lat,
        lng = this.lng,
        country = this.country,
        createdDate = this.createdDate!!,
        areaCode = this.areaCode,
        surfaceArea = this.surfaceArea,
        price = this.price,
        soldDate = this.sold,
        id = this.id,
    )
}


