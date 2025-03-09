package com.ocproject.realestatemanager.presentation.scene.propertylist.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import com.ocproject.realestatemanager.presentation.scene.propertylist.PropertyListEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyListTopBar(
    onEvent: (PropertyListEvent) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToAddPropertyScreen: (propertyId: Long?) -> Unit,
    onNavigateToMapOfProperties: () -> Unit,
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
                text = "My App"
            )
        },
        actions = {
            IconButton(onClick = { menuExpanded = true }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Localized description",
                )
            }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
            ) {
                // 6
                DropdownMenuItem(
                    text = {
                        Text("Add Property")
                    },
                    onClick = {
                        menuExpanded = false
                        onNavigateToAddPropertyScreen(null)
                    },
                )
                DropdownMenuItem(
                    text = {
                        Text("Display Criteria")
                    },
                    onClick = {
                        onEvent(PropertyListEvent.OpenFilter)
                        menuExpanded = false
                    },
                )
                DropdownMenuItem(
                    text = {
                        Text("Map")
                    },
                    onClick = {
                        onNavigateToMapOfProperties()
                        menuExpanded = false
                    },
                )
            }
        },
        modifier = modifier
    )

}