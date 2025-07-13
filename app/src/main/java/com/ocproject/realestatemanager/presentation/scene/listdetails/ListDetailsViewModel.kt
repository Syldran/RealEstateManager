package com.ocproject.realestatemanager.presentation.scene.listdetails

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ocproject.realestatemanager.core.DataState
import com.ocproject.realestatemanager.core.InterestPoint
import com.ocproject.realestatemanager.core.Order
import com.ocproject.realestatemanager.core.SellingStatus
import com.ocproject.realestatemanager.core.SortType
import com.ocproject.realestatemanager.core.utils.Range
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.usecases.DeletePropertyUseCase
import com.ocproject.realestatemanager.domain.usecases.GetPropertyDetailsUseCase
import com.ocproject.realestatemanager.domain.usecases.GetPropertyListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import timber.log.Timber
import java.text.DateFormat
import java.util.Date

class ListDetailsViewModel(
    @InjectedParam
    private val getPropertyListUseCase: GetPropertyListUseCase,
    private val deletePropertyUseCase: DeletePropertyUseCase,
    private val getPropertyDetailsUseCase: GetPropertyDetailsUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ListDetailsState())
    val state = _state.asStateFlow()

    init {
        getPropertyList()
        onEvent(ListDetailsEvent.GetDetails)
    }

    private fun setMaxPrice() {
        viewModelScope.launch {
            val maxPriceProperty = state.value.properties.maxByOrNull { propertyWithPhoto ->
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
            val maxSurfaceProperty = state.value.properties.maxByOrNull { propertyWithPhoto ->
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
            for (property in state.value.properties) {
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

    private fun addTags() {
        val currentTags = emptyList<InterestPoint>().toMutableList()
        if (state.value.transportTag) {
            currentTags.add(InterestPoint.TRANSPORT)
        } else {
            if (currentTags.contains(InterestPoint.TRANSPORT)) {
                currentTags.remove(InterestPoint.TRANSPORT)
            }
        }
        if (state.value.schoolTag) {
            currentTags.add(InterestPoint.SCHOOL)
        } else {
            if (currentTags.contains(InterestPoint.SCHOOL)) {
                currentTags.remove(InterestPoint.SCHOOL)
            }
        }
        if (state.value.shopTag) {
            currentTags.add(InterestPoint.SHOP)
        } else {
            if (currentTags.contains(InterestPoint.SHOP)) {
                currentTags.remove(InterestPoint.SHOP)
            }
        }
        if (state.value.parkTag) {
            currentTags.add(InterestPoint.PARK)
        } else {
            if (currentTags.contains(InterestPoint.PARK)) {
                currentTags.remove(InterestPoint.PARK)
            }
        }
        state.value.selectedTags.value = currentTags
    }

    fun getPropertyList() {

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
                    onEvent(ListDetailsEvent.UpdateProperties(propertiesDataState.data))
                    setMaxPrice()
                    setMaxSurface()
                    getAreaCodes()
                }
            }
        }.launchIn(viewModelScope)

    }

    fun sortProperties(): List<Property> {
        addTags()
        var filteredProperties: MutableList<Property> = emptyList<Property>().toMutableList()
        filteredProperties.addAll(
            state.value.properties.filter { property ->
                state.value.selectedTags.value.isEmpty() ||
                        state.value.selectedTags.value.all { it in property.interestPoints }
            }
                .filter { it.price!! >= state.value.rangePrice.lower && it.price <= state.value.rangePrice.upper }
                .filter { it.surfaceArea!! >= state.value.rangeSurface.lower && it.surfaceArea <= state.value.rangeSurface.upper }
            // WHY ???
//                .filter { it.createdDate >= state.value.rangeDate.lower && it.createdDate <= state.value.rangeDate.upper }
        )

        filteredProperties = sortByAreaCode(filteredProperties.toList()).toMutableList()
        return when (state.value.soldStatus) {
            SellingStatus.SOLD -> {
                subFilter(
                    filteredProperties
                        .filter { it.sold != null && it.sold >= state.value.soldRangeDate.lower && it.sold <= state.value.soldRangeDate.upper })
            }

            SellingStatus.ALL -> {
                subFilter(
                    filteredProperties
                )

            }

            SellingStatus.PURCHASABLE -> {
                subFilter(filteredProperties.filter { it.sold == null })
            }
        }
    }

    private fun sortByPrice(
        properties: List<Property>,
    ): List<Property> {
        return when (state.value.orderPrice) {
            Order.ASC -> {
                properties
                    .sortedBy { it.price }
            }

            Order.DESC -> {
                properties
                    .sortedByDescending { it.price }
            }
        }
    }

    private fun sortBySurface(
        properties: List<Property>,
    ): List<Property> {
        return when (state.value.orderSurface) {
            Order.ASC -> {
                properties
                    .sortedBy { it.surfaceArea }
            }

            Order.DESC -> {
                properties
                    .sortedByDescending { it.surfaceArea }
            }
        }
    }

    private fun sortByDate(
        properties: List<Property>,
    ): List<Property> {
        return when (state.value.orderDate) {
            Order.ASC ->
                properties
                    .sortedBy { it.createdDate }

            Order.DESC ->
                properties
                    .sortedByDescending { it.createdDate }
        }
    }

    private fun sortByAreaCode(
        properties: List<Property>,
    ): List<Property> {
        return if (state.value.chosenAreaCode != null) {
            properties.filter { it.areaCode == state.value.chosenAreaCode }
        } else {
            properties
        }
    }

    private fun subFilter(
        properties: List<Property>,
    ): List<Property> {
        return when (state.value.sortType) {
            SortType.PRICE -> {
                sortByPrice(properties)
            }

            SortType.DATE -> {
                sortByDate(properties)
            }

            SortType.SURFACE -> {
                sortBySurface(properties)
            }
        }
    }

    fun datePresentation(property: Property): String {
        val date = Date(property.createdDate!!)
        val formatter: DateFormat =
            DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
        return formatter.format(date)
    }

    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
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
                getPropertyList()
            }

            is ListDetailsEvent.GetProperties -> {

                viewModelScope.launch {
                    getPropertyList()
                }
            }

            is ListDetailsEvent.GetDetails -> {
                viewModelScope.launch {
                    if (state.value.selectedProperty != null) {
                        onEvent(
                            ListDetailsEvent.UpdateSelectedProperty(
                                getPropertyDetailsUseCase(
                                    state.value.selectedProperty!!.id
                                )
                            )
                        )
                    } else {
                        onEvent(ListDetailsEvent.UpdateSelectedProperty(null))
                    }
                    Timber.tag("DETAILS")
                        .d("photonbr :${state.value.selectedProperty?.photoList?.size}, selectedPropertyId :${state.value.selectedProperty?.id}")
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
                _state.update {
                    it.copy(
                        rangePrice = Range<Int>(
                            event.rangePrice.lower.toInt(),
                            event.rangePrice.upper.toInt()
                        )
                    )
                }
                getPropertyList()
            }

            is ListDetailsEvent.SetRangeSurface -> {
                _state.update {
                    it.copy(
                        rangeSurface = Range<Int>(
                            event.rangeSurface.lower.toInt(),
                            event.rangeSurface.upper.toInt()
                        )
                    )
                }
                getPropertyList()
            }

            is ListDetailsEvent.OnParkChecked -> {
                _state.update {
                    it.copy(
                        parkTag = !event.value,
                    )
                }
                getPropertyList()
            }

            is ListDetailsEvent.OnSchoolChecked -> {
                _state.update {
                    it.copy(
                        schoolTag = !event.value,
                    )
                }
                getPropertyList()
            }

            is ListDetailsEvent.OnShopChecked -> {
                _state.update {
                    it.copy(
                        shopTag = !event.value,
                    )
                }
                getPropertyList()
            }

            is ListDetailsEvent.OnTransportChecked -> {
                _state.update {
                    it.copy(
                        transportTag = !event.value,
                    )
                }
                getPropertyList()
            }

            is ListDetailsEvent.OnAreaCodeChosen -> {
                _state.update {
                    it.copy(
                        chosenAreaCode = event.code
                    )
                }
                getPropertyList()
            }

            is ListDetailsEvent.OnDateRangeSelected -> {
                _state.update {
                    it.copy(
                        rangeDate = Range<Long>(event.startRange, event.endRange)
                    )
                }
                getPropertyList()
            }

            is ListDetailsEvent.OnSoldDateRangeSelected -> {
                _state.update {
                    it.copy(
                        soldRangeDate = Range<Long>(event.startRange, event.endRange)
                    )
                }
                getPropertyList()
            }

            is ListDetailsEvent.UpdateSelectedProperty -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            selectedProperty = event.property
                        )
                    }
                }
                Timber.tag("SELECTED_PROPERTY").d("${state.value.selectedProperty?.id}")
            }

            is ListDetailsEvent.UpdateSortedProperties -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            sortedProperties = sortProperties()
                        )
                    }
                }
            }

            is ListDetailsEvent.UpdateProperties -> {
                _state.update {
                    it.copy(
                        properties = event.properties
                    )
                }
                onEvent(ListDetailsEvent.UpdateSortedProperties)
            }

            is ListDetailsEvent.UpdateFilter -> {
                _state.update {
                    it.copy(
                        sortType = event.filter.sortType,
                        orderPrice = event.filter.orderPrice,
                        orderDate = event.filter.orderDate,
                        orderSurface = event.filter.orderSurface,
                        rangePrice = event.filter.rangePrice,
                        rangeDate = event.filter.rangeDate,
                        soldRangeDate = event.filter.soldRangeDate,
                        rangeSurface = event.filter.rangeSurface,
                        soldStatus = event.filter.sellingStatus,
                        schoolTag = event.filter.tagSchool,
                        transportTag = event.filter.tagTransport,
                        shopTag = event.filter.tagShop,
                        parkTag = event.filter.tagPark,
                        chosenAreaCode = event.filter.areaCodeFilter,
                    )
                }
                onEvent(ListDetailsEvent.UpdateSortedProperties)
            }
        }
    }
}