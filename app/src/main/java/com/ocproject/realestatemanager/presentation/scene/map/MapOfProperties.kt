package com.ocproject.realestatemanager.presentation.scene.map

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapOfProperties(
    viewModel: ListDetailsViewModel = koinViewModel(),
    mapViewModel: MapOfPropertiesViewModel = koinViewModel(),
    currentPosition: LatLng?,
    focusPosition: LatLng?,

    ) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val markers by mapViewModel.markers.collectAsState()

    LaunchedEffect(state.properties) {
        mapViewModel.createMarkers(state.properties)
    }
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

            val mapUiSettings by remember {
                mutableStateOf(
                    MapUiSettings(
                        compassEnabled = false,
                    )
                )
            }
            val mapProperties by remember {
                mutableStateOf(
                    MapProperties(
                        mapType = MapType.NORMAL,
                        isMyLocationEnabled = true,
                    )
                )
            }
            val markerState by remember {
                mutableStateOf(MarkerState(currentPosition))
            }
            var cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(currentPosition, 10f)
            }
            LaunchedEffect(focusPosition) {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(currentPosition, 10f),
                    1000
                )
            }
            if (focusPosition != null){
                cameraPositionState = rememberCameraPositionState{
                 position = CameraPosition.fromLatLngZoom(focusPosition, 10f)
                }

                LaunchedEffect(focusPosition) {
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(focusPosition, 10f),
                        1000
                    )
                }
            }


            Box {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = mapProperties,
                    uiSettings = mapUiSettings,
                ) {
                    Marker(
                        state = markerState
                    )

                    markers.forEach { marker ->
                        Marker(
                            state = marker.state,
                            icon = marker.icon,
                            onInfoWindowClick = {
                                Toast.makeText(context, "InfoClick", Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                }
            }
        }
    }
}