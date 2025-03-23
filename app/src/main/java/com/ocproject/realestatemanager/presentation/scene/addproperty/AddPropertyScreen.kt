package com.ocproject.realestatemanager.presentation.scene.addproperty

import androidx.activity.ComponentActivity
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ocproject.realestatemanager.core.InterestPoint
import com.ocproject.realestatemanager.presentation.scene.addproperty.components.AutocompleteSearch
import com.ocproject.realestatemanager.presentation.scene.addproperty.components.PhotosComposable
import com.ocproject.realestatemanager.presentation.scene.addproperty.components.PropertyTextField
import com.ocproject.realestatemanager.presentation.scene.addproperty.utils.ImagePicker
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddPropertyScreen(
    viewModel: AddPropertyViewModel = koinViewModel(),
    onNavigateToListDetails: () -> Unit,
) {
    val context = LocalContext.current
    val imagePicker = ImagePicker(
        context as ComponentActivity
    )
    imagePicker.RegisterPickerMulti {
        viewModel.onEvent(AddPropertyEvent.OnPhotoPicked(it))
    }
    val newProperty = viewModel.newProperty
    val photoList = viewModel.photoList
    val state by viewModel.state.collectAsState()

    var soldChecked by remember { mutableStateOf(false) }
    var schoolChecked by remember { mutableStateOf(false) }
    var parkChecked by remember { mutableStateOf(false) }
    var transportChecked by remember { mutableStateOf(false) }
    var shopChecked by remember { mutableStateOf(false) }
    newProperty.interestPoints.forEach {
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
    if (newProperty.sold == true) {
        soldChecked = true
    }

    if (state.navToPropertyListScreen) {
        onNavigateToListDetails()
        viewModel.onEvent(AddPropertyEvent.OnChangeNavigationStatus(true))
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .statusBarsPadding(),
        contentAlignment = Alignment.TopStart
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()/*.nestedScroll(nestedScrollConnection)*/,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))


            PhotosComposable(
                property = newProperty.copy(photoList = photoList.value),
                modifier = Modifier
                    .clickable { imagePicker.pickMultiImage() },
                viewModel = viewModel
            )



            Spacer(modifier = Modifier.height(16.dp))
            AutocompleteSearch(viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            PropertyTextField(
                value = newProperty.address,
                error = state.addressError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.UpdateForm(address = it))
                },
                keyboardType = KeyboardType.Text,
                labelValue = "Address",
            )
            Spacer(modifier = Modifier.height(16.dp))
            PropertyTextField(
                value = newProperty?.town ?: "",
                error = state.townError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.UpdateForm(town = it))

                },
                keyboardType = KeyboardType.Text,
                labelValue = "Town"
            )
            Spacer(modifier = Modifier.height(16.dp))
            PropertyTextField(
                value = if (newProperty.areaCode == null || newProperty.areaCode == 0) "" else newProperty.areaCode.toString(),
                error = state.areaCodeError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.UpdateForm(areaCode = it))
                },
                keyboardType = KeyboardType.Number,
                labelValue = "Area Code"
            )
            Spacer(modifier = Modifier.height(16.dp))
            PropertyTextField(
                value = newProperty?.country ?: "",
                error = state.countryError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.UpdateForm(country = it))
                },
                keyboardType = KeyboardType.Text,
                labelValue = "Country"
            )
            Spacer(modifier = Modifier.height(16.dp))
            PropertyTextField(
                value = if (newProperty.price == null || newProperty.price == 0) "" else newProperty.price.toString(),
                error = state.priceError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.UpdateForm(price = it))
                },
                keyboardType = KeyboardType.Number,
                labelValue = "Price"
            )
            Spacer(modifier = Modifier.height(16.dp))

            PropertyTextField(
                value = if (newProperty.surfaceArea == null || newProperty.surfaceArea == 0) "" else newProperty.surfaceArea.toString(),
                error = state.surfaceAreaError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.UpdateForm(surfaceArea = it))
                },
                keyboardType = KeyboardType.Number,
                labelValue = "Surface Area"
            )
            Spacer(modifier = Modifier.height(16.dp))
            PropertyTextField(
                value = if (newProperty.lat == 0.0) "" else newProperty.lat.toString(),
                error = state.latError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.UpdateForm(latitude = it))
                },
                keyboardType = KeyboardType.Decimal,
                labelValue = "Latitude"
            )
            Spacer(modifier = Modifier.height(16.dp))
            PropertyTextField(
                value = if (newProperty.lng == 0.0) "" else newProperty.lng.toString(),
                error = state.lngError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.UpdateForm(longitude = it))
                },
                keyboardType = KeyboardType.Decimal,
                labelValue = "Longitude"
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row {
                FilterChip(
                    modifier = Modifier.padding(4.dp),
                    onClick = {
                        soldChecked = !soldChecked
                        viewModel.onEvent(AddPropertyEvent.UpdateTags(sold = soldChecked))
                    },
                    label = {
                        Text("SOLD!")
                    },
                    selected = soldChecked,
                    leadingIcon = if (soldChecked) {
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
                        schoolChecked = !schoolChecked
                        viewModel.onEvent(
                            AddPropertyEvent.UpdateTags(
                                park = parkChecked,
                                school = schoolChecked,
                                shop = shopChecked,
                                transport = transportChecked
                            )
                        )

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
                        viewModel.onEvent(
                            AddPropertyEvent.UpdateTags(
                                park = parkChecked,
                                school = schoolChecked,
                                shop = shopChecked,
                                transport = transportChecked
                            )
                        )
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
                        viewModel.onEvent(
                            AddPropertyEvent.UpdateTags(
                                park = parkChecked,
                                school = schoolChecked,
                                shop = shopChecked,
                                transport = transportChecked
                            )
                        )

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
                        viewModel.onEvent(
                            AddPropertyEvent.UpdateTags(
                                park = parkChecked,
                                school = schoolChecked,
                                shop = shopChecked,
                                transport = transportChecked
                            )
                        )
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

                    onNavigateToListDetails()
                }
            }) {
                Text(text = "Save contact")
            }
            Spacer(modifier = Modifier.height(16.dp))

        }
        IconButton(
            onClick = {
                onNavigateToListDetails()
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