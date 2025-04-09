package com.ocproject.realestatemanager.presentation.scene.map

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.core.graphics.scale
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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
    currentPosition: LatLng?,
    focusPosition: LatLng,

    ) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    Column {
//        PropertyListTopBar(
//            onEvent = viewModel::onEvent,
//            onNavigateToAddPropertyScreen = { },
//            onNavigateToMapOfProperties = { },
//            modifier = Modifier
//        )
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
//                        myLocationButtonEnabled = true
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
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(focusPosition, 10f)
            }
            LaunchedEffect(focusPosition) {
            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(focusPosition, 10f))
            }
//            LaunchedEffect(focusPosition) {
//                cameraPositionState.animate(
//                    CameraUpdateFactory.newLatLngZoom(focusPosition, 10f),
//                    1000 // Animation en 1 seconde
//                )
//            }

            Box {


                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = mapProperties,
                    uiSettings = mapUiSettings,
//        onMapLoaded =
                ) {

                    Marker(
                        state = markerState
                    )

                    state.properties.forEach { property ->
                        var bitmap = null
                        var resizedBitmap: Bitmap? = null
                        if ( bitmap != null){
                            val bitmap = BitmapFactory.decodeByteArray(
                                property.photoList?.first()?.photoBytes, 0,
                                property.photoList?.first()?.photoBytes?.size!!
                            )
                            resizedBitmap = bitmap.scale(96, 96, false)
                        }



                        if (resizedBitmap != null){
                            Marker(
                                state = MarkerState(LatLng(property.lat, property.lng)),
                                onInfoWindowClick = {
                                    Toast.makeText(context, "InfoClick", Toast.LENGTH_LONG).show()
                                },
                                icon =
                                    BitmapDescriptorFactory.fromBitmap(
                                        resizedBitmap
                                    ),
//                            onClick = {
//                                Toast.makeText(context, "mapClick", Toast.LENGTH_LONG).show()
//                                false
//                            }
                            )
                        } else {
                            Marker(
                                state = MarkerState(LatLng(property.lat, property.lng)),
                                onInfoWindowClick = {
                                    Toast.makeText(context, "InfoClick", Toast.LENGTH_LONG).show()
                                },
                            )
                        }

                    }
                }

            }

        }

    }
}