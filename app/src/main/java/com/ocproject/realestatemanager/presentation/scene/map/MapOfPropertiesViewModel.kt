package com.ocproject.realestatemanager.presentation.scene.map

import android.graphics.BitmapFactory
import androidx.core.graphics.scale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.usecases.GetPropertyListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam

data class PropertyMarker(
    val state: MarkerState,
    val icon: BitmapDescriptor? = null,
    val property: Property
)

@KoinViewModel
class MapOfPropertiesViewModel(
    @InjectedParam
    private val getPropertyListUseCase: GetPropertyListUseCase,
) : ViewModel() {
    private val _markers = MutableStateFlow<List<PropertyMarker>>(emptyList())
    val markers: StateFlow<List<PropertyMarker>> = _markers.asStateFlow()

    fun createMarkers(properties: List<Property>) {
        viewModelScope.launch {
            val newMarkers = properties.map { property ->
                val markerState = MarkerState(LatLng(property.lat, property.lng))
                val icon = createMarkerIcon(property)

                PropertyMarker(
                    state = markerState,
                    icon = icon,
                    property = property
                )
            }
            _markers.value = newMarkers
        }
    }

    private fun createMarkerIcon(property: Property): BitmapDescriptor? {
        val firstPhoto = property.photoList?.firstOrNull()
        if (firstPhoto?.photoBytes != null) {
            val bitmap = BitmapFactory.decodeByteArray(
                firstPhoto.photoBytes, 0,
                firstPhoto.photoBytes.size
            )
            val resizedBitmap = bitmap.scale(96, 96, false)
            return BitmapDescriptorFactory.fromBitmap(resizedBitmap)
        }
        return null
    }
}