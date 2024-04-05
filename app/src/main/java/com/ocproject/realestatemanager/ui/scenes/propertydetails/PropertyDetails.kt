package com.ocproject.realestatemanager.ui.scenes.propertydetails


import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun PropertyDetail(
    viewModel: PropertyDetailsViewModel
    //on navigate
) {
    val state by viewModel.state.collectAsState()
    PropertyDetailsScreen(
        state = state,
    )
}

@Composable
fun PropertyDetailsScreen(
    state: PropertyDetailsViewModel.PropertyDetailsState,
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
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
        Log.d("TAG1", "PropertyDetailsScreen: ${state.picturesList.size}")
        Row(modifier = Modifier) {
            state.picturesList.forEach() {

            Log.d("TAG1", "PropertyDetailsScreen: ${it.uri}")

                when (it.isMain) {
                    true -> AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).data(it.uri).build(),
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier
                            .weight(0.25f)
                            .border(2.dp, shape = RectangleShape, color = Color.Red)
                            .height(64.dp)
                    )

                    else -> {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current).data(it.uri).build(),
                            contentDescription = null,
                            contentScale = ContentScale.FillHeight,
                            modifier = Modifier
                                .weight(0.25f)
                                .border(2.dp, shape = RectangleShape, color = Color.Black)
                                .height(64.dp)
                        )
                    }
                }
            }
        }
    }
}