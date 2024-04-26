package com.ocproject.realestatemanager.ui.scenes.propertylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ocproject.realestatemanager.data.repositories.PropertyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class PropertyListViewModel(
//    private val dao: PropertyDao,
    private val propertyRepository: PropertyRepository,
) : ViewModel() {
    //    private val propertyId: Int = checkNotNull(savedStateHandle["propertyId"])
    private val _sortType = MutableStateFlow(SortType.PRICE)
    private val _properties = _sortType
        .flatMapLatest { sortType ->
            when (sortType) {
//                SortType.PRICE -> dao.getPropertiesOrderedByPrice()
                SortType.PRICE -> propertyRepository.getPropertyListOrderedByPrice()
                /* SortType.LAST_NAME -> dao.getContactOrderedByLastName()
                 SortType.PHONE_NUMBER -> dao.getContactOrderedByPhoneNumber()*/
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(PropertyListState())
    val state = combine(_state, _sortType, _properties) { state, sortType, properties ->
        state.copy(
            properties = properties,
            sortType = sortType,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PropertyListState())


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