package com.ocproject.realestatemanager.presentation.scene.propertylist



import android.util.Range
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.ocproject.realestatemanager.MainCoroutineRule
import com.ocproject.realestatemanager.core.Filter
import com.ocproject.realestatemanager.core.Order
import com.ocproject.realestatemanager.core.SellingStatus
import com.ocproject.realestatemanager.core.SortType
import com.ocproject.realestatemanager.data.repositories.FakePropertiesRepository
import com.ocproject.realestatemanager.domain.usecases.DeletePropertyUseCase
import com.ocproject.realestatemanager.domain.usecases.GetPropertyListUseCase
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PropertyListViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    val testDispatcher = StandardTestDispatcher()

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
    fun `get properties from an empty list, list of properties is empty`() = runTest {
        fakePropertiesRepository.shouldHaveFilledList(false)

        propertyListViewModel.getPropertyList(
            filter = Filter(
                sortType = SortType.PRICE,
                orderPrice = Order.ASC,
                orderDate = Order.ASC,
                rangePrice = Range<Int>(0, Int.MAX_VALUE),
                rangeDate = Range<Long>(0, Long.MAX_VALUE),
                sellingStatus = SellingStatus.PURCHASABLE,
                tagSchool = false,
                tagTransport = false,
                tagShop = false,
                tagPark = false
            )
        )
        mainCoroutineRule.dispatcher.scheduler.advanceUntilIdle()

        assertThat(
            propertyListViewModel.state.value.properties.isEmpty()
        ).isTrue()
    }

    @Test
    fun isPropertyListLoading(){
        //simuler cas loading

        // test avec id property null et avec un id de property en bdd
    }
    @Test
    fun isPropertyListError(){

    }

    @Test
    fun isPropertyListSuccess(){}

}