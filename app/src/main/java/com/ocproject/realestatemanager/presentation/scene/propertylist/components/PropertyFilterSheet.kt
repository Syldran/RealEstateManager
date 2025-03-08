package com.ocproject.realestatemanager.presentation.scene.propertylist.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.Close
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ocproject.realestatemanager.core.Filter
import com.ocproject.realestatemanager.core.Order
import com.ocproject.realestatemanager.core.SellingStatus
import com.ocproject.realestatemanager.core.SortType
import com.ocproject.realestatemanager.core.utils.Range
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
                Row {
                    Text(
                        "Selling Status"
                    )
                }
                Row {
                    Text(
                        "Purchasable"
                    )
                    RadioButton(
                        modifier = Modifier.weight(2f),
                        selected = state.soldState == SellingStatus.PURCHASABLE,
                        onClick = {
                            onEvent(
                                PropertyListEvent.GetProperties(
                                    filter = Filter(
                                        SortType.PRICE,
                                        state.orderPrice,
                                        state.orderDate,
                                        state.orderSurface,
                                        state.rangePrice,
                                        state.rangeDate,
                                        state.rangeSurface,
                                        SellingStatus.PURCHASABLE,
                                        state.schoolState,
                                        state.parkState,
                                        state.shopState,
                                        state.transportState,
                                    )
                                )
                            )
                        }
                    )
                    Text(text = "Sold", Modifier.padding(end = 16.dp))
                    RadioButton(
                        modifier = Modifier.weight(1f),
                        selected = state.soldState == SellingStatus.SOLD,
                        onClick = {
                            onEvent(
                                PropertyListEvent.GetProperties(
                                    filter = Filter(
                                        state.sortType,
                                        state.orderPrice,
                                        state.orderDate,
                                        state.orderSurface,
                                        state.rangePrice,
                                        state.rangeDate,
                                        state.rangeSurface,
                                        SellingStatus.SOLD,
                                        state.schoolState,
                                        state.parkState,
                                        state.shopState,
                                        state.transportState,
                                    )
                                )
                            )
                        }
                    )
                    Text(text = "All", Modifier.padding(end = 16.dp))
                    RadioButton(
                        modifier = Modifier.weight(1f),
                        selected = state.soldState == SellingStatus.ALL,
                        onClick = {
                            onEvent(
                                PropertyListEvent.GetProperties(
                                    filter = Filter(
                                        state.sortType,
                                        state.orderPrice,
                                        state.orderDate,
                                        state.orderSurface,
                                        state.rangePrice,
                                        state.rangeDate,
                                        state.rangeSurface,
                                        SellingStatus.ALL,
                                        state.schoolState,
                                        state.parkState,
                                        state.shopState,
                                        state.transportState,
                                    )
                                )
                            )
                        }
                    )
                }


                Row(Modifier) {
                    // RADIO SORT TYPE PRICE
                    RadioButton(
                        modifier = Modifier.weight(2f),
                        selected = state.sortType == SortType.PRICE,
                        onClick = {
                            onEvent(
                                PropertyListEvent.GetProperties(
                                    filter = Filter(
                                        SortType.PRICE,
                                        state.orderPrice,
                                        state.orderDate,
                                        state.orderSurface,
                                        state.rangePrice,
                                        state.rangeDate,
                                        state.rangeSurface,
                                        state.soldState,
                                        state.schoolState,
                                        state.parkState,
                                        state.shopState,
                                        state.transportState,
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
                                PropertyListEvent.GetProperties(
                                    filter = Filter(
                                        state.sortType,
                                        Order.ASC,
                                        state.orderDate,
                                        state.orderSurface,
                                        state.rangePrice,
                                        state.rangeDate,
                                        state.rangeSurface,
                                        state.soldState,
                                        state.schoolState,
                                        state.parkState,
                                        state.shopState,
                                        state.transportState,
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
                                PropertyListEvent.GetProperties(
                                    filter = Filter(
                                        state.sortType,
                                        Order.DESC,
                                        state.orderDate,
                                        state.orderSurface,
                                        state.rangePrice,
                                        state.rangeDate,
                                        state.rangeSurface,
                                        state.soldState,
                                        state.schoolState,
                                        state.parkState,
                                        state.shopState,
                                        state.transportState,
                                    )
                                )
                            )
                        }
                    )
                    Text(text = Order.DESC.name, Modifier.padding(end = 16.dp))
                }

                var maxPrice = state.maxPrice
                var sliderMax = if (maxPrice > state.rangePrice.upper) {
                    state.rangePrice.upper
                } else {
                    maxPrice
                }
                var sliderPosition by remember { mutableStateOf(state.rangePrice.lower.toFloat()..sliderMax.toFloat()) }
                RangeSlider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    value = sliderPosition,
                    steps = 0,
                    onValueChange = { range ->
                        sliderPosition = range
                    },
                    valueRange = 0F..maxPrice.toFloat(),
                    onValueChangeFinished = {
                        onEvent(
                            PropertyListEvent.SetRangePrice(
                                Range<Float>(
                                    sliderPosition.start,
                                    sliderPosition.endInclusive
                                )
                            )
                        )
                    },
                )
                Text(text = "min: ${state.rangePrice.lower}    max:${sliderMax}")
                HorizontalDivider(modifier = Modifier.padding(16.dp))
                Row {
                    // RADIO SORT TYPE DATE
                    RadioButton(
                        modifier = Modifier.weight(2f),
                        selected = state.sortType == SortType.DATE,
                        onClick = {
                            onEvent(
                                PropertyListEvent.GetProperties(
                                    filter = Filter(
                                        SortType.DATE,
                                        state.orderPrice,
                                        state.orderDate,
                                        state.orderSurface,
                                        state.rangePrice,
                                        state.rangeDate,
                                        state.rangeSurface,
                                        state.soldState,
                                        state.schoolState,
                                        state.parkState,
                                        state.shopState,
                                        state.transportState,
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
                                PropertyListEvent.GetProperties(
                                    filter = Filter(
                                        state.sortType,
                                        state.orderPrice,
                                        Order.ASC,
                                        state.orderSurface,
                                        state.rangePrice,
                                        state.rangeDate,
                                        state.rangeSurface,
                                        state.soldState,
                                        state.schoolState,
                                        state.parkState,
                                        state.shopState,
                                        state.transportState,
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
                                PropertyListEvent.GetProperties(
                                    filter = Filter(
                                        state.sortType,
                                        state.orderPrice,
                                        Order.DESC,
                                        state.orderSurface,
                                        state.rangePrice,
                                        state.rangeDate,
                                        state.rangeSurface,
                                        state.soldState,
                                        state.schoolState,
                                        state.parkState,
                                        state.schoolState,
                                        state.transportState,
                                    )
                                )
                            )
                        }
                    )
                    Text(text = Order.DESC.name, Modifier.padding(end = 16.dp))
                }
                HorizontalDivider(modifier = Modifier.padding(16.dp))

                Row(Modifier) {
                    // RADIO SORT TYPE SURFACE
                    RadioButton(
                        modifier = Modifier.weight(2f),
                        selected = state.sortType == SortType.SURFACE,
                        onClick = {
                            onEvent(
                                PropertyListEvent.GetProperties(
                                    filter = Filter(
                                        SortType.SURFACE,
                                        state.orderPrice,
                                        state.orderDate,
                                        state.orderSurface,
                                        state.rangePrice,
                                        state.rangeDate,
                                        state.rangeSurface,
                                        state.soldState,
                                        state.schoolState,
                                        state.parkState,
                                        state.shopState,
                                        state.transportState,
                                    )
                                )
                            )
                        }
                    )
                    Text(text = SortType.SURFACE.name, Modifier.padding(end = 16.dp))
                    //RADIO SURFACE ASC
                    RadioButton(
                        modifier = Modifier.weight(1f),
                        selected = state.orderSurface == Order.ASC,
                        onClick = {
                            onEvent(
                                PropertyListEvent.GetProperties(
                                    filter = Filter(
                                        sortType = state.sortType,
                                        orderPrice = state.orderPrice,
                                        orderDate = state.orderDate,
                                        orderSurface = Order.ASC,
                                        rangePrice = state.rangePrice,
                                        rangeDate = state.rangeDate,
                                        rangeSurface = state.rangeSurface,
                                        sellingStatus = state.soldState,
                                        tagSchool = state.schoolState,
                                        tagTransport = state.parkState,
                                        tagShop = state.shopState,
                                        tagPark = state.transportState,
                                    )
                                )
                            )
                        }
                    )
                    Text(text = Order.ASC.name, Modifier.padding(end = 16.dp))
                    //RADIO SURFACE DESC
                    RadioButton(
                        modifier = Modifier.weight(1f),
                        selected = state.orderSurface == Order.DESC,
                        onClick = {
                            onEvent(
                                PropertyListEvent.GetProperties(
                                    filter = Filter(
                                        sortType = state.sortType,
                                        orderPrice = state.orderPrice,
                                        orderDate = state.orderDate,
                                        orderSurface = Order.DESC,
                                        rangePrice = state.rangePrice,
                                        rangeDate = state.rangeDate,
                                        rangeSurface = state.rangeSurface,
                                        sellingStatus = state.soldState,
                                        tagSchool = state.schoolState,
                                        tagTransport = state.parkState,
                                        tagShop = state.shopState,
                                        tagPark = state.transportState,
                                    )
                                )
                            )
                        }
                    )
                    Text(text = Order.DESC.name, Modifier.padding(end = 16.dp))
                }

                var maxSurface = state.maxSurface
                var surfaceSliderMax = if (maxSurface > state.rangeSurface.upper) {
                    state.rangeSurface.upper
                } else {
                    maxSurface
                }
                var surfaceSliderPosition by remember { mutableStateOf(state.rangeSurface.lower.toFloat()..surfaceSliderMax.toFloat()) }
                RangeSlider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    value = surfaceSliderPosition,
                    steps = 0,
                    onValueChange = { range ->
                        surfaceSliderPosition = range
                    },
                    valueRange = 0F..maxSurface.toFloat(),
                    onValueChangeFinished = {
                        onEvent(
                            PropertyListEvent.SetRangeSurface(
                                Range<Float>(
                                    surfaceSliderPosition.start,
                                    surfaceSliderPosition.endInclusive
                                )
                            )
                        )
                    },
                )
                Text(text = "min: ${state.rangeSurface.lower}    max:${surfaceSliderMax}")
                HorizontalDivider(modifier = Modifier.padding(16.dp))

                /*
                // ajouter validation de la date sur le textfield focus
                DateRangePicker(
//                    showModeToggle = false,
                    state = rememberDateRangePickerState(
                        initialDisplayMode = DisplayMode.Input,
                    ),

                    )
*/
                HorizontalDivider(modifier = Modifier.padding(16.dp))
                Row {
                    FilterChip(
                        modifier = Modifier.padding(4.dp),
                        onClick = {
                            onEvent(PropertyListEvent.OnSchoolChecked(state.schoolState))

                        },
                        label = {
                            Text("School")
                        },
                        selected = state.schoolState,
                        leadingIcon = if (state.schoolState) {
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
                            onEvent(
                                PropertyListEvent.OnParkChecked(state.parkState))
                        },
                        label = {
                            Text("Park")
                        },
                        selected = state.parkState,
                        leadingIcon = if (state.parkState) {
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
                            onEvent(
                                PropertyListEvent.OnShopChecked(state.shopState)
                            )
                        },
                        label = {
                            Text("Shop")
                        },
                        selected = state.shopState,
                        leadingIcon = if (state.shopState) {
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
                            onEvent(PropertyListEvent.OnTransportChecked(state.transportState))
                        },
                        label = {
                            Text("Transport")
                        },
                        selected = state.transportState,
                        leadingIcon = if (state.transportState) {
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


/*@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable*/
//fun Preview() {
//    PropertyFilterSheet(
//        state = PropertyListState(),
//        onEvent = {},
//        sheetState = SheetState(initialValue = SheetValue.Expanded, skipPartiallyExpanded = true),
//        scope = rememberCoroutineScope()
//    )
//
//}