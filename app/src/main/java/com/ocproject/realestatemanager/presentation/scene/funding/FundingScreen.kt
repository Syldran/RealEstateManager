package com.ocproject.realestatemanager.presentation.scene.funding

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ocproject.realestatemanager.R
import com.ocproject.realestatemanager.presentation.scene.addproperty.components.PropertyTextField
import com.ocproject.realestatemanager.presentation.scene.funding.FundingViewModel.Companion.calcMonthlyPayment
import com.ocproject.realestatemanager.presentation.scene.funding.FundingViewModel.Companion.displayInterest
import com.ocproject.realestatemanager.presentation.scene.funding.FundingViewModel.Companion.displayPercent
import com.ocproject.realestatemanager.presentation.scene.funding.FundingViewModel.Companion.displayTotalCost
import com.ocproject.realestatemanager.core.utils.AdaptiveDimensions
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundingScreen(
    viewModel: FundingViewModel = koinViewModel()
) {
    val sheetState = rememberModalBottomSheetState()

    val state by viewModel.state.collectAsState()
    val price: Int? = viewModel.price
    val configuration = LocalConfiguration.current
    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        FundingContentPortrait(
            state = state,
            price = price,
            sheetState = sheetState,
            onEvent = viewModel::onEvent
        )
    } else {
        FundingContentLandscape(
            state = state,
            price = price,
            sheetState = sheetState,
            onEvent = viewModel::onEvent
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundingContentLandscape(
    state: FundingState,
    price: Int?,
    sheetState: SheetState,
    onEvent: (FundingEvent) -> Unit
) {
    Scaffold(
        modifier = Modifier.padding(horizontal = AdaptiveDimensions.getSpacingMedium()),
        containerColor = colorResource(id = R.color.white),
        contentWindowInsets = WindowInsets.safeDrawing, // Applies safe area to Scaffold content

    )
    { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                PropertyTextField(
                    modifier = Modifier.weight(0.5F).padding(16.dp),
                    value = if (price == null || price == 0) "" else price.toString(),
                    error = null,
                    onValueChanged = {
                        onEvent(FundingEvent.OnPriceInput(value = it))
                    },
                    keyboardType = KeyboardType.Number,
                    labelValue = "Price"
                )
                Row(
                    modifier = Modifier
                        .weight(0.5F)
                        .padding(16.dp)
                        .border(1.dp, shape = RectangleShape, color = Color.Black)
                        .clickable(
                            true,
                            onClick = {
                                onEvent(FundingEvent.OnOpenRateList(true))
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
            }



            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                border = BorderStroke(1.dp, colorResource(id = R.color.purple_200))
            ) {
                val monthlyPayment =
                    calcMonthlyPayment(
                        price?.toFloat() ?: 0F,
                        state.chosenRate.ratio,
                        state.chosenRate.yearsInMonths.toFloat()
                    )
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Total Cost = ${

                            displayTotalCost(
                                monthlyPayment,
                                state.chosenRate.yearsInMonths
                            )
                        }"
                    )
                    Text(
                        "Monthly Payment = ${
                            String.format(
                                Locale.ROOT,
                                "%.2f",
                                monthlyPayment
                            )
                        }"
                    )
                    Text(
                        "Interest Payment = ${
                            displayInterest(
                                monthlyPayment,
                                state.chosenRate.yearsInMonths,
                                price?.toFloat() ?: 0F
                            )
                        }"
                    )
                }
            }
        }
        if (state.isRatingListSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = {
                    onEvent(
                        FundingEvent.OnOpenRateList(
                            false
                        )
                    )
                },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "7 years 3.24 % ",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable(onClick = {
                                onEvent(
                                    FundingEvent.OnRateOptionChosen(
                                        FundingRate.SEVEN_YEARS,
                                        "7 years ${displayPercent(FundingRate.SEVEN_YEARS.ratio)}"
                                    )
                                )
                                onEvent(FundingEvent.OnOpenRateList(false))
                            })
                    )
                    Text(
                        text = "10 years 3.38 % ",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable(onClick = {
                                onEvent(
                                    FundingEvent.OnRateOptionChosen(
                                        FundingRate.TEN_YEARS,
                                        "10 years ${displayPercent(FundingRate.TEN_YEARS.ratio)}"
                                    )
                                )
                                onEvent(FundingEvent.OnOpenRateList(false))
                            }
                            ))
                    Text(
                        "15 years 3.48 % ",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable(onClick = {
                                onEvent(
                                    FundingEvent.OnRateOptionChosen(
                                        FundingRate.FIFTEEN_YEARS,
                                        "15 years ${displayPercent(FundingRate.FIFTEEN_YEARS.ratio)}"
                                    )
                                )
                                onEvent(FundingEvent.OnOpenRateList(false))
                            })
                    )
                    Text(
                        text = "20 years 3.58 % ",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable(onClick = {
                                onEvent(
                                    FundingEvent.OnRateOptionChosen(
                                        FundingRate.TWENTY_YEARS,
                                        "20 years ${displayPercent(FundingRate.TWENTY_YEARS.ratio)}"
                                    )
                                )
                                onEvent(FundingEvent.OnOpenRateList(false))
                            })
                    )
                    Text(
                        text = "25 years 3.68 % ",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable(onClick = {
                                onEvent(
                                    FundingEvent.OnRateOptionChosen(
                                        FundingRate.TWENTY_FIVE_YEARS,
                                        "25 years ${displayPercent(FundingRate.TWENTY_FIVE_YEARS.ratio)}"
                                    )
                                )
                                onEvent(FundingEvent.OnOpenRateList(false))
                            })
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundingContentPortrait(
    state: FundingState,
    price: Int?,
    sheetState: SheetState,
    onEvent: (FundingEvent) -> Unit,
) {
    Scaffold(
        modifier = Modifier.padding(horizontal = 16.dp),
        containerColor = colorResource(id = R.color.white),
        contentWindowInsets = WindowInsets.safeDrawing, // Applies safe area to Scaffold content

    )
    { contentPadding ->
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
                    onEvent(FundingEvent.OnPriceInput(value = it))
                },
                keyboardType = KeyboardType.Number,
                labelValue = "Price"
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AdaptiveDimensions.getSpacingMedium())
                    .border(1.dp, shape = RectangleShape, color = Color.Black)
                    .clickable(
                        true,
                        onClick = {
                            onEvent(FundingEvent.OnOpenRateList(true))
                        }
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    modifier = Modifier
                        .padding(AdaptiveDimensions.getSpacingSmall()),
                    text = state.chosenText
                )


                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = "More options",
                )
            }

            Card(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                border = BorderStroke(1.dp, colorResource(id = R.color.purple_200))
            ) {
                val monthlyPayment =
                    calcMonthlyPayment(
                        price?.toFloat() ?: 0F,
                        state.chosenRate.ratio,
                        state.chosenRate.yearsInMonths.toFloat()
                    )
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Total Cost = ${

                            displayTotalCost(
                                monthlyPayment,
                                state.chosenRate.yearsInMonths
                            )
                        }"
                    )
                    Text(
                        "Monthly Payment = ${
                            String.format(
                                Locale.ROOT,
                                "%.2f",
                                monthlyPayment
                            )
                        }"
                    )
                    Text(
                        "Interest Payment = ${
                            displayInterest(
                                monthlyPayment,
                                state.chosenRate.yearsInMonths,
                                price?.toFloat() ?: 0F
                            )
                        }"
                    )
                }
            }
        }
        if (state.isRatingListSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = {
                    onEvent(
                        FundingEvent.OnOpenRateList(
                            false
                        )
                    )
                },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "7 years 3.24 % ",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable(onClick = {
                                onEvent(
                                    FundingEvent.OnRateOptionChosen(
                                        FundingRate.SEVEN_YEARS,
                                        "7 years ${displayPercent(FundingRate.SEVEN_YEARS.ratio)}"
                                    )
                                )
                                onEvent(FundingEvent.OnOpenRateList(false))
                            })
                    )
                    Text(
                        text = "10 years 3.38 % ",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable(onClick = {
                                onEvent(
                                    FundingEvent.OnRateOptionChosen(
                                        FundingRate.TEN_YEARS,
                                        "10 years ${displayPercent(FundingRate.TEN_YEARS.ratio)}"
                                    )
                                )
                                onEvent(FundingEvent.OnOpenRateList(false))
                            }
                            ))
                    Text(
                        "15 years 3.48 % ",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable(onClick = {
                                onEvent(
                                    FundingEvent.OnRateOptionChosen(
                                        FundingRate.FIFTEEN_YEARS,
                                        "15 years ${displayPercent(FundingRate.FIFTEEN_YEARS.ratio)}"
                                    )
                                )
                                onEvent(FundingEvent.OnOpenRateList(false))
                            })
                    )
                    Text(
                        text = "20 years 3.58 % ",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable(onClick = {
                                onEvent(
                                    FundingEvent.OnRateOptionChosen(
                                        FundingRate.TWENTY_YEARS,
                                        "20 years ${displayPercent(FundingRate.TWENTY_YEARS.ratio)}"
                                    )
                                )
                                onEvent(FundingEvent.OnOpenRateList(false))
                            })
                    )
                    Text(
                        text = "25 years 3.68 % ",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable(onClick = {
                                onEvent(
                                    FundingEvent.OnRateOptionChosen(
                                        FundingRate.TWENTY_FIVE_YEARS,
                                        "25 years ${displayPercent(FundingRate.TWENTY_FIVE_YEARS.ratio)}"
                                    )
                                )
                                onEvent(FundingEvent.OnOpenRateList(false))
                            })
                    )
                }
            }
        }
    }
}