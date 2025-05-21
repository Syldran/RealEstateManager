package com.ocproject.realestatemanager.presentation.scene.addproperty.components

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.net.InetAddress

@Composable
fun AutocompleteSearch(viewModel: AddPropertyViewModel, scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
    val context = LocalContext.current
//    val coroutineScope = rememberCoroutineScope()
    val intentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {


        when (it.resultCode) {
            Activity.RESULT_OK -> {
                it.data?.let { intent ->
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    viewModel.setPropertyFromPlace(place)
                }
                Timber.tag("AddLocation").i("Status: Result ok")
            }

            AutocompleteActivity.RESULT_ERROR -> {
                it.data?.let {
                    val status = Autocomplete.getStatusFromIntent(it)
                    Timber.tag("AddLocation").i("Status: ${status.statusMessage}")
                }
            }

            Activity.RESULT_CANCELED -> {
                Timber.tag("AddLocation").i("Status: Canceled")
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
        @VisibleForTesting
        var checkConnectivityForTest: Boolean
        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp),
            onClick = {
                if (Utils.isInternetAvailable(context)) {
                    checkConnectivityForTest = true
                    launchAutocompleteOverlay.invoke()
                } else {
                    checkConnectivityForTest = false
                    scope.launch {
                        snackbarHostState.showSnackbar("Error connexion")
                    }
                }
            }
        ) {
            Text("Select Location", modifier = Modifier.padding(horizontal = 8.dp))
            Icon(imageVector = Icons.Default.Search, contentDescription = "SearchButtonIcon")
        }
    }
}

