package com.ocproject.realestatemanager.presentation.scene.listdetails.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Card
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
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ocproject.realestatemanager.R
import com.ocproject.realestatemanager.core.SellingStatus
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
                            stringResource(R.string.selling_status),
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                            fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
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
                                stringResource(R.string.purchasable),
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                                textAlign = TextAlign.Center,
                            )
                            RadioButton(
                                modifier = Modifier,
                                selected = state.filterState.sellingStatus == SellingStatus.PURCHASABLE,
                                onClick = {
                                    onEvent(
                                        ListDetailsEvent.UpdateFilter(
                                            filter = state.filterState.copy(
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
                                text = stringResource(R.string.sold),
                                Modifier.padding(end = 16.dp),
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                                textAlign = TextAlign.Center,
                            )
                            RadioButton(
                                modifier = Modifier,
                                selected = state.filterState.sellingStatus == SellingStatus.SOLD,
                                onClick = {
                                    onEvent(
                                        ListDetailsEvent.UpdateFilter(
                                            filter = state.filterState.copy(
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
                                selected = state.filterState.sellingStatus == SellingStatus.ALL,
                                onClick = {

                                    onEvent(
                                        ListDetailsEvent.UpdateFilter(
                                            filter = state.filterState.copy(
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
                            value = if (state.filterState.minNbrPhotos == 0) "" else state.filterState.minNbrPhotos.toString(),
                            placeholder = { Text(stringResource(R.string._0)) },
                            label = { Text(stringResource(R.string.min_photos)) },
                            onValueChange = {
                                onEvent(
                                    ListDetailsEvent.UpdateFilter(
                                        filter = state.filterState.copy(
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
                    Text(
                        stringResource(R.string.price_range),
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                        fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    )
                    val maxPrice = state.maxPrice
                    val sliderMax = if (maxPrice > state.filterState.priceRange.upper) {
                        state.filterState.priceRange.upper
                    } else {
                        maxPrice
                    }
                    var sliderPosition by remember { mutableStateOf(state.filterState.priceRange.lower.toFloat()..sliderMax.toFloat()) }

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
                                    filter = state.filterState.copy(
                                        priceRange = Range(
                                            sliderPosition.start.toInt(),
                                            sliderPosition.endInclusive.toInt(),
                                        ),
                                    )
                                )
                            )
                        },
                    )
                    Text(
                        text = stringResource(
                            R.string.min_max_price_range,
                            state.filterState.priceRange.lower,
                            sliderMax
                        ),
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                        fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    )
                }

                //DATE
                Card(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                ) {
                    val dateRangePickerState = rememberDateRangePickerState(
                        initialDisplayMode = DisplayMode.Input,
                        initialSelectedStartDateMillis = state.filterState.dateRange.lower,
                        initialSelectedEndDateMillis = state.filterState.dateRange.upper
                    )
                    Text(stringResource(R.string.added_date_range))
                    DateRangePicker(
                        showModeToggle = false,
                        state = dateRangePickerState,
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextButton(
                            border = BorderStroke(
                                color = MaterialTheme.colorScheme.primary,
                                width = 1.dp
                            ),
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
                                        filter = state.filterState.copy(
                                            dateRange = Range(
                                                dateRangePickerState.selectedStartDateMillis!!,
                                                selectedDateEnd.timeInMillis
                                            ),
                                        )
                                    )
                                )
                            }
                        ) {
                            Text(
                                stringResource(R.string.validate_range),
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                            )
                        }
                    }
                }
                if (state.filterState.sellingStatus == SellingStatus.SOLD) {
                    Card(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) {
                        val soldDateRangePickerState = rememberDateRangePickerState(
                            initialDisplayMode = DisplayMode.Input,
                            initialSelectedStartDateMillis = state.filterState.soldDateRange.lower,
                            initialSelectedEndDateMillis = state.filterState.soldDateRange.upper
                        )
                        Text(
                            stringResource(R.string.sold_date_range),
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                            fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                        )

                        DateRangePicker(
                            showModeToggle = false,
                            state = soldDateRangePickerState,
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            TextButton(
                                border = BorderStroke(
                                    color = MaterialTheme.colorScheme.primary,
                                    width = 1.dp
                                ),
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
                                            filter = state.filterState.copy(
                                                soldDateRange = Range(
                                                    soldDateRangePickerState.selectedStartDateMillis!!,
                                                    soldDateRangePickerState.selectedStartDateMillis!!
                                                )
                                            )
                                        )
                                    )
                                }
                            ) {
                                Text(
                                    stringResource(R.string.validate_sold_range),
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                                )
                            }
                        }
                    }
                }

                Card(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) {
                    val maxSurface = state.maxSurface
                    val surfaceSliderMax = if (maxSurface > state.filterState.surfaceRange.upper) {
                        state.filterState.surfaceRange.upper
                    } else {
                        maxSurface
                    }
                    var surfaceSliderPosition by remember { mutableStateOf(state.filterState.surfaceRange.lower.toFloat()..surfaceSliderMax.toFloat()) }
                    Text(
                        stringResource(R.string.surface_area_range),
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                        fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    )
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
                                    filter = state.filterState.copy(
                                        surfaceRange = Range(
                                            surfaceSliderPosition.start.toInt(),
                                            surfaceSliderPosition.endInclusive.toInt()
                                        ),
                                    )
                                )
                            )
                        },
                    )
                    Text(
                        text = stringResource(
                            R.string.min_max_surface_range,
                            state.filterState.surfaceRange.lower,
                            surfaceSliderMax
                        ),
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                        fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    )
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
                            Text(text = if (state.filterState.areaCodeFilter == null) stringResource(
                                R.string.area_code
                            ) else state.filterState.areaCodeFilter.toString())
                            Icon(
                                Icons.Default.KeyboardArrowDown,
                                contentDescription = stringResource(R.string.more_options_icon_content_description)
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
                                        filter = state.filterState.copy(
                                            tagSchool = !state.filterState.tagSchool,
                                        )
                                    )
                                )
                            },
                            label = {
                                Text("School")
                            },
                            selected = state.filterState.tagSchool,
                            leadingIcon = if (state.filterState.tagSchool) {
                                {
                                    Icon(
                                        imageVector = Icons.Filled.Done,
                                        contentDescription = stringResource(R.string.done_icon_content_description),
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
                                        filter = state.filterState.copy(
                                            tagPark = !state.filterState.tagPark,
                                        )
                                    )
                                )

                            },
                            label = {
                                Text(stringResource(R.string.park))
                            },
                            selected = state.filterState.tagPark,
                            leadingIcon = if (state.filterState.tagPark) {
                                {
                                    Icon(
                                        imageVector = Icons.Filled.Done,
                                        contentDescription = stringResource(R.string.done_icon_content_description),
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
                                        filter = state.filterState.copy(
                                            tagShop = !state.filterState.tagShop,
                                        )
                                    )
                                )
                            },
                            label = {
                                Text(stringResource(R.string.shop))
                            },
                            selected = state.filterState.tagShop,
                            leadingIcon = if (state.filterState.tagShop) {
                                {
                                    Icon(
                                        imageVector = Icons.Filled.Done,
                                        contentDescription = stringResource(R.string.done_icon_content_description),
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
                                        filter = state.filterState.copy(
                                            tagTransport = !state.filterState.tagTransport,
                                        )
                                    )
                                )
                            },
                            label = {
                                Text(stringResource(R.string.transport))
                            },
                            selected = state.filterState.tagTransport,
                            leadingIcon = if (state.filterState.tagTransport) {
                                {
                                    Icon(
                                        imageVector = Icons.Filled.Done,
                                        contentDescription = stringResource(R.string.done_icon_content_description),
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
                Icon(imageVector = Icons.Rounded.Close, contentDescription = stringResource(R.string.close_icon_content_description))
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
                        item {
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
                                                    filter = state.filterState.copy(
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