package com.ocproject.realestatemanager.presentation.scene.addproperty

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.ocproject.realestatemanager.core.InterestPoint
import com.ocproject.realestatemanager.domain.models.PhotoProperty
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.usecases.GetPropertyDetailsUseCase
import com.ocproject.realestatemanager.domain.usecases.SavePropertyUseCase
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyEvent.*
import com.ocproject.realestatemanager.presentation.scene.addproperty.utils.DecimalFormatter
import com.ocproject.realestatemanager.presentation.scene.addproperty.utils.IntFormatter
import com.ocproject.realestatemanager.presentation.scene.addproperty.utils.PropertyValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

//    var newProperty: Property = (
//        Property(
//            photoList = emptyList(),
//            interestPoints = emptyList(),
//            address = "",
//            town = "",
//            lat = 0.0,
//            lng = 0.0,
//            country = "",
//            createdDate = null,
//            areaCode = null,
//            surfaceArea = null,
//            price = null,
//            id = 0L,
//            sold = null,
//        )
//    )
//        private set

//    var photoList: MutableState<List<PhotoProperty>?> = mutableStateOf(null)
//        private set


    init {

        getProperty()
        Timber.tag("afterGetProperty")
            .d("PropertyId :${state.value.newProperty.id}, PhotoSize : ${state.value.photoList.size} ")
    }

    fun getProperty() {
        if (propertyId != null && propertyId != 0L) {
            viewModelScope.launch {
                onEvent(AddPropertyEvent.UpdateNewProperty(getPropertyDetailsUseCase(propertyId)))
                onEvent(AddPropertyEvent.UpdatePhotos(state.value.newProperty.photoList))
                if (state.value.newProperty.sold != null) {
                    onEvent(AddPropertyEvent.UpdateSoldState(true))
                } else {
                    onEvent(AddPropertyEvent.UpdateSoldState(false))
                }
            }

        } else {
            onEvent(
                UpdateNewProperty(
                    Property(
                        photoList = emptyList(),
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
                        sold = null,
                    )
                )
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

            SaveProperty -> saveProperty(state.value.newProperty)
            is UpdateForm -> updateForm(event)
            is UpdateTags -> updateTags(event)
            is OnPhotoNameChanged -> {
                val photo = PhotoProperty(
                    isMain = event.photoProperty.isMain,
                    photoBytes = event.photoProperty.photoBytes,
                    name = event.value,
                )
                val list: List<PhotoProperty> = state.value.photoList
                list.forEach {
                    if (it == event.photoProperty) {
                        list[list.indexOf(it)].copy(photo.isMain, photo.name, photo.photoBytes)
                    }
                }
                Timber.tag("OnPhotoChangeList").d("${list.size}")
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
                // Ajouter à la liste existante au lieu de la remplacer
                val currentList = state.value.photoList.toMutableList()
//                    photoList.value?.toMutableList() ?: mutableListOf()
                currentList.addAll(list)
                onEvent(UpdatePhotos(currentList.toList()))
            }

            is UpdatePhotos -> {
                _state.update {
                    it.copy(
                        photoList = event.photos
                    )
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
        }


    }

    // Méthode pour ajouter une photo depuis la caméra
    fun addPhotoFromCamera(photoBytes: ByteArray) {
        val photo = PhotoProperty(
            isMain = state.value.photoList.isEmpty(),
            photoBytes = photoBytes,
            name = "Photo ${(state.value.photoList.size) + 1}"
        )

        val currentList = state.value.photoList.toMutableList()
        currentList.add(photo)
        onEvent(AddPropertyEvent.UpdatePhotos(currentList.toList()))
    }

    // Méthode pour supprimer une photo spécifique
    fun removePhoto(photoToRemove: PhotoProperty) {
        val currentList = state.value.photoList.toMutableList()
        currentList.remove(photoToRemove)

        // Si on supprime la photo principale et qu'il reste des photos,
        // marquer la première comme principale
        if (photoToRemove.isMain && currentList.isNotEmpty()) {
            currentList[0] = currentList[0].copy(isMain = true)
        }

        onEvent(AddPropertyEvent.UpdatePhotos(currentList))
    }

    private fun onChangeNavStatus() {
        _state.update {
            it.copy(
                navToPropertyListScreen = false
            )
        }
    }

    fun saveProperty(property: Property) {
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
                    if (property.createdDate == null) {
//                    Timber.tag("AddPropPhotos1").d("${state.value.photoList.size}")
                        savePropertyUseCase(
                            property.copy(
                                photoList = state.value.photoList,
                                createdDate = Calendar.getInstance().timeInMillis
                            ),
                        )
                    } else {
//                    Timber.tag("AddPropPhotos2").d("${state.value.photoList.size}")
                        savePropertyUseCase(
                            property.copy(
                                photoList = state.value.photoList,
                            )
                        )

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

    fun updateForm(event: AddPropertyEvent.UpdateForm) {
        var decimalFormatter = DecimalFormatter()
        var intFormatter = IntFormatter()
        onEvent(
            AddPropertyEvent.UpdateNewProperty(
                state.value.newProperty.copy(
                    address = event.address ?: state.value.newProperty.address,
                    town = event.town ?: state.value.newProperty.town,
                    country = event.country ?: state.value.newProperty.country,
                    areaCode = event.areaCode?.let { intFormatter.cleanup(it) }?.toInt()
                        ?: state.value.newProperty.areaCode,
                    lat = event.latitude?.let { decimalFormatter.cleanup(it) }?.toDouble()
                        ?: state.value.newProperty.lat,
                    lng = event.longitude?.let { decimalFormatter.cleanup(it) }?.toDouble()
                        ?: state.value.newProperty.lng,
                    price = event.price?.let { intFormatter.cleanup(it) }?.toInt()
                        ?: state.value.newProperty.price,
                    surfaceArea = event.surfaceArea?.let { intFormatter.cleanup(it) }?.toInt()
                        ?: state.value.newProperty.surfaceArea,
                )
            )
        )

    }

    fun updateTags(event: AddPropertyEvent.UpdateTags) {
        val list: MutableList<InterestPoint> = mutableListOf<InterestPoint>()
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
            AddPropertyEvent.UpdateNewProperty(
                state.value.newProperty.copy(
                    interestPoints = list,
//                    sold = event.sold
                )
            )
        )
    }
}