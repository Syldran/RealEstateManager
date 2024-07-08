package com.ocproject.realestatemanager.presentation.scene.addproperty

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ocproject.realestatemanager.models.InterestPoint
import com.ocproject.realestatemanager.models.PropertyWithPhotos
import com.ocproject.realestatemanager.presentation.scene.addproperty.components.AutocompleteSearch
import com.ocproject.realestatemanager.presentation.scene.addproperty.components.PhotosComposable
import com.ocproject.realestatemanager.presentation.scene.addproperty.components.PropertyTextField
import com.ocproject.realestatemanager.presentation.scene.addproperty.utils.ImagePicker
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddPropertyScreen(
    viewModel: AddPropertyViewModel = koinViewModel(),
    onNavigateToPropertyListScreen: () -> Unit,
) {
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = -available.y
                /*coroutineScope.launch {
                    lazyGridState.scrollBy(delta)
                }*/
                return Offset.Zero
            }

        }
    }
    val imagePicker = ImagePicker(
        LocalContext.current as ComponentActivity
    )
    imagePicker.RegisterPickerMulti {
        viewModel.onEvent(AddPropertyEvent.OnPhotoPicked(it))
    }
    val newProperty = viewModel.newProperty
    val photoList = viewModel.photoList
    val state by viewModel.state.collectAsState()

    var schoolChecked by remember { mutableStateOf(false) }
    var parkChecked by remember { mutableStateOf(false) }
    var transportChecked by remember { mutableStateOf(false) }
    var shopChecked by remember { mutableStateOf(false) }
    newProperty?.interestPoints?.forEach {
        when (it) {
            InterestPoint.PARK -> {
                parkChecked = true
            }

            InterestPoint.SCHOOL -> {
                schoolChecked = true
            }

            InterestPoint.SHOP -> {
                shopChecked = true
            }

            InterestPoint.TRANSPORT -> {
                transportChecked = true
            }
        }
    }

    if (state.navToPropertyListScreen) {
        onNavigateToPropertyListScreen()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.TopStart
    ) {


        Column(
            modifier = Modifier.fillMaxWidth()/*.nestedScroll(nestedScrollConnection)*/,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            if (photoList.value == null) {
                Box(modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(40))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .clickable {
                        imagePicker.pickMultiImage()
                    }
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        shape = RoundedCornerShape(40)
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Add Photo",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(40.dp)
                    )
                }
            } else {

                PhotosComposable(
                    propertyWithPhotos = PropertyWithPhotos(
                        newProperty!!,
                        photoList.value
                    ),
                    modifier = Modifier
                        .size(150.dp)
                        .clickable { imagePicker.pickMultiImage() },
                    viewModel = viewModel
                )
//                PropertyPhoto(
//                    propertyWithPhotos = PropertyWithPhotos(newProperty, photoList.value),
//                    modifier = Modifier
//                        .size(150.dp)
//                        .clickable { imagePicker.pickMultiImage() }
//                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            AutocompleteSearch(viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            PropertyTextField(
                value = newProperty?.address ?: "",
                placeholder = "Address",
                error = state.addressError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.OnAddressChanged(it))
                },
                keyboardType = KeyboardType.Text
            )
            Spacer(modifier = Modifier.height(16.dp))
            PropertyTextField(
                value = newProperty?.town ?: "",
                placeholder = "Town",
                error = state.townError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.OnTownChanged(it))
                },
                keyboardType = KeyboardType.Text
            )
            Spacer(modifier = Modifier.height(16.dp))
            PropertyTextField(
                value = if (newProperty?.areaCode == null) "" else newProperty.areaCode.toString(),
                placeholder = "Area Code",
                error = state.areaCodeError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.OnAreaCodeChanged(it))
                },
                keyboardType = KeyboardType.Number,
            )
            Spacer(modifier = Modifier.height(16.dp))
            PropertyTextField(
                value = newProperty?.country ?: "",
                placeholder = "Country",
                error = state.countryError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.OnCountryChanged(it))
                },
                keyboardType = KeyboardType.Text
            )
            Spacer(modifier = Modifier.height(16.dp))
            PropertyTextField(
                value = if (newProperty?.price == null) "" else newProperty.price.toString(),
                placeholder = "Price",
                error = state.priceError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.OnPriceChanged(it))
                },
                keyboardType = KeyboardType.Number
            )
            Spacer(modifier = Modifier.height(16.dp))

            PropertyTextField(
                value = if (newProperty?.surfaceArea == null) "" else newProperty.surfaceArea.toString(),
                placeholder = "Surface Area",
                error = state.surfaceAreaError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.OnSurfaceAreaChanged(it))
                },
                keyboardType = KeyboardType.Number
            )
            Spacer(modifier = Modifier.height(16.dp))
            PropertyTextField(
                value = if (newProperty?.lat == null || newProperty.lat == 0.0) "" else newProperty.lat.toString(),
                placeholder = "Latitude",
                error = state.latError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.OnLatChanged(it))
                },
                keyboardType = KeyboardType.Decimal
            )
            Spacer(modifier = Modifier.height(16.dp))
            PropertyTextField(
                value = if (newProperty?.lng == null || newProperty.lng == 0.0) "" else newProperty.lng.toString(),
                placeholder = "Longitude",
                error = state.lngError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.OnLngChanged(it))
                },
                keyboardType = KeyboardType.Decimal
            )

            Row {
                FilterChip(
                    modifier = Modifier.padding(4.dp),
                    onClick = {
                        schoolChecked = !schoolChecked
                        viewModel.onEvent(AddPropertyEvent.OnSchoolChecked(schoolChecked))
                    },
                    label = {
                        Text("School")
                    },
                    selected = schoolChecked,
                    leadingIcon = if (schoolChecked) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    },
                )

                FilterChip(
                    modifier = Modifier.padding(4.dp),
                    onClick = {
                        parkChecked = !parkChecked
                        viewModel.onEvent(AddPropertyEvent.OnParkChecked(parkChecked))
                    },
                    label = {
                        Text("Park")
                    },
                    selected = parkChecked,
                    leadingIcon = if (parkChecked) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    },
                )

                FilterChip(
                    modifier = Modifier.padding(4.dp),
                    onClick = {
                        shopChecked = !shopChecked
                        viewModel.onEvent(AddPropertyEvent.OnShopChecked(shopChecked))
                    },
                    label = {
                        Text("Shop")
                    },
                    selected = shopChecked,
                    leadingIcon = if (shopChecked) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    },
                )

                FilterChip(
                    modifier = Modifier.padding(4.dp),
                    onClick = {
                        transportChecked = !transportChecked
                        viewModel.onEvent(AddPropertyEvent.OnTransportChecked(transportChecked))
                    },
                    label = {
                        Text("Transport")
                    },
                    selected = transportChecked,
                    leadingIcon = if (transportChecked) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    },
                )
            }


            Button(onClick = {
                viewModel.onEvent(AddPropertyEvent.SaveProperty)
                if (state.navToPropertyListScreen) {
                    onNavigateToPropertyListScreen()
                }
            }) {
                Text(text = "Save contact")
            }
            Spacer(modifier = Modifier.height(16.dp))

        }
        IconButton(
            onClick = {
                onNavigateToPropertyListScreen()
            },
            colors = IconButtonColors(
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Icon(imageVector = Icons.Rounded.Close, contentDescription = "Close")
        }
    }
}