package com.ocproject.realestatemanager.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.ocproject.realestatemanager.data.repositories.FakePropertiesRepository
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.usecases.DeletePropertyUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DeletePropertyUseCaseTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakePropertyRepository: FakePropertiesRepository
    private lateinit var deleteProperty: DeletePropertyUseCase

    private val testProperty = Property(
        photoList = emptyList(),
        interestPoints = emptyList(),
        address = "Somewhere",
        description = "Description of a Somewhere",
        town = "NowhereCity",
        lat = 120.5,
        lng = 50.30,
        country = "Faraway",
        createdDate = 1700000000000L, // Fixed timestamp instead of Calendar.getInstance()
        areaCode = 18290,
        surfaceArea = 150,
        price = 150000,
        sold = -1,
        id = 1L,
    )

    @Before
    fun setUp() {
        fakePropertyRepository = FakePropertiesRepository()
        deleteProperty = DeletePropertyUseCase(fakePropertyRepository)
        fakePropertyRepository.shouldHaveFilledList(true)
    }

    @Test
    fun `delete property from list, property is deleted`() = runTest {
        val initialCount = fakePropertyRepository.getPropertiesCount()
        
        deleteProperty.invoke(testProperty)
        
        assertThat(fakePropertyRepository.getPropertiesCount()).isEqualTo(initialCount - 1)
        assertThat(fakePropertyRepository.getPropertyById(testProperty.id)).isNull()
    }

    @Test
    fun `delete non-existent property, no error occurs`() = runTest {
        val nonExistentProperty = testProperty.copy(id = 999L)
        val initialCount = fakePropertyRepository.getPropertiesCount()
        
        deleteProperty.invoke(nonExistentProperty)
        
        assertThat(fakePropertyRepository.getPropertiesCount()).isEqualTo(initialCount)
    }

    @Test
    fun `delete property with different id, correct property is deleted`() = runTest {
        val propertyToDelete = Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            address = "Test Address",
            description = "Test Description",
            town = "Test Town",
            lat = 0.0,
            lng = 0.0,
            country = "Test Country",
            createdDate = 1700000000000L,
            areaCode = 12345,
            surfaceArea = 100,
            price = 200000,
            sold = -1,
            id = 2L,
        )
        
        val initialCount = fakePropertyRepository.getPropertiesCount()
        
        deleteProperty.invoke(propertyToDelete)
        
        assertThat(fakePropertyRepository.getPropertiesCount()).isEqualTo(initialCount - 1)
        assertThat(fakePropertyRepository.getPropertyById(2L)).isNull()
        // Verify other properties still exist
        assertThat(fakePropertyRepository.getPropertyById(1L)).isNotNull()
    }
}