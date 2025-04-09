package com.ocproject.realestatemanager.presentation.scene.listdetails

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import java.text.DateFormat
import java.util.Date

class ListDetailsViewModel(
    @InjectedParam
    private val getPropertyListUseCase: GetPropertyListUseCase,
    private val deletePropertyUseCase: DeletePropertyUseCase,
) : ViewModel() {


    val sortedProperties = MutableStateFlow(emptyList<Property>())
    private var _properties = emptyList<Property>()
    private val selectedTags = mutableStateOf(listOf<InterestPoint>())


    private val _filter = MutableStateFlow(
        Filter(
            SortType.PRICE,
            Order.ASC,
            Order.ASC,
            Order.ASC,
            Range<Int>(0, Int.MAX_VALUE),
            Range<Long>(0L, Long.MAX_VALUE),
            Range<Int>(0, Int.MAX_VALUE),
            SellingStatus.ALL,
            tagSchool = false,
            tagTransport = false,
            tagShop = false,
            tagPark = false,
            areaCodeFilter = null,
        )
    )

    private val _state = MutableStateFlow(ListDetailsState())
    val state: StateFlow<ListDetailsState> = combine(
        _state,
        sortedProperties,
        _filter
    ) { state, sortedProperties, filter ->
        state.copy(
            properties = sortedProperties,
            sortType = filter.sortType,
            orderPrice = filter.orderPrice,
            orderDate = filter.orderDate,
            orderSurface = filter.orderSurface,
            rangeDate = filter.rangeDate,
            rangeSurface = filter.rangeSurface,
            soldState = filter.sellingStatus,
            shopState = filter.tagShop,
            schoolState = filter.tagSchool,
            parkState = filter.tagPark,
            transportState = filter.tagTransport,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ListDetailsState())

    init {
        getPropertyList(_filter.value)
    }

    private fun setMaxPrice() {
        viewModelScope.launch {
            val maxPriceProperty = _properties.maxByOrNull { propertyWithPhoto ->
                propertyWithPhoto.price!!
            }

            _state.update { propertyListState ->
                propertyListState.copy(
                    maxPrice = (maxPriceProperty?.price) ?: Int.MAX_VALUE
                )
            }
        }
    }

    private fun setMaxSurface() {
        viewModelScope.launch {
            val maxSurfaceProperty = _properties.maxByOrNull { propertyWithPhoto ->
                propertyWithPhoto.surfaceArea!!
            }

            _state.update { propertyListState ->
                propertyListState.copy(
                    maxSurface = (maxSurfaceProperty?.surfaceArea) ?: Int.MAX_VALUE
                )
            }
        }
    }

    fun getAreaCodes() {
        viewModelScope.launch {
            val areaCodesFilter = mutableListOf<Int>()
            for (property in _properties) {
                if (!areaCodesFilter.contains(property.areaCode)) {
                    areaCodesFilter += property.areaCode!!
                }
            }
            _state.update {
                it.copy(
                    areaCodeList = areaCodesFilter
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

    fun getPropertyList(filter: Filter) {

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
                    setMaxSurface()
                    getPropertiesSorted(filter)
                    getAreaCodes()
                }
            }
        }.launchIn(viewModelScope)

    }

    fun getPropertiesSorted(filter: Filter) {
        addTags(filter)
        viewModelScope.launch {
            var filteredProperties: MutableList<Property> =
                emptyList<Property>().toMutableList()
            sortedProperties.update {

                filteredProperties.addAll(
                    _properties.filter { property ->
                        selectedTags.value.isEmpty() ||
                                selectedTags.value.all { it in property.interestPoints }
                    }
                )
                filteredProperties = sortByAreaCode(filteredProperties, filter).toMutableList()
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

    private fun sortBySurface(
        properties: List<Property>,
        filter: Filter
    ): List<Property> {
        return when (filter.orderSurface) {
            Order.ASC -> {
                properties
                    .sortedBy { it.surfaceArea }
                    .filter { it.surfaceArea!! >= filter.rangeSurface.lower && it.surfaceArea <= filter.rangeSurface.upper }
            }

            Order.DESC -> {
                properties
                    .sortedByDescending { it.surfaceArea }
                    .filter { it.surfaceArea!! >= filter.rangeSurface.lower && it.surfaceArea <= filter.rangeSurface.upper }
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

    private fun sortByAreaCode(
        properties: List<Property>,
        filter: Filter
    ): List<Property> {
        return if (filter.areaCodeFilter != null) {
            properties.filter { it.areaCode == filter.areaCodeFilter }
        } else {
            properties
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

            SortType.SURFACE -> {
                sortBySurface(properties, filter)
            }
        }
    }

    fun datePresentation(property: Property): String {
        val date = Date(property.createdDate!!)
        val formatter: DateFormat =
            DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
        return formatter.format(date)
    }

    fun onEvent(event: ListDetailsEvent) {
        when (event) {
            is ListDetailsEvent.OnClickPropertyDisplayMode -> {
                _state.update {
                    it.copy(
                        mapMode = event.map
                    )
                }
            }

            is ListDetailsEvent.DeleteProperty -> {
                viewModelScope.launch {
                    deletePropertyUseCase(event.property)
                }
                getPropertyList(_filter.value)
            }

            is ListDetailsEvent.GetProperties -> {

                viewModelScope.launch {
                    _filter.update {
                        it.copy(
                            sortType = event.filter.sortType,
                            orderPrice = event.filter.orderPrice,
                            orderDate = event.filter.orderDate,
                            orderSurface = event.filter.orderSurface,
                            rangePrice = event.filter.rangePrice,
//                            rangeDate = event.filter.rangeDate,
                            rangeSurface = event.filter.rangeSurface,
                            sellingStatus = event.filter.sellingStatus,
                            tagSchool = event.filter.tagSchool,
                            tagShop = event.filter.tagShop,
                            tagTransport = event.filter.tagTransport,
                            tagPark = event.filter.tagPark,
                            areaCodeFilter = event.filter.areaCodeFilter
                        )
                    }
                    getPropertyList(_filter.value)
                }
            }

            ListDetailsEvent.DismissFilter -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isFilterSheetOpen = false,
                        )
                    }
                }
            }

            ListDetailsEvent.OpenFilter -> {
                _state.update {
                    it.copy(
                        isFilterSheetOpen = true
                    )
                }
            }

            is ListDetailsEvent.SetRangePrice -> {
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
                        orderSurface = state.value.orderSurface,
                        rangePrice = state.value.rangePrice,
                        rangeDate = state.value.rangeDate,
                        rangeSurface = state.value.rangeSurface,
                        sellingStatus = state.value.soldState,
                        tagSchool = state.value.schoolState,
                        tagShop = state.value.shopState,
                        tagTransport = state.value.transportState,
                        tagPark = state.value.parkState,
                        areaCodeFilter = state.value.chosenAreaCode,
                    )
                )
            }

            is ListDetailsEvent.SetRangeSurface -> {
                _filter.update {
                    it.copy(
                        rangeSurface = Range<Int>(
                            event.rangeSurface.lower.toInt(),
                            event.rangeSurface.upper.toInt()
                        )
                    )
                }
                getPropertiesSorted(
                    Filter(
                        sortType = state.value.sortType,
                        orderPrice = state.value.orderPrice,
                        orderDate = state.value.orderDate,
                        orderSurface = state.value.orderSurface,
                        rangePrice = state.value.rangePrice,
                        rangeDate = state.value.rangeDate,
                        rangeSurface = state.value.rangeSurface,
                        sellingStatus = state.value.soldState,
                        tagSchool = state.value.schoolState,
                        tagShop = state.value.shopState,
                        tagTransport = state.value.transportState,
                        tagPark = state.value.parkState,
                        areaCodeFilter = state.value.chosenAreaCode,
                    )
                )
            }

            is ListDetailsEvent.OnParkChecked -> {
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
                        orderSurface = state.value.orderSurface,
                        rangePrice = state.value.rangePrice,
                        rangeDate = state.value.rangeDate,
                        rangeSurface = state.value.rangeSurface,
                        sellingStatus = state.value.soldState,
                        tagSchool = state.value.schoolState,
                        tagShop = state.value.shopState,
                        tagTransport = state.value.transportState,
                        tagPark = state.value.parkState,
                        areaCodeFilter = state.value.chosenAreaCode,
                    )
                )
            }

            is ListDetailsEvent.OnSchoolChecked -> {
                _filter.update {
                    it.copy(
                        tagSchool = !event.value,
                    )
                }
                getPropertyList(
                    filter = Filter(
                        sortType = state.value.sortType,
                        orderPrice = state.value.orderPrice,
                        orderDate = state.value.orderDate,
                        orderSurface = state.value.orderSurface,
                        rangePrice = state.value.rangePrice,
                        rangeDate = state.value.rangeDate,
                        rangeSurface = state.value.rangeSurface,
                        sellingStatus = state.value.soldState,
                        tagSchool = state.value.schoolState,
                        tagShop = state.value.shopState,
                        tagTransport = state.value.transportState,
                        tagPark = state.value.parkState,
                        areaCodeFilter = state.value.chosenAreaCode,
                    )
                )
            }

            is ListDetailsEvent.OnShopChecked -> {
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
                        orderSurface = state.value.orderSurface,
                        rangePrice = state.value.rangePrice,
                        rangeDate = state.value.rangeDate,
                        rangeSurface = state.value.rangeSurface,
                        sellingStatus = state.value.soldState,
                        tagSchool = state.value.schoolState,
                        tagShop = state.value.shopState,
                        tagTransport = state.value.transportState,
                        tagPark = state.value.parkState,
                        areaCodeFilter = state.value.chosenAreaCode,
                    )
                )
            }

            is ListDetailsEvent.OnTransportChecked -> {
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
                        orderSurface = state.value.orderSurface,
                        rangePrice = state.value.rangePrice,
                        rangeDate = state.value.rangeDate,
                        rangeSurface = state.value.rangeSurface,
                        sellingStatus = state.value.soldState,
                        tagSchool = state.value.schoolState,
                        tagShop = state.value.shopState,
                        tagTransport = state.value.transportState,
                        tagPark = state.value.parkState,
                        areaCodeFilter = null,
                    )
                )
                _state.value = _state.value.copy(
                    transportState = !event.value
                )
            }

            is ListDetailsEvent.OnAreaCodeChosen -> {
                _state.update {
                    it.copy(
                        chosenAreaCode = event.code
                    )
                }
                getPropertiesSorted(
                    Filter(
                        sortType = state.value.sortType,
                        orderPrice = state.value.orderPrice,
                        orderDate = state.value.orderDate,
                        orderSurface = state.value.orderSurface,
                        rangePrice = state.value.rangePrice,
                        rangeDate = state.value.rangeDate,
                        rangeSurface = state.value.rangeSurface,
                        sellingStatus = state.value.soldState,
                        tagSchool = state.value.schoolState,
                        tagShop = state.value.shopState,
                        tagTransport = state.value.transportState,
                        tagPark = state.value.parkState,
                        areaCodeFilter = state.value.chosenAreaCode,
                    )
                )
            }

//            is ListDetailsEvent.OnAreaCodeList -> {
//                _state.update {
//                    it.copy(
//                        areaCodeList = getAreaCodes()
//                    )
//                }
//            }
        }
    }
}