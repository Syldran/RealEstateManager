package com.ocproject.realestatemanager.presentation.scene.addproperty.components

import android.app.Activity
import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.ocproject.realestatemanager.core.utils.Utils
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetAddress

@Composable
fun AutocompleteSearch(viewModel: AddPropertyViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
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
            onClick = {
                if (Utils.isInternetAvailable(context)) {
                    launchAutocompleteOverlay
                } else {
                    Toast.makeText(context, "erreur co", Toast.LENGTH_LONG).show()
                }


            }
        ) {
            Text("Select Location", modifier = Modifier.padding(horizontal = 8.dp))
            Icon(imageVector = Icons.Default.Search, contentDescription = "SearchButtonIcon")
        }
    }
}

suspend fun isInternetWorking(): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val address = InetAddress.getByName("8.8.8.8") // Google DNS
            !address.equals("")
        } catch (e: Exception) {
            false
        }
    }
}