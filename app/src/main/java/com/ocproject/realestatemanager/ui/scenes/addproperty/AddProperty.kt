package com.ocproject.realestatemanager.ui.scenes.addproperty

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.ocproject.realestatemanager.ui.theme.RealestatemanagerTheme
import com.openclassrooms.realestatemanager.models.PictureOfProperty
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber


@Composable
fun AddProperty(
    viewModel: AddPropertyViewModel = koinViewModel(),
    onNavigateToPropertiesScreen: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
//    val flowBorderColor by viewModel.colorBorder.collectAsState()
    AddPropertyScreen(
        viewModel = viewModel,
        state = state,
        onSetType = {
            viewModel.onEvent(AddPropertyEvent.SetType(it))
        },
        onSetPrice = {
            viewModel.onEvent(AddPropertyEvent.SetPrice(it))
        },
        onSetDescription = {
            viewModel.onEvent(AddPropertyEvent.SetDescription(it))
        },
        onSetArea = {
            viewModel.onEvent(AddPropertyEvent.SetArea(it))
        },
        onSetNumberOfRooms = {
            viewModel.onEvent(AddPropertyEvent.SetNumberOfRooms(it))
        },
        onSetAddress = {
            viewModel.onEvent(AddPropertyEvent.SetAddress(it))
        },
        onSetState = {
            viewModel.onEvent(AddPropertyEvent.SetState(it))
        },
        onSetLat = {
            viewModel.onEvent(AddPropertyEvent.SetLat(it))
        },
        onSetLng = {
            viewModel.onEvent(AddPropertyEvent.SetLng(it))
        },
        onSetPictureList = {
            viewModel.onEvent(AddPropertyEvent.SetPictureList(it))
        },
        onSaveClick = {
            viewModel.onEvent(AddPropertyEvent.SaveProperty)
            onNavigateToPropertiesScreen()
        },
        onSetMainPicture = {
            viewModel.onEvent(AddPropertyEvent.SetMainPicture(it))
        }
    )

}

@Composable
fun AddPropertyScreen(
    viewModel: AddPropertyViewModel,
    state: AddPropertyState,
    onSetType: (type: String) -> Unit,
    onSetPrice: (price: String) -> Unit,
    onSetDescription: (description: String) -> Unit,
    onSetArea: (area: String) -> Unit,
    onSetNumberOfRooms: (numberOfRoom: String) -> Unit,
    onSetAddress: (address: String) -> Unit,
    onSetState: (state: String) -> Unit,
    onSetLat: (lat: String) -> Unit,
    onSetLng: (lat: String) -> Unit,
    onSetPictureList: (pictureList: List<PictureOfProperty>) -> Unit,
    onSaveClick: () -> Unit,
    onSetMainPicture: (picture: PictureOfProperty) -> Unit,
) {

    val context = LocalContext.current
    var result = remember { mutableStateListOf<PictureOfProperty>() }
    if (viewModel.propertyId != null && viewModel.propertyId != 0) {
        result = state.picturesList.toMutableStateList()
    }
    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(),
            onResult = { maybeUris ->
                maybeUris.let { uris ->
                    val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    uris.forEach {
                        if (checkUriPersisted(context.contentResolver, it)) {
                            context.contentResolver.releasePersistableUriPermission(it, flags)
                        }
                        context.contentResolver.takePersistableUriPermission(it, flags)
                    }
                    result.clear()
                    var index = 0
                    val mainPicture: PictureOfProperty = state.mainPic
                        ?: PictureOfProperty(
                            uri = uris[0].toString(),
                            id = 0,
                            isMain = true,
                            propertyId = 1
                        )
                    uris.forEach {
                        result.add(
                            PictureOfProperty(
                                uri = it.toString(),
                                propertyId = 1,
                                isMain = it.toString() == mainPicture.uri
                            )
                        )
                        index++
                    }
                    onSetPictureList(result)
                }
            },
        )

    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
