package com.ocproject.realestatemanager.data.repositories

import com.ocproject.realestatemanager.core.Filter
import com.ocproject.realestatemanager.core.InterestPoint
import com.ocproject.realestatemanager.core.Order
import com.ocproject.realestatemanager.core.SellingStatus
import com.ocproject.realestatemanager.core.SortType
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.repositories.PropertiesRepository
import java.util.Calendar

class FakePropertiesRepository : PropertiesRepository {

    private var properties = mutableListOf<Property>()

    fun shouldHaveFilledList(shouldHaveFilledList: Boolean) {
        properties =
            if (shouldHaveFilledList) {
                mutableListOf(
                    Property(
                        photoList = emptyList(),
                        interestPoints = listOf(InterestPoint.SCHOOL, InterestPoint.PARK),
                        description = "Belle maison avec jardin",
                        address = "123 Rue de la Paix",
                        town = "Paris",
                        lat = 48.8566,
                        lng = 2.3522,
                        country = "France",
                        createdDate = 1640995200000, // 2022-01-01
                        areaCode = 75001,
                        surfaceArea = 150,
                        price = 450000,
                        sold = -1, // Purchasable
                        id = 1L,
                    ),
                    Property(
                        photoList = emptyList(),
                        interestPoints = listOf(InterestPoint.TRANSPORT, InterestPoint.SHOP),
                        description = "Appartement moderne",
                        address = "456 Avenue des Champs",
                        town = "Lyon",
                        lat = 45.7578,
                        lng = 4.8320,
                        country = "France",
                        createdDate = 1643673600000, // 2022-02-01
                        areaCode = 69001,
                        surfaceArea = 80,
                        price = 280000,
                        sold = 1, // Sold
                        id = 2L,
                    ),
                    Property(
                        photoList = emptyList(),
                        interestPoints = listOf(InterestPoint.SCHOOL),
                        description = "Maison de ville",
                        address = "789 Boulevard Central",
                        town = "Marseille",
                        lat = 43.2965,
                        lng = 5.3698,
                        country = "France",
                        createdDate = 1646092800000, // 2022-03-01
                        areaCode = 13001,
                        surfaceArea = 200,
                        price = 650000,
                        sold = -1, // Purchasable
                        id = 3L,
                    ),
                    Property(
                        photoList = emptyList(),
                        interestPoints = listOf(InterestPoint.PARK, InterestPoint.SHOP),
                        description = "Loft contemporain",
                        address = "321 Quai de la Seine",
                        town = "Paris",
                        lat = 48.8566,
                        lng = 2.3522,
                        country = "France",
                        createdDate = 1648771200000, // 2022-04-01
                        areaCode = 75002,
                        surfaceArea = 120,
                        price = 380000,
                        sold = -1, // Purchasable
                        id = 4L,
                    ),
                    Property(
                        photoList = emptyList(),
                        interestPoints = listOf(InterestPoint.TRANSPORT),
                        description = "Studio rénové",
                        address = "654 Rue du Commerce",
                        town = "Toulouse",
                        lat = 43.6047,
                        lng = 1.4442,
                        country = "France",
                        createdDate = 1651363200000, // 2022-05-01
                        areaCode = 31000,
                        surfaceArea = 45,
                        price = 180000,
                        sold = 1, // Sold
                        id = 5L,
                    )
                )
            } else {
                mutableListOf()
            }
    }


    override suspend fun upsertProperty(property: Property): Long {
        // Search if a property with same id already exist.
        // return -1 if not found or id of first occurrence.
        val existingIndex = properties.indexOfFirst { it.id == property.id }
        // If property already exist.
        if (existingIndex != -1) {
            // We replace it.
            properties[existingIndex] = property
            return property.id
        } else {  // If not it's a new property to add.
            // If id not defined we find the max id existing (0 if list empty) and add 1 to it.
            val newId = if (property.id == -1L) {
                (properties.maxOfOrNull { it.id } ?: 0) + 1
            } else {
                // id defined so we just use it.
                property.id
            }
            val propertyWithId = property.copy(id = newId)
            // Add property with good id to properties.
            properties.add(propertyWithId)
            return newId
        }
    }

