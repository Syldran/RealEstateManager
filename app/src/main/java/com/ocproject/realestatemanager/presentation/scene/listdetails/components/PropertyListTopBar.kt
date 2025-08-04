package com.ocproject.realestatemanager.presentation.scene.listdetails.components

import android.content.res.Configuration
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.ocproject.realestatemanager.R
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
                text = stringResource(R.string.top_bar_title)
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
                        contentDescription = stringResource(R.string.details_displayed)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.LocationOn,
                        contentDescription = stringResource(R.string.map_displayed)
                    )
                }
            }
        },
        actions = {

            IconButton(
                onClick = { onNavigateToAddPropertyScreen(null) },
                modifier.testTag(stringResource(R.string.topbar_add_test_tag))
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.icon_add_content_description),
                )
            }
            IconButton(
                onClick = { menuExpanded = true },
                modifier = Modifier.testTag(stringResource(R.string.topbar_menu_icon_test_tag))
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = stringResource(R.string.appbar_menu_icon_content_description),
                )
            }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
            ) {
                val configuration = LocalConfiguration.current
                if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(R.string.map_geolocation_item_menu_appbar),
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily
                            )
                        },
                        onClick = {
                            onNavigateToMapOfProperties()
                            menuExpanded = false
                        },
                    )
                }
                DropdownMenuItem(
                    text = {
                        Text(
                            stringResource(R.string.display_criteria_item_menu_appbar),
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                            fontFamily = MaterialTheme.typography.bodyMedium.fontFamily
                        )
                    },
                    onClick = {
                        onEvent(ListDetailsEvent.OpenFilter)
                        menuExpanded = false
                    },
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            stringResource(R.string.funding_item_menu_appbar),
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                            fontFamily = MaterialTheme.typography.bodyMedium.fontFamily
                        )
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