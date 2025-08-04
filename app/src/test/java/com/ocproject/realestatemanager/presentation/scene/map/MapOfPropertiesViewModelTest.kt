package com.ocproject.realestatemanager.presentation.scene.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ocproject.realestatemanager.MainCoroutineRule
import com.ocproject.realestatemanager.core.InterestPoint
import com.ocproject.realestatemanager.domain.models.Property
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MapOfPropertiesViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: MapOfPropertiesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        viewModel = MapOfPropertiesViewModel()

    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun `create markers test`() = runTest {
        val propertyList = listOf<Property>(
            Property(
                photoList = emptyList(),
                interestPoints = listOf(InterestPoint.SCHOOL),
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
                sold = -1,
                id = 1L,
            ),
            Property(
                photoList = emptyList(),
                interestPoints = listOf(InterestPoint.TRANSPORT),
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
                sold = -1,
                id = 2L,
            ),
            Property(
                photoList = emptyList(),
                interestPoints = listOf(InterestPoint.SCHOOL),
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
                sold = -1,
                id = 2L,
            )
        )
        viewModel.createMarkers(propertyList)
        advanceUntilIdle()
        assert(viewModel.markers.value.size == propertyList.size)
    }
}