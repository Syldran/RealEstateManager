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
import androidx.compose.runtime.key
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
                        viewModel.onEvent(ListDetailsEvent.UpdateSelectedProperty(property))
                        Timber.tag("SELECTED_ListDetailspri").d("${state.selectedProperty?.id}")
                        navigator.navigateTo(
                            ListDetailPaneScaffoldRole.Detail,
                            content = property
                        )
                        Timber.tag("SELECTED_ListDetails").d("${state.selectedProperty?.id}")
                    },
                )
            },
            detailPane = {
                AnimatedPane {
                    if (navigator.currentDestination?.content != null) {
                        Timber.tag("SELECTED_ListDetails2").d("${state.selectedProperty?.id}")
                        if (state.mapMode == false) {
                            PropertyDetailScreen(
                                navigateBack = {
                                    navigator.navigateBack()
                                },
                                onNavigateToAddPropertyScreen = {
                                    onNavigateToAddPropertyScreen(
                                        state.selectedProperty?.id
                                    )
                                },
                            )
                        }
                    }
                    else {
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

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarouselExample() {
    data class CarouselItem(
        val id: Int,
        @DrawableRes val imageResId: Int,
        val contentDescription: String
    )

    val carouselItems = remember {
        listOf(
            CarouselItem(0, R.drawable.cupcake, "cupcake"),
            CarouselItem(1, R.drawable.donut, "donut"),
            CarouselItem(2, R.drawable.eclair, "eclair"),
            CarouselItem(3, R.drawable.froyo, "froyo"),
            CarouselItem(4, R.drawable.gingerbread, "gingerbread"),
        )
    }

    HorizontalUncontainedCarousel(
        state = rememberCarouselState { carouselItems.count() },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 16.dp, bottom = 16.dp),
        itemWidth = 186.dp,
        itemSpacing = 8.dp,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) { i ->
        val item = carouselItems[i]
        Image(
            modifier = Modifier
                .height(205.dp)
                .maskClip(MaterialTheme.shapes.extraLarge),
            painter = painterResource(id = item.imageResId),
            contentDescription = item.contentDescription,
            contentScale = ContentScale.Crop
        )
    }
}
*/