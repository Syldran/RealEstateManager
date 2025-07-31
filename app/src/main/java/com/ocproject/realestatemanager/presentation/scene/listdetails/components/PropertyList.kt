package com.ocproject.realestatemanager.presentation.scene.listdetails.components

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ocproject.realestatemanager.R
import com.ocproject.realestatemanager.core.InterestPoint
import com.ocproject.realestatemanager.core.Order
import com.ocproject.realestatemanager.core.SortType
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsEvent
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsState
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PropertyList(
    viewModel: ListDetailsViewModel = koinViewModel(),
    onClick: (property: Property?) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    PropertyListContent(
        state = state,
        onEvent = viewModel::onEvent,
        onClick = onClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyListContent(
    state: ListDetailsState,
    onClick: (Property?) -> Unit,
    onEvent: (ListDetailsEvent) -> Unit
) {
    Row {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
        ) {
            item {
                Column {
                    Text(
                        text = stringResource(
                            R.string.list_of_properties,
                            state.sortedProperties.size
                        ),
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                        fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box {
                            Row {
                                Text(stringResource(R.string.money_symbol))
                                Icon(
                                    Icons.Default.KeyboardArrowUp,
                                    contentDescription = stringResource(R.string.price_ascendant),
                                    Modifier.clickable {
                                        onEvent(
                                            ListDetailsEvent.UpdateFilter(
                                                filter = state.filterState.copy(
                                                    sortType = SortType.PRICE,
                                                    priceOrder = Order.ASC,
                                                )
                                            )
                                        )
                                    }
                                )
                                Icon(
                                    Icons.Default.KeyboardArrowDown,
                                    contentDescription = stringResource(R.string.price_descendant),
                                    Modifier.clickable {
                                        onEvent(
                                            ListDetailsEvent.UpdateFilter(
                                                filter = state.filterState.copy(
                                                    sortType = SortType.PRICE,
                                                    priceOrder = Order.DESC,
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
                                Text(
                                    "Date ",
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                                )
                                Icon(
                                    Icons.Default.KeyboardArrowUp,
                                    contentDescription = stringResource(R.string.date_ascendant),
                                    Modifier.clickable {
                                        onEvent(
                                            ListDetailsEvent.UpdateFilter(
                                                filter = state.filterState.copy(
                                                    sortType = SortType.DATE,
                                                    dateOrder = Order.ASC,
                                                )
                                            )
                                        )
                                    }
                                )
                                Icon(
                                    Icons.Default.KeyboardArrowDown,
                                    contentDescription = stringResource(R.string.date_descendant),
                                    Modifier.clickable {
                                        onEvent(
                                            ListDetailsEvent.UpdateFilter(
                                                filter = state.filterState.copy(
                                                    sortType = SortType.DATE,
                                                    dateOrder = Order.DESC,
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
                                Icon(
                                    Icons.Default.KeyboardArrowUp,
                                    contentDescription = stringResource(R.string.surface_ascendant),
                                    modifier = Modifier.clickable {
                                        if (state.filterState.sortType != SortType.AREA || state.filterState.surfaceOrder != Order.ASC) {
                                            onEvent(
                                                ListDetailsEvent.UpdateFilter(
                                                    filter = state.filterState.copy(
                                                        sortType = SortType.AREA,
                                                        surfaceOrder = Order.ASC,
                                                    )
                                                )
                                            )
                                        }
                                    }
                                )
                                Icon(
                                    Icons.Default.KeyboardArrowDown,
                                    contentDescription = stringResource(R.string.surface_descendant),
                                    modifier = Modifier.clickable {
                                        if (state.filterState.sortType != SortType.AREA || state.filterState.surfaceOrder != Order.DESC) {
                                            onEvent(
                                                ListDetailsEvent.UpdateFilter(
                                                    filter = state.filterState.copy(
                                                        sortType = SortType.AREA,
                                                        surfaceOrder = Order.DESC,
                                                    )
                                                )
                                            )
                                        }
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

            if (state.sortedProperties.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (state.isLoadingProgressBar) stringResource(R.string.loading) else stringResource(
                                R.string.no_properties_found
                            ),
                            color = Color.Gray,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                            fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                        )
                    }
                }
            } else {
                items(
                    items = state.sortedProperties,
                    key = { property ->
                        property.id
                    }
                ) { property: Property ->
                    PropertyListItem(
                        property = property,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onClick(property)
                            }
                            .padding(start = 16.dp, end = 16.dp),
                        onEvent = onEvent,
                    )
                }
            }
        }
        VerticalDivider()
    }


}

@Preview
@Composable
fun MyListPreview() {
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
            sold = null,
        ), Property(
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
        )
    )
    val statePreview = ListDetailsState(sortedProperties = propertyList)
//    val statePreview = ListDetailsState(sortedProperties = emptyList(), isLoadingProgressBar = true)
    PropertyListContent(
        state = statePreview,
        onClick = {},
        onEvent = {}
    )
}

