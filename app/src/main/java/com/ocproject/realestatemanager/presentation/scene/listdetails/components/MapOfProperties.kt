package com.ocproject.realestatemanager.presentation.scene.listdetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.ocproject.realestatemanager.core.utils.UtilsKotlin
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsEvent
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsState


@Composable
fun MapOfProperties(
    state: ListDetailsState,
    onEvent: (ListDetailsEvent) -> Unit,
    currentPosition: LatLng?,
    focusPosition: LatLng?,
    navigateBack: () -> Unit = {},
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    Column {

        if (currentPosition == null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Failed to acquire current Location"
                )
            }
        } else {
            val mapProperties by remember {
                mutableStateOf(
                    MapProperties(
                        mapType = MapType.NORMAL,
                        isMyLocationEnabled = true,
                    )
                )
            }
            var cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(currentPosition, 10f)
            }
            
            // Focus on selected property when it changes
            LaunchedEffect(state.selectedProperty?.id) {
                if (state.selectedProperty != null) {
                    val selectedPropertyLatLng = LatLng(state.selectedProperty.lat, state.selectedProperty.lng)
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(selectedPropertyLatLng, 15f),
                        1000
                    )
                }
            }
            
            LaunchedEffect(focusPosition) {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(currentPosition, 10f),
                    1000
                )
            }
            if (focusPosition != null) {
                cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(focusPosition, 10f)
                }

                LaunchedEffect(focusPosition) {
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(focusPosition, 10f),
                        1000
                    )
                }
            }
            
            // Box to contain both the map and the navigation button
            Box(modifier = Modifier.fillMaxSize()) {
                // GoogleMap fills the entire box
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = mapProperties
                ) {
                    // Add current location marker
                    Marker(
                        state = MarkerState(currentPosition),
                        title = "Current Location"
                    )

                    // Add property markers
                    state.properties.forEach { property ->
                        // Create a unique marker state for each property
                        val propertyLatLng = LatLng(property.lat, property.lng)
                        val propertyMarkerState = remember(property.id) { MarkerState(propertyLatLng) }
                        val isSelected = state.selectedProperty?.id == property.id

                        Marker(
                            state = propertyMarkerState,
                            title = property.address,
                            snippet = "${property.areaCode} ${property.town} • $${property.price}",
                            icon = UtilsKotlin.createMarkerIcon(property, isSelected),
                            onClick = {
                                it.showInfoWindow()
                                onEvent(ListDetailsEvent.UpdateSelectedProperty(property))
                                true
                            }
                        )
                    }
                }
                
                // Navigation button positioned on top of the map (only in portrait mode)
                if (!isLandscape) {
                    FilledTonalIconButton(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopStart),
                        onClick = { navigateBack() },
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                }
            }
        }
    }
}


