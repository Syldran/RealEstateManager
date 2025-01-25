package com.ocproject.realestatemanager.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.ocproject.realestatemanager.data.repositories.FakePropertiesRepository
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.usecases.GetPropertyDetailsUseCase
import com.ocproject.realestatemanager.domain.usecases.GetPropertyListUseCase
import kotlinx.coroutines.flow.toList
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
    fun `getPropertyDetails(propertyId) equal property with propertyId`() = runTest {
        fakePropertyRepository.shouldHaveFilledList(true)

        val property = getPropertyDetails.invoke(1L)

        assertThat(property).isEqualTo(
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
        )
    }

}