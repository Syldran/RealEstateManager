package com.ocproject.realestatemanager.presentation.scene.funding

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyEvent
import com.ocproject.realestatemanager.presentation.scene.addproperty.components.PropertyTextField
import org.koin.androidx.compose.koinViewModel
import java.nio.file.WatchEvent
import kotlin.math.roundToInt

@Composable
fun FundingScreen(
    viewModel: FundingViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    var price: Int? = viewModel.price
    Column(
        modifier = Modifier.padding(16.dp),
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
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .border(1.dp, shape = RectangleShape, color = Color.Black)
                    .clickable(
                        true,
                        onClick = {
                            expanded = true
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically,
            )
            {
                Text(modifier = Modifier.padding(8.dp),
                    text = state.chosenText)
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "More options")

            }
// replace with bottom sheet.
            DropdownMenu(
                modifier = Modifier.padding(8.dp),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("7 years 3.24 % ") },
                    onClick = {
                        viewModel.onEvent(
                            FundingEvent.OnRateOptionChosen(
                                FundingRate.ON7YEARS,
                                "7 years ${FundingRate.ON7YEARS * 100F} % "
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
                                FundingRate.ON10YEARS,
                                "10 years ${FundingRate.ON10YEARS * 100F} % "
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
                                FundingRate.ON15YEARS,
                                "15 years ${FundingRate.ON15YEARS * 100F} % "
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
                                FundingRate.ON20YEARS,
                                "20 years ${FundingRate.ON20YEARS * 100F} % "
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
                                FundingRate.ON25YEARS,
                                "25 years ${FundingRate.ON25YEARS * 100F} % "
                            )
                        )
                        expanded = false
                    }
                )
            }

        }
        Card(
            modifier = Modifier.padding(16.dp)
        ) {
            //add notarial price
            var monthlyPayment =
                viewModel.calcMonthlyPayment(
                    price?.toDouble() ?: 0.0,
                    state.chosenRate.toDouble(),
                    240.0
                )
            Text("Total Cost = ${((monthlyPayment * 240.0) * 100.0).roundToInt() / 100.0}")
            Text("Monthly Payment = ${(monthlyPayment * 100.0).roundToInt() / 100.0}")
            Text("Interest Payment = ${((monthlyPayment * 240.0 - (price?.toDouble() ?: 0.0)) * 100.0).roundToInt() / 100.0}")
        }
    }
}

@Composable
private fun ScrollBoxes() {
    Column(
        modifier = Modifier
            .background(Color.LightGray)
            .size(100.dp)
            .verticalScroll(rememberScrollState())
    ) {
        repeat(10) {
            Text("Item $it", modifier = Modifier.padding(2.dp))
        }
    }
}

@Composable
fun MinimalDropdownMenu() {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Option 1") },
                onClick = { /* Do something... */ }
            )
            DropdownMenuItem(
                text = { Text("Option 2") },
                onClick = { /* Do something... */ }
            )
        }
    }
}