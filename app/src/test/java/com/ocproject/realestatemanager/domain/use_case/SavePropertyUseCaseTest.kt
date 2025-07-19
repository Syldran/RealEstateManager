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
    fun `save Property`() = runTest {
        fakePropertyRepository.shouldHaveFilledList(true)
        val property =    Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            address = "Somewhere",
            town = "NowhereCity",
            lat = 120.5,
            lng = 50.30,
            country = "Faraway",
            createdDate = 500L,
            areaCode = 18290,
            surfaceArea = 150,
            price = 150000,
            sold = null,
            id = 10L,
        )
        saveProperty.invoke(
         property
        )

        assertThat(fakePropertyRepository.getPropertyList().contains(property)).isTrue()
    }
}