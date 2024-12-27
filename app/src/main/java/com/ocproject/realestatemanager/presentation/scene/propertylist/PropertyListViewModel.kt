package com.ocproject.realestatemanager.presentation.scene.propertylist

import android.util.Range
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ocproject.realestatemanager.core.DataState
import com.ocproject.realestatemanager.core.Filter
import com.ocproject.realestatemanager.core.InterestPoint
import com.ocproject.realestatemanager.core.Order
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.core.SellingStatus
import com.ocproject.realestatemanager.core.SortType
import com.ocproject.realestatemanager.domain.usecases.DeletePropertyUseCase
import com.ocproject.realestatemanager.domain.usecases.GetPropertyListUseCase
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam
import java.text.DateFormat
import java.util.Date


@KoinViewModel
class PropertyListViewModel(
    @InjectedParam
    private val getPropertyListUseCase: GetPropertyListUseCase,
    private val deletePropertyUseCase: DeletePropertyUseCase,
) : ViewModel() {


    private val _sortedProperties = MutableStateFlow(emptyList<Property>())
    private var _properties = emptyList<Property>()
    private val selectedTags = mutableStateOf(listOf<InterestPoint>())
//    private val selectedTags: List<InterestPoint> = emptyList()


    private val _filter = MutableStateFlow(
        Filter(
            SortType.PRICE, Order.ASC, Order.ASC,
            Range<Int>(0, Int.MAX_VALUE), Range<Long>(0L, Long.MAX_VALUE),
            SellingStatus.ALL,
            tagSchool = false,
            tagTransport = false,
            tagShop = false,
            tagPark = false,
        )
    )

    private val _state = MutableStateFlow(PropertyListState())
    val state: StateFlow<PropertyListState> = combine(
        _state,
        _sortedProperties,
        _filter
    ) { state, sortedProperties, filter ->
        state.copy(
            properties = sortedProperties,
            sortType = filter.sortType,
            orderPrice = filter.orderPrice,
            orderDate = filter.orderDate,
            rangePrice = filter.rangePrice,
            rangeDate = filter.rangeDate,
            soldState = filter.sellingStatus,
            shopState = filter.tagShop,
            schoolState = filter.tagSchool,
            parkState = filter.tagPark,
            transportState = filter.tagTransport,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), PropertyListState())

    init {
        getPropertyList(_filter.value)
    }

    private fun setMaxPrice() {
        viewModelScope.launch {
            val maxProperty = _properties.maxByOrNull { propertyWithPhoto ->
                propertyWithPhoto.price!!
            }

            _state.update { propertyListState ->
                propertyListState.copy(
                    maxPrice = (maxProperty?.price) ?: Int.MAX_VALUE
                )
            }
        }
    }


    private fun addTags(filter: Filter) {
        val currentTags = emptyList<InterestPoint>().toMutableList()
        if (filter.tagTransport) {
            currentTags.add(InterestPoint.TRANSPORT)
        } else {
            if (currentTags.contains(InterestPoint.TRANSPORT)) {
                currentTags.remove(InterestPoint.TRANSPORT)
            }
        }
        if (filter.tagSchool) {
            currentTags.add(InterestPoint.SCHOOL)
        } else {
            if (currentTags.contains(InterestPoint.SCHOOL)) {
                currentTags.remove(InterestPoint.SCHOOL)
            }
        }
        if (filter.tagShop) {
            currentTags.add(InterestPoint.SHOP)
        } else {
            if (currentTags.contains(InterestPoint.SHOP)) {
                currentTags.remove(InterestPoint.SHOP)
            }
        }
        if (filter.tagPark) {
            currentTags.add(InterestPoint.PARK)
        } else {
            if (currentTags.contains(InterestPoint.PARK)) {
                currentTags.remove(InterestPoint.PARK)
            }
        }
        selectedTags.value = currentTags
    }

    private fun getPropertyList(filter: Filter) {

        getPropertyListUseCase().onEach { propertiesDataState ->
            when (propertiesDataState) {
                is DataState.Error -> {
                    _state.update {
                        it.copy(
                            isError = true,
                        )
                    }
                }

                is DataState.Loading -> {
                    _state.update {
                        it.copy(
                            isLoadingProgressBar = propertiesDataState.isLoading
                        )
                    }
                }

                is DataState.Success -> {
                    _state.update {
                        it.copy(
                            isError = false,
                        )
                    }
                    _properties = propertiesDataState.data
                    setMaxPrice()
                    getPropertiesSorted(filter)
                }
            }
        }.launchIn(viewModelScope)

    }

    private fun getPropertiesSorted(filter: Filter) {
        addTags(filter)
        viewModelScope.launch {
            val filteredProperties: MutableList<Property> =
                emptyList<Property>().toMutableList()
            _sortedProperties.update {

                filteredProperties.addAll(
                    _properties.filter { property ->
                        selectedTags.value.isEmpty() ||
                                selectedTags.value.all { it in property.interestPoints }
                    }
                )
                when (filter.sellingStatus) {
                    SellingStatus.SOLD -> {
                        subFilter(filteredProperties.filter { it.sold }, filter)
                    }

                    SellingStatus.ALL -> {
                        subFilter(filteredProperties, filter)
                    }

                    SellingStatus.PURCHASABLE -> {
                        subFilter(filteredProperties.filter { !it.sold }, filter)
                    }
                }

            }
        }
    }


    private fun sortByPrice(
        properties: List<Property>,
        filter: Filter
    ): List<Property> {
        return when (filter.orderPrice) {
            Order.ASC -> {
                properties
                    .sortedBy { it.price }
                    .filter { it.price!! >= filter.rangePrice.lower && it.price <= filter.rangePrice.upper }
            }

            Order.DESC -> {
                properties
                    .sortedByDescending { it.price }
                    .filter { it.price!! >= filter.rangePrice.lower && it.price <= filter.rangePrice.upper }
            }
        }
    }

    private fun sortByDate(
        properties: List<Property>,
        filter: Filter
    ): List<Property> {
        return when (filter.orderDate) {
            Order.ASC ->
                properties
                    .sortedBy { it.createdDate }
                    .filter { it.createdDate!! >= filter.rangeDate.lower && it.createdDate <= filter.rangeDate.upper }

            Order.DESC ->
                properties
                    .sortedByDescending { it.createdDate }
                    .filter { it.createdDate!! >= filter.rangeDate.lower && it.createdDate <= filter.rangeDate.upper }

        }
    }

    private fun subFilter(
        properties: List<Property>,
        filter: Filter
    ): List<Property> {
        return when (filter.sortType) {
            SortType.PRICE -> {
                sortByPrice(properties, filter)
            }

            SortType.DATE -> {
                sortByDate(properties, filter)
            }
        }
    }

    fun datePresentation(property: Property): String {
        val date = Date(property.createdDate!!)
        val formatter: DateFormat =
            DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
        return formatter.format(date)
    }

    fun onEvent(event: PropertyListEvent) {
        when (event) {
            is PropertyListEvent.DeleteProperty -> {
                viewModelScope.launch {
                    deletePropertyUseCase(event.property)
                }
                getPropertyList(_filter.value)
            }

            is PropertyListEvent.SortProperties -> {

                viewModelScope.launch {
                    _filter.update {
                        it.copy(
                            sortType = event.filter.sortType,
                            orderPrice = event.filter.orderPrice,
                            orderDate = event.filter.orderDate,
                            rangePrice = event.filter.rangePrice,
                            sellingStatus = event.filter.sellingStatus,
                            tagSchool = event.filter.tagSchool,
                            tagShop = event.filter.tagShop,
                            tagTransport = event.filter.tagTransport,
                            tagPark = event.filter.tagPark,
                        )
                    }
                    getPropertyList(_filter.value)
                }
            }

            PropertyListEvent.DismissFilter -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isFilterSheetOpen = false,
                        )
                    }
                }
            }

            PropertyListEvent.OpenFilter -> {
                _state.update {
                    it.copy(
                        isFilterSheetOpen = true
                    )
                }
            }

            is PropertyListEvent.SetRangePrice -> {
                _filter.update {
                    it.copy(
                        rangePrice = Range<Int>(
                            event.rangePrice.lower.toInt(),
                            event.rangePrice.upper.toInt()
                        )
                    )
                }
                getPropertiesSorted(
                    Filter(
                        sortType = state.value.sortType,
                        orderPrice = state.value.orderPrice,
                        orderDate = state.value.orderDate,
                        rangePrice = state.value.rangePrice,
                        rangeDate = state.value.rangeDate,
                        sellingStatus = state.value.soldState,
                        tagSchool = state.value.schoolState,
                        tagShop = state.value.shopState,
                        tagTransport = state.value.transportState,
                        tagPark = state.value.parkState,
                    )
                )
            }

            is PropertyListEvent.OnParkChecked -> {
                _filter.update {
                    it.copy(
                        tagPark = !event.value,
                    )
                }
                getPropertyList(
                    Filter(
                        sortType = state.value.sortType,
                        orderPrice = state.value.orderPrice,
                        orderDate = state.value.orderDate,
                        rangePrice = state.value.rangePrice,
                        rangeDate = state.value.rangeDate,
                        sellingStatus = state.value.soldState,
                        tagSchool = state.value.schoolState,
                        tagShop = state.value.shopState,
                        tagTransport = state.value.transportState,
                        tagPark = state.value.parkState,
                    )
                )
            }

            is PropertyListEvent.OnSchoolChecked -> {
                _filter.update {
                    it.copy(
                        tagSchool = !event.value,
                    )
                }
                getPropertyList(
                    filter = Filter(
                        state.value.sortType,
                        state.value.orderPrice,
                        state.value.orderDate,
                        state.value.rangePrice,
                        state.value.rangeDate,
                        state.value.soldState,
                        tagPark = state.value.parkState,
                        tagTransport = state.value.transportState,
                        tagShop = state.value.shopState,
                        tagSchool = state.value.schoolState,
                    )
                )
            }

            is PropertyListEvent.OnShopChecked -> {
                _filter.update {
                    it.copy(
                        tagShop = !event.value,
                    )
                }
                getPropertyList(
                    Filter(
                        sortType = state.value.sortType,
                        orderPrice = state.value.orderPrice,
                        orderDate = state.value.orderDate,
                        rangePrice = state.value.rangePrice,
                        rangeDate = state.value.rangeDate,
                        sellingStatus = state.value.soldState,
                        tagSchool = state.value.schoolState,
                        tagShop = state.value.shopState,
                        tagTransport = state.value.transportState,
                        tagPark = state.value.parkState,
                    )
                )
            }

            is PropertyListEvent.OnTransportChecked -> {
                _filter.update {
                    it.copy(
                        tagTransport = !event.value,
                    )
                }
                getPropertyList(
                    Filter(
                        sortType = state.value.sortType,
                        orderPrice = state.value.orderPrice,
                        orderDate = state.value.orderDate,
                        rangePrice = state.value.rangePrice,
                        rangeDate = state.value.rangeDate,
                        sellingStatus = state.value.soldState,
                        tagSchool = state.value.schoolState,
                        tagShop = state.value.shopState,
                        tagTransport = state.value.transportState,
                        tagPark = state.value.parkState,
                    )
                )
                _state.value = _state.value.copy(
                    transportState = !event.value
                )
            }
        }
    }
}

