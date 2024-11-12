package com.ocproject.realestatemanager.presentation.scene.propertylist

import android.util.Range
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ocproject.realestatemanager.data.repositories.PropertiesRepository
import com.ocproject.realestatemanager.models.Filter
import com.ocproject.realestatemanager.models.Order
import com.ocproject.realestatemanager.models.Property
import com.ocproject.realestatemanager.models.PropertyWithPhotos
import com.ocproject.realestatemanager.models.SellingStatus
import com.ocproject.realestatemanager.models.SortType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.text.DateFormat
import java.util.Date

@KoinViewModel
class PropertyListViewModel(
    private val propertiesRepository: PropertiesRepository,
) : ViewModel() {

    private val _properties = propertiesRepository.getPropertyList()
    private val _sortedProperties = MutableStateFlow(emptyList<PropertyWithPhotos>())

    private val _filter = MutableStateFlow(
        Filter(
            SortType.PRICE, Order.ASC, Order.ASC,
            Range<Int>(0, Int.MAX_VALUE), Range<Long>(0L, Long.MAX_VALUE),
            SellingStatus.ALL,
//            interestPointList = emptyList(),
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
        setMaxPrice()
        onEvent(
            PropertyListEvent.SortProperties(filter = _filter.value)
        )
    }

    private fun setMaxPrice() {
        viewModelScope.launch {
            _properties.flowOn(Dispatchers.IO)
                .collect { properties: List<PropertyWithPhotos> ->
                    val maxProperty = properties.maxByOrNull { propertyWithPhoto ->
                        propertyWithPhoto.property.price!!
                    }
                    _state.update { propertyListState ->
                        propertyListState.copy(
                            maxPrice = (maxProperty?.property?.price) ?: Int.MAX_VALUE
                        )
                    }
                }
        }
    }

    private fun getPropertiesSorted(filter: Filter) {
        viewModelScope.launch {
            _properties.flowOn(Dispatchers.IO)
                .collect { properties: List<PropertyWithPhotos> ->
                    _sortedProperties.update {
                        // tags : all tags unchecked then show all
                        // if on or more tags checked then filter with those.
                        val filteredProperties: MutableList<PropertyWithPhotos> = mutableListOf()



                        if (!filter.tagSchool && !filter.tagTransport && !filter.tagPark && !filter.tagShop) {
                            filteredProperties.addAll(properties)
                        } else {
                            if (filter.tagPark) {
                                filteredProperties.addAll(properties.filter { it.property.park })
                            }

                            if (filter.tagShop) {
                                filteredProperties.addAll(properties.filter { it.property.shop })
                            }

                            if (filter.tagSchool) {
                                filteredProperties.addAll(properties.filter { it.property.school })
                            }

                            if (filter.tagTransport) {
                                filteredProperties.addAll(properties.filter { it.property.transport })
                            }
                        }


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
                    propertiesRepository.deleteProperty(event.property)
                }
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

