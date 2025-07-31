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


@RunWith(AndroidJUnit4::class)
class DBTest {

    private lateinit var propertiesDao: PropertiesDao
    private lateinit var db: PropertiesDatabase

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
    fun upsertAndGetPropertyListTest() = runTest {
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
            sold = null,
            id = 50L,
        )
        propertiesDao.upsertProperty(property.toPropertyEntity())
        assert(propertiesDao.getPropertyDetail(50L)?.toProperty() == property)
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
            sold = null,
            id = 50L,
        )
        propertiesDao.upsertProperty(property.toPropertyEntity())
        assert(propertiesDao.getPropertyDetail(50L)?.toProperty() == property)

        propertiesDao.deleteProperty(property.toPropertyEntity())
        assert(propertiesDao.getPropertyList() == emptyList<PropertyWithPhotosEntity>())
    }

    @Test
    fun getPropertiesTest() = runTest {
        val propertiesToAdd = propertiesDao.upsertProperty(
            Property(
                photoList = emptyList(),
                interestPoints = emptyList(),
                description = "Description of a Somewhere",
                address = "Somewhere",
                town = "NowhereCity",
                lat = 120.5,
                lng = 50.30,
                country = "Faraway",
                createdDate = 500,
                areaCode = 18290,
                surfaceArea = 150,
                price = 150000,
                sold = null,
                id = 1L,
            ).toPropertyEntity()
        )
        val propertiesToAdd2 = propertiesDao.upsertProperty(
            Property(
                photoList = emptyList(),
                interestPoints = emptyList(),
                description = "Description of a Somewhere",
                address = "Somewhere",
                town = "Paris",
                lat = 120.5,
                lng = 50.30,
                country = "France",
                createdDate = 10000,
                areaCode = 18290,
                surfaceArea = 150,
                price = 300000,
                sold = null,
                id = 2L,
            ).toPropertyEntity()
        )
        val propertiesToAdd3 = propertiesDao.upsertProperty(
            Property(
                photoList = emptyList(),
                interestPoints = emptyList(),
                description = "Description of a Somewhere",
                address = "There",
                town = "London",
                lat = 120.5,
                lng = 50.30,
                country = "France",
                createdDate = 3000,
                areaCode = 18290,
                surfaceArea = 150,
                price = 250000,
                sold = null,
                id = 3L,
            ).toPropertyEntity()
        )
        assert(propertiesDao.getPropertyList().size == 3)

    }

    @Test
    fun upsertPhotoTest() = runTest {
        propertiesDao.upsertProperty(
            Property(
                photoList = emptyList(),
                interestPoints = emptyList(),
                description = "Description of a Somewhere",
                address = "Somewhere",
                town = "Paris",
                lat = 120.5,
                lng = 50.30,
                country = "France",
                createdDate = 10000,
                areaCode = 18290,
                surfaceArea = 150,
                price = 300000,
                sold = null,
                id = 2L,
            ).toPropertyEntity()
        )
        propertiesDao.upsertPhoto(
            PhotoProperty(
                isMain = true,
                name = "test0",
                byteArrayOf(),
                id = 1L
            ).toPhotoPropertyEntity(2L)
        )
        propertiesDao.upsertPhoto(
            PhotoProperty(
                isMain = false,
                name = "test1",
                byteArrayOf(),
                id = 2L
            ).toPhotoPropertyEntity(2L)
        )
        propertiesDao.upsertPhoto(
            PhotoProperty(
                isMain = false,
                name = "test2",
                byteArrayOf(),
                id = 3L
            ).toPhotoPropertyEntity(2L)
        )
        assert(
            propertiesDao.getPropertyDetail(2L)?.toProperty()?.photoList?.size == 3
        )
    }

    @Test
    fun deletePhotosOfPropertyTest() = runTest {
        propertiesDao.upsertProperty(
            Property(
                photoList = emptyList(),
                interestPoints = emptyList(),
                description = "Description of a Somewhere",
                address = "Somewhere",
                town = "Paris",
                lat = 120.5,
                lng = 50.30,
                country = "France",
                createdDate = 10000,
                areaCode = 18290,
                surfaceArea = 150,
                price = 300000,
                sold = null,
                id = 2L,
            ).toPropertyEntity()
        )
        propertiesDao.upsertPhoto(
            PhotoProperty(
                isMain = true,
                name = "test0",
                byteArrayOf(),
                id = 1L
            ).toPhotoPropertyEntity(2L)
        )
        propertiesDao.upsertPhoto(
            PhotoProperty(
                isMain = false,
                name = "test1",
                byteArrayOf(),
                id = 2L
            ).toPhotoPropertyEntity(2L)
        )
        propertiesDao.upsertPhoto(
            PhotoProperty(
                isMain = false,
                name = "test2",
                byteArrayOf(),
                id = 3L
            ).toPhotoPropertyEntity(2L)
        )
        propertiesDao.deletePicturesOfPropertyByIdProperty(2L)
        assert(
            propertiesDao.getPropertyDetail(2L).toProperty().photoList.isEmpty()
        )
    }

}