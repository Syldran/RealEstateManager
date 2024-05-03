package com.ocproject.realestatemanager.ui.scenes.propertylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ocproject.realestatemanager.data.repositories.PropertyRepository
import com.ocproject.realestatemanager.models.PropertyWithPictures
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel


@KoinViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class PropertyListViewModel(
    private val propertyRepository: PropertyRepository,
) : ViewModel() {
    //    private val propertyId: Int = checkNotNull(savedStateHandle["propertyId"])
    private val _sortType = MutableStateFlow(SortType.PRICEASC)
    private val _properties = propertyRepository.getPropertyList().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    // trying to make it work with only sorting in viewmodel on a list of properties from database.
    private val _sortedProperties = _sortType
        .flatMapLatest { sortType ->
            when (sortType) {
                SortType.PRICEASC -> sortByPriceAsc(_properties)
                SortType.PRICEDESC -> sortByPriceDesc(_properties)
                SortType.PRICERANGE -> TODO()
                SortType.TAGS -> TODO()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    // working with database sorting
    private val _sortedPropertiesBis = _sortType
        .flatMapLatest { sortType ->
            when (sortType) {
                SortType.PRICEASC -> propertyRepository.getPropertyListOrderedByPriceAsc()
                SortType.PRICEDESC -> propertyRepository.getPropertyListOrderedByPriceDesc()
                SortType.PRICERANGE -> TODO()
                SortType.TAGS -> TODO()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(PropertyListState())
    val state = combine(_state, _sortType, _sortedProperties) { state, sortType, properties ->
        state.copy(
            properties = properties,
            sortType = sortType,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PropertyListState())


    private fun sortByPriceAsc(properties: StateFlow<List<PropertyWithPictures>>): Flow<List<PropertyWithPictures>> {

        return flowOf(properties.value.sortedBy { it.property.price })

    }
    private fun sortByPriceDesc(properties: StateFlow<List<PropertyWithPictures>>): Flow<List<PropertyWithPictures>> {

        return flowOf(properties.value.sortedByDescending { it.property.price })

    }

    fun onEvent(event: PropertyListEvent) {
        when (event) {
            is PropertyListEvent.DeleteProperty -> {
                viewModelScope.launch {
                    propertyRepository.deleteProperty(event.property)
                }
            }


            is PropertyListEvent.SortProperties -> {
//                _state.update {
//                    it.copy(isLoading = true)
//                }
                viewModelScope.launch {
                    _sortType.value = event.sortType
                }
            }

            else -> {}
        }
    }
}