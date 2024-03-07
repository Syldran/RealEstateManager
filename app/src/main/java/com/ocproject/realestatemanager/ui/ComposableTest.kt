@file:OptIn(ExperimentalMaterial3Api::class)

package com.ocproject.realestatemanager.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ocproject.realestatemanager.ui.scenes.NavigationItem
import com.ocproject.realestatemanager.R
import kotlinx.coroutines.launch

data class DropDownItem(
    val text: String,
)

@Composable
fun PersonItem(
    personName: String,
    dropdownItems: List<DropDownItem>,
    modifier: Modifier = Modifier,
    onItemClick: (DropDownItem) -> Unit,
) {
    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    val interactionSource =
        remember {
            MutableInteractionSource()
        }
    val density = LocalDensity.current
    Card(
//        elevation = elevation,
        modifier =
        modifier.onSizeChanged {
            itemHeight = with(density) { it.height.toDp() }
        },
    ) {
        Box(
            modifier =
            Modifier
                .fillMaxWidth()
                .indication(interactionSource, LocalIndication.current)
                .pointerInput(true) {
                    detectTapGestures(onLongPress = {
                        isContextMenuVisible = true
                        pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                    }, onPress = {
                        val press = PressInteraction.Press(it)
                        interactionSource.emit(press)
                        tryAwaitRelease()
                        interactionSource.emit(PressInteraction.Release(press))
                    })
                }
                .background(Color.Magenta)
                .padding(16.dp),
        ) {
            Row {
                Icon(Icons.Outlined.Person, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = personName, color = Color.White, fontSize = 18.sp)
            }
        }
        DropdownMenu(
            expanded = isContextMenuVisible,
            onDismissRequest = {
                isContextMenuVisible = false
            },
            offset =
            pressOffset.copy(
                y = pressOffset.y - itemHeight,
            ),
        ) {
            dropdownItems.forEach {
                DropdownMenuItem(text = { Text(text = it.text) }, onClick = {
                    onItemClick(it)
                    isContextMenuVisible = false
                })
            }
        }
    }
}

@Composable
fun ComposeContextDropDownExampleUI() {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            listOf(
                "Surya",
                "Morris",
                "Shaw",
                "Gill",
                "Jane",
                "Alex",
                "Jake",
                "Surya",
                "Surya",
            )
        ) {
            PersonItem(
                personName = it, dropdownItems = listOf(
                    DropDownItem("Edit"),
                    DropDownItem("Delete"),
                    DropDownItem("Share"),
                ), onItemClick = {
                    Toast.makeText(context, it.text, Toast.LENGTH_LONG).show()
                }, modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ScrollableList() {
    LazyColumn {
        items(500) {
            Text(
                text = "Item $it",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
            )
        }
    }
}

@Composable
fun ScrollableList2() {
    LazyColumn {
        itemsIndexed(
            listOf("this", "is", "jetpack", "compose"),
        ) { index, item ->
            Text(
                text = item,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
            )
        }
    }
}

@Composable
fun Item() {
    Column {
        ListItem(
            headlineContent = { Text("Headline: Three line list item") },
            supportingContent = {
                Text("SupportingContent: Secondary text that is long and perhaps goes onto another line")
            },
            overlineContent = { Text(text = "Overline") },
            leadingContent = {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "Localized description",
                )
            },
            trailingContent = {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.Cyan),
                    contentAlignment = Alignment.Center
                )
                {
                    Text(
                        "trailingContent",
                        modifier = Modifier.background(Color.Red),
                    )
                }
            },
        )
        Divider()
    }
}

@Composable
fun SmallTopAppBarExample() {
    Scaffold(
        topBar = {
            TopAppBar(
                colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Small Top App Bar")
                },
            )
        },
    ) { innerPadding ->
        ScrollContent(innerPadding)
    }
}

@Composable
fun CenterAlignedTopAppBarExample() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "Centered Top App Bar",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Localized description",
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description",
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        content = { paddingValues -> ScrollContent(innerPadding = paddingValues) },
    )
}

