package com.ocproject.realestatemanager.data.repositories

import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.repositories.PropertiesRepository

class FakePropertiesRepository : PropertiesRepository {

    private var properties = mutableListOf<Property>()

    fun shouldHaveFilledList(shouldHaveFilledList: Boolean) {
        properties =
            if (shouldHaveFilledList) {
                mutableListOf(
                    Property(
                        photoList = emptyList(),
                        interestPoints = emptyList(),
                        address = "Somewhere",
                        town = "NowhereCity",
                        lat = 120.5,
                        lng = 50.30,
                        country = "Faraway",
                        createdDate = null,
                        areaCode = 18290,
                        surfaceArea = 150,
                        price = 150000,
                        sold = false,
                        id = 1L,
                    ),
                    Property(
                        photoList = emptyList(),
                        interestPoints = emptyList(),
                        address = "Somewhere",
                        town = "Paris",
                        lat = 120.5,
                        lng = 50.30,
                        country = "France",
                        createdDate = null,
                        areaCode = 18290,
                        surfaceArea = 150,
                        price = 150000,
                        sold = false,
                        id = 2L,
                    ),
                    Property(
                        photoList = emptyList(),
                        interestPoints = emptyList(),
                        address = "Somewhere",
                        town = "Londres",
                        lat = 120.5,
                        lng = 50.30,
                        country = "Angleterre",
                        createdDate = null,
                        areaCode = 18290,
                        surfaceArea = 150,
                        price = 150000,
                        sold = false,
                        id = 3L,
                    ),
                )
            } else {
                mutableListOf()
            }
    }

    override suspend fun upsertProperty(property: Property): Long {
        properties.add(property)
        return property.id
    }

    override suspend fun deleteProperty(property: Property) {
        properties.remove(property)
    }

    override suspend fun getPropertyList(): List<Property> {
        return properties
    }

    override suspend fun getProperty(id: Long): Property {
        var propertyToReturn: Property = Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            address = "",
            town = "",
            lat = 0.0,
            lng = 0.0,
            country = "",
            createdDate = null,
            areaCode = null,
            surfaceArea = null,
            price = null,
            sold = false,
            id = -1L
        )
        properties.forEach {
            if (it.id == id) propertyToReturn = it
        }
        return propertyToReturn
    }
}
