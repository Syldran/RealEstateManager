package com.ocproject.realestatemanager.data.repositories


import com.ocproject.realestatemanager.data.entities.PropertyEntity
import com.ocproject.realestatemanager.data.entities.PropertyWithPhotosEntity
import com.ocproject.realestatemanager.data.toPropertyEntity
import com.ocproject.realestatemanager.domain.models.Property
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar


class PropertiesRepositoryTest {
    val property = Property(
        id = 1L,
        photoList = emptyList(),
        interestPoints = emptyList(),
        address =  "Somewhere",
        town = "NowhereCity",
        lat =  120.5,
        lng = 50.30,
        country = "Faraway",
        createdDate = 50000,
        areaCode = 18290,
        surfaceArea =  150,
        price =  150000,
        sold = null,

    )
    val property2 = Property(
        id = 2L,
        photoList = emptyList(),
        interestPoints = emptyList(),
        address =  "Somewhere",
        town = "NowhereCity",
        lat =  120.5,
        lng = 50.30,
        country = "Faraway",
        createdDate = 50000,
        areaCode = 85140,
        surfaceArea =  150,
        price =  110000,
        sold = null,
        )

    @Test
    fun addPropertyTest() = runTest {
        val repo = LocalPropertiesRepository(FakePropertiesDao())
        val propertyWithPhotos = PropertyWithPhotosEntity(property.toPropertyEntity(), emptyList())
        repo.upsertProperty(property)
        assertEquals(propertyWithPhotos.property, repo.getPropertyList().first().toPropertyEntity())
    }

        @Test
    fun deletePropertyTest() = runTest {
        val repo = LocalPropertiesRepository(FakePropertiesDao())
        val propertyWithPhotos = PropertyWithPhotosEntity(property.toPropertyEntity(), emptyList())
        repo.upsertProperty(property)
        assertEquals(propertyWithPhotos.property, repo.getPropertyList().first().toPropertyEntity())
        repo.deleteProperty(property)
        assert(repo.getPropertyList().isEmpty())
    }




    @Test
    fun getPropertyListTest() = runTest {

        val repo = LocalPropertiesRepository(FakePropertiesDao())
        repo.upsertProperty(property)
        repo.upsertProperty(property2)
        assert( repo.getPropertyList().size == 2)

    }

    @Test
    fun getPropertyTest() = runTest {
        val repo = LocalPropertiesRepository(FakePropertiesDao())
        repo.upsertProperty(property)
        repo.upsertProperty(property2)
        assert(repo.getPropertyList().last().id == repo.getProperty(2L).id)
    }
}