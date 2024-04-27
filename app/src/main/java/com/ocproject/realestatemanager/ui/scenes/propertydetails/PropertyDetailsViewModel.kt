package com.ocproject.realestatemanager.ui.scenes.propertydetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ocproject.realestatemanager.data.repositories.PropertyRepository
import com.openclassrooms.realestatemanager.models.InterestPoint
import com.openclassrooms.realestatemanager.models.PictureOfProperty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam

@KoinViewModel
class PropertyDetailsViewModel(
    @InjectedParam
    private val propertyId: Int?,
    private val propertyRepository: PropertyRepository,

    ) : ViewModel() {
    private val _state = MutableStateFlow(PropertyDetailsState())
    val state = _state.asStateFlow()

    init {
        getPropertyDetails()
    }


    private fun getPropertyDetails() {
        viewModelScope.launch {
            val propertyDetails = propertyRepository.getProperty(propertyId!!)
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
                )
            }
        }
    }
}