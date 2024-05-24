package com.ocproject.realestatemanager_remade.properties.presentation.scene.propertylist.components

import android.util.Range
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ocproject.realestatemanager.models.Filter
import com.ocproject.realestatemanager.models.Order
import com.ocproject.realestatemanager.models.SortType
import com.ocproject.realestatemanager.presentation.scene.propertylist.PropertyListEvent
import com.ocproject.realestatemanager.presentation.scene.propertylist.PropertyListState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyFilterSheet(
    state: PropertyListState,
    onEvent: (PropertyListEvent) -> Unit,
    sheetState: SheetState,
    scope: CoroutineScope,
) {
    var schoolChecked by remember { mutableStateOf(false) }
    var parkChecked by remember { mutableStateOf(false) }
    var transportChecked by remember { mutableStateOf(false) }
    var shopChecked by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = {
            scope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    onEvent(PropertyListEvent.DismissFilter)
                }
            }
        },
        sheetState = sheetState
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(Modifier) {
                    // RADIO SORT TYPE PRICE
                    RadioButton(
                        modifier = Modifier.weight(2f),
                        selected = state.sortType == SortType.PRICE,
                        onClick = {
                            onEvent(
                                PropertyListEvent.SortProperties(
                                    filter = Filter(
                                        SortType.PRICE,
                                        state.orderPrice,
                                        state.orderDate,
                                        state.rangePrice,
                                        state.rangeDate
                                    )
                                )
                            )
                        }
                    )
                    Text(text = SortType.PRICE.name, Modifier.padding(end = 16.dp))
                    //RADIO PRICE ASC
                    RadioButton(
                        modifier = Modifier.weight(1f),
                        selected = state.orderPrice == Order.ASC,
                        onClick = {
                            onEvent(
                                PropertyListEvent.SortProperties(
                                    filter = Filter(
                                        state.sortType,
                                        Order.ASC,
                                        state.orderDate,
                                        state.rangePrice,
                                        state.rangeDate
                                    )
                                )
                            )
                        }
                    )
                    Text(text = Order.ASC.name, Modifier.padding(end = 16.dp))
                    //RADIO PRICE DESC
                    RadioButton(
                        modifier = Modifier.weight(1f),
                        selected = state.orderPrice == Order.DESC,
                        onClick = {
                            onEvent(
                                PropertyListEvent.SortProperties(
                                    filter = Filter(
                                        state.sortType,
                                        Order.DESC,
                                        state.orderDate,
                                        state.rangePrice,
                                        state.rangeDate
                                    )
                                )
                            )
                        }
                    )
                    Text(text = Order.DESC.name, Modifier.padding(end = 16.dp))
                }

                var sliderPosition by remember { mutableStateOf(state.rangePrice.lower.toFloat()..state.rangePrice.upper.toFloat()) }

                RangeSlider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    value = sliderPosition,
                    steps = 0,
                    onValueChange = { range ->
                        sliderPosition = range
                    },
                    valueRange = 0F..Int.MAX_VALUE.toFloat(),
                    onValueChangeFinished = {
                        onEvent(PropertyListEvent.SetRangePrice(Range<Float>(sliderPosition.start, sliderPosition.endInclusive)))
                    },
                )
                Text(text = "min: ${state.rangePrice.lower}    max:${state.rangePrice.upper}")
                HorizontalDivider(modifier = Modifier.padding(16.dp))
                Row {
                    // RADIO SORT TYPE DATE
                    RadioButton(
                        modifier = Modifier.weight(2f),
                        selected = state.sortType == SortType.DATE,
                        onClick = {
                            onEvent(
                                PropertyListEvent.SortProperties(
                                    filter = Filter(
                                        SortType.DATE,
                                        state.orderPrice,
                                        state.orderDate,
                                        state.rangePrice,
                                        state.rangeDate
                                    )
                                )
                            )
                        }
                    )
                    Text(text = SortType.DATE.name, Modifier.padding(end = 16.dp))
                    //RADIO DATE ASC
                    RadioButton(
                        modifier = Modifier.weight(1f),
                        selected = state.orderDate == Order.ASC,
                        onClick = {
                            onEvent(
                                PropertyListEvent.SortProperties(
                                    filter = Filter(
                                        state.sortType,
                                        state.orderPrice,
                                        Order.ASC,
                                        state.rangePrice,
                                        state.rangeDate
                                    )
                                )
                            )
                        }
                    )
                    Text(text = Order.ASC.name, Modifier.padding(end = 16.dp))
                    // RADIO DATE DESC
                    RadioButton(
                        modifier = Modifier.weight(1f),
                        selected = state.orderDate == Order.DESC,
                        onClick = {
                            onEvent(
                                PropertyListEvent.SortProperties(
                                    filter = Filter(
                                        state.sortType,
                                        state.orderPrice,
                                        Order.DESC,
                                        state.rangePrice,
                                        state.rangeDate
                                    )
                                )
                            )
                        }
                    )
                    Text(text = Order.DESC.name, Modifier.padding(end = 16.dp))
                }
                DateRangePicker(
                    state = rememberDateRangePickerState(
                        initialDisplayMode = DisplayMode.Input,
                    ),

                )

                HorizontalDivider(modifier = Modifier.padding(16.dp))
                Row {
                    FilterChip(
                        modifier = Modifier.padding(4.dp),
                        onClick = {
                            schoolChecked = !schoolChecked
//                            onEvent(AddPropertyEvent.OnSchoolChecked(schoolChecked))
                        },
                        label = {
                            Text("School")
                        },
                        selected = schoolChecked,
                        leadingIcon = if (schoolChecked) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else {
                            null
                        },
                    )

                    FilterChip(
                        modifier = Modifier.padding(4.dp),
                        onClick = {
                            parkChecked = !parkChecked
//                            viewModel.onEvent(AddPropertyEvent.OnParkChecked(parkChecked))
                        },
                        label = {
                            Text("Park")
                        },
                        selected = parkChecked,
                        leadingIcon = if (parkChecked) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else {
                            null
                        },
                    )

                    FilterChip(
                        modifier = Modifier.padding(4.dp),
                        onClick = {
                            shopChecked = !shopChecked
//                            viewModel.onEvent(AddPropertyEvent.OnShopChecked(shopChecked))
                        },
                        label = {
                            Text("Shop")
                        },
                        selected = shopChecked,
                        leadingIcon = if (shopChecked) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else {
                            null
                        },
                    )

                    FilterChip(
                        modifier = Modifier.padding(4.dp),
                        onClick = {
                            transportChecked = !transportChecked
//                            viewModel.onEvent(AddPropertyEvent.OnTransportChecked(transportChecked))
                        },
                        label = {
                            Text("Transport")
                        },
                        selected = transportChecked,
                        leadingIcon = if (transportChecked) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else {
                            null
                        },
                    )
                }

            }
            IconButton(
                onClick = {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onEvent(PropertyListEvent.DismissFilter)
                        }
                    }
                },
                colors = IconButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    disabledContentColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Icon(imageVector = Icons.Rounded.Close, contentDescription = "Close")
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun Preview() {
    PropertyFilterSheet(
        state = PropertyListState(),
        onEvent = {},
        sheetState = SheetState(initialValue = SheetValue.Expanded, skipPartiallyExpanded = true),
        scope = rememberCoroutineScope()
    )

}