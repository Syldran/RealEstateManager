package com.ocproject.realestatemanager.presentation.scene.propertylist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import com.ocproject.realestatemanager.presentation.scene.propertylist.components.PropertyListItem
import com.ocproject.realestatemanager.presentation.scene.propertylist.components.PropertyListTopBar
import com.ocproject.realestatemanager.presentation.scene.propertylist.components.PropertyFilterSheet
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyListScreen(
    viewModel: PropertyListViewModel = koinViewModel(),
    onNavigateToAddPropertyScreen: (propertyId: Long?) -> Unit,
    onNavigateToPropertyDetailScreen: (propertyId: Long) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    Scaffold(
        topBar = {
            PropertyListTopBar(
                onEvent = viewModel::onEvent,
                onNavigateToAddPropertyScreen =  onNavigateToAddPropertyScreen,
                modifier = Modifier
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onNavigateToAddPropertyScreen(null)
                },
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add property"
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = it,
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
                key = { propertyWithPhotos ->
                    propertyWithPhotos.property.id
                }) { propertyWithPhotos ->
                PropertyListItem(
                    viewModel = viewModel,
                    propertyWithPhotos = propertyWithPhotos,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onNavigateToPropertyDetailScreen(propertyWithPhotos.property.id)
                        }
                        .padding(start = 16.dp, end = 16.dp),
                    onEvent = viewModel::onEvent
                )
            }
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
        }

        if (state.isLoadingProgressBar){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        }

        when {
            state.isError -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator(color = Color.Red)
                }
            }
        }
    }
}

