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
import java.lang.Exception
import java.util.Calendar

@OptIn(ExperimentalCoroutinesApi::class)
class ListDetailsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    val mockRangeInt = Range(0, Int.MAX_VALUE)
    val mockRangeLong = Range(0L, Calendar.getInstance().timeInMillis)

    val testDispatcher = StandardTestDispatcher()
    var filter = Filter(
        sortType = SortType.PRICE,
        orderPrice = Order.ASC,
        orderDate = Order.ASC,
        rangePrice = mockRangeInt,
        rangeDate = mockRangeLong,
        sellingStatus = SellingStatus.PURCHASABLE,
        tagSchool = false,
        tagTransport = false,
        tagShop = false,
        tagPark = false,
        orderSurface = Order.ASC,
        soldRangeDate = mockRangeLong,
        rangeSurface = mockRangeInt,
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

        viewModel.getPropertyList()
        coVerify { getPropertyList.invoke() }

        assertThat(
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


        viewModel.getPropertyList()
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


        viewModel.getPropertyList()
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
                filter.copy(
                    sortType = SortType.PRICE,
                    orderPrice = Order.ASC
                )
            )
        )
        advanceUntilIdle()
        coVerify { getPropertyList.invoke() }

        // Filter is set to filter by price ascending.

        // Check property0 price is lower than property1 meaning property0
        assert(viewModel.state.value.sortedProperties[0].price!! == 150000)
        assert(viewModel.state.value.sortedProperties[1].price!! == 250000)
        assert(viewModel.state.value.sortedProperties[2].price!! == 300000)

//        filter = Filter(
//            sortType = SortType.DATE,
//            orderPrice = Order.ASC,
//            orderDate = Order.DESC,
//            rangePrice = mockRangeInt,
//            rangeDate = mockRangeLong,
//            sellingStatus = SellingStatus.PURCHASABLE,
//            tagSchool = false,
//            tagTransport = false,
//            tagShop = false,
//            tagPark = false,
//            orderSurface = Order.ASC,
//            soldRangeDate = mockRangeLong,
//            rangeSurface = mockRangeInt,
//            areaCodeFilter = null,
//        )
//        viewModel.onEvent(ListDetailsEvent.UpdateFilter(filter.copy(orderDate = Order.DESC)))
//
//        // Filter is set for Descending Date Order.
//        viewModel.getPropertyList()
//        // Descending Order filter Older first to Newest last.
//        // item 0 must be lowest while following items keeps increasing
//        assert(viewModel.state.value.sortedProperties[0].createdDate == 500L)
//        assert(viewModel.state.value.sortedProperties[1].createdDate == 3000L)
//        assert(viewModel.state.value.sortedProperties[2].createdDate == 10000L)

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
        filter = Filter(
            sortType = SortType.DATE,
            orderPrice = Order.ASC,
            orderDate = Order.DESC,
            rangePrice = mockRangeInt,
            rangeDate = mockRangeLong,
            sellingStatus = SellingStatus.PURCHASABLE,
            tagSchool = false,
            tagTransport = false,
            tagShop = false,
            tagPark = false,
            orderSurface = Order.ASC,
            soldRangeDate = mockRangeLong,
            rangeSurface = mockRangeInt,
            areaCodeFilter = null,
            minNbrPhotos = 0,
        )

        viewModel.onEvent(ListDetailsEvent.UpdateFilter(filter))

        advanceUntilIdle()
        coVerify { getPropertyList.invoke() }


//        viewModel.onEvent(ListDetailsEvent.UpdateFilter(filter.copy(sortType = SortType.DATE, orderDate = Order.ASC)))


        // Filter is set for Descending Date Order.
        // Descending Order filter Older first to Newest last.
        // item 0 must be lowest while following items keeps increasing
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

        filter = Filter(
            sortType = SortType.DATE,
            orderPrice = Order.ASC,
            orderDate = Order.DESC,
            rangePrice = mockRangeInt,
            rangeDate = mockRangeLong,
            sellingStatus = SellingStatus.PURCHASABLE,
            tagSchool = true,
            tagTransport = false,
            tagShop = false,
            tagPark = false,
            orderSurface = Order.ASC,
            soldRangeDate = mockRangeLong,
            rangeSurface = mockRangeInt,
            areaCodeFilter = null,
            minNbrPhotos = 0,
        )
        val emittedStates = mutableListOf<ListDetailsState>()

        val job = launch {
            viewModel.state.toList(emittedStates)
        }

        viewModel.onEvent(ListDetailsEvent.UpdateFilter(filter.copy(tagSchool = true)))
        advanceUntilIdle()
        coVerify { getPropertyList.invoke() }

        // Filter is set to filter by school tag.

        // Filtered on School Tag so should be 2 properties.
        assert(viewModel.state.value.sortedProperties.size == 2)
        viewModel.onEvent(ListDetailsEvent.UpdateFilter(filter.copy(tagTransport = true, tagSchool = false)))
        advanceUntilIdle()
        coVerify { getPropertyList.invoke() }
        assert(viewModel.state.value.sortedProperties.size == 1)

        job.cancel()
    }
}
