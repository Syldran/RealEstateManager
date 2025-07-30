package com.ocproject.realestatemanager.presentation.scene.propertylist


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.ocproject.realestatemanager.MainCoroutineRule
import com.ocproject.realestatemanager.core.DataState
import com.ocproject.realestatemanager.core.Filter
import com.ocproject.realestatemanager.core.InterestPoint
import com.ocproject.realestatemanager.core.Order
import com.ocproject.realestatemanager.core.SellingStatus
import com.ocproject.realestatemanager.core.SortType
import com.ocproject.realestatemanager.core.utils.Range
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.usecases.DeletePropertyUseCase
import com.ocproject.realestatemanager.domain.usecases.GetPropertyDetailsUseCase
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
import java.util.Calendar

@OptIn(ExperimentalCoroutinesApi::class)
class ListDetailsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    val mockRangeInt = Range(0, Int.MAX_VALUE)
    val mockRangeLong = Range(0L, Calendar.getInstance().timeInMillis + 12583060)

    val testDispatcher = StandardTestDispatcher()
    var filter = Filter(
        sortType = SortType.PRICE,
        priceOrder = Order.ASC,
        dateOrder = Order.ASC,
        priceRange = mockRangeInt,
        dateRange = mockRangeLong,
        sellingStatus = SellingStatus.PURCHASABLE,
        tagSchool = false,
        tagTransport = false,
        tagShop = false,
        tagPark = false,
        surfaceOrder = Order.ASC,
        soldDateRange = mockRangeLong,
        surfaceRange = mockRangeInt,
        areaCodeFilter = null,
        minNbrPhotos = 0,
    )

    val getPropertyList = mockk<GetPropertyListUseCase>(relaxed = true)
    val deleteProperty = mockk<DeletePropertyUseCase>(relaxed = true)
    val getPropertyDetails = mockk<GetPropertyDetailsUseCase>(relaxed = true)

    lateinit var viewModel: ListDetailsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ListDetailsViewModel(
            getPropertyList,
            deleteProperty,
            getPropertyDetails,
        )
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get properties from a list of 2, list of properties have 2 elements`() = runTest {
        coEvery { getPropertyList.invoke() } returns flow {
            emit(
                DataState.Success(
                    listOf(
                        Property(
                            photoList = emptyList(),
                            interestPoints = emptyList(),
                            address = "Somewhere",
                            town = "NowhereCity",
                            lat = 120.5,
                            lng = 50.30,
                            country = "Faraway",
                            createdDate = Calendar.getInstance().timeInMillis,
                            areaCode = 18290,
                            surfaceArea = 150,
                            price = 150000,
                            sold = null,
                            id = 1L,
                        ),
                        Property(
                            photoList = emptyList(),
                            interestPoints = emptyList(),
                            address = "Somewhere",
                            town = "Paris",
                            lat = 120.5,
                            lng = 50.30,
                            country = "France",
                            createdDate = Calendar.getInstance().timeInMillis,
                            areaCode = 18290,
                            surfaceArea = 150,
                            price = 150000,
                            sold = null,
                            id = 2L,
                        )
                    )
                )
            )
        }

        viewModel.onEvent(ListDetailsEvent.GetProperties)
        advanceUntilIdle()
        coVerify { getPropertyList.invoke() }

        assert(
            viewModel.state.value.properties.size == 2
        )
    }

    @Test
    fun `getPropertyList emits error state on failure`() = runTest {

        coEvery { getPropertyList() } returns flow {
            emit(DataState.Error(Exception("Test Exception")))
        }
        val emittedStates = mutableListOf<ListDetailsState>()

        val job = launch {
            viewModel.state.toList(emittedStates)
        }


        viewModel.onEvent(ListDetailsEvent.GetProperties)
        advanceUntilIdle()

        assertThat(emittedStates).isNotEmpty()
        assertThat(emittedStates.last().isError).isTrue()

        assertThat(emittedStates.last().isLoadingProgressBar).isFalse()

        coVerify { getPropertyList.invoke() }

        job.cancel()
    }

    @Test
    fun `getPropertyList emits loading state on loading`() = runTest {

        coEvery { getPropertyList() } returns flow {
            emit(DataState.Loading(true))
        }
        val emittedStates = mutableListOf<ListDetailsState>()

        val job = launch {
            viewModel.state.toList(emittedStates)
        }


        viewModel.onEvent(ListDetailsEvent.GetProperties)
        advanceUntilIdle()

        assertThat(emittedStates).isNotEmpty()
        assertThat(emittedStates.last().isLoadingProgressBar).isTrue()

        coVerify { getPropertyList.invoke() }

        job.cancel()
    }

    @Test
    fun `getPropertyList sorted by price`() = runTest {
        // separate test on date & price
        coEvery { getPropertyList.invoke() } returns flow {
            emit(
                DataState.Success(
                    listOf(
                        Property(
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
                        ),
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
                            sold = null,
                            id = 2L,
                        ),
                        Property(
                            photoList = emptyList(),
                            interestPoints = emptyList(),
                            address = "There",
                            town = "London",
                            lat = 120.5,
                            lng = 50.30,
                            country = "France",
                            createdDate = 3000,
                            areaCode = 18290,
                            surfaceArea = 150,
                            price = 250000,
                            sold = null,
                            id = 3L,
                        )
                    )
                )
            )
        }
        val emittedStates = mutableListOf<ListDetailsState>()

        val job = launch {
            viewModel.state.toList(emittedStates)
        }
        viewModel.onEvent(
            ListDetailsEvent.UpdateFilter(
                viewModel.state.value.filterSate.copy(
                    sortType = SortType.PRICE,
                    priceOrder = Order.ASC,
                )
            )
        )
        viewModel.onEvent(ListDetailsEvent.GetProperties)
        advanceUntilIdle()
        coVerify { getPropertyList.invoke() }

        // Filter is set to filter by price ascending.

        // Check property0 price is lower than property1 meaning property0
        assert(viewModel.state.value.sortedProperties[0].price!! == 150000)
        assert(viewModel.state.value.sortedProperties[1].price!! == 250000)
        assert(viewModel.state.value.sortedProperties[2].price!! == 300000)

        viewModel.onEvent(
            ListDetailsEvent.UpdateFilter(
                viewModel.state.value.filterSate.copy(
                    sortType = SortType.PRICE,
                    priceOrder = Order.DESC,
                )
            )
        )
        viewModel.onEvent(ListDetailsEvent.GetProperties)
        advanceUntilIdle()
        coVerify { getPropertyList.invoke() }
        assert(viewModel.state.value.sortedProperties[0].price!! == 300000)
        assert(viewModel.state.value.sortedProperties[1].price!! == 250000)
        assert(viewModel.state.value.sortedProperties[2].price!! == 150000)
        job.cancel()
    }

    @Test
    fun `getPropertyList sorted by date`() = runTest {
        // separate test on date & price
        coEvery { getPropertyList.invoke() } returns flow {
            emit(
                DataState.Success(
                    listOf(
                        Property(
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
                        ),
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
                            sold = null,
                            id = 2L,
                        ),
                        Property(
                            photoList = emptyList(),
                            interestPoints = emptyList(),
                            address = "There",
                            town = "London",
                            lat = 120.5,
                            lng = 50.30,
                            country = "France",
                            createdDate = 3000,
                            areaCode = 18290,
                            surfaceArea = 150,
                            price = 250000,
                            sold = null,
                            id = 3L,
                        )
                    )
                )
            )
        }
        val emittedStates = mutableListOf<ListDetailsState>()

        val job = launch {
            viewModel.state.toList(emittedStates)
        }

        viewModel.onEvent(
            ListDetailsEvent.UpdateFilter(
                viewModel.state.value.filterSate.copy(
                    sortType = SortType.DATE,
                    dateOrder = Order.DESC,
                )
            )
        )


        viewModel.onEvent(ListDetailsEvent.GetProperties)
        advanceUntilIdle()
        coVerify { getPropertyList.invoke() }


//        viewModel.onEvent(ListDetailsEvent.UpdateFilter(filter.copy(sortType = SortType.DATE, orderDate = Order.ASC)))


        // Filter is set for Descending Date Order.
        // Descending Order filter Older first to Newest last.
        // item 0 must be lowest while following items keeps increasing
        assert(viewModel.state.value.sortedProperties[0].createdDate == 10000L)
        assert(viewModel.state.value.sortedProperties[1].createdDate == 3000L)
        assert(viewModel.state.value.sortedProperties[2].createdDate == 500L)

        viewModel.onEvent(
            ListDetailsEvent.UpdateFilter(
                viewModel.state.value.filterSate.copy(
                    sortType = SortType.DATE,
                    dateOrder = Order.ASC,
                )
            )
        )


        viewModel.onEvent(ListDetailsEvent.GetProperties)
        advanceUntilIdle()
        coVerify { getPropertyList.invoke() }
        assert(viewModel.state.value.sortedProperties[0].createdDate == 500L)
        assert(viewModel.state.value.sortedProperties[1].createdDate == 3000L)
        assert(viewModel.state.value.sortedProperties[2].createdDate == 10000L)
        job.cancel()
    }

    @Test
    fun `getPropertyList sorted by tags`() = runTest {
        coEvery { getPropertyList.invoke() } returns flow {
            emit(
                DataState.Success(
                    listOf(
                        Property(
                            photoList = emptyList(),
                            interestPoints = listOf(InterestPoint.SCHOOL),
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
                        ),
                        Property(
                            photoList = emptyList(),
                            interestPoints = listOf(InterestPoint.TRANSPORT),
                            address = "Somewhere",
                            town = "Paris",
                            lat = 120.5,
                            lng = 50.30,
                            country = "France",
                            createdDate = 10000,
                            areaCode = 18290,
                            surfaceArea = 150,
                            price = 300000,
                            sold = null,
                            id = 2L,
                        ),
                        Property(
                            photoList = emptyList(),
                            interestPoints = listOf(InterestPoint.SCHOOL),
                            address = "There",
                            town = "London",
                            lat = 120.5,
                            lng = 50.30,
                            country = "France",
                            createdDate = 3000,
                            areaCode = 18290,
                            surfaceArea = 150,
                            price = 250000,
                            sold = null,
                            id = 2L,
                        )
                    )
                )
            )
        }
        val emittedStates = mutableListOf<ListDetailsState>()

        val job = launch {
            viewModel.state.toList(emittedStates)
        }

        viewModel.onEvent(ListDetailsEvent.UpdateFilter(
            filter = viewModel.state.value.filterSate.copy(
                tagTransport = false,
                tagSchool = true,
                tagShop = false,
                tagPark = false,
            )
        ))
        viewModel.onEvent(ListDetailsEvent.GetProperties)
        advanceUntilIdle()
        coVerify { getPropertyList.invoke() }

        // Filter is set to filter by school tag.

        // Filtered on School Tag so should be 2 properties.
        assert(viewModel.state.value.sortedProperties.size == 2)
        viewModel.onEvent(
            ListDetailsEvent.UpdateFilter(
                viewModel.state.value.filterSate.copy(
                    tagTransport = true,
                    tagSchool = false
                )
            )
        )
        advanceUntilIdle()
        coVerify { getPropertyList.invoke() }
        assert(viewModel.state.value.sortedProperties.size == 1)

        job.cancel()
    }

    @Test
    fun `getPropertyList sorted by surfaceArea`() = runTest {
        coEvery { getPropertyList.invoke() } returns flow {
            emit(
                DataState.Success(
                    listOf(
                        Property(
                            photoList = emptyList(),
                            interestPoints = emptyList(),
                            address = "Somewhere",
                            town = "NowhereCity",
                            lat = 120.5,
                            lng = 50.30,
                            country = "Faraway",
                            createdDate = 500,
                            areaCode = 21550,
                            surfaceArea = 25,
                            price = 150000,
                            sold = null,
                            id = 1L,
                        ),
                        Property(
                            photoList = emptyList(),
                            interestPoints = emptyList(),
                            address = "Somewhere",
                            town = "Paris",
                            lat = 120.5,
                            lng = 50.30,
                            country = "France",
                            createdDate = 10000,
                            areaCode = 73110,
                            surfaceArea = 300,
                            price = 300000,
                            sold = null,
                            id = 2L,
                        ),
                        Property(
                            photoList = emptyList(),
                            interestPoints = emptyList(),
                            address = "There",
                            town = "London",
                            lat = 120.5,
                            lng = 50.30,
                            country = "France",
                            createdDate = 3000,
                            areaCode = 18290,
                            surfaceArea = 150,
                            price = 250000,
                            sold = null,
                            id = 3L,
                        )
                    )
                )
            )
        }
        val emittedStates = mutableListOf<ListDetailsState>()

        val job = launch {
            viewModel.state.toList(emittedStates)
        }

        viewModel.onEvent(
            ListDetailsEvent.UpdateFilter(
                filter = viewModel.state.value.filterSate.copy(
                    sortType = SortType.AREA,
                    surfaceOrder = Order.ASC
                )
            )
        )
        viewModel.onEvent(ListDetailsEvent.GetProperties)
        advanceUntilIdle()

        coVerify { getPropertyList.invoke() }

        assert(viewModel.state.value.sortedProperties[0].surfaceArea == 25)
        assert(viewModel.state.value.sortedProperties[1].surfaceArea == 150)
        assert(viewModel.state.value.sortedProperties[2].surfaceArea == 300)


        viewModel.onEvent(
            ListDetailsEvent.UpdateFilter(
                filter = viewModel.state.value.filterSate.copy(
                    sortType = SortType.AREA,
                    surfaceOrder = Order.DESC
                )
            )
        )
        viewModel.onEvent(ListDetailsEvent.GetProperties)
        advanceUntilIdle()
        coVerify { getPropertyList.invoke() }
        assert(viewModel.state.value.sortedProperties[0].surfaceArea == 300)
        assert(viewModel.state.value.sortedProperties[1].surfaceArea == 150)
        assert(viewModel.state.value.sortedProperties[2].surfaceArea == 25)

        job.cancel()
    }

    @Test
    fun `getPropertyList sorted by areaCode`() = runTest {
        coEvery { getPropertyList.invoke() } returns flow {
            emit(
                DataState.Success(
                    listOf(
                        Property(
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
                        ),
                        Property(
                            photoList = emptyList(),
                            interestPoints = emptyList(),
                            address = "Somewhere",
                            town = "Paris",
                            lat = 120.5,
                            lng = 50.30,
                            country = "France",
                            createdDate = 10000,
                            areaCode = 21550,
                            surfaceArea = 150,
                            price = 300000,
                            sold = null,
                            id = 2L,
                        ),
                        Property(
                            photoList = emptyList(),
                            interestPoints = emptyList(),
                            address = "There",
                            town = "London",
                            lat = 120.5,
                            lng = 50.30,
                            country = "France",
                            createdDate = 3000,
                            areaCode = 18290,
                            surfaceArea = 150,
                            price = 250000,
                            sold = null,
                            id = 3L,
                        )
                    )
                )
            )
        }
        val emittedStates = mutableListOf<ListDetailsState>()

        val job = launch {
            viewModel.state.toList(emittedStates)
        }
        viewModel.onEvent(
            ListDetailsEvent.UpdateFilter(
                viewModel.state.value.filterSate.copy(
                    areaCodeFilter = 18290,
                )
            )
        )
        viewModel.onEvent(ListDetailsEvent.GetProperties)
        advanceUntilIdle()
        coVerify { getPropertyList.invoke() }

        assert(viewModel.state.value.sortedProperties.size == 2)

        job.cancel()
    }

    @Test
    fun `update Selected Property Test`() = runTest {
        viewModel.onEvent(
            ListDetailsEvent.UpdateSelectedProperty(
                Property(
                    photoList = emptyList(),
                    interestPoints = emptyList(),
                    address = "There",
                    town = "London",
                    lat = 120.5,
                    lng = 50.30,
                    country = "France",
                    createdDate = 3000,
                    areaCode = 18290,
                    surfaceArea = 150,
                    price = 250000,
                    sold = null,
                    id = 3L,
                )
            )
        )
        assert(viewModel.state.value.selectedProperty?.id == 3L)
        viewModel.onEvent(ListDetailsEvent.UpdateSelectedProperty(null))
        assert(viewModel.state.value.selectedProperty?.id == null)
    }

    @Test
    fun `update filter state Test`() = runTest {
        val filterToTest = Filter(
            sortType = SortType.DATE,
            priceOrder = Order.ASC,
            dateOrder = Order.DESC,
            surfaceOrder = Order.ASC,
            priceRange = Range(90000, 150000),
            dateRange = Range(15000L, 600000L),
            soldDateRange = Range(15000L, 600000L),
            surfaceRange = Range(50, 150),
            sellingStatus = SellingStatus.PURCHASABLE,
            tagSchool = true,
            tagTransport = false,
            tagShop = false,
            tagPark = false,
            areaCodeFilter = 18000,
            minNbrPhotos = 1,
        )
        viewModel.onEvent(
            ListDetailsEvent.UpdateFilter(
                filter = filterToTest
            )
        )


        assert(viewModel.state.value.filterSate == filterToTest)
    }

    @Test
    fun `update various ui states test`() = runTest {
        viewModel.onEvent(
            ListDetailsEvent.OpenFilter
        )
        advanceUntilIdle()
        assert(viewModel.state.value.isFilterSheetOpen)

        viewModel.onEvent(
            ListDetailsEvent.DismissFilter
        )
        advanceUntilIdle()
        assert(!viewModel.state.value.isFilterSheetOpen)

        viewModel.onEvent(ListDetailsEvent.OnClickPropertyDisplayMode(true))
        advanceUntilIdle()
        assert(viewModel.state.value.mapMode)

        viewModel.onEvent(ListDetailsEvent.OnClickPropertyDisplayMode(false))
        advanceUntilIdle()
        assert(!viewModel.state.value.mapMode)
    }

    @Test
    fun `get property from id`() = runTest {
        val property1 = Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            address = "Somewhere",
            town = "NowhereCity",
            lat = 120.5,
            lng = 50.30,
            country = "Faraway",
            createdDate = Calendar.getInstance().timeInMillis,
            areaCode = 18290,
            surfaceArea = 150,
            price = 150000,
            sold = null,
            id = 1L,
        )
        val property2 = Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            address = "Somewhere",
            town = "Paris",
            lat = 120.5,
            lng = 50.30,
            country = "France",
            createdDate = Calendar.getInstance().timeInMillis,
            areaCode = 18290,
            surfaceArea = 150,
            price = 150000,
            sold = null,
            id = 2L,
        )
        coEvery { getPropertyList.invoke() } returns flow {
            emit(
                DataState.Success(
                    listOf(
                        property1,
                        property2
                    )
                )
            )
        }

        viewModel.onEvent(ListDetailsEvent.GetDetails(2L))
        advanceUntilIdle()
        coVerify { getPropertyDetails.invoke(2L) }

        assertThat(
            viewModel.state.value.selectedProperty == property2
        )
    }

//    @Test
//    fun `update sorted properties test`() = runTest {
//
//        viewModel.onEvent(ListDetailsEvent.UpdateSortedProperties)
//    }

    @Test
    fun `delete property test`() = runTest {
        val property1 = Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            address = "Somewhere",
            town = "NowhereCity",
            lat = 120.5,
            lng = 50.30,
            country = "Faraway",
            createdDate = Calendar.getInstance().timeInMillis,
            areaCode = 18290,
            surfaceArea = 150,
            price = 150000,
            sold = null,
            id = 1L,
        )
        val property2 = Property(
            photoList = emptyList(),
            interestPoints = emptyList(),
            address = "Somewhere",
            town = "Paris",
            lat = 120.5,
            lng = 50.30,
            country = "France",
            createdDate = Calendar.getInstance().timeInMillis,
            areaCode = 18290,
            surfaceArea = 150,
            price = 150000,
            sold = null,
            id = 2L,
        )
        coEvery { getPropertyList.invoke() } returns flow {
            emit(
                DataState.Success(
                    listOf(
                        property1,
                        property2,
                    )
                )
            )
        }

        viewModel.onEvent(ListDetailsEvent.GetProperties)
        advanceUntilIdle()
        coVerify { getPropertyList.invoke() }

        val result = viewModel.state.value.properties.size

        viewModel.onEvent(ListDetailsEvent.DeleteProperty(property2))
        advanceUntilIdle()

        coVerify { deleteProperty.invoke(property2) }

        assertThat(
            viewModel.state.value.properties.size == result - 1
        )
    }

    @Test
    fun `sort list test`() = runTest {
        val listProperty = mutableListOf<Property>()
    }


}
