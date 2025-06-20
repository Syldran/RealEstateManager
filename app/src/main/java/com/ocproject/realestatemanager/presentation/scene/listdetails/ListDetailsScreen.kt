@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.ocproject.realestatemanager.presentation.scene.listdetails

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.presentation.scene.map.MapOfProperties
import com.ocproject.realestatemanager.presentation.scene.propertydetails.PropertyDetailScreen
import com.ocproject.realestatemanager.presentation.scene.propertylist.PropertyListScreen
import com.ocproject.realestatemanager.presentation.scene.propertylist.components.PropertyListTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListDetails(
    viewModel: ListDetailsViewModel = koinViewModel(),
    onNavigateToAddPropertyScreen: (propertyId: Long?) -> Unit,
    onNavigateToMapOfProperties: () -> Unit,
    onNavigateToFundingScreen: () -> Unit,
    currentPosition: LatLng?,
) {

    val state by viewModel.state.collectAsState()
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    LaunchedEffect(Unit) {
        viewModel.onEvent(ListDetailsEvent.GetProperties(viewModel._filter.value))
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
//                navigator.navigateTo(
//                    ListDetailPaneScaffoldRole.Detail,
//                    content = null
//                )
                PropertyListScreen(
                    onClick = { property ->
                        navigator.navigateTo(
                            ListDetailPaneScaffoldRole.Detail,
                            content = property
                        )
                    },
                )
            },
            detailPane = {
                AnimatedPane {
                    if (navigator.currentDestination?.content != null) {
                        navigator.currentDestination?.content?.let { property: Any ->
                            var property = property as Property
                            Box {

                                if (state.mapMode == false) {
                                    PropertyDetailScreen(
                                        property = property,
                                        navigateBack = {
                                            navigator.navigateBack()
                                        },
                                        onNavigateToAddPropertyScreen = {
                                            onNavigateToAddPropertyScreen(
                                                property.id
                                            )
                                        },
                                    )
                                } else {
                                    // possible création des markers ici en amont de MapOfProperties.
                                    MapOfProperties(
                                        currentPosition = currentPosition,
                                        focusPosition = if (property == null) {
                                            null
                                        } else {
                                            LatLng(property.lat, property.lng)
                                        }
                                    )
                                }

                            }
                        }
                    }else {
                        Box {

                            Text (text = "Add or select a Property.", modifier = Modifier.fillMaxSize().padding(8.dp), textAlign = TextAlign.Center)
                        }
                    }
                }
            },
        )
    }
}