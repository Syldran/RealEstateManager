@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.ocproject.realestatemanager.presentation.scene.listdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.presentation.scene.listdetails.components.PropertyList
import com.ocproject.realestatemanager.presentation.scene.map.MapOfProperties
import com.ocproject.realestatemanager.presentation.scene.listdetails.components.PropertyDetails
import com.ocproject.realestatemanager.presentation.scene.listdetails.components.PropertyListTopBar
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun ListDetails(
    viewModel: ListDetailsViewModel = koinViewModel(),
    onNavigateToAddPropertyScreen: (propertyId: Long?) -> Unit,
    onNavigateToMapOfProperties: () -> Unit,
    onNavigateToFundingScreen: () -> Unit,
    currentPosition: LatLng?,
) {

    val state by viewModel.state.collectAsState()
    val navigator = rememberListDetailPaneScaffoldNavigator<Property>()
    val scope = rememberCoroutineScope()
    LaunchedEffect(state) {
        viewModel.onEvent(ListDetailsEvent.GetProperties)
    }
    Column {
        PropertyListTopBar(
            state = state,
            onEvent = viewModel::onEvent,
            onNavigateToAddPropertyScreen = onNavigateToAddPropertyScreen,
            onNavigateToMapOfProperties = onNavigateToMapOfProperties,
            onNavigateToFundingScreen = onNavigateToFundingScreen,
            modifier = Modifier
        )
        NavigableListDetailPaneScaffold(
            navigator = navigator,
            listPane = {
                PropertyList(
                    onClick = { property ->

                        viewModel.onEvent(ListDetailsEvent.UpdateSelectedProperty(property))
                        scope.launch {
                            navigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail,
                                property
                            )
                        }
                    },
                )
            },
            detailPane = {
                AnimatedPane {
                    if (navigator.currentDestination?.contentKey != null) {
                        Timber.tag("SELECTED_ListDetails2").d("${state.selectedProperty?.id}")
                        if (!state.mapMode) {
                            PropertyDetails(
                                navigateBack = {
                                    scope.launch {
                                        navigator.navigateBack()
                                    }
                                },
                                onNavigateToAddPropertyScreen = {
                                    onNavigateToAddPropertyScreen(
                                        state.selectedProperty?.id
                                    )
                                },
                            )
                        } else {
                            MapOfProperties(
                                currentPosition = currentPosition,
                                focusPosition = if (false) {
                                    null
                                } else {
                                    LatLng(
                                        state.selectedProperty?.lat ?: 0.0,
                                        state.selectedProperty?.lng ?: 0.0
                                    )
                                }
                            )
                        }
                    } else {
                        Text(
                            text = "Add or select a Property.",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            },
        )
    }
}