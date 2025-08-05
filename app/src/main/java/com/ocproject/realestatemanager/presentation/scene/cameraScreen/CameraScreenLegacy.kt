package com.ocproject.realestatemanager.presentation.scene.cameraScreen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.hardware.Camera
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream

/**
 * Legacy camera implementation for Android 6.0+ using Camera API
 */
@Composable
fun CameraScreenLegacy(
    onPhotoCaptured: (ByteArray) -> Unit = {},
    navigateBack: () -> Unit = {},
) {
    val context = LocalContext.current
    var camera: Camera? by remember { mutableStateOf(null) }
    var hasPermission by remember { mutableStateOf(false) }
    
    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
        if (isGranted) {
            // Initialize camera after permission granted
            camera = initializeCamera(context)
        }
    }
    
    // Check permission on first launch
    LaunchedEffect(Unit) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                hasPermission = true
                camera = initializeCamera(context)
            }
            else -> {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
    
    // Cleanup camera on dispose
    DisposableEffect(Unit) {
        onDispose {
            camera?.release()
        }
    }
    
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (!hasPermission) {
            Text("Camera permission required")
            Button(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }) {
                Text("Grant Permission")
            }
        } else if (camera == null) {
            Text("Initializing camera...")
        } else {
            Text("Camera ready")
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = {
                    takePhoto(camera!!) { bitmap ->
                        val byteArray = bitmapToByteArray(bitmap)
                        onPhotoCaptured(byteArray)
                        navigateBack()
                    }
                }
            ) {
                Text("Take Photo")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(onClick = navigateBack) {
                Text("Cancel")
            }
        }
    }
}

private fun initializeCamera(context: Context): Camera? {
    return try {
        val camera = Camera.open()
        camera.setPreviewCallback { _, _ ->
            // Preview callback implementation if needed
        }
        camera
    } catch (e: Exception) {
        Log.e("CameraScreenLegacy", "Failed to initialize camera", e)
        null
    }
}

private fun takePhoto(camera: Camera, onPhotoTaken: (Bitmap) -> Unit) {
    try {
        camera.takePicture(null, null) { data, _ ->
            data?.let { bytes ->
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                // Rotate bitmap if needed
                val rotatedBitmap = rotateBitmap(bitmap, 90f)
                onPhotoTaken(rotatedBitmap)
            }
        }
    } catch (e: Exception) {
        Log.e("CameraScreenLegacy", "Failed to take photo", e)
    }
}

private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
    val matrix = Matrix().apply {
        postRotate(degrees)
    }
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
    return stream.toByteArray()
} 