@Composable
fun DMenu() {
    var expanded by remember { mutableStateOf(false) }
    Column {
        Text("Some text")
        Box {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "more options button",
                modifier = Modifier
                    .clickable {
                        expanded = true
                    }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                DropdownMenuItem(onClick = { expanded = false }, text = { Text("Add property") })
                DropdownMenuItem(
                    onClick = { expanded = false },
                    text = { Text("Display criteria") })
            }
        }
    }
}

@Composable
fun Test() {
    var context = LocalContext.current
    var menuExpanded by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Simple TopAppBar",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description",
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {

                    }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Localized description",
                        )
                    }
                },
            )
        },
        content = { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val list = (0..75).map { it.toString() }
                items(count = list.size) {
                    Text(
                        text = list[it],
                        style = MaterialTheme.typography.bodyLarge,
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }
            }
        },
    )
}

@Composable
fun ScrollContent(innerPadding: PaddingValues) {
    val range = 1..100

    LazyColumn(
        modifier =
        Modifier
            .fillMaxSize(),
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(range.count()) { index ->
            Text(text = "- List item number ${index + 1}")
        }
    }
}


//@Composable
//fun TestMenu(navControler: NavController) {
//    val context = LocalContext.current
////    val intent = Intent(context, AddProperty::class.java)
//    Scaffold(
//        topBar = {
//            HomeTopAppBar(
//                navControler = navControler,
//                modifier = Modifier.fillMaxWidth()
//            )
//        },
//        content = { innerPadding ->
//            LazyColumn(
//                contentPadding = innerPadding,
//                verticalArrangement = Arrangement.spacedBy(8.dp),
//            ) {
//                val list = (0..75).map { it.toString() }
//                items(count = list.size) {
////                    ListItem(headlineContent = {
//                    Text(
//                        text = "Property ${list[it]}",
//                        style = MaterialTheme.typography.bodyLarge,
//                        modifier = Modifier
//                            .background(Color.Magenta)
//                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp)
//                            .clickable(onClick = { navControler.navigate(Screen.AddProperty.route) }),
//                        textAlign = TextAlign.Center
//                    )
////                    })
//                }
//            }
//        }
//    ) /*{
//            paddingValues ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues),
//            contentAlignment = Alignment.Center,
//        ) {
//
//        }
//    }*/
//}

/*
@Composable
fun HomeTopAppBar(
    navControler: NavController,
    modifier: Modifier = Modifier,
) {
    var context = LocalContext.current
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
                        Toast.makeText(context, "Add Property", Toast.LENGTH_LONG).show()
                        navControler.navigate(Screen.AddProperty.route)
                    },
                )
                DropdownMenuItem(
                    text = {
                        Text("Display Criteria")
                    },
                    onClick = {
                        Toast.makeText(context, "Display Criteria", Toast.LENGTH_LONG).show()
                    },
                )
                DropdownMenuItem(
                    text = {
                        Text("About")
                    },
                    onClick = { */
/* TODO *//*
 },
                )
            }
        },
        modifier = modifier
    )

}
*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavDrawer() {
    val items = listOf(
        NavigationItem(
            title = "All",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
        ),
        NavigationItem(
            title = "Urgent",
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info,
            badgeCount = 45
        ),
        NavigationItem(
            title = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
        ),
    )

    Surface(modifier = Modifier.fillMaxSize()) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var selectedItemIndex by rememberSaveable {
            mutableStateOf(0)
        }
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    Spacer(modifier = Modifier.height(16.dp))
                    items.forEachIndexed { index, item ->
                        NavigationDrawerItem(
                            label = {
                                Text(text = item.title)
                            },
                            selected = index == selectedItemIndex,
                            onClick = {
//                                            navController.navigate(item.route)
                                selectedItemIndex = index
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            },
                            badge = {
                                item.badgeCount?.let {
                                    Text(text = item.badgeCount.toString())
                                }
                            },
                            modifier = Modifier
                                .padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            },
            drawerState = drawerState
        ) {

            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(text = "Todo App")
                        },
                        actions = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu",
                                )
                            }
                        }
                    )
                }
            ) {paddingValues ->
                val x = paddingValues
            }


        }

    }
}

//@Preview(showBackground = true)
//@Composable
//fun ComposablePreview() {
//    TestMenu()
//}