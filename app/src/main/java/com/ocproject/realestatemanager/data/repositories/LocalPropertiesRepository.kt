package com.ocproject.realestatemanager.data.repositories

import com.ocproject.realestatemanager.core.Filter
import com.ocproject.realestatemanager.core.InterestPoint
import com.ocproject.realestatemanager.core.Order
import com.ocproject.realestatemanager.core.SellingStatus
import com.ocproject.realestatemanager.core.SortType
import com.ocproject.realestatemanager.core.utils.UtilsKotlin
import com.ocproject.realestatemanager.data.database.PropertiesDao
import com.ocproject.realestatemanager.data.toPhotoPropertyEntity
import com.ocproject.realestatemanager.data.toProperty
import com.ocproject.realestatemanager.data.toPropertyEntity
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.repositories.PropertiesRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking


class LocalPropertiesRepository(
    private val dao: PropertiesDao,
) : PropertiesRepository {
    override suspend fun upsertProperty(property: Property): Long {
        var propertyToAdd: Long = dao.upsertProperty(property.toPropertyEntity())
        // upsert return -1 for replacing existing data
        if (propertyToAdd < 0L) {
            propertyToAdd = property.id
        }
        dao.deletePicturesOfPropertyByIdProperty(propertyToAdd)
        property.photoList.forEach {
            runBlocking {
                async {
                    dao.upsertPhoto(it.toPhotoPropertyEntity(propertyToAdd))
                }
            }
        }



        return propertyToAdd
    }


    override suspend fun deleteProperty(property: Property) {
        val propertyToDelete = property.toPropertyEntity()
        dao.deleteProperty(propertyToDelete)
    }

    override suspend fun getPropertyList(): List<Property> {
        val properties: List<Property> = dao.getPropertyList().map { it.toProperty() }
        return properties
    }

    override suspend fun getPropertyListFiltered(filter: Filter): List<Property> {
        val interestPoints: MutableList<InterestPoint> = mutableListOf()
        // Create a listOfInterest from filter tags.
        if (filter.tagSchool) {
            interestPoints.add(InterestPoint.SCHOOL)
        }
        if (filter.tagPark) {
            interestPoints.add(InterestPoint.PARK)
        }
        if (filter.tagTransport) {
            interestPoints.add(InterestPoint.TRANSPORT)
        }
        if (filter.tagShop) {
            interestPoints.add(InterestPoint.SHOP)
        }
        // Order & transform the listOfInterest to String the same way Converter do to allow comparison.
        var list: String? = null
        if (UtilsKotlin.fromListInterestPoint(interestPoints) != "") {
            list = UtilsKotlin.fromListInterestPoint(interestPoints)
        }

        // null return all property,
        //-1 return purchasable ones.
        // 1 sold ones.
        var sellingStatus: Long? = 0L
        if (filter.sellingStatus == SellingStatus.PURCHASABLE) {
            sellingStatus = -1L
        }
        if (filter.sellingStatus == SellingStatus.ALL) {
            sellingStatus = null
        }
        if (filter.sellingStatus == SellingStatus.SOLD) {
            sellingStatus = 1L
        }

        var properties: List<Property>
        when (filter.sortType) {
            SortType.PRICE -> {
                if (filter.priceOrder == Order.ASC) {
                    properties = dao.getPropertyListPriceASC(
                        areaCode = filter.areaCodeFilter,
                        type = filter.typeHousing,
                        interestPoints = list,
                        minPhotos = filter.minNbrPhotos,
                        minAddedDate = filter.dateRange.lower,
                        maxAddedDate = filter.dateRange.upper,
                        minSoldDate = filter.soldDateRange.lower,
                        maxSoldDate = filter.soldDateRange.upper,
                        sellingStatus = sellingStatus,
                        minPrice = filter.priceRange.lower,
                        maxPrice = filter.priceRange.upper,
                        minSurface = filter.surfaceRange.lower,
                        maxSurface = filter.surfaceRange.upper,
                    ).map { it.toProperty() }
                } else {
                    properties = dao.getPropertyListPriceDESC(
                        areaCode = filter.areaCodeFilter,
                        type = filter.typeHousing,
                        interestPoints = list,
                        minPhotos = filter.minNbrPhotos,
                        minAddedDate = filter.dateRange.lower,
                        maxAddedDate = filter.dateRange.upper,
                        minSoldDate = filter.soldDateRange.lower,
                        maxSoldDate = filter.soldDateRange.upper,
                        sellingStatus = sellingStatus,
                        minPrice = filter.priceRange.lower,
                        maxPrice = filter.priceRange.upper,
                        minSurface = filter.surfaceRange.lower,
                        maxSurface = filter.surfaceRange.upper,
                    ).map { it.toProperty() }
                }
            }

            SortType.DATE -> {
                if (filter.dateOrder == Order.ASC) {
                    properties = dao.getPropertyListDateASC(
                        areaCode = filter.areaCodeFilter,
                        type = filter.typeHousing,
                        interestPoints = list,
                        minPhotos = filter.minNbrPhotos,
                        minAddedDate = filter.dateRange.lower,
                        maxAddedDate = filter.dateRange.upper,
                        minSoldDate = filter.soldDateRange.lower,
                        maxSoldDate = filter.soldDateRange.upper,
                        sellingStatus = sellingStatus,
                        minPrice = filter.priceRange.lower,
                        maxPrice = filter.priceRange.upper,
                        minSurface = filter.surfaceRange.lower,
                        maxSurface = filter.surfaceRange.upper,
                    ).map { it.toProperty() }
                } else {
                    properties = dao.getPropertyListDateDESC(
                        areaCode = filter.areaCodeFilter,
                        type = filter.typeHousing,
                        interestPoints = list,
                        minPhotos = filter.minNbrPhotos,
                        minAddedDate = filter.dateRange.lower,
                        maxAddedDate = filter.dateRange.upper,
                        minSoldDate = filter.soldDateRange.lower,
                        maxSoldDate = filter.soldDateRange.upper,
                        sellingStatus = sellingStatus,
                        minPrice = filter.priceRange.lower,
                        maxPrice = filter.priceRange.upper,
                        minSurface = filter.surfaceRange.lower,
                        maxSurface = filter.surfaceRange.upper,
                    ).map { it.toProperty() }
                }
            }

            SortType.AREA -> {
                if (filter.surfaceOrder == Order.ASC) {
                    properties = dao.getPropertyListSurfaceASC(
                        areaCode = filter.areaCodeFilter,
                        type = filter.typeHousing,
                        interestPoints = list,
                        minPhotos = filter.minNbrPhotos,
                        minAddedDate = filter.dateRange.lower,
                        maxAddedDate = filter.dateRange.upper,
                        minSoldDate = filter.soldDateRange.lower,
                        maxSoldDate = filter.soldDateRange.upper,
                        sellingStatus = sellingStatus,
                        minPrice = filter.priceRange.lower,
                        maxPrice = filter.priceRange.upper,
                        minSurface = filter.surfaceRange.lower,
                        maxSurface = filter.surfaceRange.upper,
                    ).map { it.toProperty() }
                } else {
                    properties = dao.getPropertyListSurfaceDESC(
                        areaCode = filter.areaCodeFilter,
                        type = filter.typeHousing,
                        interestPoints = list,
                        minPhotos = filter.minNbrPhotos,
                        minAddedDate = filter.dateRange.lower,
                        maxAddedDate = filter.dateRange.upper,
                        minSoldDate = filter.soldDateRange.lower,
                        maxSoldDate = filter.soldDateRange.upper,
                        sellingStatus = sellingStatus,
                        minPrice = filter.priceRange.lower,
                        maxPrice = filter.priceRange.upper,
                        minSurface = filter.surfaceRange.lower,
                        maxSurface = filter.surfaceRange.upper,
                    ).map { it.toProperty() }
                }
            }
        }


        return properties
    }


    override suspend fun getProperty(id: Long): Property {
        val propertyDetails = dao.getPropertyDetail(id)
        return propertyDetails.toProperty()
    }
}