    override suspend fun deleteProperty(property: Property) {
        properties.removeIf { it.id == property.id }
    }

    override suspend fun getPropertyList(): List<Property> {
        return properties.toList()
    }

    override suspend fun getPropertyListFiltered(filter: Filter): List<Property> {
        var filteredProperties = properties.toMutableList()

        // Filter by selling status
        when (filter.sellingStatus) {
            SellingStatus.PURCHASABLE -> {
                filteredProperties = filteredProperties.filter { it.sold == -1L }.toMutableList()
            }

            SellingStatus.SOLD -> {
                filteredProperties = filteredProperties.filter { it.sold == 1L }.toMutableList()
            }

            SellingStatus.ALL -> {
                // No filtering needed
            }
        }

        // Filter by area code
        if (filter.areaCodeFilter != null) {
            filteredProperties =
                filteredProperties.filter { it.areaCode == filter.areaCodeFilter }.toMutableList()
        }

        // Filter by interest points
        val requiredInterestPoints = mutableListOf<InterestPoint>()
        if (filter.tagSchool) requiredInterestPoints.add(InterestPoint.SCHOOL)
        if (filter.tagPark) requiredInterestPoints.add(InterestPoint.PARK)
        if (filter.tagShop) requiredInterestPoints.add(InterestPoint.SHOP)
        if (filter.tagTransport) requiredInterestPoints.add(InterestPoint.TRANSPORT)

        if (requiredInterestPoints.isNotEmpty()) {
            filteredProperties = filteredProperties.filter { property ->
                requiredInterestPoints.all { requiredPoint ->
                    property.interestPoints.contains(requiredPoint)
                }
            }.toMutableList()
        }

        // Filter by minimum number of photos
        filteredProperties =
            filteredProperties.filter { it.photoList.size >= filter.minNbrPhotos }.toMutableList()

        // Filter by price range
        filteredProperties = filteredProperties.filter { property ->
            property.price >= filter.priceRange.lower && property.price <= filter.priceRange.upper
        }.toMutableList()

        // Filter by surface range
        filteredProperties = filteredProperties.filter { property ->
            property.surfaceArea >= filter.surfaceRange.lower && property.surfaceArea <= filter.surfaceRange.upper
        }.toMutableList()

        // Filter by date range
        filteredProperties = filteredProperties.filter { property ->
            property.createdDate!! >= filter.dateRange.lower && property.createdDate <= filter.dateRange.upper
        }.toMutableList()

        // Sort the filtered results
        val sortedProperties = when (filter.sortType) {
            SortType.PRICE -> {
                if (filter.priceOrder == Order.ASC) {
                    filteredProperties.sortedBy { it.price }
                } else {
                    filteredProperties.sortedByDescending { it.price }
                }
            }

            SortType.DATE -> {
                if (filter.dateOrder == Order.ASC) {
                    filteredProperties.sortedBy { it.createdDate }
                } else {
                    filteredProperties.sortedByDescending { it.createdDate }
                }
            }

            SortType.AREA -> {
                if (filter.surfaceOrder == Order.ASC) {
                    filteredProperties.sortedBy { it.surfaceArea }
                } else {
                    filteredProperties.sortedByDescending { it.surfaceArea }
                }
            }
        }

        return sortedProperties
    }

    override suspend fun getProperty(id: Long): Property {
        return properties.find { it.id == id } ?: Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            description = "",
            address = "",
            town = "",
            lat = 0.0,
            lng = 0.0,
            country = "",
            createdDate = Calendar.getInstance().timeInMillis,
            areaCode = 0,
            surfaceArea = 0,
            price = 1,
            sold = -1,
            id = -1L
        )
    }

    // Utilities
    fun clearProperties() {
        properties.clear()
    }

    fun addProperty(property: Property) {
        properties.add(property)
    }

    fun getPropertiesCount(): Int {
        return properties.size
    }

    fun getPropertyById(id: Long): Property? {
        return properties.find { it.id == id }
    }
}
