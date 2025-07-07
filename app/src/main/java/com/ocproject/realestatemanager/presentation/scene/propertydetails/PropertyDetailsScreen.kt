package com.ocproject.realestatemanager.presentation.scene.propertydetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ocproject.realestatemanager.BuildConfig
import com.ocproject.realestatemanager.core.InterestPoint
import com.ocproject.realestatemanager.presentation.scene.propertydetails.components.PagerPhotoDetails
import timber.log.Timber


@Composable
fun PropertyDetailScreen(
    viewModel: PropertyDetailsViewModel,
    propertyId: Long,
    navigateBack: () -> Unit,
    onNavigateToAddPropertyScreen: (propertyId: Long?) -> Unit,
) {
    val property = viewModel.selectedProperty

    // Mettre à jour l'ID de la propriété dans le ViewModel
    LaunchedEffect(propertyId) {
        viewModel.updatePropertyId(propertyId)
    }
    Box(
        modifier = Modifier
            .background(Color.Red)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .statusBarsPadding(),
//        contentAlignment = Alignment.TopStart
    ) {
        IconButton(
            onClick = navigateBack
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back"
            )
        }
        Column(
            modifier = Modifier
                .background(Color.Blue)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (property == null) {

                Text("Select or Add Property")

            } else {

                Spacer(Modifier.height(60.dp))
                Timber.tag("TEST").d("${property.photoList?.size}")
                Timber.tag("TEST1").d("${property.sold}")
                PagerPhotoDetails(property = property)
//                PhotosDetailsComposable(
//                    propertyWithPhotos = property
//                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "${property.address} ${property.town}",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )
                Spacer(Modifier.height(16.dp))
                Row {
                    if (property.sold != null) {
                        SuggestionChip(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            onClick = {},
                            label = { Text("SOLD") },
                        )
                    }

                    property.interestPoints.forEach {
                        when (it) {
                            InterestPoint.PARK -> {
                                SuggestionChip(
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    onClick = {},
                                    label = { Text("Park") },
                                )
                            }

                            InterestPoint.SCHOOL -> {

                                SuggestionChip(
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    onClick = {},
                                    label = { Text("School") },
                                )
                            }

                            InterestPoint.SHOP -> {
                                SuggestionChip(
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    onClick = { },
                                    label = { Text("Shop") },
                                )
                            }

                            InterestPoint.TRANSPORT -> {
                                SuggestionChip(
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    onClick = {},
                                    label = { Text("Transport") },
                                )
                            }
                        }
                    }

                }
                Spacer(Modifier.height(16.dp))
                FilledTonalIconButton(
                    onClick = { onNavigateToAddPropertyScreen(property.id) },
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Edit,
                        contentDescription = "Edit contact"
                    )
                }
                Text(text = property.address)
                Spacer(Modifier.height(16.dp))
                Text(text = property.town)
                Spacer(Modifier.height(16.dp))

                AsyncImage(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxSize(),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(
                            "https://maps.googleapis.com/maps/api/staticmap?center=${property.lat},${property.lng}&markers=color:red|${property.lat},${property.lng}&zoom=14&size=500x500&scale=2&key=${BuildConfig.PLACES_API_KEY}"
                        ).build(), contentDescription = ""
                )
            }


        }
    }
}