package com.ocproject.realestatemanager.ui.scenes.addproperty

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ocproject.realestatemanager.db.PropertyDao
import com.ocproject.realestatemanager.models.PropertyWithPictures
import com.openclassrooms.realestatemanager.models.InterestPoint
import com.openclassrooms.realestatemanager.models.PictureOfProperty
import com.openclassrooms.realestatemanager.models.Property
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.util.Calendar

@KoinViewModel
class AddPropertyViewModel(
    private val dao: PropertyDao
) : ViewModel() {
    private val _state = MutableStateFlow(AddPropertyState())
    val state = _state.asStateFlow()

//
    fun onEvent(event: AddPropertyEvent) {
        when (event) {



            AddPropertyEvent.SaveProperty -> {
                val type = state.value.type
                val price = state.value.price
                val area = state.value.area
                val numberOfRooms = state.value.numberOfRooms
                val description = state.value.description
                val address = state.value.address
                val state1 = state.value.state
                val lat = state.value.lat
                val lng = state.value.lng
                val pictureList = state.value.picturesList
                val interestPoint = state.value.interestPoints

                if (price == 0 || address.isBlank() || area == 0 || state1.isBlank()) {
                    return
                }

                val calendar = Calendar.getInstance()
                val property = Property(
                    type = type,
                    price = price,
                    area = area,
                    numberOfRooms = numberOfRooms,
                    description = description,
//                    pictureList = emptyList(),
                    address = address,
                    interestPoints = interestPoint.toString(),
                    state = state1,
                    createDate = "${calendar.get(Calendar.YEAR)}//${calendar.get(Calendar.MONTH)}//${
                        calendar.get(
                            Calendar.DAY_OF_MONTH
                        )
                    }",
                    lat = lat,
                    lng = lng,
                    agentId = "Agent1",
                    soldDate = "",
                )

                viewModelScope.launch {
                    dao.upsertProperty(property)
                }
                _state.update {
                    it.copy(
                        type = "",
                        price = 0,
                        area = 0,
                        numberOfRooms = 0,
                        description = "",
                        picturesList = emptyList(),
                        address = "",
                        interestPoints = emptyList(),
                        state = "",
                        lat = 0.0,
                        lng = 0.0,
                        createDate = "",
                        agentId = "",
                        soldDate = "",
                    )
                }
            }

            is AddPropertyEvent.SetType -> {
                _state.update {
                    it.copy(
                        type = event.type
                    )
                }
            }

            is AddPropertyEvent.SetPrice -> {
                _state.update {
                    it.copy(
                        price = event.price
                    )
                }
            }

            is AddPropertyEvent.SetArea -> {
                _state.update {
                    it.copy(
                        area = event.area
                    )
                }
            }

            is AddPropertyEvent.SetAddress -> {
                _state.update {
                    it.copy(
                        address = event.address
                    )
                }
            }

            is AddPropertyEvent.SetDescription -> {
                _state.update {
                    it.copy(
                        description = event.description
                    )
                }
            }

            is AddPropertyEvent.SetLat -> {
                _state.update {
                    it.copy(
                        lat = event.lat
                    )
                }
            }

            is AddPropertyEvent.SetLng -> {
                _state.update {
                    it.copy(
                        lng = event.lng
                    )
                }
            }

            is AddPropertyEvent.SetNumberOfRooms -> {
                _state.update {
                    it.copy(
                        numberOfRooms = event.numberOfRooms
                    )
                }
            }

            is AddPropertyEvent.SetState -> {
                _state.update {
                    it.copy(
                        state = event.state
                    )
                }
            }

//            is AddPropertyEvent.GetPropertyDetail -> {
//
//            }

            is AddPropertyEvent.SetInterestPoints -> TODO()
            is AddPropertyEvent.SetPictureList -> {
                _state.update {
                    it.copy(
                        picturesList = event.pictureList
                    )
                }
            }
        }
    }

//    fun getPropertyDetail(id: Int): PropertyWithPictures {
//        return dao.getPropertyDetails(id)
//    }

    sealed interface AddPropertyEvent {
        data object SaveProperty : AddPropertyEvent
        data class SetType(val type: String) : AddPropertyEvent
        data class SetPrice(val price: Int) : AddPropertyEvent
        data class SetArea(val area: Int) : AddPropertyEvent
        data class SetNumberOfRooms(val numberOfRooms: Int) : AddPropertyEvent
        data class SetDescription(val description: String) : AddPropertyEvent
        data class SetAddress(val address: String) : AddPropertyEvent
        data class SetState(val state: String) : AddPropertyEvent
        data class SetLat(val lat: Double) : AddPropertyEvent
        data class SetLng(val lng: Double) : AddPropertyEvent
        data class SetPictureList(val pictureList: List<Uri>): AddPropertyEvent
        data class SetInterestPoints(val interestPoints: List<InterestPoint>): AddPropertyEvent
    }

    data class AddPropertyState(
        val type: String = "",
        val price: Int = 0,
        val area: Int = 0,
        val numberOfRooms: Int = 0,
        val description: String = "",
        val picturesList: List<Uri> = emptyList(), // to create
        val address: String = "",
        val interestPoints: List<InterestPoint> = emptyList(), // to create
        val state: String = "",
        val createDate: String = "",
        val soldDate: String = "",
        val agentId: String = "",
        val lat: Double = 0.0,
        val lng: Double = 0.0,
        //
        val isAddingProperty:Boolean = false,
    )

}