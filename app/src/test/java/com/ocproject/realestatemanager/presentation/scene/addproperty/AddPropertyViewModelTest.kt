package com.ocproject.realestatemanager.presentation.scene.addproperty

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ocproject.realestatemanager.MainCoroutineRule
import com.ocproject.realestatemanager.core.InterestPoint
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.usecases.GetPropertyDetailsUseCase
import com.ocproject.realestatemanager.domain.usecases.SavePropertyUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
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
import java.util.Calendar

@OptIn(ExperimentalCoroutinesApi::class)
class AddPropertyViewModelTest {
    //goal 80% viewModel
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    val testDispatcher = StandardTestDispatcher()

    private val getPropertyDetails = mockk<GetPropertyDetailsUseCase>(relaxed = true)
    private val saveProperty = mockk<SavePropertyUseCase>(relaxed = true)
    private lateinit var viewModel: AddPropertyViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        viewModel = AddPropertyViewModel(1L, getPropertyDetails, saveProperty)

    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get Property, editing case`() = runTest {
        //Editing case so property id as existing value
        val property = Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
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
        )
        coEvery { getPropertyDetails.invoke(any()) } returns property
        viewModel.getProperty()
        advanceUntilIdle()
        coVerify { getPropertyDetails.invoke(any()) }
        assert(viewModel.state.value.newProperty.id == 1L)
    }

    @Test
    fun `save property test`() = runTest {
        viewModel.saveProperty(
            Property(
                photoList = emptyList(),
                interestPoints = emptyList(),
                address = "Somewhere",
                town = "Paris",
                lat = 120.5,
                lng = 50.30,
                country = "France",
                createdDate = 10000,
                areaCode = 18290,
                surfaceArea = 150,
                price = 300000,
                sold =null,
                id = 2L,
            )
        )
        advanceUntilIdle()
        coVerify { saveProperty(any()) }
    }

    @Test
    fun `update form test`() = runTest {
        coEvery { getPropertyDetails(1L) } returns Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            address = "",
            town = "",
            lat = 0.0,
            lng = 0.0,
            country = "",
            createdDate = Calendar.getInstance().timeInMillis,
            areaCode = null,
            surfaceArea = null,
            price = null,
            id = 1L,
            sold = null,
        )
        val viewModelTest = AddPropertyViewModel(1L, getPropertyDetails, saveProperty)
        advanceUntilIdle()
        viewModelTest.updateForm(AddPropertyEvent.UpdateForm(town = "Paris"))
        advanceUntilIdle()
        val town = viewModelTest.state.value.newProperty.town
        assertEquals("Paris", town)
    }


    @Test
    fun `update tags test`() = runTest {

        coEvery { getPropertyDetails(1L) } returns Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            address = "",
            town = "",
            lat = 0.0,
            lng = 0.0,
            country = "",
            createdDate = Calendar.getInstance().timeInMillis,
            areaCode = null,
            surfaceArea = null,
            price = null,
            id = 1L,
            sold = null,
        )
        val viewModelTest = AddPropertyViewModel(1L, getPropertyDetails, saveProperty)
        advanceUntilIdle()

        viewModelTest.updateTags(
            AddPropertyEvent.UpdateTags(
                school = true,
                shop = false,
                park = false,
                transport = false,
            )
        )
        advanceUntilIdle()
        val interestPoint = viewModelTest.state.value.newProperty.interestPoints
        assert(interestPoint.contains(InterestPoint.SCHOOL))
    }
}