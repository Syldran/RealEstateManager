package com.ocproject.realestatemanager.presentation.scene.map

import com.ocproject.realestatemanager.R
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.ocproject.realestatemanager.domain.usecases.GetPropertyListUseCase
import com.ocproject.realestatemanager.presentation.navigation.Screen
import com.ocproject.realestatemanager.presentation.scene.propertydetails.PropertyDetailsViewModel
import com.ocproject.realestatemanager.presentation.scene.propertylist.PropertyListViewModel
import com.ocproject.realestatemanager.presentation.scene.propertylist.components.PropertyFilterSheet
import com.ocproject.realestatemanager.presentation.scene.propertylist.components.PropertyListTopBar
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber
import kotlin.coroutines.coroutineContext
import androidx.core.graphics.scale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapOfProperties(
    viewModel: PropertyListViewModel = koinViewModel(),
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
                        val bitmap = BitmapFactory.decodeByteArray(
                            property.photoList?.first()?.photoBytes, 0,
                            property.photoList?.first()?.photoBytes?.size!!
                        )
                        val resizedBitmap = bitmap.scale(96, 96, false)


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
//                    MarkerInfoWindow(
//                        state = MarkerState(position = LatLng(property.lat, property.lng)),
//                        icon = BitmapDescriptorFactory.fromResource(R.drawable.baseline_image)
//                    ) {
//                        Column(
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            verticalArrangement = Arrangement.Center,
//                            modifier = Modifier
//                                .border(
//                                    BorderStroke(1.dp, Color.Black),
//                                    RoundedCornerShape(10)
//                                )
//                                .clip(RoundedCornerShape(10))
//                                .background(Color.Blue)
//                                .padding(20.dp)
//                        ) {
//                            Text("Title", fontWeight = FontWeight.Bold, color = Color.White)
//                            Text("Description", fontWeight = FontWeight.Medium, color = Color.White)
//                        }
//                    }
                    }
                }

            }

        }
//        when {
//            state.isFilterSheetOpen -> {
//                PropertyFilterSheet(
//                    state = state,
//                    onEvent = viewModel::onEvent,
//                    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
//                    scope = rememberCoroutineScope(),
//                )
//            }
//        }
    }
}