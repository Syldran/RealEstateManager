package com.ocproject.realestatemanager.presentation.scene.cameraScreen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.ocproject.realestatemanager.R
import com.ocproject.realestatemanager.core.GlobalSnackBarManager
import com.ocproject.realestatemanager.presentation.scene.cameraScreen.utils.bitmapToByteArray
import com.ocproject.realestatemanager.presentation.scene.cameraScreen.utils.imageProxyToBitmapWithRotation
import com.ocproject.realestatemanager.presentation.scene.cameraScreen.CameraScreenLegacy
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import androidx.core.app.ActivityCompat
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.activity.ComponentActivity

@Composable
fun CameraScreen(
    onPhotoCaptured: (ByteArray) -> Unit = {},
    navigateBack: () -> Unit = {},
) {
    // Use legacy camera for Android 6.0-7.1, CameraX for Android 8.0+
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        CameraScreenLegacy(
            onPhotoCaptured = onPhotoCaptured,
            navigateBack = navigateBack
        )
        return
    }
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val preview = Preview.Builder().build()
    val previewView = remember {
        PreviewView(context)
    }
    val cameraxSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
    val imageCapture = remember {
        ImageCapture.Builder().build()
    }

    // state for permissions
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    
    var showCameraRationaleDialog by remember { mutableStateOf(false) }
    var showCameraSettingsDialog by remember { mutableStateOf(false) }
    val activity = LocalContext.current as? ComponentActivity

    // Launcher for asking permissions
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (!isGranted) {
            // Check if user permanently denied camera permission
            if (activity?.let { act ->
                !ActivityCompat.shouldShowRequestPermissionRationale(act, Manifest.permission.CAMERA)
            } ?: true) {
                showCameraSettingsDialog = true
            }
        }
    }

    // Only request camera permission if not already granted
    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            if (activity?.let { act ->
                ActivityCompat.shouldShowRequestPermissionRationale(act, Manifest.permission.CAMERA)
            } ?: false) {
                showCameraRationaleDialog = true
            } else {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    LaunchedEffect(hasCameraPermission) {
        if (hasCameraPermission) {
            val cameraProvider = context.getCameraProvider()
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraxSelector, preview, imageCapture)
            preview.setSurfaceProvider(previewView.surfaceProvider)
        }
    }

    Column {
        if (hasCameraPermission) {
            Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.weight(4f))
            {
                AndroidView({ previewView }, modifier = Modifier.fillMaxSize())
                Button(
                    onClick = {
                        captureImage(imageCapture, context) { photoBytes ->
                            onPhotoCaptured(photoBytes)
                        }
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(text = stringResource(R.string.capture_image))
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Navigation button similar to PropertyDetails
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        FilledTonalIconButton(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            onClick = { navigateBack() },
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                    }
                    Text(stringResource(R.string.permissions_camera_required))
                }
            }
        }
    }
    
    // Dialog to explain why we need camera permission
    if (showCameraRationaleDialog) {
        AlertDialog(
            onDismissRequest = { showCameraRationaleDialog = false },
            title = { Text("Camera required") },
            text = { 
                Text(
                    "Application need your phone camera to :\n\n" +
                    "• capture photo of properties.\n\n" +
                    "Photos are saved locally and your device."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCameraRationaleDialog = false
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                ) {
                    Text("Authorise")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showCameraRationaleDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    
    // Dialog to guide user to settings if camera permission is permanently denied
    if (showCameraSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showCameraSettingsDialog = false },
            title = { Text("Permission caméra refusée") },
            text = { 
                Text(
                    "Access has been denied for good.\n\n" +
                    "To capture photo of property , " +
                    "please enable use in settings."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCameraSettingsDialog = false
                        // Open app settings
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                    }
                ) {
                    Text("Settings")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showCameraSettingsDialog = false }
                ) {
                    Text("Proceed without camera")
                }
            }
        )
    }
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ContextCompat.getMainExecutor(this)
            } else {
                java.util.concurrent.Executors.newSingleThreadExecutor()
            })
        }
    }


private fun captureImage(
    imageCapture: ImageCapture,
    context: Context,
    onPhotoCaptured: (ByteArray) -> Unit
) {

    imageCapture.takePicture(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ContextCompat.getMainExecutor(context)
        } else {
            java.util.concurrent.Executors.newSingleThreadExecutor()
        }, 
        object : ImageCapture.OnImageCapturedCallback() {
        override fun onCaptureSuccess(image: ImageProxy) {
            // Convert image to Bitmap with proper rotation correction
            val bitmap = imageProxyToBitmapWithRotation(image)

            // Convert bitmap to ByteArray
            val photoBytes = bitmapToByteArray(bitmap)

            // Recycle the bitmap to free memory
            bitmap.recycle()

            // Return photo by Callback
            onPhotoCaptured(photoBytes)

            // Show success message using GlobalSnackBar
            GlobalSnackBarManager.showSnackMsg(
                context.getString(R.string.photo_captured_successfully),
                isSuccess = true
            )
            
            super.onCaptureSuccess(image)
            image.close()
        }

        override fun onError(exception: ImageCaptureException) {
            // Show error message using GlobalSnackBar
            GlobalSnackBarManager.showSnackMsg(
                context.getString(R.string.capture_failed),
                isSuccess = false
            )
            
            super.onError(exception)
        }
    })
}