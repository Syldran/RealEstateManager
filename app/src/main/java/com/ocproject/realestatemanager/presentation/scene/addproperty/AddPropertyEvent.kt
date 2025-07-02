package com.ocproject.realestatemanager.presentation.scene.addproperty

import androidx.compose.ui.graphics.ImageBitmap
import com.ocproject.realestatemanager.domain.models.PhotoProperty
import java.util.Date

sealed interface AddPropertyEvent {
    class OnPhotoPicked(val listByteArray: List<ByteArray>?) : AddPropertyEvent
    data object SaveProperty : AddPropertyEvent
    data class OnChangeNavigationStatus(val value : Boolean) : AddPropertyEvent
    data class UpdateForm(
        val town: String? = null,
        val address: String? = null,
        val country: String? = null,
        val areaCode: String? = null,
        val surfaceArea: String? = null,
        val price: String? = null,
        val latitude: String? = null,
        val longitude: String? = null,
    ) : AddPropertyEvent

    data class UpdateTags(
        val school: Boolean = false,
        val shop: Boolean = false,
        val park: Boolean = false,
        val transport: Boolean = false,
        val sold: Long? = null,
    ) : AddPropertyEvent

    data class OnPhotoNameChanged(val photoProperty: PhotoProperty, val value: String) :
        AddPropertyEvent


}