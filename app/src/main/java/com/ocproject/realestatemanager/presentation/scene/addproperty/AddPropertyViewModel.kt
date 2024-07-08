package com.ocproject.realestatemanager.presentation.scene.addproperty

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.ocproject.realestatemanager.data.repositories.PropertiesRepository
import com.ocproject.realestatemanager.models.InterestPoint
import com.ocproject.realestatemanager.models.PhotoProperty
import com.ocproject.realestatemanager.models.Property
import com.ocproject.realestatemanager.presentation.scene.addproperty.utils.PropertyValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.util.Calendar

@KoinViewModel
class AddPropertyViewModel(
    private val propertyId: Long?,
    private val propertiesRepository: PropertiesRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AddPropertyState())
    val state = _state.asStateFlow()

    var newProperty: Property? by mutableStateOf(null)
        private set

    var photoList: MutableState<List<PhotoProperty>?> = mutableStateOf(null)
        private set

//    var valueString : MutableState<String>

    init {
        getProperty()
    }

    private fun getProperty() {

        if (propertyId != null && propertyId != 0L) {
            viewModelScope.launch {
                newProperty = propertiesRepository.getProperty(propertyId).property
                photoList.value = propertiesRepository.getProperty(propertyId).photoList


            }
        } else {
            newProperty = Property(
                interestPoints = null,
                address = "a",
                town = "a",
                lat = 0.0,
                lng = 0.0,
                country = "a",
                createdDate = null,
                areaCode = 123,
                surfaceArea = 123,
                price = 123,
                id = 0L
            )
        }
    }


    fun setPropertyFromPlace(place: Place) {
        var listAddressComponents = place.addressComponents?.asList()?.toList()
        var address: String = ""
        var town: String = ""
        var country: String = ""
        var code: String = ""
        listAddressComponents?.forEach {
            when (it.types.toString()) {
                "[street_number]" -> {
                    address += "${it.name} "
                }

                "[route]" -> {
                    address += it.name
                }

                "[locality, political]" -> {
                    town = it.name
                }

                "[country, political]" -> {
                    country = it.name
                }

                "[postal_code]" -> {
                    code = it.name
                }
            }
        }
        onEvent(AddPropertyEvent.OnAddressChanged(address))
        onEvent(AddPropertyEvent.OnTownChanged(town))
        onEvent(AddPropertyEvent.OnCountryChanged(country))
        onEvent(AddPropertyEvent.OnAreaCodeChanged(code))
        onEvent(AddPropertyEvent.OnLatChanged(place.latLng?.latitude.toString()))
        onEvent(AddPropertyEvent.OnLngChanged(place.latLng?.longitude.toString()))
    }

    fun onEvent(event: AddPropertyEvent) {
        when (event) {

            AddPropertyEvent.SaveProperty -> {
                newProperty?.let { property ->
                    val result = PropertyValidator.validateProperty(property)
                    val errors = listOfNotNull(
                        result.addressError,
                        result.townError,
                        result.areaCodeError,
                        result.countryError,
                        result.priceError,
                        result.surfaceAreaError,
                        result.latError,
                        result.lngError,
                    )

                    if (errors.isEmpty()) {
                        _state.update {
                            it.copy(
                                addressError = null,
                                townError = null,
                                areaCodeError = null,
                                countryError = null,
                                priceError = null,
                                surfaceAreaError = null,
                                latError = null,
                                lngError = null,
                            )
                        }

                        viewModelScope.launch {
                            var idProperty: Long
                            if (property.createdDate == null) {
                                idProperty = propertiesRepository.upsertProperty(
                                    property.copy(
                                        createdDate = Calendar.getInstance().timeInMillis
                                    )
                                )
                            } else {
                                idProperty = propertiesRepository.upsertProperty(property)
                            }

                            if (propertyId != null && propertyId != 0L) {
                                idProperty = propertyId
                                propertiesRepository.deletePicturesOfPropertyById(idProperty)
                            }

                            if (photoList.value != null) {
                                for (photo in photoList.value!!) {
                                    val photoProperty = PhotoProperty(
                                        isMain = photo.isMain,
                                        photoBytes = photo.photoBytes,
                                        name = photo.name,
                                        propertyId = idProperty
                                    )
                                    propertiesRepository.upsertPhotoProperty(photoProperty)
                                }
                            }

                        }


                        _state.update {
                            it.copy(navToPropertyListScreen = true)
                        }
                    } else {
                        _state.update {
                            it.copy(
                                addressError = result.addressError,
                                townError = result.townError,
                                areaCodeError = result.areaCodeError,
                                countryError = result.countryError,
                                priceError = result.priceError,
                                surfaceAreaError = result.surfaceAreaError,
                                latError = result.latError,
                                lngError = result.lngError,
                            )
                        }
                    }
                }
            }

            is AddPropertyEvent.OnAddressChanged -> {
                newProperty = newProperty?.copy(
                    address = event.value
                )
            }

            is AddPropertyEvent.OnTownChanged -> {
                newProperty = newProperty?.copy(
                    town = event.value
                )
            }

            is AddPropertyEvent.OnAreaCodeChanged -> {
                try {
                    val value: Int? = when (event.value.isEmpty()) {
                        true -> {
                            null
                        }

                        else -> {
                            event.value.toInt()
                        }
                    }
                    newProperty = newProperty?.copy(
                        areaCode = value
                    )
                } catch (e: Exception) {
                    Log.d("TAG", "onEvent: error : ${e.message}")
                    newProperty = newProperty?.copy(
                        areaCode = null
                    )
                }

            }


            is AddPropertyEvent.OnCountryChanged -> {
                newProperty = newProperty?.copy(
                    country = event.value
                )
            }

            is AddPropertyEvent.OnSurfaceAreaChanged -> {
                val value: Int? = when (event.value.isEmpty()) {
                    true -> {
                        null
                    }

                    else -> {
                        event.value.toInt()
                    }
                }
                newProperty = newProperty?.copy(
                    surfaceArea = value
                )
            }

            is AddPropertyEvent.OnLatChanged -> {
                try {
                    val value: Double? = when (event.value.isEmpty()) {
                        true -> {
                            null
                        }

                        else -> {
                            event.value.toDouble()
                        }
                    }
                    newProperty = value?.let {
                        newProperty?.copy(
                            lat = it
                        )
                    }
                } catch (e: Exception) {
                    Log.d("TAG", "onEvent: error : ${e.message}")
                    newProperty = newProperty?.copy(
                        lat = 0.0
                    )
                }
            }

            is AddPropertyEvent.OnLngChanged -> {
                try {
                    val value: Double? = when (event.value.isEmpty()) {
                        true -> {
                            null
                        }

                        else -> {
                            event.value.toDouble()
                        }
                    }

                    newProperty = value?.let {
                        newProperty?.copy(
                            lng = it
                        )
                    }
                } catch (e: Exception) {
                    Log.d("TAG", "onEvent: error : ${e.message}")
                    newProperty = newProperty?.copy(
                        lng = 0.0
                    )
                }
            }

            is AddPropertyEvent.OnPriceChanged -> {
                val value: Int? = when (event.value.isEmpty()) {
                    true -> {
                        null
                    }

                    else -> {
                        event.value.toInt()
                    }
                }
                newProperty = newProperty?.copy(
                    price = value
                )
            }

            is AddPropertyEvent.OnPhotoNameChanged -> {

                val photo = PhotoProperty(
                    isMain = event.photoProperty.isMain,
                    photoBytes = event.photoProperty.photoBytes,
                    name = event.value,
                    propertyId = event.photoProperty.propertyId
                )
                val list: MutableList<PhotoProperty> = photoList.value?.toMutableList()!!
                list.forEach {
                    if (it == event.photoProperty) {
                        list[list.indexOf(it)] = photo
                    }
                }
                photoList.value = list
            }


            is AddPropertyEvent.OnPhotoPicked -> {
//                newProperty = newProperty?.copy(
//                    photo = event.listByteArray
//                )
                var cpt: Int = 0
                val list = mutableListOf<PhotoProperty>()
                event.listByteArray?.forEach {
                    list.add(
                        PhotoProperty(
                            isMain = cpt == 0,
                            photoBytes = it,
                            name = "",
                            propertyId = 0L,
                        )
                    )

                    cpt++
                }
                photoList.value = list
            }

            is AddPropertyEvent.OnParkChecked -> {
                val list: MutableList<InterestPoint> = mutableListOf<InterestPoint>()
                if (event.value) {
                    //add InterestPoint
                    newProperty?.interestPoints?.let {
                        list.addAll(it)
                    }
                    list.add(InterestPoint.PARK)
                    newProperty = newProperty?.copy(
                        interestPoints = list
                    )
                } else {
                    //remove InterestPoint
                    newProperty?.interestPoints?.let {
                        list.addAll(it)
                        list.remove(InterestPoint.PARK)
                        newProperty = newProperty?.copy(
                            interestPoints = list
                        )
                    }
                }
            }

            is AddPropertyEvent.OnSchoolChecked -> {
                val list: MutableList<InterestPoint> = mutableListOf<InterestPoint>()
                if (event.value) {
                    //add InterestPoint
                    newProperty?.interestPoints?.let {
                        list.addAll(it)
                    }
                    list.add(InterestPoint.SCHOOL)
                    newProperty = newProperty?.copy(
                        interestPoints = list
                    )
                } else {
                    //remove InterestPoint
                    newProperty?.interestPoints?.let {
                        list.addAll(it)
                        list.remove(InterestPoint.SCHOOL)
                        newProperty = newProperty?.copy(
                            interestPoints = list
                        )
                    }
                }
            }

            is AddPropertyEvent.OnShopChecked -> {
                val list: MutableList<InterestPoint> = mutableListOf<InterestPoint>()
                if (event.value) {
                    //add InterestPoint
                    newProperty?.interestPoints?.let {
                        list.addAll(it)
                    }
                    list.add(InterestPoint.SHOP)
                    newProperty = newProperty?.copy(
                        interestPoints = list
                    )
                } else {
                    //remove InterestPoint
                    newProperty?.interestPoints?.let {
                        list.addAll(it)
                        list.remove(InterestPoint.SHOP)
                        newProperty = newProperty?.copy(
                            interestPoints = list
                        )
                    }
                }
            }

            is AddPropertyEvent.OnTransportChecked -> {
                val list: MutableList<InterestPoint> = mutableListOf<InterestPoint>()
                if (event.value) {
                    //add InterestPoint
                    newProperty?.interestPoints?.let {
                        list.addAll(it)
                    }
                    list.add(InterestPoint.TRANSPORT)
                    newProperty = newProperty?.copy(
                        interestPoints = list
                    )
                } else {
                    //remove InterestPoint
                    newProperty?.interestPoints?.let {
                        list.addAll(it)
                        list.remove(InterestPoint.TRANSPORT)
                        newProperty = newProperty?.copy(
                            interestPoints = list
                        )
                    }
                }
            }

            else -> {}
        }
    }
}
