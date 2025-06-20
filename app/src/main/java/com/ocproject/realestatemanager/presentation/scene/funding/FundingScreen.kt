package com.ocproject.realestatemanager.presentation.scene.funding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ocproject.realestatemanager.R
import com.ocproject.realestatemanager.presentation.scene.addproperty.components.PropertyTextField
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.coroutines.coroutineContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundingScreen(
    viewModel: FundingViewModel = koinViewModel()
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    val state by viewModel.state.collectAsState()
    var price: Int? = viewModel.price
    Scaffold(
        modifier = Modifier.padding(horizontal = 16.dp),
        containerColor = colorResource(id = R.color.white),
        contentWindowInsets = WindowInsets.safeDrawing, // Applies safe area to Scaffold content

    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PropertyTextField(
                value = if (price == null || price == 0) "" else price.toString(),
                error = null,
                onValueChanged = {
                    viewModel.onEvent(FundingEvent.OnPriceInput(value = it))
                },
                keyboardType = KeyboardType.Number,
                labelValue = "Price"
            )


            var expanded by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .border(1.dp, shape = RectangleShape, color = Color.Black)
                    .clickable(
                        true,
                        onClick = {
//                            expanded = true
                            showBottomSheet = true
                        }
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = state.chosenText
                )


                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = "More options",
                )
            }


// replace with bottom sheet.
            /*    DropdownMenu(
                    modifier = Modifier.padding(8.dp),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("7 years 3.24 % ") },
                        onClick = {
                            viewModel.onEvent(
                                FundingEvent.OnRateOptionChosen(
                                    FundingRate.SEVEN_YEARS,
                                    "7 years ${viewModel.displayPercent(FundingRate.SEVEN_YEARS.ratio)} % "
                                )
                            )
                            expanded = false
                        }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text("10 years 3.38 %") },
                        onClick = {
                            viewModel.onEvent(
                                FundingEvent.OnRateOptionChosen(
                                    FundingRate.TEN_YEARS,
                                    "10 years ${viewModel.displayPercent(FundingRate.TEN_YEARS.ratio)} % "
                                )
                            )
                            expanded = false
                        }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text("15 years 3.48 % ") },
                        onClick = {
                            viewModel.onEvent(
                                FundingEvent.OnRateOptionChosen(
                                    FundingRate.FIFTEEN_YEARS,
                                    "15 years ${viewModel.displayPercent(FundingRate.FIFTEEN_YEARS.ratio)} % "
                                )
                            )
                            expanded = false
                        }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text("20 years 3.58 % ") },
                        onClick = {
                            viewModel.onEvent(
                                FundingEvent.OnRateOptionChosen(
                                    FundingRate.TWENTY_YEARS,
                                    "20 years ${viewModel.displayPercent(FundingRate.TWENTY_YEARS.ratio)} % "
                                )
                            )
                            expanded = false
                        }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text("25 years 3.68 % ") },
                        onClick = {
                            viewModel.onEvent(
                                FundingEvent.OnRateOptionChosen(
                                    FundingRate.TWENTY_FIVE_YEARS,
                                    "25 years ${viewModel.displayPercent(FundingRate.TWENTY_FIVE_YEARS.ratio)} % "
                                )
                            )
                            expanded = false
                        }
                    )
                }*/
            Card(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                border = BorderStroke(1.dp, colorResource(id = R.color.purple_200))
            ) {
                //add notarial price
                var monthlyPayment =
                    viewModel.calcMonthlyPayment(
                        price?.toFloat() ?: 0F,
                        state.chosenRate.ratio,
                        state.chosenRate.yearsInMonths.toFloat()
                    )
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Total Cost = ${
                            String.format(
                                "%.2f",
                                viewModel.displayTotalCost(
                                    monthlyPayment,
                                    state.chosenRate.yearsInMonths
                                )
                            )
                        }"
                    )
                    Text("Monthly Payment = ${String.format("%.2f", monthlyPayment)}")
                    Text(
                        "Interest Payment = ${
                            String.format(
                                "%.2f",
                                viewModel.displayInterest(
                                    monthlyPayment,
                                    state.chosenRate.yearsInMonths,
                                    price?.toFloat() ?: 0F
                                )
                            )
                        }"
                    )
                }
            }
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "7 years 3.24 % ",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable(onClick = {
                                viewModel.onEvent(
                                    FundingEvent.OnRateOptionChosen(
                                        FundingRate.SEVEN_YEARS,
                                        "7 years ${viewModel.displayPercent(FundingRate.SEVEN_YEARS.ratio)} % "
                                    )
                                )
                                showBottomSheet = false
                            })
                    )
                    Text(
                        text = "10 years 3.38 % ",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable(onClick = {
                                viewModel.onEvent(
                                    FundingEvent.OnRateOptionChosen(
                                        FundingRate.TEN_YEARS,
                                        "10 years ${viewModel.displayPercent(FundingRate.TEN_YEARS.ratio)} % "
                                    )
                                )
                                showBottomSheet = false
                            }
                            ))
                    Text(
                        "15 years 3.48 % ",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable(onClick = {
                                viewModel.onEvent(
                                    FundingEvent.OnRateOptionChosen(
                                        FundingRate.FIFTEEN_YEARS,
                                        "15 years ${viewModel.displayPercent(FundingRate.FIFTEEN_YEARS.ratio)} % "
                                    )
                                )
                                showBottomSheet = false
                            })
                    )
                    Text(
                        text = "20 years 3.58 % ",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable(onClick = {
                                viewModel.onEvent(
                                    FundingEvent.OnRateOptionChosen(
                                        FundingRate.TWENTY_YEARS,
                                        "20 years ${viewModel.displayPercent(FundingRate.TWENTY_YEARS.ratio)} % "
                                    )
                                )
                                showBottomSheet = false
                            })
                    )
                    Text(
                        text = "25 years 3.68 % ",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable(onClick = {
                                viewModel.onEvent(
                                    FundingEvent.OnRateOptionChosen(
                                        FundingRate.TWENTY_FIVE_YEARS,
                                        "25 years ${viewModel.displayPercent(FundingRate.TWENTY_FIVE_YEARS.ratio)} % "
                                    )
                                )
                                showBottomSheet = false
                            })
                    )
                }
            }
        }
    }
}