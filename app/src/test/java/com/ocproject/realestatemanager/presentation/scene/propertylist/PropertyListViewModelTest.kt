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
import com.ocproject.realestatemanager.domain.usecases.GetPropertyListUseCase
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

@OptIn(ExperimentalCoroutinesApi::class)
class PropertyListViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    val mockRangeInt = Range(0, Int.MAX_VALUE)
    val mockRangeLong = Range(0L, Long.MAX_VALUE)

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
        tagPark = false
    )

    val getPropertyList = mockk<GetPropertyListUseCase>(relaxed = true)
    val deleteProperty = mockk<DeletePropertyUseCase>(relaxed = true)
    lateinit var viewModel : PropertyListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = PropertyListViewModel(getPropertyList, deleteProperty)
    }

    @After
    fun after(){
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
                            createdDate = null,
                            areaCode = 18290,
                            surfaceArea = 150,
                            price = 150000,
                            sold = false,
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
                            createdDate = null,
                            areaCode = 18290,
                            surfaceArea = 150,
                            price = 150000,
                            sold = false,
                            id = 2L,
                        )
                    )
                )
            )
        }

        viewModel.getPropertyList(filter)
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
        val emittedStates = mutableListOf<PropertyListState>()

        val job = launch {
            viewModel.state.toList(emittedStates)
        }


        viewModel.getPropertyList(filter)
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
        val emittedStates = mutableListOf<PropertyListState>()

        val job = launch {
            viewModel.state.toList(emittedStates)
        }


        viewModel.getPropertyList(filter)
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
                            sold = false,
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
                            sold = false,
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
                            sold = false,
                            id = 2L,
                        )
                    )
                )
            )
        }
        val emittedStates = mutableListOf<PropertyListState>()

        val job = launch {
            viewModel.state.toList(emittedStates)
        }

        viewModel.getPropertyList(filter)
        advanceUntilIdle()
        coVerify { getPropertyList.invoke() }

        // Filter is set to filter by price ascending.
        viewModel.getPropertiesSorted(filter)

        // Check property0 price is lower than property1 meaning property0
        assert(viewModel.sortedProperties.value[0].price!! == 150000)
        assert(viewModel.sortedProperties.value[1].price!! == 250000)
        assert(viewModel.sortedProperties.value[2].price!! == 300000)

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
            tagPark = false
        )
        // Filter is set for Descending Date Order.
        viewModel.getPropertiesSorted(filter)
        // Descending Order filter Older first to Newest last.
        // item 0 must be lowest while following items keeps increasing
        assert(viewModel.sortedProperties.value[0].createdDate!! == 500L)
        assert(viewModel.sortedProperties.value[1].createdDate!! == 3000L)
        assert(viewModel.sortedProperties.value[2].createdDate!! == 10000L)

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
                            sold = false,
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
                            sold = false,
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
                            sold = false,
                            id = 2L,
                        )
                    )
                )
            )
        }
        val emittedStates = mutableListOf<PropertyListState>()

        val job = launch {
            viewModel.state.toList(emittedStates)
        }

        viewModel.getPropertyList(filter)
        advanceUntilIdle()
        coVerify { getPropertyList.invoke() }

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
            tagPark = false
        )
        // Filter is set for Descending Date Order.
        viewModel.getPropertiesSorted(filter)
        // Descending Order filter Older first to Newest last.
        // item 0 must be lowest while following items keeps increasing
        assert(viewModel.sortedProperties.value[0].createdDate!! == 500L)
        assert(viewModel.sortedProperties.value[1].createdDate!! == 3000L)
        assert(viewModel.sortedProperties.value[2].createdDate!! == 10000L)

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
                            sold = false,
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
                            sold = false,
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
                            sold = false,
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
            tagPark = false
        )
        val emittedStates = mutableListOf<PropertyListState>()

        val job = launch {
            viewModel.state.toList(emittedStates)
        }

        viewModel.getPropertyList(filter)
        advanceUntilIdle()
        coVerify { getPropertyList.invoke() }

        // Filter is set to filter by school tag.
        viewModel.getPropertiesSorted(filter)

        // Filtered on School Tag so should be 2 properties.
        assert(viewModel.sortedProperties.value.size == 2)

        job.cancel()
    }
}
