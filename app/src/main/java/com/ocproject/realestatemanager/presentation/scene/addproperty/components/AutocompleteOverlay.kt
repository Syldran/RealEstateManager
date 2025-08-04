package com.ocproject.realestatemanager.presentation.scene.addproperty.components

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import com.ocproject.realestatemanager.core.GlobalSnackBarManager
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.ocproject.realestatemanager.R
import com.ocproject.realestatemanager.core.utils.Globals.checkConnectivityForTest
import com.ocproject.realestatemanager.core.utils.Utils.isInternetAvailable
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun AutocompleteSearch(viewModel: AddPropertyViewModel, scope: CoroutineScope) {
    val context = LocalContext.current
    val intentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->


        when (activityResult.resultCode) {
            Activity.RESULT_OK -> {
                activityResult.data?.let { intent ->
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    viewModel.setPropertyFromPlace(place)
                }
            }

            AutocompleteActivity.RESULT_ERROR -> {
                activityResult.data?.let {
                    try {
                        Autocomplete.getStatusFromIntent(it)
                    }catch (e: Exception){
                        println(e.toString())
                    }
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
                if (isInternetAvailable(context)) {
                    checkConnectivityForTest = true
                    launchAutocompleteOverlay.invoke()
                } else {
                    checkConnectivityForTest = false
                    GlobalSnackBarManager.showSnackMsg(context.getString(R.string.error_connexion), isSuccess = false)
                }
            }
        ) {
            Text(stringResource(R.string.select_location), modifier = Modifier.padding(horizontal = 8.dp))
            Icon(imageVector = Icons.Default.Search, contentDescription = stringResource(R.string.search_button_icon))
        }
    }
}

