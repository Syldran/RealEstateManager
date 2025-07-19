package com.ocproject.realestatemanager.presentation.scene.propertylist

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ocproject.realestatemanager.core.Filter
import com.ocproject.realestatemanager.core.Order
import com.ocproject.realestatemanager.core.SortType
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsEvent
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsViewModel
import com.ocproject.realestatemanager.presentation.scene.propertylist.components.PropertyListItem
import com.ocproject.realestatemanager.presentation.scene.propertylist.components.PropertyFilterSheet
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyListScreen(
    viewModel: ListDetailsViewModel = koinViewModel(),
    onClick: (property: Property?) -> Unit,
) {
    val state by viewModel.state.collectAsState()

    Row {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
        ) {


            item {
                Column {
                    Text(
                        text = "List of properties (${state.sortedProperties.size})",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box {
                            Row {
                                Text("$ ")
                                Image(
                                    Icons.Default.KeyboardArrowUp,
                                    contentDescription = "Price Ascendant",
                                    Modifier.clickable {
                                        viewModel.onEvent(
                                            ListDetailsEvent.UpdateFilter(
                                                filter = Filter(
                                                    SortType.PRICE,
                                                    Order.ASC,
                                                    state.orderDate,
                                                    state.orderSurface,
                                                    state.rangePrice,
                                                    state.rangeDate,
                                                    state.soldRangeDate,
                                                    state.rangeSurface,
                                                    state.soldStatus,
                                                    state.schoolTag,
                                                    state.parkTag,
                                                    state.shopTag,
                                                    state.transportTag,
                                                    state.chosenAreaCode,
                                                    state.minNbrPhotos,
                                                )
                                            )
                                        )
                                    }
                                )
                                Image(
                                    Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Price Descendant",
                                    Modifier.clickable {
                                        viewModel.onEvent(
                                            ListDetailsEvent.UpdateFilter(
                                                filter = Filter(
                                                    SortType.PRICE,
                                                    Order.DESC,
                                                    state.orderDate,
                                                    state.orderSurface,
                                                    state.rangePrice,
                                                    state.rangeDate,
                                                    state.soldRangeDate,
                                                    state.rangeSurface,
                                                    state.soldStatus,
                                                    state.schoolTag,
                                                    state.parkTag,
                                                    state.shopTag,
                                                    state.transportTag,
                                                    state.chosenAreaCode,
                                                    state.minNbrPhotos,
                                                )
                                            )
                                        )
                                    }
                                )

                            }

                        }
                        VerticalDivider()
                        Box {
                            Row {
                                Text(" Date ")
                                Image(
                                    Icons.Default.KeyboardArrowUp,
                                    contentDescription = "Date Ascendant",
                                    Modifier.clickable {
                                        viewModel.onEvent(
                                            ListDetailsEvent.UpdateFilter(
                                                filter = Filter(
                                                    SortType.DATE,
                                                    state.orderPrice,
                                                    Order.ASC,
                                                    state.orderSurface,
                                                    state.rangePrice,
                                                    state.rangeDate,
                                                    state.soldRangeDate,
                                                    state.rangeSurface,
                                                    state.soldStatus,
                                                    state.schoolTag,
                                                    state.parkTag,
                                                    state.shopTag,
                                                    state.transportTag,
                                                    state.chosenAreaCode,
                                                    state.minNbrPhotos,
                                                )
                                            )
                                        )
                                    }
                                )
                                Image(
                                    Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Date Descendant",
                                    Modifier.clickable {
                                        viewModel.onEvent(
                                            ListDetailsEvent.UpdateFilter(
                                                filter = Filter(
                                                    SortType.DATE,
                                                    state.orderPrice,
                                                    Order.DESC,
                                                    state.orderSurface,
                                                    state.rangePrice,
                                                    state.rangeDate,
                                                    state.soldRangeDate,
                                                    state.rangeSurface,
                                                    state.soldStatus,
                                                    state.schoolTag,
                                                    state.parkTag,
                                                    state.shopTag,
                                                    state.transportTag,
                                                    state.chosenAreaCode,
                                                    state.minNbrPhotos,
                                                )
                                            )
                                        )
                                    }
                                )

                            }
                        }
                        VerticalDivider()
                        Box {
                            Row {
                                Text(" Surface ")
                                Image(
                                    Icons.Default.KeyboardArrowUp,
                                    contentDescription = "Surface Ascendant",
                                    modifier = Modifier.clickable {
                                        viewModel.onEvent(
                                            ListDetailsEvent.UpdateFilter(
                                                filter = Filter(
                                                    SortType.AREA,
                                                    state.orderPrice,
                                                    state.orderDate,
                                                    Order.ASC,
                                                    state.rangePrice,
                                                    state.rangeDate,
                                                    state.soldRangeDate,
                                                    state.rangeSurface,
                                                    state.soldStatus,
                                                    state.schoolTag,
                                                    state.parkTag,
                                                    state.shopTag,
                                                    state.transportTag,
                                                    state.chosenAreaCode,
                                                    state.minNbrPhotos,
                                                )
                                            )
                                        )
                                    }
                                )
                                Image(
                                    Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Surface Descendant",
                                    modifier = Modifier.clickable {
                                        viewModel.onEvent(
                                            ListDetailsEvent.UpdateFilter(
                                                filter = Filter(
                                                    SortType.AREA,
                                                    state.orderPrice,
                                                    state.orderDate,
                                                    Order.DESC,
                                                    state.rangePrice,
                                                    state.rangeDate,
                                                    state.soldRangeDate,
                                                    state.rangeSurface,
                                                    state.soldStatus,
                                                    state.schoolTag,
                                                    state.parkTag,
                                                    state.shopTag,
                                                    state.transportTag,
                                                    state.chosenAreaCode,
                                                    state.minNbrPhotos,
                                                )
                                            )
                                        )
                                    }
                                )

                            }
                        }

                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )

                }


            }

            items(
                items = state.sortedProperties,
                key = { property ->
                    property.id
                }) { property: Property? ->
                PropertyListItem(
                    viewModel = viewModel,
                    propertyWithPhotos = property!!,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onClick(property)
                        }
                        .padding(start = 16.dp, end = 16.dp),
                    onEvent = viewModel::onEvent,
                )
            }
        }
        VerticalDivider()
    }
    when {
        state.isFilterSheetOpen -> {
            PropertyFilterSheet(
                state = state,
                onEvent = viewModel::onEvent,
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
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
                Text(text = "ERROR Data", color = Color.Red)

            }
        }
    }
}

