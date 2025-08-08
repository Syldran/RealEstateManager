package com.ocproject.realestatemanager.presentation.scene.listdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ocproject.realestatemanager.core.DataState
import com.ocproject.realestatemanager.core.Filter
import com.ocproject.realestatemanager.domain.usecases.DeletePropertyUseCase
import com.ocproject.realestatemanager.domain.usecases.GetPropertyDetailsUseCase
import com.ocproject.realestatemanager.domain.usecases.GetPropertyListFilteredUseCase
import com.ocproject.realestatemanager.domain.usecases.GetPropertyListUseCase
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsEvent.DeleteProperty
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsEvent.DismissFilter
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsEvent.GetDetails
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsEvent.GetPropertiesFiltered
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsEvent.OnClickPropertyDisplayMode
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsEvent.OpenFilter
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsEvent.UpdateFilter
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsEvent.UpdateSelectedProperty
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
    private val getPropertyListFilteredUseCase: GetPropertyListFilteredUseCase,
    private val deletePropertyUseCase: DeletePropertyUseCase,
    private val getPropertyDetailsUseCase: GetPropertyDetailsUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ListDetailsState())
    val state = _state.asStateFlow()

    init {
        onEvent(ListDetailsEvent.GetProperties)
    }
    private fun setMaxPrice() {

        viewModelScope.launch {
            val maxPriceProperty = state.value.properties.maxByOrNull { property ->
                property.price
            }

            maxPriceProperty?.price?.let {
                if (state.value.maxPrice < it){
                    _state.update { propertyListState ->
                        propertyListState.copy(
                            maxPrice = maxPriceProperty.price
                        )
                    }
                }
            }
        }
    }

    private fun setMaxSurface() {
        viewModelScope.launch {
            val maxSurfaceProperty = state.value.properties.maxByOrNull { property ->
                property.surfaceArea
            }

            maxSurfaceProperty?.surfaceArea?.let {
                if (state.value.maxSurface < it){
                    _state.update { propertyListState ->
                        propertyListState.copy(
                            maxSurface = maxSurfaceProperty.surfaceArea
                        )
                    }
                }
            }
        }
    }

    fun getAreaCodes() {
        viewModelScope.launch {
            val areaCodesFilter = mutableListOf<Int>()
            for (property in state.value.properties) {
                if (!areaCodesFilter.contains(property.areaCode)) {
                    areaCodesFilter += property.areaCode
                }
            }
            _state.update {
                it.copy(
                    areaCodeList = areaCodesFilter
                )
            }
        }
    }


    fun onEvent(event: ListDetailsEvent) {
        when (event) {
            is OnClickPropertyDisplayMode -> {
                _state.update {
                    it.copy(
                        mapMode = event.map
                    )
                }
            }


            is DeleteProperty -> {
                viewModelScope.launch {
                    deletePropertyUseCase(event.property)
                }
                onEvent(GetPropertiesFiltered(state.value.filterState))
            }

            is GetDetails -> {
                viewModelScope.launch {
                    onEvent(
                        UpdateSelectedProperty(
                            getPropertyDetailsUseCase(
                                event.propertyId
                            )
                        )
                    )
                }
//                onEvent(GetPropertiesFiltered(state.value.filterState))
            }

            DismissFilter -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isFilterSheetOpen = false,
                        )
                    }
                }
            }

            OpenFilter -> {
                _state.update {
                    it.copy(
                        isFilterSheetOpen = true
                    )
                }
            }

            is UpdateSelectedProperty -> {
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

            is UpdateFilter -> {
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
                            typeHousing = event.filter.typeHousing,
                            minNbrPhotos = event.filter.minNbrPhotos,
                        )
                    )
                }
//                onEvent(GetPropertiesFiltered(state.value.filterState))
            }

            is GetPropertiesFiltered -> {
                getPropertyListFilteredUseCase(state.value.filterState).onEach { propertiesDataState ->
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
                            getAreaCodes()
                            setMaxPrice()
                            setMaxSurface()
                        }
                    }
                }.launchIn(viewModelScope)
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
                            getAreaCodes()
                            setMaxSurface()
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }
}