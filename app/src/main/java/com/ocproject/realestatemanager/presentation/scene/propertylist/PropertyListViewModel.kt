package com.ocproject.realestatemanager.presentation.scene.propertylist

import android.util.Range
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ocproject.realestatemanager.data.repositories.PropertiesRepository
import com.ocproject.realestatemanager.models.Filter
import com.ocproject.realestatemanager.models.Order
import com.ocproject.realestatemanager.models.Property
import com.ocproject.realestatemanager.models.PropertyWithPhotos
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

//contentProvider pour expo data
// test unit
//
@KoinViewModel
class PropertyListViewModel(
    private val propertiesRepository: PropertiesRepository,
) : ViewModel() {

    private val _properties = propertiesRepository.getPropertyList()
    private val _sortedProperties = MutableStateFlow(emptyList<PropertyWithPhotos>())

    private val _filter = MutableStateFlow(
        Filter(
            SortType.PRICE, Order.ASC, Order.ASC,
            Range<Int>(0, Int.MAX_VALUE), Range<Long>(0L, Long.MAX_VALUE)
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

                        when (filter.sortType) {
                            SortType.PRICE -> {
                                when (filter.orderPrice) {
                                    Order.ASC -> {
                                        properties
                                            .sortedBy { it.property.price }
                                            .filter { it.property.price!! >= filter.rangePrice.lower && it.property.price <= filter.rangePrice.upper }

                                    }

                                    Order.DESC -> properties
                                        .sortedByDescending { it.property.price }
                                        .filter { it.property.price!! >= filter.rangePrice.lower && it.property.price <= filter.rangePrice.upper }

                                }
                            }

                            SortType.DATE -> {
                                when (filter.orderDate) {
                                    Order.ASC -> properties
                                        .sortedBy { it.property.createdDate }
                                        .filter { it.property.createdDate!! >= filter.rangeDate.lower && it.property.createdDate <= filter.rangeDate.upper }

                                    Order.DESC -> properties
                                        .sortedByDescending { it.property.createdDate }
                                        .filter { it.property.createdDate!! >= filter.rangeDate.lower && it.property.createdDate <= filter.rangeDate.upper }

                                }
                            }


                        }
                    }
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
                            rangePrice = event.filter.rangePrice
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
                    )
                )
            }
        }
    }
}
