@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.ocproject.realestatemanager.presentation.scene.listdetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsEvent.OnClickPropertyDisplayMode
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
    Column {
        PropertyListTopBar(
            onEvent = viewModel::onEvent,
            onNavigateToAddPropertyScreen = onNavigateToAddPropertyScreen,
            onNavigateToMapOfProperties = onNavigateToMapOfProperties,
            onNavigateToFundingScreen = onNavigateToFundingScreen,
            modifier = Modifier
        )

        NavigableListDetailPaneScaffold(
            navigator = navigator,
            listPane = {
                // if list affiche liste if map affiche map
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
                    navigator.currentDestination?.content?.let { property ->
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
                                    focusPosition = LatLng(property.lat, property.lng)
                                )
                            }
                            FloatingActionButton(
                                modifier = Modifier.padding(16.dp),
                                onClick = {
                                    viewModel.onEvent(OnClickPropertyDisplayMode(map = !state.mapMode))
                                },
                            ) {
                                if (state.mapMode) {
                                    Icon(
                                        imageVector = Icons.Rounded.Info,
                                        contentDescription = "detailMode"
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Rounded.LocationOn,
                                        contentDescription = "mapMode"
                                    )
                                }
                            }
                        }
                    }
                }
            },
        )
    }

}
