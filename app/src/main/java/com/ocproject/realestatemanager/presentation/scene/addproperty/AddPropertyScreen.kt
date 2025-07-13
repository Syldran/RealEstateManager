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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
    val snackBarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current
    val imagePicker = ImagePicker(
        context as ComponentActivity
    )
    imagePicker.RegisterPickerMulti {
        viewModel.onEvent(AddPropertyEvent.OnPhotoPicked(it))
    }

    val state by viewModel.state.collectAsState()

    // État pour la BottomSheet
    var showPhotoOptionsBottomSheet by remember { mutableStateOf(false) }

    // Fonction pour gérer la photo capturée depuis la caméra
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

//    var soldChecked by remember { mutableStateOf(false) }
    var schoolChecked by remember { mutableStateOf(false) }
    var parkChecked by remember { mutableStateOf(false) }
    var transportChecked by remember { mutableStateOf(false) }
    var shopChecked by remember { mutableStateOf(false) }
    state.newProperty.interestPoints.forEach {
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
//    soldChecked = if (state.newProperty.sold != null) {
//        true
//    } else {
//        false
//    }

    if (state.navToPropertyListScreen) {
        onNavigateToListDetails()
        viewModel.onEvent(AddPropertyEvent.OnChangeNavigationStatus(true))
    }

    Scaffold(
        modifier = Modifier.padding(horizontal = 16.dp),
        containerColor = colorResource(id = R.color.white),
        contentWindowInsets = WindowInsets.safeDrawing, // Applies safe area to Scaffold content
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
            /*.nestedScroll(nestedScrollConnection)*/,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Affichage des photos déjà ajoutées
            PhotosComposable(
                property = state.newProperty.copy(photoList = state.photoList),
                viewModel = viewModel,
//                modifier = Modifier
//                    .clickable {
//                        showPhotoOptionsBottomSheet = true
//                    },
            )

            // Bouton "Ajouter une photo"
            Button(
                onClick = { showPhotoOptionsBottomSheet = true },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Ajouter une photo")
            }

            // BottomSheet pour les options de photo
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
                            text = "Choisir une photo",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        ListItem(
                            headlineContent = { Text("Prendre une photo") },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Filled.Call,
                                    contentDescription = "Camera"
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
                            headlineContent = { Text("Choisir depuis la galerie") },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "Gallery"
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
            AutocompleteSearch(viewModel, scope, snackBarHostState)
            Spacer(modifier = Modifier.height(16.dp))
            PropertyTextField(
                value = state.newProperty.address,
                error = state.addressError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.UpdateForm(address = it))
                },
                keyboardType = KeyboardType.Text,
                labelValue = "Address",
            )
            Spacer(modifier = Modifier.height(16.dp))
            PropertyTextField(
                value = state.newProperty?.town ?: "",
                error = state.townError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.UpdateForm(town = it))

                },
                keyboardType = KeyboardType.Text,
                labelValue = "Town"
            )
            Spacer(modifier = Modifier.height(16.dp))
            PropertyTextField(
                value = if (state.newProperty.areaCode == null || state.newProperty.areaCode == 0) "" else state.newProperty.areaCode.toString(),
                error = state.areaCodeError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.UpdateForm(areaCode = it))
                },
                keyboardType = KeyboardType.Number,
                labelValue = "Area Code"
            )
            Spacer(modifier = Modifier.height(16.dp))
            PropertyTextField(
                value = state.newProperty?.country ?: "",
                error = state.countryError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.UpdateForm(country = it))
                },
                keyboardType = KeyboardType.Text,
                labelValue = "Country"
            )
            Spacer(modifier = Modifier.height(16.dp))
            PropertyTextField(
                value = if (state.newProperty.price == null || state.newProperty.price == 0) "" else state.newProperty.price.toString(),
                error = state.priceError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.UpdateForm(price = it))
                },
                keyboardType = KeyboardType.Number,
                labelValue = "Price"
            )
            Spacer(modifier = Modifier.height(16.dp))

            PropertyTextField(
                value = if (state.newProperty.surfaceArea == null || state.newProperty.surfaceArea == 0) "" else state.newProperty.surfaceArea.toString(),
                error = state.surfaceAreaError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.UpdateForm(surfaceArea = it))
                },
                keyboardType = KeyboardType.Number,
                labelValue = "Surface Area"
            )
            Spacer(modifier = Modifier.height(16.dp))
            PropertyTextField(
                value = if (state.newProperty.lat == 0.0) "" else state.newProperty.lat.toString(),
                error = state.latError,
                onValueChanged = {
                    viewModel.onEvent(AddPropertyEvent.UpdateForm(latitude = it))
                },
                keyboardType = KeyboardType.Decimal,
                labelValue = "Latitude"
            )
            Spacer(modifier = Modifier.height(16.dp))
            PropertyTextField(
                value = if (state.newProperty.lng == 0.0) "" else state.newProperty.lng.toString(),
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

            Row {
                FilterChip(
                    modifier = Modifier.padding(4.dp),
                    onClick = {

                        viewModel.onEvent(AddPropertyEvent.UpdateSoldState(!state.soldState))
                        if (state.soldState) {
                            viewModel.onEvent(
                                AddPropertyEvent.UpdateNewProperty(
                                    state.newProperty.copy(
                                        sold = null
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
                        Text("SOLD!")
                    },
                    selected = state.soldState,
                    leadingIcon = if (state.soldState) {
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
            Icon(imageVector = Icons.Rounded.Close, contentDescription = "Close")
        }
    }
}