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
import java.util.Calendar

class DeletePropertyUseCaseTest {
    // same property is already in repository
    val property = Property(
        photoList = emptyList(),
        interestPoints = emptyList(),
        address =  "Somewhere",
        description = "Description of a Somewhere",
        town = "NowhereCity",
        lat =  120.5,
        lng = 50.30,
        country = "Faraway",
        createdDate = Calendar.getInstance().timeInMillis,
        areaCode = 18290,
        surfaceArea =  150,
        price =  150000,
        sold = null,
        id = 1L,
    )

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakePropertyRepository: FakePropertiesRepository
    private lateinit var deleteProperty: DeletePropertyUseCase

    @Before
    fun setUp() {
        fakePropertyRepository = FakePropertiesRepository()
        deleteProperty = DeletePropertyUseCase(fakePropertyRepository)
        fakePropertyRepository.shouldHaveFilledList(true)
    }

    @Test
    fun `delete property from list, property is deleted`() = runTest {
        deleteProperty.invoke(property)
        assertThat(
            !fakePropertyRepository.getPropertyList().contains(property)
        )
    }
}