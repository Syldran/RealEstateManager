package com.ocproject.realestatemanager.presentation.scene.propertylist.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsEvent
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsEvent.OnClickPropertyDisplayMode
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyListTopBar(
    state: ListDetailsState,
    onEvent: (ListDetailsEvent) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToAddPropertyScreen: (propertyId: Long?) -> Unit,
    onNavigateToMapOfProperties: () -> Unit,
    onNavigateToFundingScreen: () -> Unit,
) {
    var menuExpanded by remember {
        mutableStateOf(false)
    }

    CenterAlignedTopAppBar(
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
        title = {
            Text(
                text = "Real Estate Manager"
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    onEvent(OnClickPropertyDisplayMode(map = !state.mapMode))
                },
            ) {
                if (state.mapMode) {
                    Icon(
                        imageVector = Icons.Rounded.Info,
                        contentDescription = "detailMode"
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.LocationOn,
                        contentDescription = "mapMode"
                    )
                }
            }
        },
        actions = {

            IconButton(
                onClick = { onNavigateToAddPropertyScreen(null) },
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Property",
                )
            }
            IconButton(
                onClick = { menuExpanded = true },
                modifier = Modifier.testTag("topBarMenuIcon")
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "AppBarMenu",
                )
            }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
            ) {
//                DropdownMenuItem(
//                    modifier = Modifier.testTag("menuItemAdd"),
//                    text = {
//                        Text("Add Property")
//                    },
//                    onClick = {
//                        menuExpanded = false
//                        onNavigateToAddPropertyScreen(null)
//                    },
//                )
                DropdownMenuItem(
                    text = {
                        Text("Display Criteria")
                    },
                    onClick = {
                        onEvent(ListDetailsEvent.OpenFilter)
                        menuExpanded = false
                    },
                )
                DropdownMenuItem(
                    text = {
                        Text("Funding")
                    },
                    onClick = {
                        onNavigateToFundingScreen()
                        menuExpanded = false
                    },
                )
            }
        },
        modifier = modifier
    )

}