package com.ocproject.realestatemanager.ui.scenes.propertylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ocproject.realestatemanager.data.repositories.PropertyRepository
import com.ocproject.realestatemanager.models.PropertyWithPictures
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel


@KoinViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class PropertyListViewModel(
    private val propertyRepository: PropertyRepository,
) : ViewModel() {
    private val _sortType = MutableStateFlow(SortType.PRICE_ASC)
    private val _properties = propertyRepository.getPropertyList()
    private val _sortedListProperties = MutableStateFlow(emptyList<PropertyWithPictures>())

    private val _state = MutableStateFlow(PropertyListState())
    val state = combine(_state, _sortType, _sortedListProperties) { state, sortType, properties ->
        state.copy(
            properties = properties,
            sortType = sortType,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PropertyListState())

    init {
        onEvent(PropertyListEvent.SortProperties(state.value.sortType))
    }

    private fun getProperties(sortType: SortType) {
        viewModelScope.launch {
            _properties.flowOn(Dispatchers.IO)
                .collect { properties: List<PropertyWithPictures> ->
                    _sortedListProperties.update {
                        when (sortType) {
                            SortType.PRICE_ASC -> properties.sortedBy { it.property.price }
                            SortType.PRICE_DESC -> properties.sortedByDescending { it.property.price }
                            SortType.PRICE_RANGE -> TODO()
                            SortType.TAGS -> TODO()
                        }
                    }
                }
        }
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
                getProperties(event.sortType)
            }

            is PropertyListEvent.OpenFilter -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            openFilterState = event.openFilterState
                        )
                    }
                }
            }

            else -> {}
        }
    }
}
