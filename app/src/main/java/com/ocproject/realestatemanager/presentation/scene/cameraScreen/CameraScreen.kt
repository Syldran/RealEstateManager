package com.ocproject.realestatemanager.presentation.scene.cameraScreen

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CameraScreen(
//    viewModel: CameraViewModel = koinViewModel<CameraViewModel>(),
    viewModel: AddPropertyViewModel= koinViewModel()
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
    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraxSelector, preview, imageCapture)
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }
    Column {
        Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.weight(4f)) {
            AndroidView({ previewView }, modifier = Modifier.fillMaxSize())
            Button(
                onClick = { captureImage(imageCapture, context, /*state, viewModel*/) },
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
//            if (state.imageTaken != null) {
//                Image(bitmap = state.imageTaken!!, contentDescription = "Image Taken", /*modifier = Modifier.rotate(90f), contentScale = ContentScale.FillHeight*/)
//            }
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


private fun captureImage(imageCapture: ImageCapture, context: Context, /*state: CameraState, viewModel: CameraViewModel*/) {

    imageCapture.takePicture(ContextCompat.getMainExecutor(context), object :
        ImageCapture.OnImageCapturedCallback() {
        override fun onCaptureSuccess(image: ImageProxy) {
            //get bitmap from image
//            viewModel.onEvent(AEvent.OnImageTaken(imageProxyToBitmap(image).asImageBitmap()))
            val toast = Toast.makeText(context, "success", Toast.LENGTH_LONG)
            toast.show()
            super.onCaptureSuccess(image)
            image.close()

        }

        override fun onError(exception: ImageCaptureException) {
            super.onError(exception)
        }

    })
    var bitmap: Bitmap? = null
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
}