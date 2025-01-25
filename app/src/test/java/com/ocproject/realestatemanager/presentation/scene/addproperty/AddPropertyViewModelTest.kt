package com.ocproject.realestatemanager.presentation.scene.addproperty

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.ExpectFailure.assertThat
import com.ocproject.realestatemanager.MainCoroutineRule
import com.ocproject.realestatemanager.data.repositories.FakePropertiesRepository
import com.ocproject.realestatemanager.data.repositories.LocalPropertiesRepository
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.usecases.GetPropertyDetailsUseCase
import com.ocproject.realestatemanager.domain.usecases.GetPropertyListUseCase
import com.ocproject.realestatemanager.domain.usecases.SavePropertyUseCase
import com.ocproject.realestatemanager.presentation.scene.propertydetails.PropertyDetailsState
import com.ocproject.realestatemanager.presentation.scene.propertylist.PropertyListState
import com.ocproject.realestatemanager.presentation.scene.propertylist.PropertyListViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

@OptIn(ExperimentalCoroutinesApi::class)
class AddPropertyViewModelTest {
    //goal 80% viewModel
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    val testDispatcher = StandardTestDispatcher()

    private var id: Long? = null
    private val getPropertyDetails = mockk<GetPropertyDetailsUseCase>(relaxed = true)
    private val saveProperty = mockk<SavePropertyUseCase>(relaxed = true)
    private lateinit var viewModel: AddPropertyViewModel
//    private val testScope = TestCoroutineScope(testDispatcher)

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
            sold = false,
            id = 1L,
        )
        coEvery { getPropertyDetails.invoke(any()) } returns property
        viewModel.getProperty()
        coVerify { getPropertyDetails.invoke(any()) }
//        assert(viewModel.newProperty.id == 1L)
    }


    /*
    @Test
    fun onEventOnAddressChanged() {


        // simuler un changement d'état et vérifier qu'il change bien
    }
    @Test
    fun onEventOnPriceChanged() {

        // test avec id property null et avec un id de property en bdd
        // simuler un changement d'état et vérifier qu'il change bien
    }*/

    // etc
}