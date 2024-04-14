package com.ocproject.realestatemanager.ui.scenes.propertylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ocproject.realestatemanager.models.PropertyWithPictures
import com.ocproject.realestatemanager.repositories.PropertyRepository
import com.ocproject.realestatemanager.ui.DataState
import com.ocproject.realestatemanager.ui.UIComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.*

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

//    fun execute(): Flow<DataState<List<PropertyWithPictures>>> {
//        return flow {
//            emit(DataState.Loading(true))
//            try {
//                val propertyList = propertyRepository.getPropertyListOrderedByPrice()
//                emit(DataState.Success(propertyList.single()))
//            } catch (e: Exception) {
//                e.printStackTrace()
//                emit(DataState.Error(UIComponent.Toast(e.message ?: "Unknow Error")))
//            } finally {
//                emit(DataState.Loading(false))
//            }
//        }
//    }

//    fun getPosts(){
//        intent {
//            val posts = getPosts.execute()
//            posts.onEach { dataState ->
//                when(dataState){
//                    is DataState.Loading ->{
//                        reduce {
//                            state.copy(progressBar = dataState.isLoading)
//                        }
//                    }
//                    is DataState.Success->{
//                        reduce {
//                            state.copy(posts = dataState.data)
//                        }
//                    }
//                    is DataState.Error->{
//                        when(dataState.uiComponent){
//                            is UIComponent.Toast -> {
//                                reduce {
//                                    state.copy(error = dataState.uiComponent.text)
////                                alternative way to reduce { }
////                                postSideEffect(UIComponent.Toast(dataState.uiComponent.text))
//                                }
//                            }
//                        }
//                    }
//                }
//            }.launchIn(viewModelScope)
//        }
//    }

//    private fun getNotes() {
//        getNotesJob?.cancel()
//        getNotesJob = getNotesStream.invoke(_uiState.value.selectedDate.toLocalDate())
//            .onEach { result ->
//                when (result) {
//                    is Result.Error -> {
//                        _uiState.update {
//                            val messages = it.messages + result.message
//                            it.copy(isLoading = false, messages = messages)
//                        }
//                    }
//
//                    is Result.Loading -> {
//                        _uiState.update { it.copy(isLoading = true) }
//                    }
//
//                    is Result.Success -> {
//                        _uiState.update {
//                            it.copy(
//                                isLoading = true,
//                                visibleNotes = result.data
//                            )
//                        }
//                    }
//                }
//            }
//            .launchIn(viewModelScope)
//    }


//    private fun fetchMovies() {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                updateState { oldState -> oldState.copy(isLoading = true) }
//                repository.getPopularMovies().body()?.results?.let {
//                    updateState { oldState ->
//                        oldState.copy(
//                            isLoading = false,
//                            movies = it
//                        )
//                    }
//                }
//            } catch (e: Exception) {
//                updateState { it.copy(isLoading = false, errorMessage = e.message) }
//            }
//        }
//    }

//    sealed interface PropertyListEvent {
//        data class SortProperties(val sortType: SortType) : PropertyListEvent
//        data class DeleteProperty(val property: Property) : PropertyListEvent
//    }


//    data class PropertyListState(
//        val properties: List<PropertyWithPictures> = emptyList(),
//        val sortType: SortType = SortType.PRICE
//    )

}