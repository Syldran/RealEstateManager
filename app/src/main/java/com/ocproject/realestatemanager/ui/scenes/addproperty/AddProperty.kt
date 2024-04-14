package com.ocproject.realestatemanager.ui.scenes.addproperty

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Picture
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import com.ocproject.realestatemanager.ui.theme.RealestatemanagerTheme
import com.openclassrooms.realestatemanager.models.PictureOfProperty
import org.koin.androidx.compose.koinViewModel


@Composable
fun AddProperty(
    viewModel: AddPropertyViewModel = koinViewModel(),
    onNavigateToPropertiesScreen: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
//    val flowBorderColor by viewModel.colorBorder.collectAsState()
    AddPropertyScreen(
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
    val result = remember { mutableStateListOf<PictureOfProperty>() }
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
                    var mainPicture: PictureOfProperty
                    if (state.mainPic == null) {
                        mainPicture = PictureOfProperty(uri = uris[0].toString(), id = 0, isMain = true, propertyId = 1)
                    } else {
                        mainPicture= state.mainPic
                    }
                    uris.forEach {
                        result.add(
                            PictureOfProperty(
                                uri = it.toString(),
                                propertyId = 1,
                                isMain = if (it.toString() == mainPicture.uri) true else false
                            )
                        )
                        index++
                    }
                    onSetPictureList(result)
                }
            },
        )
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
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

        Row() {

            result.forEach {
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
                            Log.d("TAG", "AddPropertyScreen: HERE id ${state.mainPic!!.id}, propertyId ${state.mainPic!!.propertyId}, isMain ${state.mainPic!!.isMain}, uri ${state.mainPic!!.uri}")
                            state.picturesList.forEach {
                                Log.d("TAG", "AddPropertyScreen: THERE id ${it.id}, propertyId ${it.propertyId}, isMain ${it.isMain}, uri ${it.uri}")

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


fun checkUriPersisted(contentResolver: ContentResolver, uri: Uri): Boolean {
    return contentResolver.persistedUriPermissions.any { perm -> perm.uri == uri }
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
        )
    }
}