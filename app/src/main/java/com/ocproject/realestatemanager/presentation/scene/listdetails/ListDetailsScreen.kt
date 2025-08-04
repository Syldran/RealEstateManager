@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.ocproject.realestatemanager.presentation.scene.listdetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.ocproject.realestatemanager.R
import com.ocproject.realestatemanager.core.Filter
import com.ocproject.realestatemanager.core.InterestPoint
import com.ocproject.realestatemanager.core.Order
import com.ocproject.realestatemanager.core.SellingStatus
import com.ocproject.realestatemanager.core.SortType
import com.ocproject.realestatemanager.core.utils.Range
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.presentation.scene.listdetails.components.PropertyDetails
import com.ocproject.realestatemanager.presentation.scene.listdetails.components.PropertyFilterSheet
import com.ocproject.realestatemanager.presentation.scene.listdetails.components.PropertyList
import com.ocproject.realestatemanager.presentation.scene.listdetails.components.PropertyListTopBar
import com.ocproject.realestatemanager.presentation.scene.map.MapOfProperties
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListDetails(
    viewModel: ListDetailsViewModel = koinViewModel(),
    onNavigateToAddPropertyScreen: (propertyId: Long?) -> Unit,
    onNavigateToMapOfProperties: () -> Unit,
    onNavigateToFundingScreen: () -> Unit,
    currentPosition: LatLng?,
    globalSnackbarHostState: SnackbarHostState,
) {

    val state by viewModel.state.collectAsState()


    LaunchedEffect(state.properties, state.filterState) {
        viewModel.onEvent(ListDetailsEvent.GetPropertiesFiltered(state.filterState))
    }

    ListDetailsContent(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateToAddPropertyScreen = onNavigateToAddPropertyScreen,
        onNavigateToMapOfProperties = onNavigateToMapOfProperties,
        onNavigateToFundingScreen = onNavigateToFundingScreen,
        currentPosition = currentPosition,
        globalSnackbarHostState = globalSnackbarHostState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListDetailsContent(
    state: ListDetailsState,
    onEvent: (ListDetailsEvent) -> Unit,
    onNavigateToAddPropertyScreen: (Long?) -> Unit,
    onNavigateToMapOfProperties: () -> Unit,
    onNavigateToFundingScreen: () -> Unit,
    currentPosition: LatLng?,
    globalSnackbarHostState: SnackbarHostState,
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Property>()
    val scope = rememberCoroutineScope()
    Column {
        PropertyListTopBar(
            state = state,
            onEvent = onEvent,
            onNavigateToAddPropertyScreen = onNavigateToAddPropertyScreen,
            onNavigateToMapOfProperties = onNavigateToMapOfProperties,
            onNavigateToFundingScreen = onNavigateToFundingScreen,
            modifier = Modifier
        )
        NavigableListDetailPaneScaffold(
            navigator = navigator,
            listPane = {
                Box(
                    modifier = Modifier.weight(1 / 4F)
                ) {
                    PropertyList(
                        onClick = { property ->
                            onEvent(ListDetailsEvent.UpdateSelectedProperty(property))

                            scope.launch {
                                navigator.navigateTo(
                                    ListDetailPaneScaffoldRole.Detail,
                                    property
                                )
                            }
                        },
                    )
                }
            },
            detailPane = {
                AnimatedPane {
                    Box(
                        modifier = Modifier.weight(3 / 4F)
                    ) {
                        val property = state.selectedProperty
                        if (property != null) {
                            if (!state.mapMode) {
                                PropertyDetails(
                                    navigateBack = {
                                        scope.launch {
                                            navigator.navigateBack()
                                        }
                                    },
                                    onNavigateToAddPropertyScreen = {
                                        onNavigateToAddPropertyScreen(
                                            property.id
                                        )
                                    },
                                )
                            } else {
                                MapOfProperties(
                                    currentPosition = currentPosition,
                                    focusPosition = LatLng(
                                        property.lat,
                                        property.lng,
                                    ),
                                    globalSnackbarHostState = globalSnackbarHostState
                                )
                            }
                        } else if (!state.mapMode) {

                            Text(
                                text = stringResource(R.string.empty_list_details_screen_msg),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                textAlign = TextAlign.Center
                            )
                        } else {
                            MapOfProperties(
                                currentPosition = currentPosition,
                                focusPosition = currentPosition,
                                globalSnackbarHostState = globalSnackbarHostState
                            )
                        }
                    }
                }
            },
        )
        when {
            state.isFilterSheetOpen -> {
                PropertyFilterSheet(
                    state = state,
                    onEvent = onEvent,
                    scope = rememberCoroutineScope(),
                )
            }

            state.isLoadingProgressBar -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.Red)
                }
            }

            state.isError -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = stringResource(R.string.error_data), color = Color.Red)
                }
            }
        }
    }
}

@Preview
@Composable
fun ListDetailsPreview() {
    // Preview doesn't need globalSnackbarHostState
    val mockSnackbarHostState = androidx.compose.material3.SnackbarHostState()
    val propertyList = listOf(
        Property(
            id = 1L,
            photoList = emptyList(),
            interestPoints = listOf(),
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
            type = "House",
            nbrRoom = 3,
            realEstateAgent = "John Doe",
        ),
        Property(
            id = 2L,
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
            sold = -1,
            type = "Apartment",
            nbrRoom = 2,
            realEstateAgent = "Jane Smith",
        ),         Property(
            id = 3L,
            photoList = emptyList(),
            interestPoints = listOf(InterestPoint.SCHOOL),
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
            sold = 2500L,
            type = "Studio",
            nbrRoom = 1,
            realEstateAgent = "Bob Wilson",
        )
    )

    val statePreview = ListDetailsState(
        properties = propertyList,
        mapMode = false,
        filterState = Filter(
            sortType = SortType.PRICE,
            priceOrder = Order.ASC,
            dateOrder = Order.ASC,
            surfaceOrder = Order.ASC,
            priceRange = Range(0, Int.MAX_VALUE),
            dateRange = Range(0L, Calendar.getInstance().timeInMillis + 12583060),
            soldDateRange = Range(0L, Calendar.getInstance().timeInMillis + 12583060),
            surfaceRange = Range(0, Int.MAX_VALUE),
            sellingStatus = SellingStatus.ALL,
            tagSchool = false,
            tagTransport = false,
            tagShop = false,
            tagPark = false,
            areaCodeFilter =  null,
            typeHousing = null,
            minNbrPhotos = 0,
        ),
        areaCodeList = emptyList()
    )
    ListDetailsContent(
        state = statePreview,
        onEvent = {},
        onNavigateToAddPropertyScreen = {},
        onNavigateToMapOfProperties = {},
        onNavigateToFundingScreen = {},
        currentPosition = LatLng(5.2, 5.8),
        globalSnackbarHostState = mockSnackbarHostState
    )
}