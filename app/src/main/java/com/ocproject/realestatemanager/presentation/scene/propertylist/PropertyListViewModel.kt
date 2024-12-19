package com.ocproject.realestatemanager.presentation.scene.propertylist

import android.util.Range
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ocproject.realestatemanager.core.DataState
import com.ocproject.realestatemanager.domain.models.Filter
import com.ocproject.realestatemanager.domain.models.InterestPoint
import com.ocproject.realestatemanager.domain.models.Order
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.models.PropertyWithPhotos
import com.ocproject.realestatemanager.domain.models.SellingStatus
import com.ocproject.realestatemanager.domain.models.SortType
import com.ocproject.realestatemanager.domain.usecases.DeletePropertyUseCase
import com.ocproject.realestatemanager.domain.usecases.GetPropertyListUseCase
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


    private val _sortedProperties = MutableStateFlow(emptyList<PropertyWithPhotos>())
    private var _properties = emptyList<PropertyWithPhotos>()
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
        getPropertyList()
        getPropertiesSorted(_filter.value)
    }

    private fun setMaxPrice() {
        viewModelScope.launch {


            val maxProperty = _state.value.properties.maxByOrNull { propertyWithPhoto ->
                propertyWithPhoto.property.price!!
            }

            _state.update { propertyListState ->
                propertyListState.copy(
                    maxPrice = (maxProperty?.property?.price) ?: Int.MAX_VALUE
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

    private fun getPropertyList() {

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
                        _properties += propertiesDataState.data
                        _sortedProperties.update {
                            propertiesDataState.data
                        }
                    }

                }

            }.launchIn(viewModelScope)

    }

    private fun getPropertiesSorted(filter: Filter) {

        addTags(filter)
        viewModelScope.launch {
            val filteredProperties: MutableList<PropertyWithPhotos> =
                emptyList<PropertyWithPhotos>().toMutableList()
            _sortedProperties.update {

                filteredProperties.addAll(
                    _properties.filter { property ->
                        selectedTags.value.isEmpty() ||
                                selectedTags.value.all { it in property.property.interestPoints }
                    }
                )
                when (filter.sellingStatus) {
                    SellingStatus.SOLD -> {
                        subFilter(filteredProperties.filter { it.property.sold }, filter)
                    }

                    SellingStatus.ALL -> {
                        subFilter(filteredProperties, filter)
                    }

                    SellingStatus.PURCHASABLE -> {
                        subFilter(filteredProperties.filter { !it.property.sold }, filter)
                    }
                }
            }
            _state.update {
                it.copy(
                    properties = filteredProperties
                )
            }
        }
    }


    private fun sortByPrice(
        properties: List<PropertyWithPhotos>,
        filter: Filter
    ): List<PropertyWithPhotos> {
        return when (filter.orderPrice) {
            Order.ASC -> {
                properties
                    .sortedBy { it.property.price }
                    .filter { it.property.price!! >= filter.rangePrice.lower && it.property.price <= filter.rangePrice.upper }
            }

            Order.DESC -> {
                properties
                    .sortedByDescending { it.property.price }
                    .filter { it.property.price!! >= filter.rangePrice.lower && it.property.price <= filter.rangePrice.upper }
            }
        }
    }

    private fun sortByDate(
        properties: List<PropertyWithPhotos>,
        filter: Filter
    ): List<PropertyWithPhotos> {
        return when (filter.orderDate) {
            Order.ASC ->
                properties
                    .sortedBy { it.property.createdDate }
                    .filter { it.property.createdDate!! >= filter.rangeDate.lower && it.property.createdDate <= filter.rangeDate.upper }

            Order.DESC ->
                properties
                    .sortedByDescending { it.property.createdDate }
                    .filter { it.property.createdDate!! >= filter.rangeDate.lower && it.property.createdDate <= filter.rangeDate.upper }

        }
    }

    private fun subFilter(
        properties: List<PropertyWithPhotos>,
        filter: Filter
    ): List<PropertyWithPhotos> {
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
                getPropertyList()
                getPropertiesSorted(_filter.value)
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
                    getPropertiesSorted(
                        _filter.value
                    )
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

            is PropertyListEvent.OnSchoolChecked -> {
                _filter.update {
                    it.copy(
                        tagSchool = !event.value,
                    )
                }
                getPropertiesSorted(
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

            is PropertyListEvent.OnTransportChecked -> {
//                _filter.value = _filter.value.copy(
//                    tagTransport = !event.value,
//                )
                _filter.update {
                    it.copy(
                        tagTransport = !event.value,
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
                _state.value = _state.value.copy(
                    transportState = !event.value
                )
            }
        }
    }
}

