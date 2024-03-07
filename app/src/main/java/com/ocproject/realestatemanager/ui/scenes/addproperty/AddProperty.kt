package com.ocproject.realestatemanager.ui.scenes.addproperty

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
    AddPropertyScreen(
        state = state,
        onSetType = {
            viewModel.onEvent(AddPropertyViewModel.AddPropertyEvent.SetType(it))
        },
        onSetPrice = {
            viewModel.onEvent(AddPropertyViewModel.AddPropertyEvent.SetPrice(it.toInt()))
        },
        onSetDescription = {
            viewModel.onEvent(AddPropertyViewModel.AddPropertyEvent.SetDescription(it))
        },
        onSetArea = {
            viewModel.onEvent(AddPropertyViewModel.AddPropertyEvent.SetArea(it.toInt()))
        },
        onSetNumberOfRooms = {
            viewModel.onEvent(AddPropertyViewModel.AddPropertyEvent.SetNumberOfRooms(it.toInt()))
        },
        onSetAddress = {
            viewModel.onEvent(AddPropertyViewModel.AddPropertyEvent.SetAddress(it))
        },
        onSetState = {
            viewModel.onEvent(AddPropertyViewModel.AddPropertyEvent.SetState(it))
        },
        onSetLat = {
            viewModel.onEvent(AddPropertyViewModel.AddPropertyEvent.SetLat(it.toDouble()))
        },
        onSetLng = {
            viewModel.onEvent(AddPropertyViewModel.AddPropertyEvent.SetLng(it.toDouble()))
        },
        onSaveClick = {
            viewModel.onEvent(AddPropertyViewModel.AddPropertyEvent.SaveProperty)
            onNavigateToPropertiesScreen()
        }
    )

}

@Composable
fun AddPropertyScreen(
    state: AddPropertyViewModel.AddPropertyState,
    onSetType: (type: String) -> Unit,
    onSetPrice: (price: String) -> Unit,
    onSetDescription: (description: String) -> Unit,
    onSetArea: (area: String) -> Unit,
    onSetNumberOfRooms: (numberOfRoom: String) -> Unit,
    onSetAddress: (address: String) -> Unit,
    onSetState: (state: String) -> Unit,
    onSetLat: (lat: String) -> Unit,
    onSetLng: (lat: String) -> Unit,
    onSaveClick: () -> Unit
) {


    val result = remember { mutableStateListOf<PictureOfProperty>() }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(4)) {
            result.clear()
            it.forEach {
                result.add(PictureOfProperty(uri = it.toString()))
            }

            Log.d("TAG", "launcher: result = ${result.size}")
        }


    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        var input by rememberSaveable { mutableStateOf("") }
//        TextField(
//            value = input,
//            onValueChange = { newText ->
//                input = newText.trimStart { it == '0' }
//            },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
//        )

        TextField(
// check somewhere that text entered is a number
            modifier = Modifier.fillMaxWidth(),
            value = if (state.numberOfRooms == 0) "" else state.numberOfRooms.toString(),
            onValueChange = onSetNumberOfRooms,
            placeholder = {
                Text(text = "Number of rooms")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.type,
            onValueChange = onSetType,

            placeholder = {
                Text(text = "Type")
            },
        )
        TextField(// check somewhere that text entered is a number
            modifier = Modifier.fillMaxWidth(),
            value = if (state.price == 0) "" else state.price.toString(),
            onValueChange = onSetPrice,
//            {
//                viewModel.onEvent(PropertyEvent.SetPrice(it.toInt()))
//            },

            placeholder = {
                Text(text = "Price")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

        )
        TextField(// check somewhere that text entered is a number
            modifier = Modifier.fillMaxWidth(),
            value = if (state.area == 0) "" else state.area.toString(),
            onValueChange = onSetArea,
            /*{
                viewModel.onEvent(PropertyEvent.SetArea(it.toInt()))
            },*/
            placeholder = {
                Text(text = "Area")
            }
        )
        TextField(// check somewhere that text entered is a number
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
//            {
//                viewModel.onEvent(PropertyEvent.SetDescription(it))
//            },
            placeholder = {
                Text(text = "Description")
            }
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.address,
            onValueChange = onSetAddress,
//            {
//                viewModel.onEvent(PropertyEvent.SetAddress(it))
//            },
            placeholder = {
                Text(text = "Address")
            }
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.state,
            onValueChange = onSetState,
//            {
//                viewModel.onEvent(PropertyEvent.SetState(it))
//            },
            placeholder = {
                Text(text = "State")
            }
        )
        TextField(// check somewhere that text entered is a number
            modifier = Modifier.fillMaxWidth(),
            value = if (state.lat == 0.0) "" else state.lat.toString(),

            onValueChange = {
                try {
                    onSetLat(it)
//                    viewModel.onEvent(PropertyEvent.SetLat(it.toDouble()))
                } catch (e: Exception) {
                }

            },
            placeholder = {
                Text(text = "Latitude")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        TextField(// check somewhere that text entered is a number
            modifier = Modifier.fillMaxWidth(),
            value = if (state.lat == 0.0) "" else state.lng.toString(),
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
                //pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))

            }) {
                Text(text = "Browse Picture")

            }
        }

        Row() {
            result.forEach {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(it.uri).build(),
                    contentDescription = null
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(onClick = {
                Log.d("TAG", "AddPropertyScreen: remember value is ${result.size}")
                onSaveClick()
            }) {
                Text(text = "Save")
            }
        }
    }
}


@Preview
@Composable
private fun AddPropertyPreview() {
    RealestatemanagerTheme {
        AddPropertyScreen(
            state = AddPropertyViewModel.AddPropertyState(),
            onSetType = {},
            onSetPrice = {},
            onSetDescription = {},
            onSetArea = {},
            onSetNumberOfRooms = {},
            onSetAddress = {},
            onSetState = {},
            onSetLat = {},
            onSetLng = {},
            onSaveClick = {},
        )
    }
}

