//package com.ocproject.realestatemanager.presentation.utils
//
//package com.ocproject.realestatemanager.presentation.scene.propertylist
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.rounded.Add
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.FloatingActionButton
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.key
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberUpdatedState
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.ocproject.realestatemanager.domain.models.Property
//import com.ocproject.realestatemanager.presentation.navigation.isTablet
//import com.ocproject.realestatemanager.presentation.scene.propertydetails.PropertyDetailScreen
//import com.ocproject.realestatemanager.presentation.scene.propertylist.components.PropertyListItem
//import com.ocproject.realestatemanager.presentation.scene.propertylist.components.PropertyListTopBar
//import org.koin.androidx.compose.koinViewModel
//import org.koin.core.parameter.parametersOf
//
//@Composable
//fun PropertyListScreen(
//    viewModel: PropertyListViewModel = koinViewModel(),
//    onNavigateToAddPropertyScreen: (propertyId: Long?) -> Unit,
//    onNavigateToPropertyDetailScreen: (propertyId: Long) -> Unit
//) {
//    val state by viewModel.state.collectAsState()
//    var selectedPropertyId by remember { mutableStateOf<Long?>(null) }
//    val selectedPropertyState = rememberUpdatedState(selectedPropertyId)
//    Scaffold(
//        topBar = {
//            PropertyListTopBar(
//                onEvent = viewModel::onEvent,
//                onNavigateToAddPropertyScreen = onNavigateToAddPropertyScreen,
//                modifier = Modifier
//            )
//        },
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = { onNavigateToAddPropertyScreen(null) },
//                shape = RoundedCornerShape(20.dp)
//            ) {
//                Icon(
//                    imageVector = Icons.Rounded.Add,
//                    contentDescription = "Add property"
//                )
//            }
//        }
//    ) { paddingValues ->
//
//        if (isTablet()) {
//            Row(modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)) {
//                PropertyList(
//                    properties = state.properties,
//                    viewModel = viewModel,
//                    onPropertySelected = { selectedPropertyId = it }
//                )
//
//                Box(modifier = Modifier.weight(1f)) {
//                    selectedPropertyState.value?.let { propertyId ->
//                        PropertyDetailScreen(
//                            viewModel = koinViewModel(parameters = { parametersOf(propertyId) }),
//                            onNavigateToAddPropertyScreen = onNavigateToAddPropertyScreen,
//                            onNavigateToPropertyListScreen = { selectedPropertyId = null }
//                        )
//                    } ?: Text(
//                        text = "Sélectionnez une propriété",
//                        modifier = Modifier.align(Alignment.Center),
//                        fontSize = 20.sp
//                    )
//                }
//            }
//        } else {
//            LazyColumn(
//                modifier = Modifier.fillMaxSize(),
//                contentPadding = paddingValues,
//            ) {
//                item {
//                    Text(
//                        text = "List of properties (${state.properties.size})",
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp),
//                        fontWeight = FontWeight.Bold
//                    )
//                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
//                }
//
//                items(
//                    items = state.properties,
//                    key = { property -> property.id }
//                ) { propertyWithPhotos ->
//                    PropertyListItem(
//                        viewModel = viewModel,
//                        propertyWithPhotos = propertyWithPhotos,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable {
//                                onNavigateToPropertyDetailScreen(propertyWithPhotos.id)
//                            }
//                            .padding(start = 16.dp, end = 16.dp),
//                        onEvent = viewModel::onEvent
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun PropertyList(
//    properties: List<Property>,
//    onPropertySelected: (Long) -> Unit,
//    viewModel: PropertyListViewModel
//) {
//    LazyColumn(
//        modifier = Modifier.fillMaxWidth(0.3f)
//    ) {
//        items(
//            items = properties,
//            key = { it.id }
//        ) { propertyWithPhotos ->
//            PropertyListItem(
//                propertyWithPhotos = propertyWithPhotos,
//                viewModel = viewModel,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable {
//                        onPropertySelected(propertyWithPhotos.id)
//                    }
//                    .padding(start = 16.dp, end = 16.dp),
//                onEvent = {}
//            )
//        }
//    }
//}
//
//@Composable
//fun isTablet(): Boolean {
//    val configuration = LocalConfiguration.current
//    val screenWidth = configuration.screenWidthDp.dp
//    return screenWidth >= 600.dp
//}