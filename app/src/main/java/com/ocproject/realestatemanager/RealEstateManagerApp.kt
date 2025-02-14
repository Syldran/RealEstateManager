package com.ocproject.realestatemanager
import android.os.Parcelable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ocproject.realestatemanager.presentation.navigation.Screen
import com.ocproject.realestatemanager.presentation.scene.addproperty.addPropertyScreen
import com.ocproject.realestatemanager.presentation.scene.propertydetails.addPropertyDetailsScreen
import com.ocproject.realestatemanager.presentation.scene.propertylist.addPropertyListScreen
import com.ocproject.realestatemanager.core.ui.theme.RealestatemanagerTheme
import com.ocproject.realestatemanager.presentation.scene.propertylist.PropertyListScreen
import kotlinx.parcelize.Parcelize
import org.koin.compose.KoinContext


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun RealEstateManagerApp(
    darkTheme: Boolean,
    dynamicColor: Boolean,
) {
    RealestatemanagerTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
    ) {
        val navigator = rememberListDetailPaneScaffoldNavigator<Any>()

        NavigableListDetailPaneScaffold(
            navigator = navigator,
            listPane = {
                ListPane(
                    onClick = { item ->
                        navigator.navigateTo(
                            ListDetailPaneScaffoldRole.Detail,
                            content = item
                        )
                    }
                )
                PropertyListScreen(
                    onClick = {

                    })
            },
            detailPane = {
                AnimatedPane {
                    navigator.currentDestination?.content?.let { item ->
                        DetailPane(
                            item = item as Item,
                            navigateBack = {
                                navigator.navigateBack()
                            }
                        )
                    }
                }
            },
        )

        val navController = rememberNavController()
        KoinContext {
            NavHost(
                navController = navController,
                startDestination = Screen.PropertyListScreen.route
            ){
                addPropertyListScreen(navController)
                addPropertyScreen(navController)
                addPropertyDetailsScreen(navController)
            }
        }
    }
}

@Parcelize
class Item(val id: Int) : Parcelable

@Composable
fun DetailPane(item: Item, navigateBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Red.copy(alpha = 0.1f))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconButton(
            onClick = navigateBack
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = null
            )
        }
        Text(
            text = "Item: ${item.id}",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ListPane(onClick: (Item) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp)
    ) {
        items(10) { index ->
            Text(
                text = "Index: $index",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillParentMaxWidth()
                    .clickable { onClick(Item(id = index)) }
                    .padding(20.dp)
            )

        }
    }
}