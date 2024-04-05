package com.ocproject.realestatemanager.ui.scenes.propertydetails

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ocproject.realestatemanager.db.PropertyDao
import com.ocproject.realestatemanager.repositories.PropertyRepository
import com.openclassrooms.realestatemanager.models.InterestPoint
import com.openclassrooms.realestatemanager.models.PictureOfProperty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam

@KoinViewModel
class PropertyDetailsViewModel(
    @InjectedParam
    private val propertyId: Int,
    private val propertyRepository: PropertyRepository,

    ) : ViewModel() {
    val state = MutableStateFlow(PropertyDetailsState())

    init {
        getPropertyDetails()
    }

    // mettre en suspend faire dans treah io
    fun getPropertyDetails() {
        viewModelScope.launch {
            val propertyDetails = propertyRepository.getProperty(propertyId)
            state.update {
                it.copy(
                    type = propertyDetails.property.type,
                    price = propertyDetails.property.price,
                    area = propertyDetails.property.area,
                    numberOfRooms = propertyDetails.property.numberOfRooms,
                    description = propertyDetails.property.description,
                    picturesList = propertyDetails.pictureList,
                    address = propertyDetails.property.address,
                    //interestPoints = propertyDetails.property.interestPoints,
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


    data class PropertyDetailsState(
        val type: String = "",
        val price: Int = 0,
        val area: Int = 0,
        val numberOfRooms: Int = 0,
        val description: String = "",
        val picturesList: List<PictureOfProperty> = emptyList(), // to create
        val address: String = "",
        val interestPoints: List<InterestPoint> = emptyList(), // to create
        val state: String = "",
        val createDate: String = "",
        val soldDate: String = "",
        val agentId: String = "",
        val lat: Double = 0.0,
        val lng: Double = 0.0,
    )


//
//    data class PropertyDetailsState(
//        val propertyWithPictures: PropertyWithPictures,
//    )
}