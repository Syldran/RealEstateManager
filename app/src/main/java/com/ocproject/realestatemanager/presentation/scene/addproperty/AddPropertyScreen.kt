package com.ocproject.realestatemanager.presentation.scene.addproperty

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ocproject.realestatemanager.core.InterestPoint
import com.ocproject.realestatemanager.presentation.scene.addproperty.components.AutocompleteSearch
import com.ocproject.realestatemanager.presentation.scene.addproperty.components.PhotosComposable
import com.ocproject.realestatemanager.presentation.scene.addproperty.components.PropertyTextField
import com.ocproject.realestatemanager.presentation.scene.addproperty.utils.ImagePicker
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar
import com.ocproject.realestatemanager.R

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPropertyScreen(
    viewModel: AddPropertyViewModel = koinViewModel(),
    onNavigateToListDetails: () -> Unit,
    onNavigateToCamera: ((ByteArray) -> Unit) -> Unit,
    navController: NavController,
) {
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val imagePicker = ImagePicker(
        context as ComponentActivity
    )
    imagePicker.RegisterPickerMulti {
        viewModel.onEvent(AddPropertyEvent.OnPhotoPicked(it))
    }

    val state by viewModel.state.collectAsState()

    // State for BottomSheet
    var showPhotoOptionsBottomSheet by remember { mutableStateOf(false) }


    // Manage captured photo from camera
    fun handlePhotoCaptured(photoBytes: ByteArray) {
        viewModel.addPhotoFromCamera(photoBytes)
    }

    // Listen for captured photos from camera screen
    LaunchedEffect(Unit) {
        // Check for captured photo in saved state handle
        navController.currentBackStackEntry?.savedStateHandle?.get<ByteArray>("photo_captured")
            ?.let { photoBytes ->
                handlePhotoCaptured(photoBytes)
                // Clear the saved state
                navController.currentBackStackEntry?.savedStateHandle?.remove<ByteArray>("photo_captured")
            }
    }

    // Global toast handling is now managed at the app level

    var schoolChecked by remember { mutableStateOf(false) }
    var parkChecked by remember { mutableStateOf(false) }
    var transportChecked by remember { mutableStateOf(false) }
    var shopChecked by remember { mutableStateOf(false) }
    state.newProperty.interestPoints.forEach {
        when (it) {
            InterestPoint.SCHOOL -> {
                schoolChecked = true
            }

            InterestPoint.PARK -> {
                parkChecked = true
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
        onNavigateToListDetails()
        viewModel.onEvent(AddPropertyEvent.OnChangeNavigationStatus(true))
    }

    Scaffold(
        modifier = Modifier.padding(horizontal = 16.dp),
        containerColor = colorResource(id = R.color.white),
        contentWindowInsets = WindowInsets.safeDrawing, // Applies safe area to Scaffold content
        // Global SnackbarHost is handled in RealEstateManagerApp.kt
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))


            // Display already added photos
            PhotosComposable(
                property = state.newProperty.copy(photoList = state.photoList),
                viewModel = viewModel,
            )

            // Add photos button
            Button(
                onClick = { showPhotoOptionsBottomSheet = true },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(stringResource(R.string.add_a_photo))
            }

            // BottomSheet for photos source.
            if (showPhotoOptionsBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showPhotoOptionsBottomSheet = false },
                    containerColor = colorResource(id = R.color.white)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.choose_a_photo),
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        ListItem(
                            headlineContent = { Text(stringResource(R.string.take_a_photo)) },
                            leadingContent = {
                                Icon(
                                    painter = painterResource(R.drawable.outline_camera_24),
                                    contentDescription = stringResource(R.string.camera)
                                )
                            },
                            modifier = Modifier.clickable {
                                showPhotoOptionsBottomSheet = false
                                onNavigateToCamera { photoBytes ->
                                    handlePhotoCaptured(photoBytes)
                                }
                            }
                        )

                        ListItem(
                            headlineContent = { Text(stringResource(R.string.choose_from_gallery)) },
                            leadingContent = {
                                Icon(
                                    painter = painterResource(R.drawable.outline_photo_library_24),
                                    contentDescription = stringResource(R.string.gallery)
                                )
                            },
                            modifier = Modifier.clickable {
                                showPhotoOptionsBottomSheet = false
                                imagePicker.pickMultiImage()
                            }
                        )

                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }


            Spacer(modifier = Modifier.height(16.dp))
            AutocompleteSearch(viewModel, scope)
            PropertyTextField(
                modifier = Modifier.padding(8.dp),
                value = state.newProperty.address,
                error = state.addressError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.UpdateForm(address = it))
                },
                keyboardType = KeyboardType.Text,
                labelValue = stringResource(R.string.address),
            )
            PropertyTextField(
                modifier = Modifier.padding(8.dp),
                value = state.newProperty.town,
                error = state.townError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.UpdateForm(town = it))

                },
                keyboardType = KeyboardType.Text,
                labelValue = stringResource(R.string.town)
            )
            PropertyTextField(
                modifier = Modifier.padding(8.dp),
                value = if (state.newProperty.areaCode == 0) "" else state.newProperty.areaCode.toString(),
                error = state.areaCodeError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.UpdateForm(areaCode = it))
                },
                keyboardType = KeyboardType.Number,
                labelValue = stringResource(R.string.area_code)
            )
            PropertyTextField(
                modifier = Modifier.padding(8.dp),
                value = state.newProperty.country,
                error = state.countryError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.UpdateForm(country = it))
                },
                keyboardType = KeyboardType.Text,
                labelValue = stringResource(R.string.country)
            )
            PropertyTextField(
                modifier = Modifier.padding(8.dp),
                value = if (state.latitudeInput.isNotEmpty()) state.latitudeInput else if (state.newProperty.lat == 0.0) "" else state.newProperty.lat.toString(),
                error = state.latError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.UpdateLatitudeInput(it))
                },
                keyboardType = KeyboardType.Decimal,
                labelValue = stringResource(R.string.latitude)
            )
            PropertyTextField(
                modifier = Modifier.padding(8.dp),
                value = if (state.longitudeInput.isNotEmpty()) state.longitudeInput else if (state.newProperty.lng == 0.0) "" else state.newProperty.lng.toString(),
                error = state.lngError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.UpdateLongitudeInput(it))
                },
                keyboardType = KeyboardType.Decimal,
                labelValue = stringResource(R.string.longitude)
            )
            PropertyTextField(
                modifier = Modifier.padding(8.dp),
                value = if (state.newProperty.price == 0) "" else state.newProperty.price.toString(),
                error = state.priceError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.UpdateForm(price = it))
                },
                keyboardType = KeyboardType.Number,
                labelValue = stringResource(R.string.price)
            )
            PropertyTextField(
                modifier = Modifier.padding(8.dp),
                value = if (state.newProperty.surfaceArea == 0) "" else state.newProperty.surfaceArea.toString(),
                error = state.surfaceAreaError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.UpdateForm(surfaceArea = it))
                },
                keyboardType = KeyboardType.Number,
                labelValue = stringResource(R.string.surface_area)
            )

            PropertyTextField(
                modifier = Modifier.padding(8.dp),
                value = state.newProperty.description,
                error = state.descriptionError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.UpdateForm(description = it))
                },
                keyboardType = KeyboardType.Text,
                labelValue = stringResource(R.string.description),
            )
            PropertyTextField(
                modifier = Modifier.padding(8.dp),
                value = state.newProperty.type,
                error = state.typeError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.UpdateForm(type = it))
                },
                keyboardType = KeyboardType.Text,
                labelValue = stringResource(R.string.type),
            )
            PropertyTextField(
                modifier = Modifier.padding(8.dp),
                value = if (state.newProperty.nbrRoom == 0) "" else state.newProperty.nbrRoom.toString(),
                error = state.nbrRoomError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.UpdateForm(nbrRoom = it))
                },
                keyboardType = KeyboardType.Number,
                labelValue = stringResource(R.string.number_of_rooms),
            )
            PropertyTextField(
                modifier = Modifier.padding(8.dp),
                value = state.newProperty.realEstateAgent,
                error = state.realEstateAgentError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.UpdateForm(realEstateAgent = it))
                },
                keyboardType = KeyboardType.Text,
                labelValue = stringResource(R.string.real_estate_agent),
            )

            Row {
                FilterChip(
                    modifier = Modifier.padding(4.dp),
                    onClick = {
                        schoolChecked = !schoolChecked
                        viewModel.onEvent(
                            AddPropertyEvent.UpdateTags(
                                school = schoolChecked,
                                park = parkChecked,
                                shop = shopChecked,
                                transport = transportChecked
                            )
                        )

                    },
                    label = {
                        Text(stringResource(R.string.school))
                    },
                    selected = schoolChecked,
                    leadingIcon = if (schoolChecked) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = stringResource(R.string.done_icon_content_description),
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
                                school = schoolChecked,
                                park = parkChecked,
                                shop = shopChecked,
                                transport = transportChecked
                            )
                        )
                    },
                    label = {
                        Text(stringResource(R.string.park))
                    },
                    selected = parkChecked,
                    leadingIcon = if (parkChecked) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = stringResource(R.string.done_icon_content_description),
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
                                school = schoolChecked,
                                park = parkChecked,
                                shop = shopChecked,
                                transport = transportChecked
                            )
                        )

                    },
                    label = {
                        Text(stringResource(R.string.shop))
                    },
                    selected = shopChecked,
                    leadingIcon = if (shopChecked) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = stringResource(R.string.done_icon_content_description),
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
                                school = schoolChecked,
                                park = parkChecked,
                                shop = shopChecked,
                                transport = transportChecked
                            )
                        )
                    },
                    label = {
                        Text(stringResource(R.string.transport))
                    },
                    selected = transportChecked,
                    leadingIcon = if (transportChecked) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = stringResource(R.string.done_icon_content_description),
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    },
                )
            }

            Row {
                FilterChip(
                    modifier = Modifier.padding(4.dp),
                    onClick = {

                        viewModel.onEvent(AddPropertyEvent.UpdateSoldState(!state.soldState))
                        if (state.soldState) {
                            viewModel.onEvent(
                                AddPropertyEvent.UpdateNewProperty(
                                    state.newProperty.copy(
                                        sold = -1
                                    )
                                )
                            )
                        } else {
                            viewModel.onEvent(
                                AddPropertyEvent.UpdateNewProperty(
                                    state.newProperty.copy(
                                        sold = Calendar.getInstance().timeInMillis
                                    )
                                )
                            )
                        }

                    },
                    label = {
                        Text(stringResource(R.string.sold))
                    },
                    selected = state.soldState,
                    leadingIcon = if (state.soldState) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = stringResource(R.string.done_icon_content_description),
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    },
                )
            }


            val successMessage = stringResource(R.string.property_saved_successfully)
            val failureMessage = stringResource(R.string.property_save_failed)
            Button(onClick = {
                viewModel.onEvent(
                    AddPropertyEvent.SaveProperty(
                        successMessage = successMessage,
                        failureMessage = failureMessage
                    )
                )
                if (state.navToPropertyListScreen) {
                    onNavigateToListDetails()
                    viewModel.onEvent(AddPropertyEvent.OnChangeNavigationStatus(true))
                }
            }) {
                Text(text = stringResource(R.string.save_property))
            }
            Spacer(modifier = Modifier.height(16.dp))

        }
        IconButton(
            modifier = Modifier.padding(top = 32.dp),
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
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = stringResource(R.string.close)
            )
        }
    }
}