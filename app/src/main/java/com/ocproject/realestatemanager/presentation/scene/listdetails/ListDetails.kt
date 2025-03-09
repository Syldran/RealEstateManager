@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.ocproject.realestatemanager.presentation.scene.listdetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.LatLng
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.presentation.scene.propertydetails.PropertyDetailScreen
import com.ocproject.realestatemanager.presentation.scene.propertylist.PropertyListScreen
import com.ocproject.realestatemanager.presentation.scene.propertylist.PropertyListViewModel
import com.ocproject.realestatemanager.presentation.scene.propertylist.components.PropertyListTopBar
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ListDetails(
    viewModel: PropertyListViewModel = koinViewModel(),
    onNavigateToAddPropertyScreen: (propertyId: Long?) -> Unit,
    onNavigateToMapOfProperties: (/*currentPosition: LatLng*/) -> Unit,
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    Column {
        PropertyListTopBar(
            onEvent = viewModel::onEvent,
            onNavigateToAddPropertyScreen = onNavigateToAddPropertyScreen,
            onNavigateToMapOfProperties = onNavigateToMapOfProperties,
            modifier = Modifier
        )
        NavigableListDetailPaneScaffold(
            navigator = navigator,
            listPane = {
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
                        PropertyDetailScreen(
                            property = property as Property,
                            navigateBack = {
                                navigator.navigateBack()
                            },
                            onNavigateToAddPropertyScreen = {onNavigateToAddPropertyScreen(property.id)},
                        )

                    }
                }
            },
        )
    }
}