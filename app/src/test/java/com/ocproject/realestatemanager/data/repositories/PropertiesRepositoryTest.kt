package com.ocproject.realestatemanager.data.repositories


import com.ocproject.realestatemanager.data.entities.PropertyEntity
import com.ocproject.realestatemanager.data.entities.PropertyWithPhotosEntity
import com.ocproject.realestatemanager.data.toPropertyEntity
import com.ocproject.realestatemanager.domain.models.Property
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test


class PropertiesRepositoryTest {
    val property = Property(
        photoList = emptyList(),
        interestPoints = emptyList(),
        address =  "Somewhere",
        town = "NowhereCity",
        lat =  120.5,
        lng = 50.30,
        "Faraway",
        createdDate =  null,
        areaCode = 18290,
        surfaceArea =  150,
        price =  150000,
        sold = false,
        id = 1L,
    )


    @Test
    fun delete_Property() = runTest {
        val repo = LocalPropertiesRepository(FakePropertiesDao())
        val propertyWithPhotos = PropertyWithPhotosEntity(property.toPropertyEntity(), null)
        repo.upsertProperty(property)
        assertEquals(propertyWithPhotos.property, repo.getPropertyList().first().toPropertyEntity())
        repo.deleteProperty(property)
        assert(repo.getPropertyList().isEmpty())


        //given
        //when
        //then
        // appel méthode repo et check méthode dao called once
//        verify(repository, times(1)).someMethod()
    }

    @Test
    fun insertPhotoProperty() {
    }

    @Test
    fun deletePicturesOfPropertyById() {
    }

    @Test
    fun getPropertyList() {
    }

    @Test
    fun getProperty() {
    }
}