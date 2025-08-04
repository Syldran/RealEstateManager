package com.ocproject.realestatemanager.presentation.scene.listdetails.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ocproject.realestatemanager.BuildConfig
import com.ocproject.realestatemanager.R
import com.ocproject.realestatemanager.core.InterestPoint
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsEvent
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsState
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun PropertyDetails(
    viewModel: ListDetailsViewModel = koinViewModel(),
    navigateBack: () -> Unit,
    onNavigateToAddPropertyScreen: (propertyId: Long?) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(state.selectedProperty?.id) {
        state.selectedProperty?.let { property ->
            viewModel.onEvent(ListDetailsEvent.GetDetails(property.id))
        }
    }
    val configuration = LocalConfiguration.current
    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        // Layout pour paysage
        PropertyDetailsLandscape(
            state = state,
            navigateBack,
            onNavigateToAddPropertyScreen
        )
    } else {
        // Layout pour portrait
        PropertyDetailsPortrait(
            state = state,
            navigateBack,
            onNavigateToAddPropertyScreen
        )
    }

}

@Composable
fun PropertyDetailsPortrait(
    state: ListDetailsState,
    navigateBack: () -> Unit,
    onNavigateToAddPropertyScreen: (propertyId: Long?) -> Unit
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .statusBarsPadding(),
        contentAlignment = Alignment.TopStart
    ) {

        Column(
            modifier = Modifier
//                .background(Color.Blue)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                FilledTonalIconButton(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    onClick =
                        {
                            navigateBack()
                        },
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
//                    tint = Color.Green
                    )
                }
                FilledTonalIconButton(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    onClick = {
                        onNavigateToAddPropertyScreen(state.selectedProperty!!.id)
                    },
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Edit,
                        contentDescription = "Edit property"
                    )
                }
            }
            Text(
                "Media",
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                fontStyle = MaterialTheme.typography.headlineSmall.fontStyle
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                PhotosComposable(property = state.selectedProperty!!)
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            Column {
                Text(
                    "Description",
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontStyle = MaterialTheme.typography.headlineSmall.fontStyle
                )
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = state.selectedProperty!!.description,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                )
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(0.5F)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            painter = painterResource(R.drawable.baseline_settings_overscan_24),
                            contentDescription = "Surface Icon",
                        )
                        Column {
                            Text(
                                "Surface :",
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                            )
                            Text(
                                "${state.selectedProperty?.surfaceArea}",
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            painter = painterResource(R.drawable.baseline_attach_money_24),
                            contentDescription = "Price Icon",
                        )
                        Column {
                            Text(
                                "Price :",
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                            )
                            Text(
                                "${state.selectedProperty?.price} $",
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            painter = painterResource(R.drawable.outline_add_home_24),
                            contentDescription = "Date Added Icon",
                        )
                        Column {
                            Text(
                                "Date added :",
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                            )
                            Text(
                                "${dateFormat.format(state.selectedProperty?.createdDate)}",
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                            )
                        }
                    }

                }
                VerticalDivider(Modifier.padding(8.dp))
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(0.5F)
                ) {

                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            modifier = Modifier.padding(4.dp),
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "Location Icon",
                        )
                        Column {
                            Text(
                                "Location :",
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                            )
                            Text(
                                state.selectedProperty?.address ?: "",
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                            )
                            Text(
                                text = state.selectedProperty?.areaCode?.toString() ?: "0",
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                            )
                            Text(
                                state.selectedProperty?.town ?: "",
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                            )
                            Text(
                                state.selectedProperty?.country ?: "",
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                            )
                        }
                    }
                    if (state.selectedProperty?.sold != -1L) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.padding(horizontal = 4.dp),
                                painter = painterResource(R.drawable.outline_sell_24),
                                contentDescription = "Surface Icon",
                            )
                            Column {
                                Text(
                                    "Date Sold :",
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                                )
                                Text(
                                    "${dateFormat.format(state.selectedProperty?.sold)}",
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                state.selectedProperty?.interestPoints?.forEach {
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
            HorizontalDivider(Modifier.padding(vertical = 16.dp))

            AsyncImage(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(
                        "https://maps.googleapis.com/maps/api/staticmap?center=${state.selectedProperty?.lat},${state.selectedProperty!!.lng}&markers=color:red|${state.selectedProperty.lat},${state.selectedProperty.lng}&zoom=14&size=500x500&scale=2&key=${BuildConfig.PLACES_API_KEY}"
                    ).build(), contentDescription = ""
            )


        }
    }
}

