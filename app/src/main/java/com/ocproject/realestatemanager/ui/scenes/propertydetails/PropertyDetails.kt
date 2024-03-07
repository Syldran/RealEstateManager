package com.ocproject.realestatemanager.ui.scenes.propertydetails


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import kotlin.math.log

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
    }
}