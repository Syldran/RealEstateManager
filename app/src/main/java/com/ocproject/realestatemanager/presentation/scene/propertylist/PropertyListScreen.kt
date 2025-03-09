package com.ocproject.realestatemanager.presentation.scene.propertylist

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.presentation.scene.propertylist.components.PropertyListItem
import com.ocproject.realestatemanager.presentation.scene.propertylist.components.PropertyFilterSheet
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyListScreen(
    viewModel: PropertyListViewModel = koinViewModel(),
    onClick: (property: Property?) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    Row {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
        ) {


            item {
                Text(
                    text = "List of properties (${state.properties.size})",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }

            items(
                items = state.properties,
                key = { property ->
                    property.id
                }) { property ->
                PropertyListItem(
                    viewModel = viewModel,
                    propertyWithPhotos = property,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onClick(property)
//                            onNavigateToPropertyDetailScreen(property.id)
                        }
                        .padding(start = 16.dp, end = 16.dp),
                    onEvent = viewModel::onEvent
                )
            }
        }
        VerticalDivider()
    }
    when {
        state.isFilterSheetOpen -> {
            PropertyFilterSheet(
                state = state,
                onEvent = viewModel::onEvent,
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                scope = rememberCoroutineScope(),
            )
        }

        state.isLoadingProgressBar -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Red)
            }
        }

        state.isError -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "ERROR Data", color = Color.Red)

            }
        }
    }
}

