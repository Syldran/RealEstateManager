package com.ocproject.realestatemanager.presentation.scene.cameraScreen

import androidx.compose.ui.graphics.ImageBitmap
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyEvent

sealed interface CameraEvent {
    data class OnImageTaken(val imageBitmap: ImageBitmap) : CameraEvent
}