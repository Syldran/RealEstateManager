package com.ocproject.realestatemanager.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.ocproject.realestatemanager.data.repositories.FakePropertiesRepository
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.usecases.SavePropertyUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SavePropertyUseCaseTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakePropertyRepository: FakePropertiesRepository
    private lateinit var saveProperty: SavePropertyUseCase

    @Before
    fun setUp() {
        fakePropertyRepository = FakePropertiesRepository()
        saveProperty = SavePropertyUseCase(fakePropertyRepository)
    }

    @Test
    fun `save new property returns correct id`() = runTest {
        fakePropertyRepository.shouldHaveFilledList(true)
        
        val newProperty = Property(
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
            surfaceArea = 150,
            price = 150000,
            sold = -1,
            id = -1L, // New property
        )
        
        val resultId = saveProperty.invoke(newProperty)
        
        assertThat(resultId).isGreaterThan(0L)
        assertThat(fakePropertyRepository.getPropertyList().contains(newProperty.copy(id = resultId))).isTrue()
    }

    @Test
    fun `save existing property updates it`() = runTest {
        fakePropertyRepository.shouldHaveFilledList(true)
        
        val existingProperty = Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            description = "Updated Description",
            address = "Updated Address",
            town = "Updated Town",
            lat = 45.0,
            lng = 45.0,
            country = "Updated Country",
            createdDate = 1000L,
            areaCode = 12345,
            surfaceArea = 200,
            price = 300000,
            sold = 1,
            id = 1L, // Existing property
        )
        
        val resultId = saveProperty.invoke(existingProperty)
        
        assertThat(resultId).isEqualTo(1L)
        assertThat(fakePropertyRepository.getPropertyList().contains(existingProperty)).isTrue()
    }

    @Test
    fun `save property to empty repository assigns correct id`() = runTest {
        fakePropertyRepository.shouldHaveFilledList(false)
        
        val newProperty = Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            description = "First Property",
            address = "First Address",
            town = "First Town",
            lat = 0.0,
            lng = 0.0,
            country = "First Country",
            createdDate = 1000L,
            areaCode = 12345,
            surfaceArea = 100,
            price = 100000,
            sold = -1,
            id = -1L,
        )
        
        val resultId = saveProperty.invoke(newProperty)
        
        assertThat(resultId).isEqualTo(1L)
        assertThat(fakePropertyRepository.getPropertiesCount()).isEqualTo(1)
    }

    @Test
    fun `save multiple properties assigns sequential ids`() = runTest {
        fakePropertyRepository.shouldHaveFilledList(false)
        
        val property1 = Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            description = "Property 1",
            address = "Address 1",
            town = "Town 1",
            lat = 0.0,
            lng = 0.0,
            country = "Country 1",
            createdDate = 1000L,
            areaCode = 12345,
            surfaceArea = 100,
            price = 100000,
            sold = -1,
            id = -1L,
        )
        
        val property2 = Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            description = "Property 2",
            address = "Address 2",
            town = "Town 2",
            lat = 0.0,
            lng = 0.0,
            country = "Country 2",
            createdDate = 2000L,
            areaCode = 54321,
            surfaceArea = 200,
            price = 200000,
            sold = -1,
            id = -1L,
        )
        
        val id1 = saveProperty.invoke(property1)
        val id2 = saveProperty.invoke(property2)
        
        assertThat(id1).isEqualTo(1L)
        assertThat(id2).isEqualTo(2L)
        assertThat(fakePropertyRepository.getPropertiesCount()).isEqualTo(2)
    }
}