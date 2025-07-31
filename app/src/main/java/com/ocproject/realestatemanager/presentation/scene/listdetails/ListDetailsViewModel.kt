package com.ocproject.realestatemanager.presentation.scene.listdetails

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ocproject.realestatemanager.core.DataState
import com.ocproject.realestatemanager.core.Filter
import com.ocproject.realestatemanager.core.InterestPoint
import com.ocproject.realestatemanager.core.Order
import com.ocproject.realestatemanager.core.SellingStatus
import com.ocproject.realestatemanager.core.SortType
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

class ListDetailsViewModel(
    @InjectedParam
    private val getPropertyListUseCase: GetPropertyListUseCase,
    private val deletePropertyUseCase: DeletePropertyUseCase,
    private val getPropertyDetailsUseCase: GetPropertyDetailsUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ListDetailsState())
    val state = _state.asStateFlow()

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
        if (state.value.filterState.tagTransport) {
            currentTags.add(InterestPoint.TRANSPORT)
        } else {
            if (currentTags.contains(InterestPoint.TRANSPORT)) {
                currentTags.remove(InterestPoint.TRANSPORT)
            }
        }
        if (state.value.filterState.tagSchool) {
            currentTags.add(InterestPoint.SCHOOL)
        } else {
            if (currentTags.contains(InterestPoint.SCHOOL)) {
                currentTags.remove(InterestPoint.SCHOOL)
            }
        }
        if (state.value.filterState.tagShop) {
            currentTags.add(InterestPoint.SHOP)
        } else {
            if (currentTags.contains(InterestPoint.SHOP)) {
                currentTags.remove(InterestPoint.SHOP)
            }
        }
        if (state.value.filterState.tagPark) {
            currentTags.add(InterestPoint.PARK)
        } else {
            if (currentTags.contains(InterestPoint.PARK)) {
                currentTags.remove(InterestPoint.PARK)
            }
        }
        state.value.selectedTags.value = currentTags
    }

    private fun sortByPrice(
        properties: List<Property>,
    ): List<Property> {
        return when (state.value.filterState.priceOrder) {
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
        return when (state.value.filterState.surfaceOrder) {
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
        return when (state.value.filterState.dateOrder) {
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
        return if (state.value.filterState.areaCodeFilter != null) {
            properties.filter { it.areaCode == state.value.filterState.areaCodeFilter }
        } else {
            properties
        }
    }

    private fun subFilter(
        properties: List<Property>,
    ): List<Property> {
        return when (state.value.filterState.sortType) {
            SortType.PRICE -> {
                sortByPrice(properties)
            }

            SortType.DATE -> {
                sortByDate(
                    properties
                )
            }

            SortType.AREA -> {
                sortBySurface(properties)
            }
        }
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
                onEvent(ListDetailsEvent.GetProperties)
            }

            is ListDetailsEvent.GetProperties -> {
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
                            _state.update {
                                it.copy(
                                    properties = propertiesDataState.data
                                )
                            }
                            setMaxPrice()
                            setMaxSurface()
                            getAreaCodes()
                            onEvent(ListDetailsEvent.UpdateSortedProperties)
                        }
                    }
                }.launchIn(viewModelScope)

//                viewModelScope.launch {
//                    getPropertyList()
//                }
            }

            is ListDetailsEvent.GetDetails -> {
                viewModelScope.launch {
                    onEvent(
                        ListDetailsEvent.UpdateSelectedProperty(
                            getPropertyDetailsUseCase(
                                event.propertyId
                            )
                        )
                    )
                }
                onEvent(ListDetailsEvent.GetProperties)
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

            is ListDetailsEvent.UpdateSelectedProperty -> {
                if (event.property != null) {
                    _state.update {
                        it.copy(
                            selectedProperty = event.property
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            selectedProperty = null
                        )
                    }
                }
            }

            is ListDetailsEvent.UpdateSortedProperties -> {
                addTags()
                var filteredProperties: MutableList<Property> = emptyList<Property>().toMutableList()
                filteredProperties.addAll(
                    state.value.properties
                        .filter { property ->
                            state.value.selectedTags.value.isEmpty() ||
                                    state.value.selectedTags.value.all { it in property.interestPoints }
                        }
                                    .filter { it.price!! >= state.value.filterState.priceRange.lower && it.price <= state.value.filterState.priceRange.upper }
            .filter { it.surfaceArea!! >= state.value.filterState.surfaceRange.lower && it.surfaceArea <= state.value.filterState.surfaceRange.upper }
            .filter { it.photoList.size >= state.value.filterState.minNbrPhotos }
            .filter { it.createdDate!! >= state.value.filterState.dateRange.lower && it.createdDate <= state.value.filterState.dateRange.upper }
                )

                filteredProperties = sortByAreaCode(filteredProperties.toList()).toMutableList()
                viewModelScope.launch {
                    _state.update { listDetailsState ->
                        listDetailsState.copy(
                            sortedProperties = when (state.value.filterState.sellingStatus) {
                                SellingStatus.SOLD -> {
                                    subFilter(
                                        filteredProperties
                                            .filter { it.sold != null && it.sold >= state.value.filterState.soldDateRange.lower && it.sold <= state.value.filterState.soldDateRange.upper }
                                    )

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
                        )
                    }
                }
            }

            is ListDetailsEvent.UpdateFilter -> {
                _state.update {
                    it.copy(
                        filterState = Filter(
                            sortType = event.filter.sortType,
                            priceOrder = event.filter.priceOrder,
                            dateOrder = event.filter.dateOrder,
                            surfaceOrder = event.filter.surfaceOrder,
                            priceRange = event.filter.priceRange,
                            dateRange = event.filter.dateRange,
                            soldDateRange = event.filter.soldDateRange,
                            surfaceRange = event.filter.surfaceRange,
                            sellingStatus = event.filter.sellingStatus,
                            tagSchool = event.filter.tagSchool,
                            tagTransport = event.filter.tagTransport,
                            tagShop = event.filter.tagShop,
                            tagPark = event.filter.tagPark,
                            areaCodeFilter = event.filter.areaCodeFilter,
                            minNbrPhotos = event.filter.minNbrPhotos,
                        )
                    )
                }
                onEvent(ListDetailsEvent.UpdateSortedProperties)

            }
        }
    }
}