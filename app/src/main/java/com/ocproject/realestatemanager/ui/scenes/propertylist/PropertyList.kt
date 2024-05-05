package com.ocproject.realestatemanager.ui.scenes.propertylist

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ocproject.realestatemanager.R
import com.ocproject.realestatemanager.ui.scenes.propertylist.components.BottomSheetFilter
import com.ocproject.realestatemanager.ui.scenes.propertylist.components.PropertyListTopBar
import com.ocproject.realestatemanager.ui.theme.RealestatemanagerTheme
import com.openclassrooms.realestatemanager.models.Property
import org.koin.androidx.compose.koinViewModel

@Composable
fun PropertyList(
    viewModel: PropertyListViewModel = koinViewModel(),
    onNavigateToAddPropertyScreen: (propertyId: Int?) -> Unit,
    onNavigateToDetailsPropertyScreen: (propertyId: Int) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    ListPropertiesScreen(
        state = state,
        onSortProperties = {
            viewModel.onEvent(PropertyListEvent.SortProperties(it))
        },
        onDelete = {
            viewModel.onEvent(PropertyListEvent.DeleteProperty(it))
        },
        onFilter = {
            viewModel.onEvent(PropertyListEvent.OpenFilter(it))
        },
        onNavigateToAddPropertyScreen = onNavigateToAddPropertyScreen,
        onNavigateToDetailsPropertyScreen = onNavigateToDetailsPropertyScreen,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListPropertiesScreen(
    state: PropertyListState,
    onDelete: (property: Property) -> Unit,
    onSortProperties: (sortType: SortType) -> Unit,
    onFilter: (filterState: Boolean) -> Unit,
    onNavigateToAddPropertyScreen: (propertyId: Int?) -> Unit,
    onNavigateToDetailsPropertyScreen: (propertyId: Int) -> Unit,
) {
    Scaffold(
        topBar = {
            PropertyListTopBar(
                onFilter,
                modifier = Modifier.fillMaxWidth(),
                onNavigateToAddPropertyScreen = onNavigateToAddPropertyScreen
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onNavigateToAddPropertyScreen(null)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Property"
                )

            }
        },

        ) { padding ->
        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()

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
                    SortType.entries.forEach { sortType ->
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
                HorizontalDivider()
            }
            if (state.isLoading) {
//                CircularProgressIndicator(
//                    modifier = Modifier.width(64.dp),
//                    color = MaterialTheme.colorScheme.secondary,
//                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
//                )
                // afficher le loader
            } else {
                items(items = state.properties, key = {property -> property.property.id}) { property ->
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable(onClick = {
                                    onNavigateToDetailsPropertyScreen(property.property.id)
                                })
                        ) {
                            Row(modifier = Modifier.weight(0.25f)) {
                                //default img to add to ressources
                                var uriMain:String? = null
                                property.pictureList?.forEach {
                                    if (it.isMain) uriMain = it.uri
                                }
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(uriMain?: R.drawable.baseline_image).build(),
                                    contentDescription = null,
                                    contentScale = ContentScale.FillHeight,
                                    modifier = Modifier
                                        .weight(0.25f)
                                        .border(
                                            2.dp,
                                            shape = RectangleShape,
                                            color = Color.Black
                                        )
                                        .height(64.dp)
                                        .align(CenterVertically)
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(0.75f)
                                    .padding(start = 8.dp),
                            ) {

                                Text(
                                    text = "id : ${property.property.id}",
                                    fontSize = 14.sp,
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
                        HorizontalDivider()
                    }
                }
            }
        }

        when {
            state.openFilterState -> {
                BottomSheetFilter(
                    onDismissRequest = { onFilter(false) },
                    sheetState = sheetState,
                    scope = scope
                )
            }

        }
    }
}

@Preview
@Composable
private fun PropertiesPreview() {
    RealestatemanagerTheme {
        ListPropertiesScreen(
            state = PropertyListState(),
            onDelete = {},
            onSortProperties = {},
            onFilter = {},
            onNavigateToAddPropertyScreen = {},
            onNavigateToDetailsPropertyScreen = {}
        )
    }
}