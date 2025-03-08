package com.ocproject.realestatemanager

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ocproject.realestatemanager.presentation.navigation.Screen
import com.ocproject.realestatemanager.presentation.scene.addproperty.addPropertyScreen
import com.ocproject.realestatemanager.core.ui.theme.RealestatemanagerTheme
import com.ocproject.realestatemanager.presentation.scene.listdetails.listDetails
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

        val navController = rememberNavController()
        KoinContext {
            NavHost(
                navController = navController,
                startDestination = Screen.ListDetails.route
//                startDestination = Screen.PropertyListScreen.route
            ) {
                listDetails(navController)
//                propertyListScreen(navController)
                addPropertyScreen(navController)
//                propertyDetailsScreen(navController)
            }
        }
    }
}

//@Parcelize
//class Item(val id: Int) : Parcelable
//
//@Composable
//fun DetailPane(item: Item, navigateBack: () -> Unit) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(color = Color.Red.copy(alpha = 0.1f))
//            .padding(20.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        IconButton(
//            onClick = navigateBack
//        ) {
//            Icon(
//                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
//                contentDescription = null
//            )
//        }
//        Text(
//            text = "Item: ${item.id}",
//            fontSize = 32.sp,
//            fontWeight = FontWeight.Bold
//        )
//    }
//}

//@Composable
//fun ListPane(onClick: (Item) -> Unit) {
//    LazyColumn(
//        modifier = Modifier.fillMaxSize(),
//        contentPadding = PaddingValues(20.dp)
//    ) {
//        items(10) { index ->
//            Text(
//                text = "Index: $index",
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier
//                    .fillParentMaxWidth()
//                    .clickable { onClick(Item(id = index)) }
//                    .padding(20.dp)
//            )
//
//        }
//    }
//}