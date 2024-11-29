package com.ocproject.realestatemanager.presentation.scene.addproperty.components

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyViewModel

@Composable
fun AutocompleteSearch(viewModel: AddPropertyViewModel) {
    val context = LocalContext.current

    val intentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        when (it.resultCode) {
            Activity.RESULT_OK -> {
                it.data?.let { intent ->
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    viewModel.setPropertyFromPlace(place)
                }
            }

            AutocompleteActivity.RESULT_ERROR -> {
                it.data?.let {
                    val status = Autocomplete.getStatusFromIntent(it)
                    Log.i("AddLocation", "Status: ${status.statusMessage}")
                }
            }

            Activity.RESULT_CANCELED -> {
                // The user canceled the operation.
            }
        }
    }

    val launchAutocompleteOverlay = {
        val fields = listOf(Place.Field.ID, Place.Field.ADDRESS_COMPONENTS, Place.Field.LAT_LNG)
        val intent = Autocomplete
            .IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .build(context)
        intentLauncher.launch(intent)
    }

    Column {
        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp),
            onClick = launchAutocompleteOverlay
        ) {
            Text("Select Location", modifier = Modifier.padding(horizontal = 8.dp))
            Icon(imageVector = Icons.Default.Search, contentDescription = "SearchButtonIcon")
        }
    }
}