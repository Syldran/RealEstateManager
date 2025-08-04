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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CameraScreen(
    onPhotoCaptured: (ByteArray) -> Unit = {},
    globalSnackbarHostState: SnackbarHostState,
) {
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

    // Launcher for asking permissions
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(hasCameraPermission) {
        if (hasCameraPermission) {
            val cameraProvider = context.getCameraProvider()
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraxSelector, preview, imageCapture)
            preview.surfaceProvider = previewView.surfaceProvider
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
                    Text(stringResource(R.string.permissions_camera_required))
                    Button(
                        onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }
                    ) {
                        Text(stringResource(R.string.ask_permissions))
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }


private fun captureImage(
    imageCapture: ImageCapture,
    context: Context,
    onPhotoCaptured: (ByteArray) -> Unit
) {

    imageCapture.takePicture(ContextCompat.getMainExecutor(context), object :
        ImageCapture.OnImageCapturedCallback() {
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