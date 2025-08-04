package com.ocproject.testdb


import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ocproject.realestatemanager.data.database.PropertiesDao
import com.ocproject.realestatemanager.data.database.PropertiesDatabase
import com.ocproject.realestatemanager.data.entities.PropertyWithPhotosEntity
import com.ocproject.realestatemanager.data.toPhotoPropertyEntity
import com.ocproject.realestatemanager.data.toProperty
import com.ocproject.realestatemanager.data.toPropertyEntity
import com.ocproject.realestatemanager.domain.models.PhotoProperty
import com.ocproject.realestatemanager.domain.models.Property
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar
import kotlin.assert


@RunWith(AndroidJUnit4::class)
class DBTest {

    private lateinit var propertiesDao: PropertiesDao
    private lateinit var db: PropertiesDatabase

    var propertiesToAdd = listOf(
        Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            description = "Description of a Somewhere",
            address = "Somewhere",
            town = "NowhereCity",
            lat = 120.5,
            lng = 50.30,
            country = "Faraway",
            createdDate = 500L,
            areaCode = 18290,
            surfaceArea = 0,
            price = 150000,
            sold = 5000L,
            id = 1L,
            type = "House",
            nbrRoom = 3,
            realEstateAgent = "Agent 1",
        ).toPropertyEntity(),
        Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            description = "Description of a Somewhere",
            address = "Somewhere",
            town = "Paris",
            lat = 120.5,
            lng = 50.30,
            country = "France",
            createdDate = 10000L,
            areaCode = 75500,
            surfaceArea = 300,
            price = 300000,
            sold = -1,
            id = 2L,
            type = "Apartment",
            nbrRoom = 2,
            realEstateAgent = "Agent 2",
        ).toPropertyEntity(),
        Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            description = "Description of a Somewhere",
            address = "There",
            town = "London",
            lat = 120.5,
            lng = 50.30,
            country = "France",
            createdDate = 3000L,
            areaCode = 21850,
            surfaceArea = 150,
            price = 250000,
            sold = 1500000L,
            id = 3L,
            type = "House",
            nbrRoom = 4,
            realEstateAgent = "Agent 3",
        ).toPropertyEntity(),
        Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            description = "Description of a Somewhere",
            address = "Somewhere",
            town = "NowhereCity",
            lat = 120.5,
            lng = 50.30,
            country = "Faraway",
            createdDate = 600L,
            areaCode = 92720,
            surfaceArea = 90,
            price = 150000,
            sold = 1000000L,
            id = 4L,
            type = "Studio",
            nbrRoom = 1,
            realEstateAgent = "Agent 4",
        ).toPropertyEntity(),

        )
    var photosToAdd = listOf(
        PhotoProperty(
            isMain = true,
            name = "test0",
            byteArrayOf(),
            id = 1L
        ).toPhotoPropertyEntity(2L),
        PhotoProperty(
            isMain = false,
            name = "test1",
            byteArrayOf(),
            id = 2L
        ).toPhotoPropertyEntity(2L),
        PhotoProperty(
            isMain = false,
            name = "test2",
            byteArrayOf(),
            id = 3L
        ).toPhotoPropertyEntity(2L)

    )

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            PropertiesDatabase::class.java
        ).allowMainThreadQueries()
            .build()
        propertiesDao = db.dao
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun upsertAndGetSpecificPropertyTest() = runTest {
        val property = Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            address = "Somewhere",
            description = "Description of a Somewhere",
            town = "NowhereCity",
            lat = 120.5,
            lng = 50.30,
            country = "Faraway",
            createdDate = Calendar.getInstance().timeInMillis,
            areaCode = 18290,
            surfaceArea = 150,
            price = 150000,
            sold = -1,
            id = 50L,
            type = "House",
            nbrRoom = 3,
            realEstateAgent = "Test Agent",
        )
        propertiesDao.upsertProperty(property.toPropertyEntity())
        assert(propertiesDao.getPropertyDetail(50L).toProperty() == property)
    }

    @Test
    fun deletePropertiesTest() = runTest {
        val property = Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            description = "Description of a Somewhere",
            address = "Somewhere",
            town = "NowhereCity",
            lat = 120.5,
            lng = 50.30,
            country = "Faraway",
            createdDate = Calendar.getInstance().timeInMillis,
            areaCode = 18290,
            surfaceArea = 150,
            price = 150000,
            sold = -1,
            id = 50L,
            type = "House",
            nbrRoom = 3,
            realEstateAgent = "Test Agent",
        )
        propertiesDao.upsertProperty(property.toPropertyEntity())
        assert(propertiesDao.getPropertyDetail(50L).toProperty() == property)

        propertiesDao.deleteProperty(property.toPropertyEntity())
        assert(propertiesDao.getPropertyList() == emptyList<PropertyWithPhotosEntity>())
    }

    @Test
    fun dateRangeParamTest() = runTest {
        propertiesToAdd.forEach {
            propertiesDao.upsertProperty(it)
        }

        val result = propertiesDao.getPropertyListPriceASC(
            areaCode = null,
            type = null,
            interestPoints = null,
            minPhotos = 0,
            minAddedDate = 450L,
            maxAddedDate = 550L,
            minSoldDate = 0L,
            maxSoldDate = Long.MAX_VALUE,
            sellingStatus = null,
            minPrice = 0,
            maxPrice = Int.MAX_VALUE,
            minSurface = 0,
            maxSurface = Int.MAX_VALUE,
        )

        assert(result.size == 1)
        assert(result[0].property.createdDate in 450L.. 550L )
    }

    @Test
    fun soldDateRangeParamTest() = runTest {
        propertiesToAdd.forEach {
            propertiesDao.upsertProperty(it)
        }
        val result = propertiesDao.getPropertyListPriceASC(
            areaCode = null,
            type = null,
            interestPoints = null,
            minPhotos = 0,
            minAddedDate = 0L,
            maxAddedDate = Long.MAX_VALUE,
            minSoldDate = 999999L,
            maxSoldDate = 1000001L,
            sellingStatus = 1,
            minPrice = 0,
            maxPrice = Int.MAX_VALUE,
            minSurface = 0,
            maxSurface = Int.MAX_VALUE,
        )

        assert(result.size == 1)
        assert(result[0].property.soldDate in 999999L.. 1000001L )
    }

    @Test
    fun minPhotosParamTest() = runTest {
        propertiesToAdd.forEach {
            propertiesDao.upsertProperty(it)
        }
        photosToAdd.forEach {
            propertiesDao.upsertPhoto(it)
        }
        val result = propertiesDao.getPropertyListPriceASC(
            areaCode = null,
            type = null,
            interestPoints = null,
            minPhotos = 1,
            minAddedDate = 0L,
            maxAddedDate = Long.MAX_VALUE,
            minSoldDate = 0L,
            maxSoldDate = Long.MAX_VALUE,
            sellingStatus = null,
            minPrice = 0,
            maxPrice = Int.MAX_VALUE,
            minSurface = 0,
            maxSurface = Int.MAX_VALUE,
        )
        // Property id = 2L is the only one with photos.
        assert(result.size == 1)
        result[0].photoList?.size?.let { assert(it >= 1) }
    }

    @Test
    fun areaCodeParamTest() = runTest {
        propertiesToAdd.forEach {
            propertiesDao.upsertProperty(it)
        }
        val result = propertiesDao.getPropertyListPriceASC(
            areaCode = 92720,
            type = null,
            interestPoints = null,
            minPhotos = 0,
            minAddedDate = 0L,
            maxAddedDate = Long.MAX_VALUE,
            minSoldDate = 0L,
            maxSoldDate = Long.MAX_VALUE,
            sellingStatus = null, // SellingStatus.ALL
            minPrice = 0,
            maxPrice = Int.MAX_VALUE,
            minSurface = 0,
            maxSurface = Int.MAX_VALUE,
        )
        // In our 4 properties only one (id = 4 ) has code 92720
        assert(result.size == 1)
        assert(result[0].property.areaCode == 92720)
    }

    @Test
    fun typeParamTest() = runTest {
        propertiesToAdd.forEach {
            propertiesDao.upsertProperty(it)
        }
        val result = propertiesDao.getPropertyListPriceASC(
            areaCode = null,
            type = "Apartment",
            interestPoints = null,
            minPhotos = 0,
            minAddedDate = 0L,
            maxAddedDate = Long.MAX_VALUE,
            minSoldDate = 0L,
            maxSoldDate = Long.MAX_VALUE,
            sellingStatus = null, // SellingStatus.ALL
            minPrice = 0,
            maxPrice = Int.MAX_VALUE,
            minSurface = 0,
            maxSurface = Int.MAX_VALUE,
        )
        // In our 4 properties only one (id = 4 ) has code 92720
        assert(result.size == 1)
        assert(result[0].property.type == "Apartment")
    }

    

    @Test
    fun getPropertiesPriceAscTest() = runTest {
        propertiesToAdd.forEach {
            propertiesDao.upsertProperty(it)
        }
        val result = propertiesDao.getPropertyListPriceASC(
            areaCode = null, // Any area codes
            type = null,
            interestPoints = null,
            minPhotos = 0,
            minAddedDate = 0L,
            maxAddedDate = Long.MAX_VALUE,
            minSoldDate = 0L,
            maxSoldDate = Long.MAX_VALUE,
            sellingStatus = null, // SellingStatus.ALL
            minPrice = 0,
            maxPrice = Int.MAX_VALUE,
            minSurface = 0,
            maxSurface = Int.MAX_VALUE,
        )
        assert(result.size == 4)
        assert(result[0].property.price <= result[1].property.price)
        assert(result[1].property.price <= result[2].property.price)
        assert(result[2].property.price <= result[3].property.price)
    }

    @Test
    fun getPropertiesPriceDescTest() = runTest {
        propertiesToAdd.forEach {
            propertiesDao.upsertProperty(it)
        }
        val result = propertiesDao.getPropertyListPriceDESC(
            areaCode = null, // Any area codes
            type = null,
            interestPoints = null,
            minPhotos = 0,
            minAddedDate = 0L,
            maxAddedDate = Long.MAX_VALUE,
            minSoldDate = 0L,
            maxSoldDate = Long.MAX_VALUE,
            sellingStatus = null, // SellingStatus.ALL
            minPrice = 0,
            maxPrice = Int.MAX_VALUE,
            minSurface = 0,
            maxSurface = Int.MAX_VALUE,
        )
        assert(result.size == 4)
        assert(result[0].property.price >= result[1].property.price)
        assert(result[1].property.price >= result[2].property.price)
        assert(result[2].property.price >= result[3].property.price)
    }


    @Test
    fun getPropertiesDateAscTest() = runTest {
        propertiesToAdd.forEach {
            propertiesDao.upsertProperty(it)
        }
        val result = propertiesDao.getPropertyListDateASC(
            areaCode = null, // Any area codes
            type = null,
            interestPoints = null,
            minPhotos = 0,
            minAddedDate = 0L,
            maxAddedDate = Long.MAX_VALUE,
            minSoldDate = 0L,
            maxSoldDate = Long.MAX_VALUE,
            sellingStatus = null, // SellingStatus.ALL
            minPrice = 0,
            maxPrice = Int.MAX_VALUE,
            minSurface = 0,
            maxSurface = Int.MAX_VALUE,
        )
        assert(result.size == 4)
        assert(result[0].property.createdDate <= result[1].property.createdDate)
        assert(result[1].property.createdDate <= result[2].property.createdDate)
        assert(result[2].property.createdDate <= result[3].property.createdDate)
    }

    @Test
    fun getPropertiesDateDescTest() = runTest {
        propertiesToAdd.forEach {
            propertiesDao.upsertProperty(it)
        }
        val result = propertiesDao.getPropertyListDateDESC(
            areaCode = null, // Any area codes
            type = null,
            interestPoints = null,
            minPhotos = 0,
            minAddedDate = 0L,
            maxAddedDate = Long.MAX_VALUE,
            minSoldDate = 0L,
            maxSoldDate = Long.MAX_VALUE,
            sellingStatus = null, // SellingStatus.ALL
            minPrice = 0,
            maxPrice = Int.MAX_VALUE,
            minSurface = 0,
            maxSurface = Int.MAX_VALUE,
        )
        assert(result.size == 4)
        assert(result[0].property.createdDate >= result[1].property.createdDate)
        assert(result[1].property.createdDate >= result[2].property.createdDate)
        assert(result[2].property.createdDate >= result[3].property.createdDate)
    }

    @Test
    fun getPropertiesSurfaceAscTest() = runTest {
        propertiesToAdd.forEach {
            propertiesDao.upsertProperty(it)
        }
        val result = propertiesDao.getPropertyListSurfaceASC(
            areaCode = null, // Any area codes
            type = null,
            interestPoints = null,
            minPhotos = 0,
            minAddedDate = 0L,
            maxAddedDate = Long.MAX_VALUE,
            minSoldDate = 0L,
            maxSoldDate = Long.MAX_VALUE,
            sellingStatus = null, // SellingStatus.ALL
            minPrice = 0,
            maxPrice = Int.MAX_VALUE,
            minSurface = 0,
            maxSurface = Int.MAX_VALUE,
        )
        assert(result.size == 4)
        assert(result[0].property.surfaceArea <= result[1].property.surfaceArea)
        assert(result[1].property.surfaceArea <= result[2].property.surfaceArea)
        assert(result[2].property.surfaceArea <= result[3].property.surfaceArea)
    }

    @Test
    fun getPropertiesSurfaceDescTest() = runTest {
        propertiesToAdd.forEach {
            propertiesDao.upsertProperty(it)
        }
        val result = propertiesDao.getPropertyListSurfaceDESC(
            areaCode = null, // Any area codes
            type = null,
            interestPoints = null,
            minPhotos = 0,
            minAddedDate = 0L,
            maxAddedDate = Long.MAX_VALUE,
            minSoldDate = 0L,
            maxSoldDate = Long.MAX_VALUE,
            sellingStatus = null, // SellingStatus.ALL
            minPrice = 0,
            maxPrice = Int.MAX_VALUE,
            minSurface = 0,
            maxSurface = Int.MAX_VALUE,
        )
        assert(result.size == 4)
        assert(result[0].property.surfaceArea >= result[1].property.surfaceArea)
        assert(result[1].property.surfaceArea >= result[2].property.surfaceArea)
        assert(result[2].property.surfaceArea >= result[3].property.surfaceArea)
    }

    @Test
    fun getPropertiesTest() = runTest {
        propertiesToAdd.forEach {
            propertiesDao.upsertProperty(it)
        }
        assert(propertiesDao.getPropertyList().size == 4)
    }

    @Test
    fun upsertPhotoTest() = runTest {
        // Add property with id == 2L
        propertiesDao.upsertProperty(
            propertiesToAdd[1]
        )
        photosToAdd.forEach {
            propertiesDao.upsertPhoto(it)
        }
        assert(
            propertiesDao.getPropertyDetail(2L).toProperty().photoList.size == 3
        )
    }

    @Test
    fun deletePhotosOfPropertyTest() = runTest {
        // Add property with id == 2L
        propertiesDao.upsertProperty(
            propertiesToAdd[1]
        )
        photosToAdd.forEach {
            propertiesDao.upsertPhoto(it)
        }
        propertiesDao.deletePicturesOfPropertyByIdProperty(2L)
        assert(
            propertiesDao.getPropertyDetail(2L).toProperty().photoList.isEmpty()
        )
    }

    @Test
    fun priceRangeFilterTest() = runTest {
        propertiesToAdd.forEach {
            propertiesDao.upsertProperty(it)
        }
        val result = propertiesDao.getPropertyListPriceASC(
            areaCode = null,
            type = null,
            interestPoints = null,
            minPhotos = 0,
            minAddedDate = 0L,
            maxAddedDate = Long.MAX_VALUE,
            minSoldDate = 0L,
            maxSoldDate = Long.MAX_VALUE,
            sellingStatus = null,
            minPrice = 200000,
            maxPrice = 350000,
            minSurface = 0,
            maxSurface = Int.MAX_VALUE,
        )
        // Only properties with price between 200k and 350k should be returned
        assert(result.size == 2)
        assert(result.all { it.property.price in 200000..350000 })
    }

    @Test
    fun surfaceAreaFilterTest() = runTest {
        propertiesToAdd.forEach {
            propertiesDao.upsertProperty(it)
        }
        val result = propertiesDao.getPropertyListPriceASC(
            areaCode = null,
            type = null,
            interestPoints = null,
            minPhotos = 0,
            minAddedDate = 0L,
            maxAddedDate = Long.MAX_VALUE,
            minSoldDate = 0L,
            maxSoldDate = Long.MAX_VALUE,
            sellingStatus = null,
            minPrice = 0,
            maxPrice = Int.MAX_VALUE,
            minSurface = 100,
            maxSurface = 200,
        )
        // Only properties with surface area between 100 and 200 should be returned
        assert(result.size == 1)
        assert(result[0].property.surfaceArea in 100..200)
    }

    @Test
    fun soldPropertiesFilterTest() = runTest {
        propertiesToAdd.forEach {
            propertiesDao.upsertProperty(it)
        }
        val result = propertiesDao.getPropertyListPriceASC(
            areaCode = null,
            type = null,
            interestPoints = null,
            minPhotos = 0,
            minAddedDate = 0L,
            maxAddedDate = Long.MAX_VALUE,
            minSoldDate = 0L,
            maxSoldDate = Long.MAX_VALUE,
            sellingStatus = 1L, // Only sold properties
            minPrice = 0,
            maxPrice = Int.MAX_VALUE,
            minSurface = 0,
            maxSurface = Int.MAX_VALUE,
        )
        // Only properties with soldDate > 0 should be returned
        assert(result.size == 3)
        assert(result.all { it.property.soldDate > 0 })
    }

    @Test
    fun unsoldPropertiesFilterTest() = runTest {
        propertiesToAdd.forEach {
            propertiesDao.upsertProperty(it)
        }
        val result = propertiesDao.getPropertyListPriceASC(
            areaCode = null,
            type = null,
            interestPoints = null,
            minPhotos = 0,
            minAddedDate = 0L,
            maxAddedDate = Long.MAX_VALUE,
            minSoldDate = 0L,
            maxSoldDate = Long.MAX_VALUE,
            sellingStatus = -1L, // Only unsold properties
            minPrice = 0,
            maxPrice = Int.MAX_VALUE,
            minSurface = 0,
            maxSurface = Int.MAX_VALUE,
        )
        // Only properties with soldDate = -1 should be returned
        assert(result.size == 1)
        assert(result[0].property.soldDate == -1L)
    }

}