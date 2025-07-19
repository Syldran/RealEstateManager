package com.ocproject.realestatemanager.presentation.scene.propertylist.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ocproject.realestatemanager.core.Filter
import com.ocproject.realestatemanager.core.SellingStatus
import com.ocproject.realestatemanager.core.SortType
import com.ocproject.realestatemanager.core.utils.Range
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsEvent
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyFilterSheet(
    state: ListDetailsState,
    onEvent: (ListDetailsEvent) -> Unit,
    sheetState: SheetState,
    scope: CoroutineScope,
) {
    var isAreaCodeListExpended by remember { mutableStateOf(false) }

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
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                        .fillMaxWidth()
                ) {
                    Row {
                        Text(
                            "Selling Status"
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Purchasable",
                                textAlign = TextAlign.Center,
                            )
                            RadioButton(
                                modifier = Modifier,
                                selected = state.soldStatus == SellingStatus.PURCHASABLE,
                                onClick = {
                                    onEvent(
                                        ListDetailsEvent.UpdateFilter(
                                            filter = Filter(
                                                SortType.PRICE,
                                                state.orderPrice,
                                                state.orderDate,
                                                state.orderSurface,
                                                state.rangePrice,
                                                state.rangeDate,
                                                state.soldRangeDate,
                                                state.rangeSurface,
                                                SellingStatus.PURCHASABLE,
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
                        Column(
                            modifier = Modifier,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Sold", Modifier.padding(end = 16.dp),
                                textAlign = TextAlign.Center,
                            )
                            RadioButton(
                                modifier = Modifier,
                                selected = state.soldStatus == SellingStatus.SOLD,
                                onClick = {
                                    onEvent(
                                        ListDetailsEvent.UpdateFilter(
                                            filter = Filter(
                                                state.sortType,
                                                state.orderPrice,
                                                state.orderDate,
                                                state.orderSurface,
                                                state.rangePrice,
                                                state.rangeDate,
                                                state.soldRangeDate,
                                                state.rangeSurface,
                                                SellingStatus.SOLD,
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
                        Column(
                            modifier = Modifier,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "All",
                                textAlign = TextAlign.Center,
                            )
                            RadioButton(
                                modifier = Modifier,
                                selected = state.soldStatus == SellingStatus.ALL,
                                onClick = {
                                    onEvent(
                                        ListDetailsEvent.UpdateFilter(
                                            filter = Filter(
                                                state.sortType,
                                                state.orderPrice,
                                                state.orderDate,
                                                state.orderSurface,
                                                state.rangePrice,
                                                state.rangeDate,
                                                state.soldRangeDate,
                                                state.rangeSurface,
                                                SellingStatus.ALL,
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
                Card(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
//                        Text(text = "Photos Minimum: ")
                        OutlinedTextField(
                            shape = RoundedCornerShape(size = 8.dp),
                            colors = TextFieldDefaults.colors(focusedContainerColor = Color.White,),
                            modifier = Modifier,
                            value = if (state.minNbrPhotos == 0) "" else state.minNbrPhotos.toString(),
                            placeholder = { Text("0") },
                            label = {Text("Min Photos")},
                            onValueChange = {
                                onEvent(
                                    ListDetailsEvent.UpdateFilter(
                                        filter = Filter(
                                            state.sortType,
                                            state.orderPrice,
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
                                            if (it.isEmpty()) {
                                                0
                                            } else {
                                                it.toInt()
                                            },
                                        )
                                    )
                                )


                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

                            )
                    }
                }

                Card(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) {
                    Text("Price Range")
                    val maxPrice = state.maxPrice
                    val sliderMax = if (maxPrice > state.rangePrice.upper) {
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

                //DATE
                Card(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) {
                    val dateRangePickerState = rememberDateRangePickerState(
                        initialDisplayMode = DisplayMode.Input,
                        initialSelectedStartDateMillis = state.rangeDate.lower,
                        initialSelectedEndDateMillis = state.rangeDate.upper
                    )
                    Text("Added Date Range")
                    DateRangePicker(
                        showModeToggle = false,
                        state = dateRangePickerState,
                    )
                    TextButton(
                        onClick = {
                            val selectedDateEnd = Calendar.getInstance()
                            selectedDateEnd.setTimeInMillis(dateRangePickerState.selectedEndDateMillis!!)
                            selectedDateEnd.set(Calendar.HOUR_OF_DAY, 0)
                            selectedDateEnd.set(Calendar.MINUTE, 0)
                            selectedDateEnd.set(Calendar.SECOND, 0)
                            selectedDateEnd.set(Calendar.MILLISECOND, 0)
                            selectedDateEnd.add(Calendar.DAY_OF_YEAR, 1)
                            selectedDateEnd.add(Calendar.MILLISECOND, -1)

                            onEvent(
                                ListDetailsEvent.OnDateRangeSelected(
                                    dateRangePickerState.selectedStartDateMillis!!,
                                    selectedDateEnd.timeInMillis
                                )
                            )
                        }
                    ) {
                        Text("Validate Date")
                    }
                }
                if (state.soldStatus == SellingStatus.SOLD) {
                    Card(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) {
                        val soldDateRangePickerState = rememberDateRangePickerState(
                            initialDisplayMode = DisplayMode.Input,
                            initialSelectedStartDateMillis = state.soldRangeDate.lower,
                            initialSelectedEndDateMillis = state.soldRangeDate.upper
                        )
                        Text("Sold Date Range")

                        DateRangePicker(
                            showModeToggle = false,
                            state = soldDateRangePickerState,
                        )
                        TextButton(
                            onClick = {
                                val selectedSoldDateEnd = Calendar.getInstance()
                                selectedSoldDateEnd.setTimeInMillis(soldDateRangePickerState.selectedEndDateMillis!!)
                                selectedSoldDateEnd.set(Calendar.HOUR_OF_DAY, 0)
                                selectedSoldDateEnd.set(Calendar.MINUTE, 0)
                                selectedSoldDateEnd.set(Calendar.SECOND, 0)
                                selectedSoldDateEnd.set(Calendar.MILLISECOND, 0)
                                selectedSoldDateEnd.add(Calendar.DAY_OF_YEAR, 1)
                                selectedSoldDateEnd.add(Calendar.MILLISECOND, -1)
                                onEvent(
                                    ListDetailsEvent.OnSoldDateRangeSelected(
                                        soldDateRangePickerState.selectedStartDateMillis!!,
                                        selectedSoldDateEnd.timeInMillis
                                    )
                                )
                            }
                        ) {
                            Text("Validate Sold Date Range")
                        }
                    }
                }

                Card(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) {
                    val maxSurface = state.maxSurface
                    val surfaceSliderMax = if (maxSurface > state.rangeSurface.upper) {
                        state.rangeSurface.upper
                    } else {
                        maxSurface
                    }
                    var surfaceSliderPosition by remember { mutableStateOf(state.rangeSurface.lower.toFloat()..surfaceSliderMax.toFloat()) }
                    Text("Surface Area Range")
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
                                        isAreaCodeListExpended = true
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
                            expanded = isAreaCodeListExpended,
                            onDismissRequest = { isAreaCodeListExpended = false },
                        ) {
                            DropdownMenuItem(
                                text = { Text("Area Code") },
                                onClick = {
                                    onEvent(ListDetailsEvent.OnAreaCodeChosen(null))
                                    isAreaCodeListExpended = false
                                }
                            )
                            for (i in state.areaCodeList) {
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("$i") },
                                    onClick = {
                                        onEvent(ListDetailsEvent.OnAreaCodeChosen(i))
                                        isAreaCodeListExpended = false
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
                                onEvent(ListDetailsEvent.OnSchoolChecked(state.schoolTag))

                            },
                            label = {
                                Text("School")
                            },
                            selected = state.schoolTag,
                            leadingIcon = if (state.schoolTag) {
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
                                    ListDetailsEvent.OnParkChecked(state.parkTag)
                                )
                            },
                            label = {
                                Text("Park")
                            },
                            selected = state.parkTag,
                            leadingIcon = if (state.parkTag) {
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
                                    ListDetailsEvent.OnShopChecked(state.shopTag)
                                )
                            },
                            label = {
                                Text("Shop")
                            },
                            selected = state.shopTag,
                            leadingIcon = if (state.shopTag) {
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
                                onEvent(ListDetailsEvent.OnTransportChecked(state.transportTag))
                            },
                            label = {
                                Text("Transport")
                            },
                            selected = state.transportTag,
                            leadingIcon = if (state.transportTag) {
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