@Composable
fun PropertyDetailsLandscape(
    state: ListDetailsState,
    navigateBack: () -> Unit,
    onNavigateToAddPropertyScreen: (propertyId: Long?) -> Unit
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .statusBarsPadding(),
        contentAlignment = Alignment.TopStart
    ) {

        Column(
            modifier = Modifier
//                .background(Color.Blue)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                FilledTonalIconButton(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    onClick = {
                        onNavigateToAddPropertyScreen(state.selectedProperty!!.id)
                    },
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Edit,
                        contentDescription = "Edit property"
                    )
                }
            }
            Text(
                "Media",
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                fontStyle = MaterialTheme.typography.headlineSmall.fontStyle
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                PhotosComposable(property = state.selectedProperty!!)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            Column {
                Text(
                    "Description",
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontStyle = MaterialTheme.typography.headlineSmall.fontStyle
                )
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = state.selectedProperty!!.description,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                )
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(0.5F)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Surface Icon",
                        )
                        Column {
                            Text(
                                "Surface :",
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                            )
                            Text(
                                "${state.selectedProperty?.surfaceArea}",
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            painter = painterResource(R.drawable.baseline_attach_money_24),
                            contentDescription = "Price Icon",
                        )
                        Column {
                            Text(
                                "Price :",
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                            )
                            Text(
                                "${state.selectedProperty?.price} $",
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            painter = painterResource(R.drawable.outline_add_home_24),
                            contentDescription = "Date Added Icon",
                        )
                        Column {
                            Text(
                                "Date added :",
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                            )
                            Text(
                                "${dateFormat.format(state.selectedProperty?.createdDate)}",
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                            )
                        }
                    }
                }
                VerticalDivider(Modifier.padding(8.dp))
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(0.5F)
                ) {

                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            modifier = Modifier.padding(4.dp),
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "Location Icon",
                        )
                        Column {
                            Text(
                                "Location :",
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                            )
                            Text(
                                state.selectedProperty?.address ?: "",
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                            )
                            Text(
                                state.selectedProperty?.town ?: "",
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                            )
                            Text(
                                state.selectedProperty?.country ?: "",
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                            )
                        }
                    }
                    if (state.selectedProperty?.sold != -1L) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.padding(horizontal = 4.dp),
                                painter = painterResource(R.drawable.outline_sell_24),
                                contentDescription = "Surface Icon",
                            )
                            Column {
                                Text(
                                    "Date Sold :",
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                                )
                                Text(
                                    "${dateFormat.format(state.selectedProperty?.sold)}",
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                state.selectedProperty?.interestPoints?.forEach {
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
            HorizontalDivider(Modifier.padding(vertical = 16.dp))

            AsyncImage(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(
                        "https://maps.googleapis.com/maps/api/staticmap?center=${state.selectedProperty?.lat},${state.selectedProperty!!.lng}&markers=color:red|${state.selectedProperty.lat},${state.selectedProperty.lng}&zoom=14&size=500x500&scale=2&key=${BuildConfig.PLACES_API_KEY}"
                    ).build(), contentDescription = ""
            )


        }
    }
}

@Preview
@Composable
fun PropertyDetailsPreview() {
    val sampleProperty = Property(
        id = 1L,
        photoList = emptyList(),
        interestPoints = listOf(InterestPoint.PARK, InterestPoint.SCHOOL),
        description = "A property for preview.",
        address = "123 Main St",
        town = "SampleTown",
        lat = 37.7749,
        lng = -122.4194,
        country = "SampleCountry",
        createdDate = 0L,
        price = 150000,
        surfaceArea = 150,
        areaCode = 18290,
        sold = 1000L,
    )
    val sampleState = ListDetailsState(selectedProperty = sampleProperty)
    PropertyDetailsPortrait(
        state = sampleState,
        navigateBack = {},
        onNavigateToAddPropertyScreen = {}
    )
}