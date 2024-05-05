package com.ocproject.realestatemanager.ui.scenes.addproperty

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.ocproject.realestatemanager.data.repositories.PropertyRepository
import com.ocproject.realestatemanager.utils.UtilsKotlin
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
    val propertyId: Int?,
    private val propertyRepository: PropertyRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(AddPropertyState())
    val state = _state.asStateFlow()

    init {
        getProperty()
    }

    private fun getProperty() {
        // if update operation, property editing
        if (propertyId != null && propertyId != 0) {
            viewModelScope.launch {
                val propertyDetails = propertyRepository.getProperty(propertyId)
                var mainPicture: PictureOfProperty? = null
                propertyDetails.pictureList?.forEach {
                    if (it.isMain) mainPicture = it
                }
                _state.update {
                    it.copy(
                        id = propertyDetails.property.id,
                        type = propertyDetails.property.type,
                        price = propertyDetails.property.price,
                        area = propertyDetails.property.area,
                        numberOfRooms = propertyDetails.property.numberOfRooms,
                        description = propertyDetails.property.description,
                        picturesList = propertyDetails.pictureList,
                        address = propertyDetails.property.address,
                        state = propertyDetails.property.state,
                        createDate = propertyDetails.property.createDate,
                        soldDate = propertyDetails.property.soldDate,
                        agentId = propertyDetails.property.agentId,
                        lat = propertyDetails.property.lat,
                        lng = propertyDetails.property.lng,
                        mainPic = mainPicture,
                    )
                }
            }
        }
    }

    fun setPropertyFromPlace(place: Place) {
        var listAddressComponents = place.addressComponents?.asList()?.toList()
        var address: String = ""
        var country: String = ""
        var code: String = ""
        listAddressComponents?.forEach {
            Log.d("TAG", "setAddressFromPlace: ${it.types}")
            Log.d("TAG", "setAddressFromPlace: ${it.name}")
            when (it.types.toString()) {
                "[street_number]" -> {
                    address += "${it.name} "
                }

                "[route]" -> {
                    address += "${it.name}, "
                }

                "[locality, political]" -> {
                    address += "${it.name} "
                }

                "[country, political]" -> {
                    country = "${it.name} "
                }

                "[postal_code]" -> {
                    code = "${it.name} "
                }
            }
            code += country
        }
        onEvent(AddPropertyEvent.SetAddress(address))
        onEvent(AddPropertyEvent.SetState(code))
        onEvent(AddPropertyEvent.SetLat(place.latLng?.latitude.toString()))
        onEvent(AddPropertyEvent.SetLng(place.latLng?.longitude.toString()))
    }

    fun onEvent(event: AddPropertyEvent) {
        when (event) {
            AddPropertyEvent.SaveProperty -> {
                val id = state.value.id
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
                    id = propertyId ?: 0,
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
                    var idProperty = propertyRepository.upsertProperty(property).toInt()
                    // check if update (propertyId not null & upsert return -1) or insert situation ( propertyId null & upsert return id).
                    if (propertyId != null && propertyId != 0) {
                        idProperty = propertyId
                    }


                    propertyRepository.deletePicturesOfPropertyById(idProperty)
                    if (pictureList != null) {
                        for (pic in pictureList) {
                            val pictureOfProperty = PictureOfProperty(
                                isMain = pic.isMain,
                                uri = pic.uri,
                                name = pic.name,
                                propertyId = idProperty
                            )
                            propertyRepository.upsertPictureOfProperty(pictureOfProperty)
                        }
                    }

                }

                _state.update {
                    it.copy(
                        id = 0,
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

            is AddPropertyEvent.SetInterestPoints -> TODO()

            is AddPropertyEvent.SetPictureList -> {
                if (event.pictureList.isNotEmpty()) {
                    _state.update {


                        it.copy(
                            mainPic = event.pictureList[0],
                            picturesList = event.pictureList
                        )
                    }
                }
            }

            is AddPropertyEvent.SetMainPicture -> {
                _state.update {
                    it.copy(
                        mainPic = event.picture
                    )
                }
                viewModelScope.launch {
                    _state.value.picturesList?.forEach {
                        it.isMain = it.uri == _state.value.mainPic?.uri
                    }
                }

            }

            is AddPropertyEvent.UpdatePredictions -> {
//                if (state.value.isSearching) {
                _state.update {
                    it.copy(
                        updatedPredictions = event.predictions,
                        isSearching = false
                    )
                }
//                }
            }

            is AddPropertyEvent.ChangeText -> {
                _state.update {
                    it.copy(
                        changeText = event.text,
                        isSearching = true
                    )
                }
            }

            is AddPropertyEvent.IsSearching -> {
                _state.update {
                    it.copy(
                        isSearching = event.searching
                    )
                }
            }
        }
    }
}