package com.ocproject.realestatemanager.presentation.scene.propertylist


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.ocproject.realestatemanager.MainCoroutineRule
import com.ocproject.realestatemanager.core.DataState
import com.ocproject.realestatemanager.core.Filter
import com.ocproject.realestatemanager.core.Order
import com.ocproject.realestatemanager.core.SellingStatus
import com.ocproject.realestatemanager.core.SortType
import com.ocproject.realestatemanager.core.utils.Range
import com.ocproject.realestatemanager.data.repositories.FakePropertiesRepository
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.usecases.DeletePropertyUseCase
import com.ocproject.realestatemanager.domain.usecases.GetPropertyListUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PropertyListViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

//    val mockRangeInt = mockk<Range<Int>> {
//        every { lower } returns 0
//        every { upper } returns Int.MAX_VALUE
//        every { equals(any()) } returns true
//    }
//    val mockRangeLong = mockk<Range<Long>> {
//        every { lower } returns 0
//        every { upper } returns Long.MAX_VALUE
//        every { equals(any()) } returns true
//    }

    val mockedRangeI = mockk<Range<Int>>()
    val mockedRangeL = mockk<Range<Long>>()

    //    val testDispatcher = StandardTestDispatcher()
    val filter = Filter(
        sortType = SortType.PRICE,
        orderPrice = Order.ASC,
        orderDate = Order.ASC,
        rangePrice = mockedRangeI,
        rangeDate = mockedRangeL,
        sellingStatus = SellingStatus.PURCHASABLE,
        tagSchool = false,
        tagTransport = false,
        tagShop = false,
        tagPark = false
    )

    private lateinit var fakePropertiesRepository: FakePropertiesRepository

    private lateinit var getPropertyList: GetPropertyListUseCase
    private lateinit var deleteProperty: DeletePropertyUseCase

    private lateinit var propertyListViewModel: PropertyListViewModel

    @Before
    fun setUp() {
        fakePropertiesRepository = FakePropertiesRepository()
        deleteProperty = DeletePropertyUseCase(fakePropertiesRepository)
        getPropertyList = GetPropertyListUseCase(fakePropertiesRepository)

        propertyListViewModel = PropertyListViewModel(getPropertyList, deleteProperty)
    }

    @Test
    fun `get properties from a list of 3, list of properties have 3 elements`() = runTest {
        val mockProperties = listOf(
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
            ),
            Property(
                photoList = emptyList(),
                interestPoints = emptyList(),
                address = "Somewhere",
                town = "Londres",
                lat = 120.5,
                lng = 50.30,
                country = "Angleterre",
                createdDate = null,
                areaCode = 18290,
                surfaceArea = 150,
                price = 150000,
                sold = false,
                id = 3L,
            ),
        )

        coEvery { getPropertyList() } returns flow {
            emit(DataState.Loading(true))
            emit(DataState.Success(mockProperties))
            emit(DataState.Loading(false))
        }

        // WHEN
        val filter = filter
        fakePropertiesRepository.shouldHaveFilledList(true)
        propertyListViewModel.getPropertyList(filter)

//        propertyListViewModel.getPropertyList(
//            filter = filter
//        )
//        mainCoroutineRule.dispatcher.scheduler.advanceUntilIdle()

        assertThat(
            propertyListViewModel.state.value.properties.size == 3
        )
    }


//    @Test
//    fun `should update state to Error when use case is return failure`() = runTest {
//        coEvery { getPropertyList() } returns flow {
//            emit(DataState.Loading(true))
//            emit(DataState.Error(mockProperties))
//            emit(DataState.Loading(false))
//        }
//        val states = mutableListOf<PropertyListState>()
//
//        propertyListViewModel.onEvent(PropertyListEvent.GetProperties(filter))
//
//        // manually make it emit error
//
//        assertThat(
//            propertyListViewModel.state.toList(states).contains(PropertyListState().isError == true)
//        )
//    }

}