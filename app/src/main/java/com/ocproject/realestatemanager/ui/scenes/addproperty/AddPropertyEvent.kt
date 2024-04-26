package com.ocproject.realestatemanager.ui.scenes.addproperty

import android.graphics.Picture
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.openclassrooms.realestatemanager.models.InterestPoint
import com.openclassrooms.realestatemanager.models.PictureOfProperty

sealed class AddPropertyEvent {
    data object SaveProperty : AddPropertyEvent()
    data class SetType(val type: String) : AddPropertyEvent()
    data class SetPrice(val price: String) : AddPropertyEvent()
    data class SetArea(val area: String) : AddPropertyEvent()
    data class SetNumberOfRooms(val numberOfRooms: String) : AddPropertyEvent()
    data class SetDescription(val description: String) : AddPropertyEvent()
    data class SetAddress(val address: String) : AddPropertyEvent()
    data class SetState(val state: String) : AddPropertyEvent()
    data class SetLat(val lat: String) : AddPropertyEvent()
    data class SetLng(val lng: String) : AddPropertyEvent()
    data class SetPictureList(val pictureList: List<PictureOfProperty>) : AddPropertyEvent()
    data class SetInterestPoints(val interestPoints: List<InterestPoint>) : AddPropertyEvent()
    data class SetMainPicture(val picture: PictureOfProperty) : AddPropertyEvent()
    data class UpdatePredictions(val predictions: List<AutocompletePrediction>) : AddPropertyEvent()
    data class ChangeText(val text: String): AddPropertyEvent()
    data class IsSearching(val searching: Boolean): AddPropertyEvent()
}
