package com.ocproject.realestatemanager.presentation.scene.propertylist.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.modifier.modifierLocalProvider
import androidx.compose.ui.unit.dp
import com.ocproject.realestatemanager.core.Filter
import com.ocproject.realestatemanager.core.Order
import com.ocproject.realestatemanager.core.SellingStatus
import com.ocproject.realestatemanager.core.SortType
import com.ocproject.realestatemanager.core.utils.Range
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsEvent
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyFilterSheet(
    state: ListDetailsState,
    onEvent: (ListDetailsEvent) -> Unit,
    sheetState: SheetState,
    scope: CoroutineScope,
) {

    ModalBottomSheet(
        onDismissRequest = {
            scope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    onEvent(ListDetailsEvent.DismissFilter)
                }
            }
        },
        sheetState = sheetState
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopStart
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
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
                                    ListDetailsEvent.GetProperties(
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
                                            state.chosenAreaCode,
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
                                    ListDetailsEvent.GetProperties(
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
                                            state.chosenAreaCode,
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
                                    ListDetailsEvent.GetProperties(
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
                                            state.chosenAreaCode,
                                        )
                                    )
                                )
                            }
                        )
                    }
                }
                Card(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) {
                    Row(Modifier) {
                        // RADIO SORT TYPE PRICE
                        RadioButton(
                            modifier = Modifier.weight(2f),
                            selected = state.sortType == SortType.PRICE,
                            onClick = {
                                onEvent(
                                    ListDetailsEvent.GetProperties(
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
                                            state.chosenAreaCode,
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
                                    ListDetailsEvent.GetProperties(
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
                                            state.chosenAreaCode,
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
                                    ListDetailsEvent.GetProperties(
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
                                            state.chosenAreaCode,
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
                                ListDetailsEvent.SetRangePrice(
                                    Range<Float>(
                                        sliderPosition.start,
                                        sliderPosition.endInclusive
                                    )
                                )
                            )
                        },
                    )
                    Text(text = "min: ${state.rangePrice.lower}    max:${sliderMax}")
                }

                Card(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) {
                    Column() {
                        Row {
                            // RADIO SORT TYPE DATE
                            RadioButton(
                                modifier = Modifier.weight(2f),
                                selected = state.sortType == SortType.DATE,
                                onClick = {
                                    onEvent(
                                        ListDetailsEvent.GetProperties(
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
                                                state.chosenAreaCode,
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
                                        ListDetailsEvent.GetProperties(
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
                                                state.chosenAreaCode,
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
                                        ListDetailsEvent.GetProperties(
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
                                                state.chosenAreaCode,
                                            )
                                        )
                                    )
                                }
                            )
                            Text(text = Order.DESC.name, Modifier.padding(end = 16.dp))
                        }
                        val dateRangePickerState = rememberDateRangePickerState(
                            initialDisplayMode = DisplayMode.Input,
                            initialSelectedStartDateMillis = state.rangeDate.lower,
                            initialSelectedEndDateMillis = state.rangeDate.upper
                        )
                        DateRangePicker(
//                    showModeToggle = false,
                            state = dateRangePickerState,
                        )
                        TextButton(
                            onClick = {
                                onEvent(
                                    ListDetailsEvent.OnDateRangeSelected(
                                        dateRangePickerState.selectedStartDateMillis!!,
                                        dateRangePickerState.selectedEndDateMillis!!
                                    )
                                )
                            }
                        ) {
                            Text("Validate Date")
                        }
                    }
                }
                Card(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) {
                    Row(Modifier) {
                        // RADIO SORT TYPE SURFACE
                        RadioButton(
                            modifier = Modifier.weight(2f),
                            selected = state.sortType == SortType.SURFACE,
                            onClick = {
                                onEvent(
                                    ListDetailsEvent.GetProperties(
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
                                            state.chosenAreaCode,
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
                                    ListDetailsEvent.GetProperties(
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
                                            state.chosenAreaCode,
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
                                    ListDetailsEvent.GetProperties(
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
                                            state.chosenAreaCode,
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
                                ListDetailsEvent.SetRangeSurface(
                                    Range<Float>(
                                        surfaceSliderPosition.start,
                                        surfaceSliderPosition.endInclusive
                                    )
                                )
                            )
                        },
                    )
                    Text(text = "min: ${state.rangeSurface.lower}    max:${surfaceSliderMax}")
                }


                var expanded by remember { mutableStateOf(false) }
                Card(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                        .fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, shape = RectangleShape, color = Color.Black)
                                .clickable(
                                    true,
                                    onClick = {
                                        expanded = true
                                    }
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text = if (state.chosenAreaCode == null) "Area Code" else state.chosenAreaCode.toString())
                            Icon(
                                Icons.Default.KeyboardArrowDown,
                                contentDescription = "More options"
                            )
                        }
                        // replace with bottom sheet.
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            DropdownMenuItem(
                                text = { Text("Area Code") },
                                onClick = {
                                    onEvent(ListDetailsEvent.OnAreaCodeChosen(null))
                                    expanded = false
                                }
                            )
                            for (i in state.areaCodeList) {
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("$i") },
                                    onClick = {
                                        onEvent(ListDetailsEvent.OnAreaCodeChosen(i))
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                        .fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        FilterChip(
                            modifier = Modifier.padding(4.dp),
                            onClick = {
                                onEvent(ListDetailsEvent.OnSchoolChecked(state.schoolState))

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
                                    ListDetailsEvent.OnParkChecked(state.parkState)
                                )
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
                                    ListDetailsEvent.OnShopChecked(state.shopState)
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
                                onEvent(ListDetailsEvent.OnTransportChecked(state.transportState))
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
            }
            IconButton(
                onClick = {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onEvent(ListDetailsEvent.DismissFilter)
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