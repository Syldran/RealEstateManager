package com.ocproject.realestatemanager.ui.scenes.addproperty

import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ocproject.realestatemanager.db.PropertyDao
import com.ocproject.realestatemanager.repositories.PropertyRepository
import com.ocproject.realestatemanager.utils.UtilsKotlin
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
    private val propertyRepository: PropertyRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(AddPropertyState())
    val state = _state.asStateFlow()

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
                    val id = propertyRepository.upsertProperty(property)

                    for (pic in pictureList){
                            val pictureOfProperty = PictureOfProperty(
                                isMain =  pic.isMain,
                                uri = pic.uri,
                                name = pic.name,
                                propertyId = id.toInt()
                            )
                            propertyRepository.upsertPictureOfProperty(pictureOfProperty)
                    }
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
                    if (event.price.isBlank()) {
                        it.copy(
                            price = 0
                        )
                    } else if (UtilsKotlin.isInteger(event.price)) {
                        it.copy(
                            price = event.price.toInt()
                        )
                    } else return
                }
            }

            is AddPropertyEvent.SetArea -> {
                _state.update {
                    it.copy(
                        area = event.area.toInt()
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
                        lat = event.lat.toDouble()
                    )
                }
            }

            is AddPropertyEvent.SetLng -> {
                _state.update {

                    it.copy(
                        lng = event.lng.toDouble()
                    )
                }
            }

            is AddPropertyEvent.SetNumberOfRooms -> {
                _state.update {
                    if (event.numberOfRooms.isBlank()) {
                        it.copy(
                            numberOfRooms = 0
                        )
                    } else if (UtilsKotlin.isInteger(event.numberOfRooms)) {
                        it.copy(
                            numberOfRooms = event.numberOfRooms.toInt()
                        )
                    } else return
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
                        mainPic = event.pictureList[0],
                        picturesList = event.pictureList
                    )
                }
            }

            is AddPropertyEvent.SetMainPicture -> {
                _state.update {
                    it.copy(
                      mainPic = event.picture
                    )
                }
                viewModelScope.launch {
                    _state.value.picturesList.forEach {
                        it.isMain = it.uri == _state.value.mainPic?.uri
                    }
                }

            }
        }
    }

//    fun getPropertyDetail(id: Int): PropertyWithPictures {
//        return dao.getPropertyDetails(id)
//    }





}