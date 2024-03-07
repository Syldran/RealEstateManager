package com.ocproject.realestatemanager.ui.scenes.propertylist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ocproject.realestatemanager.ui.scenes.propertylist.components.PropertyListTopBar
import com.ocproject.realestatemanager.ui.theme.RealestatemanagerTheme
import com.openclassrooms.realestatemanager.models.Property
import org.koin.androidx.compose.koinViewModel

@Composable
fun PropertyList(
    viewModel: PropertyListViewModel = koinViewModel(),
    onNavigateToAddPropertyScreen: () -> Unit,
    onNavigateToDetailsPropertyScreen: (propertyId: Int) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    ListPropertiesScreen(
        state = state,
        onSortProperties = {
            viewModel.onEvent(PropertyListViewModel.PropertyListEvent.SortProperties(it))
        },
        onDelete = {
            viewModel.onEvent(PropertyListViewModel.PropertyListEvent.DeleteProperty(it))
        },
        onNavigateToAddPropertyScreen = onNavigateToAddPropertyScreen,
        onNavigateToDetailsPropertyScreen = onNavigateToDetailsPropertyScreen,
    )
}

@Composable
fun ListPropertiesScreen(
    state: PropertyListViewModel.PropertyListState,
    onDelete: (property: Property) -> Unit,
    onSortProperties: (sortType: PropertyListViewModel.SortType) -> Unit,
    onNavigateToAddPropertyScreen: () -> Unit,
    onNavigateToDetailsPropertyScreen: (propertyId: Int) -> Unit,
) {
    Scaffold(
        topBar = {
            PropertyListTopBar(
                modifier = Modifier.fillMaxWidth(),
                onNavigateToAddPropertyScreen = onNavigateToAddPropertyScreen
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onNavigateToAddPropertyScreen()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Property"
                )

            }
        },

        ) { padding ->

        LazyColumn(
            contentPadding = padding,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = CenterVertically
                ) {
                    PropertyListViewModel.SortType.entries.forEach { sortType ->
                        Row(
                            modifier = Modifier
                                .clickable {
                                    onSortProperties(sortType)
                                },
                            verticalAlignment = CenterVertically
                        ) {
                            RadioButton(
                                selected = state.sortType == sortType,
                                onClick = {
                                    onSortProperties(sortType)
                                }
                            )
                            Text(text = sortType.name)
                        }
                    }
                }
            }
            items(state.properties) { property ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(onClick = {
                            onNavigateToDetailsPropertyScreen(property.property.id)
                        })
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Divider()
                        Text(
                            text = "id : ${property.property.id}",
                            fontSize = 14.sp
                        )
                        Text(
                            text = property.property.address,
                            fontSize = 20.sp
                        )
                        Text(text = property.property.price.toString(), fontSize = 12.sp)
                    }
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Property",
                        modifier = Modifier
                            .clickable {
                                onDelete(property.property)
                            }
                            .align(CenterVertically)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PropertiesPreview() {

    RealestatemanagerTheme {
        ListPropertiesScreen(
            state = PropertyListViewModel.PropertyListState(),
            onDelete = {},
            onSortProperties = {},
            onNavigateToAddPropertyScreen = {},
            onNavigateToDetailsPropertyScreen = {}
        )
    }
}