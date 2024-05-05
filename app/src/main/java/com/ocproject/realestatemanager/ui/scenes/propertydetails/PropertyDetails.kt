package com.ocproject.realestatemanager.ui.scenes.propertydetails


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.ocproject.realestatemanager.BuildConfig
import org.koin.androidx.compose.koinViewModel

@Composable
fun PropertyDetail(
    viewModel: PropertyDetailsViewModel = koinViewModel(),
    onNavigateToAddPropertyScreen: (propertyId: Int?) -> Unit,

    ) {
    val state by viewModel.state.collectAsState()
    PropertyDetailsScreen(
        state = state,
        onNavigateToAddPropertyScreen = onNavigateToAddPropertyScreen
    )
}

@Composable
fun PropertyDetailsScreen(
    state: PropertyDetailsState,
    onNavigateToAddPropertyScreen: (propertyId: Int?) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val latLng = LatLng(state.lat, state.lng)
        val cameraPositionState = CameraPositionState(
            position = CameraPosition.fromLatLngZoom(
                latLng, 10f
            )
        )
        Text(text = "Property id : ${state.id}")
        Text(text = "Property type : ${state.type}")
        Text(text = "Property price : ${state.price}")
        Text(text = "Property area : ${state.area}")
        Text(text = "Property number of rooms : ${state.numberOfRooms}")
        Text(text = "Property description : ${state.description}")
        Text(text = "Property address : ${state.address}")
        Text(text = "Property state : ${state.state}")
        Text(text = "Property created Date : ${state.createDate}")
        Text(text = "Property sold date : ${state.soldDate}")
        Text(text = "Property agent id : ${state.agentId}")
        Text(text = "Property lat : ${state.lat}")
        Text(text = "Property lng : ${state.lng}")

        Row(modifier = Modifier) {
            state.picturesList?.forEach() {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(it.uri).build(),
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .weight(0.25f)
                        .border(
                            2.dp,
                            shape = RectangleShape,
                            color = if (it.isMain) Color.Red else Color.Black
                        )
                        .height(64.dp)
                )
            }
        }
        Box(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    onNavigateToAddPropertyScreen(state.id)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Property"
                )
            }
        }
        AsyncImage(
            modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(
                    "https://maps.googleapis.com/maps/api/staticmap?center=${latLng.latitude},${latLng.longitude}&markers=color:red|${latLng.latitude},${latLng.longitude}&zoom=14&size=500x500&scale=2&key=${BuildConfig.PLACES_API_KEY}"
                ).build(), contentDescription = ""
        )
//        GoogleMap(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(320.dp),
////                .verticalScroll(rememberScrollState()),
//            cameraPositionState = cameraPositionState,
////                            keep map static
//            properties = MapProperties(
//                latLngBoundsForCameraTarget = LatLngBounds(
//                    latLng,
//                    latLng
//                )
//            )
//        ) {
//            Marker(
//                state = MarkerState(position = latLng),
//                title = "Marker",
//                snippet = "Marker",
//            )
//        }
    }
}