package com.ocproject.realestatemanager.presentation.scene.listdetails.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ocproject.realestatemanager.core.Filter
import com.ocproject.realestatemanager.core.SellingStatus
import com.ocproject.realestatemanager.core.SortType
import com.ocproject.realestatemanager.core.ui.theme.RealEstateManagerTheme
import com.ocproject.realestatemanager.core.utils.Range
import com.ocproject.realestatemanager.presentation.scene.funding.FundingEvent
import com.ocproject.realestatemanager.presentation.scene.funding.FundingRate
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
    val sheetState = rememberModalBottomSheetState()


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
                                selected = state.filterSate.sellingStatus == SellingStatus.PURCHASABLE,
                                onClick = {
                                    onEvent(
                                        ListDetailsEvent.UpdateFilter(
                                            filter = state.filterSate.copy(
                                                sellingStatus = SellingStatus.PURCHASABLE
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
                                selected = state.filterSate.sellingStatus == SellingStatus.SOLD,
                                onClick = {
                                    onEvent(
                                        ListDetailsEvent.UpdateFilter(
                                            filter = state.filterSate.copy(
                                                sellingStatus = SellingStatus.SOLD
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
                                selected = state.filterSate.sellingStatus == SellingStatus.ALL,
                                onClick = {

                                    onEvent(
                                        ListDetailsEvent.UpdateFilter(
                                            filter = state.filterSate.copy(
                                                sellingStatus = SellingStatus.ALL
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
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
//                        Text(text = "Photos Minimum: ")
                        OutlinedTextField(
                            shape = RoundedCornerShape(size = 8.dp),
                            colors = TextFieldDefaults.colors(focusedContainerColor = Color.White),
                            modifier = Modifier,
                            value = if (state.filterSate.minNbrPhotos == 0) "" else state.filterSate.minNbrPhotos.toString(),
                            placeholder = { Text("0") },
                            label = { Text("Min Photos") },
                            onValueChange = {
                                onEvent(
                                    ListDetailsEvent.UpdateFilter(
                                        filter = state.filterSate.copy(
                                            minNbrPhotos = if (it.isEmpty()) {
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
                    val sliderMax = if (maxPrice > state.filterSate.priceRange.upper) {
                        state.filterSate.priceRange.upper
                    } else {
                        maxPrice
                    }
                    var sliderPosition by remember { mutableStateOf(state.filterSate.priceRange.lower.toFloat()..sliderMax.toFloat()) }

                    RangeSlider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        value = sliderPosition,
                        steps = 0,
                        onValueChange = { priceRange ->
                            sliderPosition = priceRange
                        },
                        valueRange = 0F..maxPrice.toFloat(),
                        onValueChangeFinished = {
                            onEvent(
                                ListDetailsEvent.UpdateFilter(
                                    filter = state.filterSate.copy(
                                        priceRange = Range<Int>(
                                            sliderPosition.start.toInt(),
                                            sliderPosition.endInclusive.toInt(),
                                        ),
                                    )
                                )
                            )
                        },
                    )
                    Text(text = "min: ${state.filterSate.priceRange.lower}    max:${sliderMax}")
                }

                //DATE
                Card(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                ) {
                    val dateRangePickerState = rememberDateRangePickerState(
                        initialDisplayMode = DisplayMode.Input,
                        initialSelectedStartDateMillis = state.filterSate.dateRange.lower,
                        initialSelectedEndDateMillis = state.filterSate.dateRange.upper
                    )
                    Text("Added Date Range")
                    DateRangePicker(
                        showModeToggle = false,
                        state = dateRangePickerState,
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextButton(
                            border = BorderStroke(color = MaterialTheme.colorScheme.primary, width = 1.dp),
//                            colors = ButtonDefaults.textButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
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
                                    ListDetailsEvent.UpdateFilter(
                                        filter = state.filterSate.copy(
                                            dateRange = Range<Long>(
                                                dateRangePickerState.selectedStartDateMillis!!,
                                                selectedDateEnd.timeInMillis
                                            ),
                                        )
                                    )
                                )
                            }
                        ) {
                            Text("Validate Range")
                        }
                    }
                }
                if (state.filterSate.sellingStatus == SellingStatus.SOLD) {
                    Card(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) {
                        val soldDateRangePickerState = rememberDateRangePickerState(
                            initialDisplayMode = DisplayMode.Input,
                            initialSelectedStartDateMillis = state.filterSate.soldDateRange.lower,
                            initialSelectedEndDateMillis = state.filterSate.soldDateRange.upper
                        )
                        Text("Sold Date Range")

                        DateRangePicker(
                            showModeToggle = false,
                            state = soldDateRangePickerState,
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            TextButton(
                                border = BorderStroke(color = MaterialTheme.colorScheme.primary, width = 1.dp),
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
                                        ListDetailsEvent.UpdateFilter(
                                            filter = state.filterSate.copy(
                                                soldDateRange = Range<Long>(
                                                    soldDateRangePickerState.selectedStartDateMillis!!,
                                                    soldDateRangePickerState.selectedStartDateMillis!!
                                                )
                                            )
                                        )
                                    )
                                }
                            ) {
                                Text("Validate Sold Range")
                            }
                        }
                    }
                }

                Card(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) {
                    val maxSurface = state.maxSurface
                    val surfaceSliderMax = if (maxSurface > state.filterSate.surfaceRange.upper) {
                        state.filterSate.surfaceRange.upper
                    } else {
                        maxSurface
                    }
                    var surfaceSliderPosition by remember { mutableStateOf(state.filterSate.surfaceRange.lower.toFloat()..surfaceSliderMax.toFloat()) }
                    Text("Surface Area Range")
                    RangeSlider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        value = surfaceSliderPosition,
                        steps = 0,
                        onValueChange = { surfaceRange ->
                            surfaceSliderPosition = surfaceRange
                        },
                        valueRange = 0F..maxSurface.toFloat(),
                        onValueChangeFinished = {
                            onEvent(
                                ListDetailsEvent.UpdateFilter(
                                    filter = state.filterSate.copy(
                                        surfaceRange = Range<Int>(
                                            surfaceSliderPosition.start.toInt(),
                                            surfaceSliderPosition.endInclusive.toInt()
                                        ),
                                    )
                                )
                            )
                        },
                    )
                    Text(text = "min: ${state.filterSate.surfaceRange.lower}    max:${surfaceSliderMax}")
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
                            Text(text = if (state.filterSate.areaCodeFilter == null) "Area Code" else state.filterSate.areaCodeFilter.toString())
                            Icon(
                                Icons.Default.KeyboardArrowDown,
                                contentDescription = "More options"
                            )
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
                                onEvent(
                                    ListDetailsEvent.UpdateFilter(
                                        filter = state.filterSate.copy(
                                            tagSchool = !state.filterSate.tagSchool,
                                        )
                                    )
                                )
                            },
                            label = {
                                Text("School")
                            },
                            selected = state.filterSate.tagSchool,
                            leadingIcon = if (state.filterSate.tagSchool) {
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
                                    ListDetailsEvent.UpdateFilter(
                                        filter = state.filterSate.copy(
                                            tagPark = !state.filterSate.tagPark,
                                        )
                                    )
                                )

                            },
                            label = {
                                Text("Park")
                            },
                            selected = state.filterSate.tagPark,
                            leadingIcon = if (state.filterSate.tagPark) {
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
                                    ListDetailsEvent.UpdateFilter(
                                        filter = state.filterSate.copy(
                                            tagShop = !state.filterSate.tagShop,
                                        )
                                    )
                                )
                            },
                            label = {
                                Text("Shop")
                            },
                            selected = state.filterSate.tagShop,
                            leadingIcon = if (state.filterSate.tagShop) {
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
                                    ListDetailsEvent.UpdateFilter(
                                        filter = state.filterSate.copy(
                                            tagTransport = !state.filterSate.tagTransport,
                                        )
                                    )
                                )
                            },
                            label = {
                                Text("Transport")
                            },
                            selected = state.filterSate.tagTransport,
                            leadingIcon = if (state.filterSate.tagTransport) {
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
            if (isAreaCodeListExpended) {
                ModalBottomSheet(
                    onDismissRequest = {
                        isAreaCodeListExpended = false
                    },
                    sheetState = sheetState
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .fillMaxWidth()
                            .height(LocalWindowInfo.current.containerSize.height.dp),
                    ) {
                        item() {
                            HorizontalDivider()
                        }
                        items(state.areaCodeList) { areaCode ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        onClick = {
                                            onEvent(
                                                ListDetailsEvent.UpdateFilter(
                                                    filter = state.filterSate.copy(
                                                        areaCodeFilter = areaCode,
                                                    )
                                                )
                                            )
                                            isAreaCodeListExpended = false
                                        }
                                    ),
                            ) {
                                Column {
                                    Text(
                                        text = "$areaCode",
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                    HorizontalDivider(modifier = Modifier.fillMaxWidth())
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}