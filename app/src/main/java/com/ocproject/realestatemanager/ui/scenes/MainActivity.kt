package com.ocproject.realestatemanager.ui.scenes

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.rememberNavController
import com.android.volley.BuildConfig
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.ocproject.realestatemanager.BuildConfig.PLACES_API_KEY
import com.ocproject.realestatemanager.R
import com.ocproject.realestatemanager.ui.Navigation
import com.ocproject.realestatemanager.ui.scenes.addproperty.AddPropertyViewModel
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