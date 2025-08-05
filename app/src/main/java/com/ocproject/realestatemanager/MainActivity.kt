package com.ocproject.realestatemanager

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.res.stringResource
import timber.log.Timber

class MainActivity : ComponentActivity() {

    private val permissions = arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
    )

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // LocationRequest - Requirements for the location updates, i.e.,
    // how often you should receive updates, the priority, etc.
    private lateinit var locationRequest: LocationRequest

    // LocationCallback - Called when FusedLocationProviderClient
    // has a new Location
    private lateinit var locationCallback: LocationCallback

    // This will store current location info
    private var currentLocation: Location? = null

    private var locationRequired: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge is only available on Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            enableEdgeToEdge()
        }
        setContent {
            var showPermissionResult by remember { mutableStateOf(false) }
            var currentPosition: LatLng? = null
            var currentPositionState by remember {
                mutableStateOf(currentPosition)
            }

            // Request location permission
            RequestLocationPermissionUsingRememberLauncherForActivityResult(
                onPermissionGranted = {
                    // Callback when permission is granted
                    showPermissionResult = true
                    // Attempt to get the current user location first
                    getCurrentLocation(
                        onGetCurrentLocationSuccess = {
                            // Current location obtained
                            currentPositionState = LatLng(it.first, it.second)
                        },
                        onGetCurrentLocationFailed = {
                            // Fallback to last known location if current location fails
                            getLastUserLocation(
                                onGetLastLocationSuccess = {
                                    currentPositionState = LatLng(it.first, it.second)
                                },
                                onGetLastLocationFailed = { exception ->
                                    // Both current and last location failed
                                    currentPosition = null
                                    showPermissionResult = true
                                },
                                onGetLastLocationIsNull = {
                                    // Last location is null, no location available
                                    currentPosition = null
                                    showPermissionResult = true
                                }
                            )
                        }
                    )
                },
                onPermissionDenied = {
                    // Callback when permission is denied
                    showPermissionResult = true
                },
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (showPermissionResult) {
                    RealEstateManagerApp(
                        currentLocation = currentPositionState,
                        darkTheme = isSystemInDarkTheme(),
                        dynamicColor = false,
                    )
                }
            }

        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastUserLocation(
        onGetLastLocationSuccess: (Pair<Double, Double>) -> Unit,
        onGetLastLocationFailed: (Exception) -> Unit,
        onGetLastLocationIsNull: () -> Unit
    ) {
        Timber.d("🔍 Attempting to get last known location...")
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // Check if location permissions are granted
        if (areLocationPermissionsGranted()) {
            // Retrieve the last known location
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        // If location is not null, invoke the success callback with latitude and longitude
                        onGetLastLocationSuccess(Pair(it.latitude, it.longitude))
                    }?.run {
                        onGetLastLocationIsNull()
                    }
                }
                .addOnFailureListener { exception ->
                    // If an error occurs, invoke the failure callback with the exception
                    onGetLastLocationFailed(exception)
                }
        } else {
        // Permissions not granted
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(
        onGetCurrentLocationSuccess: (Pair<Double, Double>) -> Unit,
        onGetCurrentLocationFailed: (Exception) -> Unit,
        priority: Boolean = true
    ) {
        // Initialize fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        
        // Determine the accuracy priority based on the 'priority' parameter
        val accuracy = if (priority) Priority.PRIORITY_HIGH_ACCURACY
        else Priority.PRIORITY_BALANCED_POWER_ACCURACY

        // Check if location permissions are granted
        if (areLocationPermissionsGranted()) {
            // Retrieve the current location asynchronously
            fusedLocationProviderClient.getCurrentLocation(
                accuracy, CancellationTokenSource().token,
            ).addOnSuccessListener { location ->
                location?.let {
                    // If location is not null, invoke the success callback with latitude and longitude
                    onGetCurrentLocationSuccess(Pair(it.latitude, it.longitude))
                }?.run {
                    onGetCurrentLocationFailed(Exception("Current location is null"))
                }
            }.addOnFailureListener { exception ->
                // If an error occurs, invoke the failure callback with the exception
                onGetCurrentLocationFailed(exception)
            }
        } else {
            // something
        }
    }

    private fun areLocationPermissionsGranted(): Boolean {
        return (ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED)
    }
}

@Composable
fun RequestLocationPermissionUsingRememberLauncherForActivityResult(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val context = LocalContext.current
    val activity = LocalContext.current as? ComponentActivity
    var showRationaleDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    
    // Check if permissions are already granted
    val arePermissionsAlreadyGranted = remember {
        ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    // Check if we should show rationale
    val shouldShowRationale = remember {
        activity?.let { act ->
            ActivityCompat.shouldShowRequestPermissionRationale(
                act,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                act,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } ?: false
    }
    
    // 1. Create a stateful launcher using rememberLauncherForActivityResult
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        // 2. Check if all requested permissions are granted
        val arePermissionsGranted = permissionsMap.values.reduce { acc, next ->
            acc && next
        }

        // 3. Handle permission results
        if (arePermissionsGranted) {
            onPermissionGranted.invoke()
        } else {
            // Check if user permanently denied permissions
            val permanentlyDenied = permissionsMap.any { (permission, granted) ->
                !granted && activity?.let { act ->
                    !ActivityCompat.shouldShowRequestPermissionRationale(act, permission)
                } ?: true
            }
            
            if (permanentlyDenied) {
                showSettingsDialog = true
            } else {
                onPermissionDenied.invoke()
            }
        }
    }

    // 4. Launch the permission request only if permissions are not already granted
    LaunchedEffect(Unit) {
        if (!arePermissionsAlreadyGranted) {
            if (shouldShowRationale) {
                showRationaleDialog = true
            } else {
                locationPermissionLauncher.launch(
                    arrayOf(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                    )
                )
            }
        } else {
            // If permissions are already granted, call the success callback
            onPermissionGranted.invoke()
        }
    }
    
    // Dialog to explain why we need location permission
    if (showRationaleDialog) {
        AlertDialog(
            onDismissRequest = { 
                showRationaleDialog = false
                onPermissionDenied.invoke()
            },
            title = { Text("Location required.") },
            text = { 
                Text(
                    stringResource(R.string.application_needs_your_location_to_show_you_on_map) +
                            stringResource(R.string.your_location_is_used_only_locally_and_never_shared)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRationaleDialog = false
                        locationPermissionLauncher.launch(
                            arrayOf(
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                            )
                        )
                    }
                ) {
                    Text("Authorise")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showRationaleDialog = false
                        onPermissionDenied.invoke()
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    
    // Dialog to guide user to settings if permissions are permanently denied
    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { 
                showSettingsDialog = false
                onPermissionDenied.invoke()
            },
            title = { Text("Permissions denied.") },
            text = { 
                Text(
                    "Location permissions have been definitely refused.\n\n" +
                    "To use application fully, " +
                    "please activate them in settings."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSettingsDialog = false
                        // Open app settings
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                        onPermissionDenied.invoke()
                    }
                ) {
                    Text("Settings")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showSettingsDialog = false
                        onPermissionDenied.invoke()
                    }
                ) {
                    Text("Proceed without location.")
                }
            }
        )
    }
}

