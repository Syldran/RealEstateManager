package com.ocproject.realestatemanager.presentation.scene.propertylist


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertEquals
import com.ocproject.realestatemanager.MainCoroutineRule
import com.ocproject.realestatemanager.core.DataState
import com.ocproject.realestatemanager.core.Filter
import com.ocproject.realestatemanager.core.Order
import com.ocproject.realestatemanager.core.SellingStatus
import com.ocproject.realestatemanager.core.SortType
import com.ocproject.realestatemanager.core.utils.Range
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.usecases.DeletePropertyUseCase
import com.ocproject.realestatemanager.domain.usecases.GetPropertyDetailsUseCase
import com.ocproject.realestatemanager.domain.usecases.GetPropertyListFilteredUseCase
import com.ocproject.realestatemanager.domain.usecases.GetPropertyListUseCase
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsEvent
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsState
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsViewModel
import io.mockk.coEvery
import io.mockk.coVerify
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

@OptIn(ExperimentalCoroutinesApi::class)
class ListDetailsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Use fixed timestamps for consistent testing
    private val fixedTimestamp = 1700000000000L // Fixed timestamp for testing
    private val mockRangeInt = Range(0, Int.MAX_VALUE)
    private val mockRangeLong = Range(0L, fixedTimestamp + 12583060)

    val testDispatcher = StandardTestDispatcher()
    val getPropertyList = mockk<GetPropertyListUseCase>(relaxed = true)
    val getPropertyListFiltered = mockk<GetPropertyListFilteredUseCase>(relaxed = true)
    val deleteProperty = mockk<DeletePropertyUseCase>(relaxed = true)
    val getPropertyDetails = mockk<GetPropertyDetailsUseCase>(relaxed = true)

    lateinit var viewModel: ListDetailsViewModel

    var filter = Filter(
        sortType = SortType.PRICE,
        priceOrder = Order.ASC,
        dateOrder = Order.ASC,
        surfaceOrder = Order.ASC,
        priceRange = Range(0, Int.MAX_VALUE),
        dateRange = Range(0L, fixedTimestamp),
        soldDateRange = Range(0L, fixedTimestamp),
        surfaceRange = Range(0, Int.MAX_VALUE),
        sellingStatus = SellingStatus.ALL,
        tagSchool = false,
        tagPark = false,
        tagShop = false,
        tagTransport = false,
        areaCodeFilter = null,
        typeHousing = null,
        minNbrPhotos = 0,
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ListDetailsViewModel(
            getPropertyList,
            getPropertyListFiltered,
            deleteProperty,
            getPropertyDetails,
        )
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getPropertyList handles success state correctly`() = runTest {
        val testProperties = listOf(
            Property(
                photoList = emptyList(),
                interestPoints = emptyList(),
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
                type = "House",
                nbrRoom = 3,
                realEstateAgent = "Agent 1",
            ),
            Property(
                photoList = emptyList(),
                interestPoints = emptyList(),
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
                type = "Apartment",
                nbrRoom = 2,
                realEstateAgent = "Agent 2",
            )
        )

        coEvery { getPropertyListFiltered.invoke(any()) } returns flow {
            emit(DataState.Success(testProperties))
        }
        
        val emittedStates = mutableListOf<ListDetailsState>()

        val job = launch {
            viewModel.state.toList(emittedStates)
        }
        
        viewModel.onEvent(ListDetailsEvent.GetPropertiesFiltered(filter))
        advanceUntilIdle()
        
        coVerify { getPropertyListFiltered.invoke(any()) }

        // Verify that the ViewModel correctly handles success state
        assert(viewModel.state.value.properties.size == 2)
        assert(viewModel.state.value.properties == testProperties)
        assert(!viewModel.state.value.isError)
        assert(!viewModel.state.value.isLoadingProgressBar)

        job.cancel()
    }

    @Test
    fun `getPropertyList handles error state correctly`() = runTest {
        coEvery { getPropertyListFiltered.invoke(any()) } returns flow {
            emit(DataState.Error(Exception("Test Exception")))
        }
        
        val emittedStates = mutableListOf<ListDetailsState>()

        val job = launch {
            viewModel.state.toList(emittedStates)
        }

        viewModel.onEvent(ListDetailsEvent.GetPropertiesFiltered(filter))
        advanceUntilIdle()

        assertThat(emittedStates).isNotEmpty()
        assertThat(emittedStates.last().isError).isTrue()
        assertThat(emittedStates.last().isLoadingProgressBar).isFalse()

        coVerify { getPropertyListFiltered.invoke(any()) }

        job.cancel()
    }

    @Test
    fun `getPropertyList handles loading state correctly`() = runTest {
        coEvery { getPropertyListFiltered.invoke(any()) } returns flow {
            emit(DataState.Loading(true))
        }
        
        val emittedStates = mutableListOf<ListDetailsState>()

        val job = launch {
            viewModel.state.toList(emittedStates)
        }

        viewModel.onEvent(ListDetailsEvent.GetPropertiesFiltered(filter))
        advanceUntilIdle()

        assertThat(emittedStates).isNotEmpty()
        assertThat(emittedStates.last().isLoadingProgressBar).isTrue()

        coVerify { getPropertyListFiltered.invoke(any()) }

        job.cancel()
    }

    @Test
    fun `updateFilter correctly updates filter state`() = runTest {
        val newFilter = Filter(
            sortType = SortType.DATE,
            priceOrder = Order.DESC,
            dateOrder = Order.ASC,
            surfaceOrder = Order.DESC,
            priceRange = Range(100000, 200000),
            dateRange = Range(1000L, 5000L),
            soldDateRange = Range(1000L, 5000L),
            surfaceRange = Range(100, 200),
            sellingStatus = SellingStatus.SOLD,
            tagSchool = true,
            tagTransport = false,
            tagShop = true,
            tagPark = false,
            areaCodeFilter = 12345,
            typeHousing = "Apartment",
            minNbrPhotos = 2,
        )

        viewModel.onEvent(ListDetailsEvent.UpdateFilter(newFilter))
        advanceUntilIdle()

        assert(viewModel.state.value.filterState == newFilter)
    }

    @Test
    fun `updateFilter correctly handles typeHousing filter`() = runTest {
        val newFilter = Filter(
            sortType = SortType.PRICE,
            priceOrder = Order.ASC,
            dateOrder = Order.ASC,
            surfaceOrder = Order.ASC,
            priceRange = Range(0, Int.MAX_VALUE),
            dateRange = Range(0L, Long.MAX_VALUE),
            soldDateRange = Range(0L, Long.MAX_VALUE),
            surfaceRange = Range(0, Int.MAX_VALUE),
            sellingStatus = SellingStatus.ALL,
            tagSchool = false,
            tagTransport = false,
            tagShop = false,
            tagPark = false,
            areaCodeFilter = null,
            typeHousing = "House",
            minNbrPhotos = 0,
        )

        viewModel.onEvent(ListDetailsEvent.UpdateFilter(newFilter))
        advanceUntilIdle()

        assertEquals("House", viewModel.state.value.filterState.typeHousing)
    }

    @Test
    fun `updateSelectedProperty correctly updates selected property`() = runTest {
        val testProperty = Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            description = "Test Property",
            address = "Test Address",
            town = "Test Town",
            lat = 0.0,
            lng = 0.0,
            country = "Test Country",
            createdDate = 1000L,
            areaCode = 12345,
            surfaceArea = 100,
            price = 200000,
            sold = -1,
            id = 1L,
            type = "House",
            nbrRoom = 3,
            realEstateAgent = "Test Agent",
        )

        viewModel.onEvent(ListDetailsEvent.UpdateSelectedProperty(testProperty))
        advanceUntilIdle()

        assert(viewModel.state.value.selectedProperty == testProperty)

        viewModel.onEvent(ListDetailsEvent.UpdateSelectedProperty(null))
        advanceUntilIdle()

        assert(viewModel.state.value.selectedProperty == null)
    }

    @Test
    fun `filter sheet state is managed correctly`() = runTest {
        viewModel.onEvent(ListDetailsEvent.OpenFilter)
        advanceUntilIdle()
        assert(viewModel.state.value.isFilterSheetOpen)

        viewModel.onEvent(ListDetailsEvent.DismissFilter)
        advanceUntilIdle()
        assert(!viewModel.state.value.isFilterSheetOpen)
    }

    @Test
    fun `map mode state is managed correctly`() = runTest {
        viewModel.onEvent(ListDetailsEvent.OnClickPropertyDisplayMode(true))
        advanceUntilIdle()
        assert(viewModel.state.value.mapMode)

        viewModel.onEvent(ListDetailsEvent.OnClickPropertyDisplayMode(false))
        advanceUntilIdle()
        assert(!viewModel.state.value.mapMode)
    }

    @Test
    fun `getDetails calls use case with correct property id`() = runTest {
        val testProperty = Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            description = "Test Property",
            address = "Test Address",
            town = "Test Town",
            lat = 0.0,
            lng = 0.0,
            country = "Test Country",
            createdDate = 1000L,
            areaCode = 12345,
            surfaceArea = 100,
            price = 200000,
            sold = -1,
            id = 2L,
            type = "Apartment",
            nbrRoom = 2,
            realEstateAgent = "Test Agent",
        )

        coEvery { getPropertyDetails.invoke(2L) } returns testProperty

        viewModel.onEvent(ListDetailsEvent.GetDetails(2L))
        advanceUntilIdle()
        
        coVerify { getPropertyDetails.invoke(2L) }
        assert(viewModel.state.value.selectedProperty == testProperty)
    }

    @Test
    fun `deleteProperty calls use case with correct property`() = runTest {
        val testProperty = Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            description = "Test Property",
            address = "Test Address",
            town = "Test Town",
            lat = 0.0,
            lng = 0.0,
            country = "Test Country",
            createdDate = 1000L,
            areaCode = 12345,
            surfaceArea = 100,
            price = 200000,
            sold = -1,
            id = 1L,
            type = "House",
            nbrRoom = 3,
            realEstateAgent = "Test Agent",
        )

        viewModel.onEvent(ListDetailsEvent.DeleteProperty(testProperty))
        advanceUntilIdle()

        coVerify { deleteProperty.invoke(testProperty) }
    }

    @Test
    fun `selected property is correctly tracked for UI highlighting`() = runTest {
        val testProperty1 = Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            description = "Test Property 1",
            address = "Test Address 1",
            town = "Test Town 1",
            lat = 0.0,
            lng = 0.0,
            country = "Test Country 1",
            createdDate = 1000L,
            areaCode = 12345,
            surfaceArea = 100,
            price = 200000,
            sold = -1,
            id = 1L,
            type = "House",
            nbrRoom = 3,
            realEstateAgent = "Agent 1",
        )

        val testProperty2 = Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            description = "Test Property 2",
            address = "Test Address 2",
            town = "Test Town 2",
            lat = 1.0,
            lng = 1.0,
            country = "Test Country 2",
            createdDate = 2000L,
            areaCode = 54321,
            surfaceArea = 200,
            price = 300000,
            sold = -1,
            id = 2L,
            type = "Apartment",
            nbrRoom = 2,
            realEstateAgent = "Agent 2",
        )

        // Initially no property should be selected
        assert(viewModel.state.value.selectedProperty == null)

        // Select first property
        viewModel.onEvent(ListDetailsEvent.UpdateSelectedProperty(testProperty1))
        advanceUntilIdle()
        assert(viewModel.state.value.selectedProperty == testProperty1)

        // Select second property - first should be deselected
        viewModel.onEvent(ListDetailsEvent.UpdateSelectedProperty(testProperty2))
        advanceUntilIdle()
        assert(viewModel.state.value.selectedProperty == testProperty2)
        assert(viewModel.state.value.selectedProperty != testProperty1)

        // Clear selection
        viewModel.onEvent(ListDetailsEvent.UpdateSelectedProperty(null))
        advanceUntilIdle()
        assert(viewModel.state.value.selectedProperty == null)
    }
}
