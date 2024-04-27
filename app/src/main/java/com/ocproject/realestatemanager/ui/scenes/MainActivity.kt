package com.ocproject.realestatemanager.ui.scenes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.android.libraries.places.api.Places
import com.ocproject.realestatemanager.ui.Navigation
import com.ocproject.realestatemanager.ui.theme.RealestatemanagerTheme
import org.koin.compose.KoinContext

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null
)

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Places.isInitialized()) Places.initialize(applicationContext,"AIzaSyDCXBbnL9Tw5L_0G6MMtr-F7ibrX-oAx40")
        // Initialize the AutocompleteSupportFragment.

//        val autocompleteFragment =  supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
//
//        // Specify the types of place data to return.
//        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))
//
////         Set up a PlaceSelectionListener to handle the response.
//        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
//            override fun onPlaceSelected(place: Place) {
//                // TODO: Get info about the selected place.
//                Log.i("TAG", "Place: ${place.name}, ${place.id}")
//            }
//
//            override fun onError(status: Status) {
//                // TODO: Handle the error.
//                Log.i("TAG", "An error occurred: $status")
//            }
//        })
        if (!Places.isInitialized()) {
            Places.initializeWithNewPlacesApiEnabled(
                this,
                "AIzaSyDBwjoGiscmPsKDCG7Z5knq7cCZIBlVkMU"
            )
        }



        setContent {


            RealestatemanagerTheme {
                KoinContext() {


                    Navigation()
                }

            }
        }
    }
}