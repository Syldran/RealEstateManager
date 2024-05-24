package com.ocproject.realestatemanager.presentation.scene.addproperty

import com.ocproject.realestatemanager.models.PhotoProperty

sealed interface AddPropertyEvent {
    class OnPhotoPicked(val listByteArray: List<ByteArray>?): AddPropertyEvent
    data object SaveProperty : AddPropertyEvent
    data class OnTownChanged(val value: String) : AddPropertyEvent
    data class OnAddressChanged(val value: String) : AddPropertyEvent
    data class OnCountryChanged(val value: String) : AddPropertyEvent
    data class OnAreaCodeChanged(val value: String): AddPropertyEvent
    data class OnSurfaceAreaChanged(val value: String): AddPropertyEvent
    data class OnLatChanged(val value: String): AddPropertyEvent
    data class OnLngChanged(val value: String): AddPropertyEvent
    data class OnPriceChanged(val value: String): AddPropertyEvent
    data class OnSchoolChecked(val value: Boolean): AddPropertyEvent
    data class OnParkChecked(val value: Boolean): AddPropertyEvent
    data class OnShopChecked(val value: Boolean): AddPropertyEvent
    data class OnTransportChecked(val value: Boolean): AddPropertyEvent
    data class OnPhotoNameChanged(val photoProperty: PhotoProperty, val value: String): AddPropertyEvent
}