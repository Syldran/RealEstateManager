package com.ocproject.realestatemanager.presentation.scene.addproperty

import com.ocproject.realestatemanager.domain.models.PhotoProperty
import com.ocproject.realestatemanager.domain.models.Property

sealed interface AddPropertyEvent {
    data class OnPhotoPicked(val listByteArray: List<ByteArray>?) : AddPropertyEvent
    data class SaveProperty(val successMessage: String, val failureMessage: String) : AddPropertyEvent
    data class OnChangeNavigationStatus(val value : Boolean) : AddPropertyEvent
    data class UpdateForm(
        val description: String? = null,
        val town: String? = null,
        val address: String? = null,
        val country: String? = null,
        val areaCode: String? = null,
        val surfaceArea: String? = null,
        val price: String? = null,
        val latitude: String? = null,
        val longitude: String? = null,
        val type: String? = null,
        val nbrRoom: String? = null,
        val realEstateAgent: String? = null,
    ) : AddPropertyEvent

    data class UpdateLatitudeInput(val input: String) : AddPropertyEvent
    data class UpdateLongitudeInput(val input: String) : AddPropertyEvent

    data class UpdateTags(
        val school: Boolean = false,
        val park: Boolean = false,
        val shop: Boolean = false,
        val transport: Boolean = false,
//        val sold: Long? = null,
    ) : AddPropertyEvent

    data class OnPhotoNameChanged(val photoProperty: PhotoProperty, val value: String) : AddPropertyEvent

    data class UpdatePhotos(val photos: List<PhotoProperty>) : AddPropertyEvent

    data class UpdateNewProperty(val newProperty: Property) : AddPropertyEvent

    data class UpdateSoldState(val soldState: Boolean) : AddPropertyEvent

}