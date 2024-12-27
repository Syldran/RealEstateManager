package com.ocproject.realestatemanager.presentation.scene.propertydetails

import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ocproject.realestatemanager.BuildConfig
import com.ocproject.realestatemanager.core.InterestPoint
import com.ocproject.realestatemanager.presentation.scene.propertydetails.components.PhotosDetailsComposable
import com.ocproject.realestatemanager.presentation.sharedcomponents.PropertyPhoto
import org.koin.androidx.compose.koinViewModel

@Composable
fun PropertyDetailScreen(
    viewModel: PropertyDetailsViewModel = koinViewModel(),
    onNavigateToAddPropertyScreen: (propertyId: Long?) -> Unit,
    onNavigateToPropertyListScreen: () -> Unit,
) {
    val selectedProperty = viewModel.selectedProperty
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .statusBarsPadding(),
        contentAlignment = Alignment.TopStart
    ) {



        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(60.dp))
            PhotosDetailsComposable(
                propertyWithPhotos = selectedProperty
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "${selectedProperty?.address} ${selectedProperty?.town}",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
            Spacer(Modifier.height(16.dp))
            Row {
                if (selectedProperty?.sold == true) {
                    SuggestionChip(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        onClick = {},
                        label = { Text("SOLD") },
                    )
                }

                selectedProperty?.interestPoints?.forEach {
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
                onClick = { onNavigateToAddPropertyScreen(selectedProperty?.id) },
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
            Text(text = selectedProperty?.address ?: "")
            Spacer(Modifier.height(16.dp))
            Text(text = selectedProperty?.town ?: "")
            Spacer(Modifier.height(16.dp))

            AsyncImage(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(
                        "https://maps.googleapis.com/maps/api/staticmap?center=${selectedProperty?.lat},${selectedProperty?.lng}&markers=color:red|${selectedProperty?.lat},${selectedProperty?.lng}&zoom=14&size=500x500&scale=2&key=${BuildConfig.PLACES_API_KEY}"
                    ).build(), contentDescription = ""
            )
        }

        IconButton(
            onClick = {
                onNavigateToPropertyListScreen()
            }
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = "Close"
            )
        }
    }
}