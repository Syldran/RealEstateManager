package com.ocproject.realestatemanager.presentation.scene.addproperty

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.ocproject.realestatemanager.domain.models.InterestPoint
import com.ocproject.realestatemanager.domain.models.PhotoProperty
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.usecases.GetPropertyDetailsUseCase
import com.ocproject.realestatemanager.domain.usecases.SavePhotoProperty
import com.ocproject.realestatemanager.domain.usecases.SavePropertyUseCase
import com.ocproject.realestatemanager.presentation.scene.addproperty.utils.DecimalFormatter
import com.ocproject.realestatemanager.presentation.scene.addproperty.utils.IntFormatter
import com.ocproject.realestatemanager.presentation.scene.addproperty.utils.PropertyValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam
import java.util.Calendar

@KoinViewModel
class AddPropertyViewModel(
    @InjectedParam
    private val propertyId: Long?,
    private val getPropertyDetailsUseCase: GetPropertyDetailsUseCase,
    private val savePropertyUseCase: SavePropertyUseCase,
    private val savePhotoProperty: SavePhotoProperty,
) : ViewModel() {
    private val _state = MutableStateFlow(AddPropertyState())
    val state = _state.asStateFlow()

    var newProperty: Property? by mutableStateOf(null)
        private set

    var photoList: MutableState<List<PhotoProperty>?> = mutableStateOf(null)
        private set

    init {
        getProperty()
    }

    private fun getProperty() {

        if (propertyId != null && propertyId != 0L) {
            viewModelScope.launch {
                val testProperty = getPropertyDetailsUseCase(propertyId)
                newProperty = getPropertyDetailsUseCase(propertyId).property
                photoList.value = getPropertyDetailsUseCase(propertyId).photoList
            }
        } else {
            newProperty = Property(
                interestPoints = emptyList(),
                address = "a",
                town = "a",
                lat = 0.0,
                lng = 0.0,
                country = "a",
                createdDate = null,
                areaCode = 123,
                surfaceArea = 123,
                price = 123,
                id = 0L,
                sold = false,
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
        /*    onEvent(AddPropertyEvent.OnAddressChanged(address))
            onEvent(AddPropertyEvent.OnTownChanged(town))
            onEvent(AddPropertyEvent.OnCountryChanged(country))
            onEvent(AddPropertyEvent.OnAreaCodeChanged(code))
            onEvent(AddPropertyEvent.OnLatChanged(place.latLng?.latitude.toString()))
            onEvent(AddPropertyEvent.OnLngChanged(place.latLng?.longitude.toString()))*/

        onEvent(
            AddPropertyEvent.UpdateForm(
                address = address,
                town = town,
                country = country,
                areaCode = code,
                latitude = place.location?.latitude.toString(),
                longitude = place.location?.longitude.toString(),
            )
        )
    }

    fun onEvent(event: AddPropertyEvent) {
        when (event) {

            AddPropertyEvent.SaveProperty -> saveProperty()
            is AddPropertyEvent.UpdateForm -> updateForm(event)
            is AddPropertyEvent.UpdateTags -> updateTags(event)
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

            is AddPropertyEvent.OnChangeNavigationStatus -> onChangeNavStatus()
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

            /*is AddPropertyEvent.OnSoldChecked -> {
                if (event.value) {
                    newProperty = newProperty?.copy(
                        sold = true
                    )
                } else {
                    newProperty = newProperty?.copy(
                        sold = false
                    )
                }

            }
            }*/

            else -> {}
        }


    }

    private fun onChangeNavStatus() {
        _state.update {
            it.copy(
                navToPropertyListScreen = false
            )
        }
    }

    private fun saveProperty() {
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
                        navToPropertyListScreen = false,
                    )
                }

                viewModelScope.launch {
                    var idProperty: Long
                    if (property.createdDate == null) {
                        idProperty = savePropertyUseCase.invoke(
                            property.copy(
                                createdDate = Calendar.getInstance().timeInMillis
                            )
                        )
                    } else {
                        idProperty = savePropertyUseCase(property)
                    }

                    if (propertyId != null && propertyId != 0L) {
                        idProperty = propertyId
                        //                                propertiesRepository.deletePicturesOfPropertyById(idProperty)
                    }

                    if (photoList.value != null) {
                        for (photo in photoList.value!!) {
                            val photoProperty = PhotoProperty(
                                isMain = photo.isMain,
                                photoBytes = photo.photoBytes,
                                name = photo.name,
                                propertyId = idProperty
                            )
                            savePhotoProperty(photoProperty)
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
                        navToPropertyListScreen = false,
                    )
                }
            }
        }
    }

    private fun updateForm(event: AddPropertyEvent.UpdateForm) {
        var decimalFormatter = DecimalFormatter()
        var intFormatter = IntFormatter()
        newProperty = newProperty?.copy(
            address = event.address ?: newProperty!!.address,
            town = event.town ?: newProperty!!.town,
            country = event.country ?: newProperty!!.country,
            areaCode = event.areaCode?.let { intFormatter.cleanup(it) }?.toInt()
                ?: newProperty!!.areaCode,
            lat = event.latitude?.let { decimalFormatter.cleanup(it) }?.toDouble()
                ?: newProperty!!.lat,
            lng = event.longitude?.let { decimalFormatter.cleanup(it) }?.toDouble()
                ?: newProperty!!.lng,
            price = event.price?.let { intFormatter.cleanup(it) }?.toInt() ?: newProperty!!.price,
            surfaceArea = event.surfaceArea?.let { intFormatter.cleanup(it) }?.toInt()
                ?: newProperty!!.surfaceArea,
        )
    }

    private fun updateTags(event: AddPropertyEvent.UpdateTags) {
        newProperty = newProperty?.copy(
            sold = event.sold
        )
        val list: MutableList<InterestPoint> = mutableListOf<InterestPoint>()
        when (event.park) {
            true -> {
                //add InterestPoint
                newProperty?.interestPoints?.let {
                    list.addAll(it)
                }
                list.add(InterestPoint.PARK)
                newProperty = newProperty?.copy(
                    interestPoints = list,
                )
            }

            false -> {
                //remove InterestPoint
                newProperty?.interestPoints?.let {
                    list.addAll(it)
                    list.remove(InterestPoint.PARK)
                    newProperty = newProperty?.copy(
                        interestPoints = list,
                    )
                }
            }
        }

        when (event.school) {
            true -> {
                //add InterestPoint
                newProperty?.interestPoints?.let {
                    list.addAll(it)
                }
                list.add(InterestPoint.SCHOOL)
                newProperty = newProperty?.copy(
                    interestPoints = list,
                )
            }

            false -> {
                //remove InterestPoint
                newProperty?.interestPoints?.let {
                    list.addAll(it)
                    list.remove(InterestPoint.SCHOOL)
                    newProperty = newProperty?.copy(
                        interestPoints = list,
                    )
                }
            }
        }

        when (event.shop) {
            true -> {
                //add InterestPoint
                newProperty?.interestPoints?.let {
                    list.addAll(it)
                }
                list.add(InterestPoint.SHOP)
                newProperty = newProperty?.copy(
                    interestPoints = list,
                )
            }

            false -> {
                //remove InterestPoint
                newProperty?.interestPoints?.let {
                    list.addAll(it)
                    list.remove(InterestPoint.SHOP)
                    newProperty = newProperty?.copy(
                        interestPoints = list,
                    )
                }
            }
        }

        when (event.transport) {
            true -> {
                //add InterestPoint
                newProperty?.interestPoints?.let {
                    list.addAll(it)
                }
                list.add(InterestPoint.TRANSPORT)
                newProperty = newProperty?.copy(
                    interestPoints = list,
                )
            }

            false -> {
                //remove InterestPoint
                newProperty?.interestPoints?.let {
                    list.addAll(it)
                    list.remove(InterestPoint.TRANSPORT)
                    newProperty = newProperty?.copy(
                        interestPoints = list,
                    )
                }
            }
        }

        newProperty = newProperty?.copy(
            interestPoints = list
        )
    }
}