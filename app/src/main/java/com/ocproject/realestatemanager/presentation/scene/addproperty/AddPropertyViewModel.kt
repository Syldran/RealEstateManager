package com.ocproject.realestatemanager.presentation.scene.addproperty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.ocproject.realestatemanager.core.InterestPoint
import com.ocproject.realestatemanager.domain.models.PhotoProperty
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.usecases.GetPropertyDetailsUseCase
import com.ocproject.realestatemanager.domain.usecases.SavePropertyUseCase
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyEvent.OnChangeNavigationStatus
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyEvent.OnPhotoNameChanged
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyEvent.OnPhotoPicked
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyEvent.SaveProperty

import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyEvent.UpdateForm
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyEvent.UpdateLatitudeInput
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyEvent.UpdateLongitudeInput
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyEvent.UpdateNewProperty
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyEvent.UpdatePhotos
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyEvent.UpdateSoldState
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyEvent.UpdateTags
import com.ocproject.realestatemanager.core.GlobalSnackBarManager
import com.ocproject.realestatemanager.presentation.scene.addproperty.utils.PropertyValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam
import timber.log.Timber
import java.util.Calendar

@KoinViewModel
class AddPropertyViewModel(
    @InjectedParam
    private val propertyId: Long?,
    private val getPropertyDetailsUseCase: GetPropertyDetailsUseCase,
    private val savePropertyUseCase: SavePropertyUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(AddPropertyState())
    val state = _state.asStateFlow()

    init {
        getProperty()
    }

    fun getProperty() {
        if (propertyId != null && propertyId > 0L) {
            viewModelScope.launch {
                val property = getPropertyDetailsUseCase(propertyId)
                onEvent(UpdateNewProperty(property))
                onEvent(UpdatePhotos(property.photoList))
                // Initialize soldState for existing properties
                if (property.sold != -1L) {
                    onEvent(UpdateSoldState(true))
                } else {
                    onEvent(UpdateSoldState(false))
                }
                // Initialize input strings for existing properties
                if (property.lat != 0.0) {
                    onEvent(UpdateLatitudeInput(property.lat.toString()))
                }
                if (property.lng != 0.0) {
                    onEvent(UpdateLongitudeInput(property.lng.toString()))
                }
            }

        } else {
            onEvent(
                UpdateNewProperty(
                    Property(
                        photoList = emptyList(),
                        interestPoints = emptyList(),
                        description = "",
                        address = "",
                        town = "",
                        lat = 0.0,
                        lng = 0.0,
                        country = "",
                        createdDate = null,
                        areaCode = 0,
                        surfaceArea = 0,
                        price = 0,
                        id = 0L,
                        sold = -1,
                        type = "",
                        nbrRoom = 0,
                        realEstateAgent = "",
                    )
                )
            )
        }
    }


    fun setPropertyFromPlace(place: Place) {
        val listAddressComponents = place.addressComponents?.asList()
        var address = ""
        var town = ""
        var country = ""
        var code = ""
        listAddressComponents?.forEach {
            Timber.tag("AddressComponent").d("${it.name} in : ${it.types}")
            if (it.types.contains("street_number")) {
                address += "${it.name} "
            } else if (it.types.contains("route")) {
                address += it.name
            } else if (it.types.contains("locality")) {
                town = it.name
            } else if (it.types.contains("country")) {
                country = it.name
            } else if (it.types.contains("postal_code")) {
                code = it.name
            }
        }
        onEvent(
            UpdateForm(
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

            is SaveProperty -> saveProperty(
                state.value.newProperty,
                event.successMessage,
                event.failureMessage
            )

            is UpdateForm -> updateForm(event)

            is UpdateTags -> updateTags(event)

            is OnPhotoNameChanged -> {
                val photo = PhotoProperty(
                    isMain = event.photoProperty.isMain,
                    photoBytes = event.photoProperty.photoBytes,
                    name = event.value,
                    id = event.photoProperty.id
                )
                val list: MutableList<PhotoProperty> = mutableListOf()
                state.value.photoList.forEach {
                    if (it == event.photoProperty) {
                        list.add(photo)
                    } else {
                        list.add(it)
                    }
                }
                onEvent(UpdatePhotos(list))
            }

            is OnChangeNavigationStatus -> onChangeNavStatus()

            is OnPhotoPicked -> {
                var cpt = 0
                val list = mutableListOf<PhotoProperty>()
                event.listByteArray?.forEach {
                    list.add(
                        PhotoProperty(
                            isMain = state.value.photoList.isEmpty() && cpt == 0,
                            photoBytes = it,
                            name = "Photo ${(state.value.photoList.size) + cpt + 1}",
                        )
                    )
                    cpt++
                }
                // Add existing list.
                val currentList = state.value.photoList.toMutableList()
                currentList.addAll(list)
                onEvent(UpdatePhotos(currentList.toList()))
            }

            is UpdatePhotos -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            photoList = event.photos
                        )
                    }
                }
            }

            is UpdateNewProperty -> {
                _state.update {
                    it.copy(
                        newProperty = event.newProperty
                    )
                }
            }

            is UpdateSoldState -> {
                _state.update {
                    it.copy(
                        soldState = event.soldState
                    )
                }
            }

            is UpdateLatitudeInput -> {
                // Filter out non-numeric characters except decimal point and minus sign
                val filteredInput = event.input.filter { it.isDigit() || it == '.' || it == '-' }
                _state.update {
                    it.copy(
                        latitudeInput = filteredInput
                    )
                }
                // Also update the property if the input is valid
                if (filteredInput.isNotEmpty() && filteredInput != "." && filteredInput != "-") {
                    // Allow valid decimal numbers including those starting with 0
                    val doubleValue = filteredInput.toDoubleOrNull()
                    if (doubleValue != null) {
                        onEvent(UpdateNewProperty(state.value.newProperty.copy(lat = doubleValue)))
                    }
                }
            }

            is UpdateLongitudeInput -> {
                // Filter out non-numeric characters except decimal point and minus sign
                val filteredInput = event.input.filter { it.isDigit() || it == '.' || it == '-' }
                _state.update {
                    it.copy(
                        longitudeInput = filteredInput
                    )
                }
                // Also update the property if the input is valid
                if (filteredInput.isNotEmpty() && filteredInput != "." && filteredInput != "-") {
                    // Allow valid decimal numbers including those starting with 0
                    val doubleValue = filteredInput.toDoubleOrNull()
                    if (doubleValue != null) {
                        onEvent(UpdateNewProperty(state.value.newProperty.copy(lng = doubleValue)))
                    }
                }
            }
        }
    }

    // Function to add photo from camera.
    fun addPhotoFromCamera(photoBytes: ByteArray) {
        val photo = PhotoProperty(
            isMain = state.value.photoList.isEmpty(),
            photoBytes = photoBytes,
            name = "Photo ${(state.value.photoList.size) + 1}"
        )

        val currentList = state.value.photoList.toMutableList()
        currentList.add(photo)
        onEvent(UpdatePhotos(currentList.toList()))
    }

    // Function to delete specific photo.
    fun removePhoto(photoToRemove: PhotoProperty) {
        val currentList = state.value.photoList.toMutableList()
        currentList.remove(photoToRemove)

        // If main photo deleted while still others photos,
        // mark new first one as main.
        if (photoToRemove.isMain && currentList.isNotEmpty()) {
            currentList[0] = currentList[0].copy(isMain = true)
        }

        onEvent(UpdatePhotos(currentList))
    }

    private fun onChangeNavStatus() {
        _state.update {
            it.copy(
                navToPropertyListScreen = false
            )
        }
    }

    fun saveProperty(property: Property, successMessage: String, failureMessage: String) {
        property.let { property ->
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
                result.typeError,
                result.nbrRoomError,
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
                        typeError = null,
                        nbrRoomError = null,
                        realEstateAgentError = null,
                        navToPropertyListScreen = false,
                    )
                }

                viewModelScope.launch {
                    try {
                        if (property.createdDate == null) {
                            savePropertyUseCase(
                                property.copy(
                                    photoList = state.value.photoList,
                                    createdDate = Calendar.getInstance().timeInMillis
                                ),
                            )
                        } else {
                            savePropertyUseCase(
                                property.copy(
                                    photoList = state.value.photoList,
                                )
                            )

                        }
                        // Show global toast message confirming property saved
                        GlobalSnackBarManager.showSnackMsg(
                            successMessage,
                            isSuccess = true
                        )
                        delay(100)
                        // Navigate - global toast will persist across navigation
                        _state.update {
                            it.copy(navToPropertyListScreen = true)
                        }
                    } catch (e: Exception) {
                        // Show global failure message
                        GlobalSnackBarManager.showSnackMsg(
                            failureMessage,
                            isSuccess = false
                        )
                    }
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
                        typeError = result.typeError,
                        nbrRoomError = result.nbrRoomError,
                    )
                }
            }
        }
    }

    fun updateForm(event: UpdateForm) {
        onEvent(
            UpdateNewProperty(
                state.value.newProperty.copy(
                    description = event.description ?: state.value.newProperty.description,
                    address = event.address ?: state.value.newProperty.address,
                    town = event.town ?: state.value.newProperty.town,
                    country = event.country ?: state.value.newProperty.country,
                    areaCode = event.areaCode?.toIntOrNull() ?: state.value.newProperty.areaCode,
                    price = event.price?.toIntOrNull() ?: state.value.newProperty.price,
                    surfaceArea = event.surfaceArea?.toIntOrNull()
                        ?: state.value.newProperty.surfaceArea,
                    lat = event.latitude?.toDoubleOrNull() ?: state.value.newProperty.lat,
                    lng = event.longitude?.toDoubleOrNull() ?: state.value.newProperty.lng,
                    type = event.type ?: state.value.newProperty.type,
                    nbrRoom = event.nbrRoom?.toIntOrNull() ?: state.value.newProperty.nbrRoom,
                    realEstateAgent = event.realEstateAgent ?: state.value.newProperty.realEstateAgent,
                )
            )
        )

        // Also update the input fields for latitude and longitude
        event.latitude?.let { lat ->
            if (lat.isNotEmpty()) {
                onEvent(UpdateLatitudeInput(lat))
            }
        }
        event.longitude?.let { lng ->
            if (lng.isNotEmpty()) {
                onEvent(UpdateLongitudeInput(lng))
            }
        }
    }

    fun updateTags(event: UpdateTags) {
        val list: MutableList<InterestPoint> = mutableListOf()
        //if newProperty interestPoints aren't null we set temp list to it's current state.
        state.value.newProperty.interestPoints.let {
            list += it
        }

        when (event.park) {
            true -> {
                // We check if tag PARK was previously inactive.
                // If it wasn't we add its new active state via temp list.
                if (!list.contains(InterestPoint.PARK)) {
                    list += InterestPoint.PARK
                }
            }

            false -> {
                // We check if tag Park was previously active.
                // If it was we remove it's active state to the inactive one via temp list.
                if (list.contains(InterestPoint.PARK)) {
                    list -= InterestPoint.PARK
                }
            }
        }

        // same as above with shop tag / InterestPoint.Shop
        when (event.shop) {
            true -> {
                if (!list.contains(InterestPoint.SHOP)) {
                    list += InterestPoint.SHOP
                }

            }

            false -> {
                if (list.contains(InterestPoint.SHOP)) {
                    list -= InterestPoint.SHOP
                }
            }
        }

        // same for school tag
        when (event.school) {
            true -> {
                if (!list.contains(InterestPoint.SCHOOL)) {
                    list += InterestPoint.SCHOOL
                }

            }

            false -> {
                if (list.contains(InterestPoint.SCHOOL)) {
                    list -= InterestPoint.SCHOOL
                }
            }
        }

        // same for transport tag
        when (event.transport) {
            true -> {
                if (!list.contains(InterestPoint.TRANSPORT)) {
                    list += InterestPoint.TRANSPORT
                }

            }

            false -> {
                if (list.contains(InterestPoint.TRANSPORT)) {
                    list -= InterestPoint.TRANSPORT
                }
            }
        }
        // Update newProperty interestPoints state with new values from temp list
        onEvent(
            UpdateNewProperty(
                state.value.newProperty.copy(
                    interestPoints = list,
                )
            )
        )
    }
}