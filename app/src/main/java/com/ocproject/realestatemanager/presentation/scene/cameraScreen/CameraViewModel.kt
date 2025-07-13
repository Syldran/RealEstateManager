package com.ocproject.realestatemanager.presentation.scene.cameraScreen

import androidx.lifecycle.ViewModel
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyEvent
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CameraViewModel() : ViewModel() {
    private val _state = MutableStateFlow(CameraState())
    val state = _state.asStateFlow()


    fun onEvent(event: CameraEvent) {
        when (event) {
            is CameraEvent.OnImageTaken -> {
                _state.update {
                    it.copy(
                        imageTaken = event.imageBitmap
                    )
                }
            }
        }
    }
}