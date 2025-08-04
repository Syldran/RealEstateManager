package com.ocproject.realestatemanager.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.ocproject.realestatemanager.core.InterestPoint
import com.ocproject.realestatemanager.data.repositories.FakePropertiesRepository
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.usecases.GetPropertyDetailsUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetPropertyDetailsUseCaseTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakePropertyRepository: FakePropertiesRepository
    private lateinit var getPropertyDetails: GetPropertyDetailsUseCase

    @Before
    fun setUp() {
        fakePropertyRepository = FakePropertiesRepository()
        getPropertyDetails = GetPropertyDetailsUseCase(fakePropertyRepository)
    }

    @Test
    fun `getPropertyDetails with existing propertyId returns correct property`() = runTest {
        fakePropertyRepository.shouldHaveFilledList(true)

        val property = getPropertyDetails.invoke(1L)

        assertThat(property).isEqualTo(
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
                type = "House",
                nbrRoom = 4,
                realEstateAgent = "John Doe",
            ),
        )
    }

    @Test
    fun `getPropertyDetails with non-existent propertyId returns default property`() = runTest {
        fakePropertyRepository.shouldHaveFilledList(true)

        val property = getPropertyDetails.invoke(999L)

        assertThat(property.id).isEqualTo(-1L)
        assertThat(property.description).isEmpty()
        assertThat(property.address).isEmpty()
        assertThat(property.town).isEmpty()
        assertThat(property.country).isEmpty()
        assertThat(property.price).isEqualTo(1)
        assertThat(property.surfaceArea).isEqualTo(0)
        assertThat(property.sold).isEqualTo(-1)
    }

    @Test
    fun `getPropertyDetails with different propertyId returns correct property`() = runTest {
        fakePropertyRepository.shouldHaveFilledList(true)

        val property = getPropertyDetails.invoke(2L)

        assertThat(property.id).isEqualTo(2L)
        assertThat(property.description).isEqualTo("Appartement moderne")
        assertThat(property.address).isEqualTo("456 Avenue des Champs")
        assertThat(property.town).isEqualTo("Lyon")
        assertThat(property.price).isEqualTo(280000)
        assertThat(property.sold).isEqualTo(1) // Sold
        assertThat(property.interestPoints).contains(InterestPoint.TRANSPORT)
        assertThat(property.interestPoints).contains(InterestPoint.SHOP)
    }

    @Test
    fun `getPropertyDetails from empty repository returns default property`() = runTest {
        fakePropertyRepository.shouldHaveFilledList(false)

        val property = getPropertyDetails.invoke(1L)

        assertThat(property.id).isEqualTo(-1L)
        assertThat(property.description).isEmpty()
        assertThat(property.address).isEmpty()
        assertThat(property.town).isEmpty()
        assertThat(property.country).isEmpty()
        assertThat(property.price).isEqualTo(1)
        assertThat(property.surfaceArea).isEqualTo(0)
        assertThat(property.sold).isEqualTo(-1)
    }
}