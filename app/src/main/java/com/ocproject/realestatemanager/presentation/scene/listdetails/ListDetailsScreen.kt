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
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.presentation.scene.map.MapOfProperties
import com.ocproject.realestatemanager.presentation.scene.propertydetails.PropertyDetailScreen
import com.ocproject.realestatemanager.presentation.scene.propertydetails.PropertyDetailsViewModel
import com.ocproject.realestatemanager.presentation.scene.propertylist.PropertyListScreen
import com.ocproject.realestatemanager.presentation.scene.propertylist.components.PropertyListTopBar
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
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
//    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    val navigator = rememberListDetailPaneScaffoldNavigator<Property>()
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
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
                PropertyListScreen(
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
                            PropertyDetailScreen(
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
                /*
                AnimatedPane {
                    if (navigator.currentDestination?.content != null) {
                        navigator.currentDestination?.content?.let { property: Any ->
                            var property = property as Property?
                            if (state.mapMode == false) {
//                                key(state.selectedProperty) {
//                                    val propertyDetailsViewModel =
//                                        koinViewModel<PropertyDetailsViewModel>(
//                                            parameters = { parametersOf(property?.id) }
//                                        )
                                    PropertyDetailScreen(
//                                        viewModel = propertyDetailsViewModel,
                                        propertyId = property?.id ?: -1,
                                        navigateBack = {
                                            navigator.navigateBack()
                                        },
                                        onNavigateToAddPropertyScreen = {
                                            onNavigateToAddPropertyScreen(
                                                property?.id
                                            )
                                        },
                                    )
//                                }
                            } else {
                                // possible création des markers ici en amount de MapOfProperties.
                                MapOfProperties(
                                    currentPosition = currentPosition,
                                    focusPosition = if (false) {
                                        null
                                    } else {
                                        LatLng(property?.lat ?: 0.0, property?.lng ?: 0.0)
                                    }
                                )
                            }


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
                }*/
            },
        )
    }
}