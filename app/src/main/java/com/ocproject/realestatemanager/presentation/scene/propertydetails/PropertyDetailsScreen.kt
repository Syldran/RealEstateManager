package com.ocproject.realestatemanager.presentation.scene.propertydetails

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowBack
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ocproject.realestatemanager.BuildConfig
import com.ocproject.realestatemanager.core.InterestPoint
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.domain.repositories.PropertiesRepository
import com.ocproject.realestatemanager.domain.usecases.GetPropertyDetailsUseCase
import com.ocproject.realestatemanager.presentation.scene.propertydetails.components.PhotosDetailsComposable
import com.ocproject.realestatemanager.presentation.sharedcomponents.PropertyPhoto
import org.koin.android.annotation.KoinViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PropertyDetailScreen(
    property: Property,
    navigateBack: () -> Unit,
    onNavigateToAddPropertyScreen: (propertyId: Long?) -> Unit,
) {
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
                propertyWithPhotos = property
            )
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
                if (property.sold == true) {
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

        IconButton(
            onClick = navigateBack
//            {
//                onNavigateToPropertyListScreen()
//            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back"
            )
        }
    }
}

// superposition d'image
//@Composable
//fun ImageWithTopRightText() {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(250.dp) // Adjust height as needed
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.sold),
//            contentDescription = "Your Image",
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.fillMaxSize()
//        )
//
//        Text(
//            text = "Top Right Text",
//            color = Color.White,
//            fontSize = 16.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier
//                .align(Alignment.TopEnd)
//                .padding(8.dp) // Adds some space from edges
//        )
//    }
//}