//        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Column {


            AutocompletePredictionList(viewModel, context, state)
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.type,
                onValueChange = onSetType,

                placeholder = {
                    Text(text = "Type")
                },
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = if (state.price == 0) "" else state.price.toString(),
                onValueChange = onSetPrice,
                placeholder = {
                    Text(text = "Price")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

            )
            TextField(// check somewhere that text entered is a number
                modifier = Modifier.fillMaxWidth(),
                value = if (state.area == 0) "" else state.area.toString(),
                onValueChange = onSetArea,
                placeholder = {
                    Text(text = "Area")
                }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = if (state.numberOfRooms == 0) "" else state.numberOfRooms.toString(),
                onValueChange = onSetNumberOfRooms,
                placeholder = {
                    Text(text = "Number of rooms")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.description,
                onValueChange = onSetDescription,
                placeholder = {
                    Text(text = "Description")
                }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.address,
                onValueChange = onSetAddress,
                placeholder = {
                    Text(text = "Address")
                }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.state,
                onValueChange = onSetState,
                placeholder = {
                    Text(text = "State")
                }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = if (state.lat == 0.0) "" else state.lat.toString(),
                onValueChange = {
                    try {
                        onSetLat(it)
                    } catch (e: Exception) {
                    }

                },
                placeholder = {
                    Text(text = "Latitude")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = if (state.lng == 0.0) "" else state.lng.toString(),
                onValueChange = {
                    try {
                        onSetLng(it)
                    } catch (e: Exception) {
                    }
                },
                placeholder = {
                    Text(text = "Longitude")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )


            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(onClick = {
                    launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
                }) {
                    Text(text = "Browse Picture")

                }
            }

            Row {

                result.forEach { it ->
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).data(it.uri).build(),
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier
                            .weight(0.25f)
                            .border(
                                2.dp,
                                shape = RectangleShape,
                                color = if (it.uri == state.mainPic?.uri) Color.Red else Color.Black
                            )
                            .height(64.dp)
                            .clickable {
                                onSetMainPicture(it)
                                Timber
                                    .tag("TAG")
                                    .d("AddPropertyScreen: HERE id " + state.mainPic?.id + ", propertyId " + state.mainPic?.propertyId + ", isMain " + state.mainPic?.isMain + ", uri " + state.mainPic?.uri)
                                state.picturesList.forEach {
                                    Timber
                                        .tag("TAG")
                                        .d("AddPropertyScreen: THERE id " + it.id + ", propertyId " + it.propertyId + ", isMain " + it.isMain + ", uri " + it.uri)

                                }

                            }
                    )

                }

            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(onClick = {
                    onSetPictureList(state.picturesList)
                    onSaveClick()
                }) {
                    Text(text = "Save")
                }
            }
        }
    }
}

fun checkUriPersisted(contentResolver: ContentResolver, uri: Uri): Boolean {
    return contentResolver.persistedUriPermissions.any { perm -> perm.uri == uri }
}


@Composable
fun AutocompletePredictionList(
    viewModel: AddPropertyViewModel,
    context: Context,
    state: AddPropertyState,
) {

    Column {
        TextField(
            enabled = true,
            value = state.changeText,
            onValueChange = { it ->
                viewModel.onEvent(AddPropertyEvent.ChangeText(it))
                autocompleteProgra(it, context = context) {
                    viewModel.onEvent(AddPropertyEvent.UpdatePredictions(it))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Search") }
        )
//        Spacer(modifier = Modifier.height(16.dp))
        Box {
            if (state.isSearching) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {

                if (state.updatedPredictions.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
//                                    .weight(1f)
                            .background(Color.White)
                            .height(296.dp)
                    ) {
                        items(state.updatedPredictions) { prediction ->
                            Text(
                                text = "${prediction.getFullText(null)}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp, horizontal = 16.dp)
                                    .clickable {
                                        viewModel.onEvent(
                                            AddPropertyEvent.ChangeText(
                                                prediction
                                                    .getFullText(null)
                                                    .toString()
                                            )
                                        )
                                        autocompleteFetch(prediction.placeId, context, viewModel)
                                        viewModel.onEvent(
                                            AddPropertyEvent.UpdatePredictions(
                                                emptyList()
                                            )
                                        )
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}


fun autocompleteProgra(
    query: String,
    context: Context,
    onUpdatePredictions: (List<AutocompletePrediction>) -> Unit
) {

    val placesClient = Places.createClient(context)
    // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
    // and once again when the user makes a selection (for example when calling fetchPlace()).
    val token = AutocompleteSessionToken.newInstance()
    // Use the builder to create a FindAutocompletePredictionsRequest.
    val request =
        FindAutocompletePredictionsRequest.builder()
            .setCountries("FR")
            .setSessionToken(token)
            .setQuery(query)
            .build()
    placesClient.findAutocompletePredictions(request)
        .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
            onUpdatePredictions(response.autocompletePredictions)
//            for (prediction in response.autocompletePredictions) {
//                Timber.tag("TAG").i(prediction.placeId)
//                Timber.tag("TAG").i(prediction.getPrimaryText(null).toString())
//            }
        }.addOnFailureListener { exception: Exception? ->
            if (exception is ApiException) {
                Timber.tag("TAG").e("Place not found: " + exception.statusCode)
            }
        }

}

fun autocompleteFetch(
    query: String,
    context: Context,
    viewModel: AddPropertyViewModel
) {

    val placesClient = Places.createClient(context)
    val token = AutocompleteSessionToken.newInstance()
    val placeFields =
        listOf(
            Place.Field.NAME,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS,
            Place.Field.ADDRESS_COMPONENTS,
            Place.Field.TYPES
        )
    val request =
        FetchPlaceRequest.builder(query, placeFields)
            .setSessionToken(token)
            .build()
    placesClient.fetchPlace(request)
        .addOnSuccessListener { response ->
            viewModel.setPropertyFromPlace(response.place)
        }.addOnFailureListener { exception: Exception? ->
            if (exception is ApiException) {
                Timber.tag("TAG").e("Place not found: " + exception.statusCode)
            }
        }

}

@Preview
@Composable
private fun AddPropertyPreview() {
    RealestatemanagerTheme {
        AddPropertyScreen(
            state = AddPropertyState(),
            onSetType = {},
            onSetPrice = {},
            onSetDescription = {},
            onSetArea = {},
            onSetNumberOfRooms = {},
            onSetAddress = {},
            onSetState = {},
            onSetLat = {},
            onSetLng = {},
            onSetPictureList = {},
            onSaveClick = {},
            onSetMainPicture = {},
            viewModel = koinViewModel()
        )
    }
}




