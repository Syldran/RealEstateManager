package com.ocproject.realestatemanager.presentation.scene.cameraScreen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CameraScreen(
    viewModel: CameraViewModel = koinViewModel(),
    onPhotoCaptured: (ByteArray) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
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
    var bitmap: Bitmap? = null

    // État pour la permission
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Launcher pour demander la permission
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
            preview.setSurfaceProvider(previewView.surfaceProvider)
        }
    }

    Column {
        if (hasCameraPermission) {
            Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.weight(4f)) {
                AndroidView({ previewView }, modifier = Modifier.fillMaxSize())
                Button(
                    onClick = {
                        captureImage(imageCapture, context, state, viewModel) { photoBytes ->
                            onPhotoCaptured(photoBytes)
                        }
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(text = "Capture Image")
                }
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Black)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Center
            ) {
                if (state.imageTaken != null) {
                    // Image(bitmap = state.imageTaken!!, contentDescription = "Image Taken")
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
                    Text("Permission de caméra requise")
                    Button(
                        onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }
                    ) {
                        Text("Demander la permission")
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
    state: CameraState,
    viewModel: CameraViewModel,
    onPhotoCaptured: (ByteArray) -> Unit
) {

    imageCapture.takePicture(ContextCompat.getMainExecutor(context), object :
        ImageCapture.OnImageCapturedCallback() {
        override fun onCaptureSuccess(image: ImageProxy) {
            // Convertir l'image en ByteArray
            val buffer = image.planes[0].buffer
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)

            // Retourner la photo via le callback
            onPhotoCaptured(bytes)

            val toast = Toast.makeText(context, "Photo capturée avec succès", Toast.LENGTH_LONG)
            toast.show()
            super.onCaptureSuccess(image)
            image.close()
        }

        override fun onError(exception: ImageCaptureException) {
            val toast = Toast.makeText(context, "Erreur lors de la capture", Toast.LENGTH_LONG)
            toast.show()
            super.onError(exception)
        }
    })
}
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun CameraScreen(
////    viewModel: CameraViewModel = koinViewModel<CameraViewModel>(),
//    propertyId: Long? = null,
//    viewModel: AddPropertyViewModel = koinViewModel(parameters = { parametersOf(propertyId) })
//) {
//    val state by viewModel.state.collectAsState()
//    val lensFacing = CameraSelector.LENS_FACING_BACK
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val context = LocalContext.current
//    val preview = Preview.Builder().build()
//    val previewView = remember {
//        PreviewView(context)
//    }
//    val cameraxSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
//    val imageCapture = remember {
//        ImageCapture.Builder().build()
//    }
//    var bitmap: Bitmap? = null
//    LaunchedEffect(lensFacing) {
//        val cameraProvider = context.getCameraProvider()
//        cameraProvider.unbindAll()
//        cameraProvider.bindToLifecycle(lifecycleOwner, cameraxSelector, preview, imageCapture)
//        preview.setSurfaceProvider(previewView.surfaceProvider)
//    }
//    Column {
//        Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.weight(4f)) {
//            AndroidView({ previewView }, modifier = Modifier.fillMaxSize())
//            Button(
//                onClick = { captureImage(imageCapture, context /*state, viewModel*/) },
//                modifier = Modifier.padding(vertical = 8.dp)
//            ) {
//                Text(text = "Capture Image")
//            }
//        }
//        Row(
//            modifier = Modifier
//                .weight(1f)
//                .background(Color.Black)
//                .fillMaxSize(),
//            horizontalArrangement = Arrangement.Center
//        ) {
////            if (state.imageTaken != null) {
////                Image(bitmap = state.imageTaken!!, contentDescription = "Image Taken", /*modifier = Modifier.rotate(90f), contentScale = ContentScale.FillHeight*/)
////            }
//        }
//    }
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
//    suspendCoroutine { continuation ->
//        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
//            cameraProvider.addListener({
//                continuation.resume(cameraProvider.get())
//            }, ContextCompat.getMainExecutor(this))
//        }
//    }
//
//
//private fun captureImage(
//    imageCapture: ImageCapture,
//    context: Context, /*state: CameraState, viewModel: CameraViewModel*/
//) {
//
//    imageCapture.takePicture(ContextCompat.getMainExecutor(context), object :
//        ImageCapture.OnImageCapturedCallback() {
//        override fun onCaptureSuccess(image: ImageProxy) {
//            //get bitmap from image
////            viewModel.onEvent(AEvent.OnImageTaken(imageProxyToBitmap(image).asImageBitmap()))
//            val toast = Toast.makeText(context, "success", Toast.LENGTH_LONG)
//            toast.show()
//            super.onCaptureSuccess(image)
//            image.close()
//
//        }
//
//        override fun onError(exception: ImageCaptureException) {
//            super.onError(exception)
//        }
//
//    })
//    var bitmap: Bitmap? = null
//    val contentValues = ContentValues().apply {
//        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
//        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
//            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
//        }
//    }
//    val outputOptions = ImageCapture.OutputFileOptions
//        .Builder(
//            context.contentResolver,
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//            contentValues
//        )
//        .build()
//    imageCapture.takePicture(
//        outputOptions,
//        ContextCompat.getMainExecutor(context),
//        object : ImageCapture.OnImageSavedCallback {
//            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
//                val toast = Toast.makeText(context, "success", Toast.LENGTH_LONG)
//                toast.show()
//            }
//
//            override fun onError(exception: ImageCaptureException) {
//                println("Failed $exception")
//            }
//
//        })
