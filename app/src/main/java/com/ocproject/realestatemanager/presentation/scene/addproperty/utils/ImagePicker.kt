package com.ocproject.realestatemanager.presentation.scene.addproperty.utils

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

class ImagePicker(
    private val activity: ComponentActivity
) {
//    private lateinit var getVisualMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var getMultiVisualMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private var byteArray: List<ByteArray>? = emptyList()

//    @Composable
//    fun RegisterPicker(onImagePicked: (ByteArray) -> Unit) {
//        getVisualMedia =
//            rememberLauncherForActivityResult(
//                contract = ActivityResultContracts.PickVisualMedia(),
//                onResult = { uri ->
//                    if (uri != null) {
//                        activity.contentResolver.openInputStream(uri)?.use {
//                            onImagePicked(it.readBytes())
//                        }
//                    }
//                }
//            )
//
//    }
//
//    fun pickImage() {
//        getVisualMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//    }

    @Composable
    fun RegisterPickerMulti(onImagesPicked: (List<ByteArray>?) -> Unit) {
        getMultiVisualMedia = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(4),
            onResult = { uris: List<Uri> ->
                if (uris.isNotEmpty()) {
                    uris.forEach { uri ->
                        activity.contentResolver.openInputStream(uri)?.use {
                            byteArray = byteArray?.plus(it.readBytes())
                        }
                    }
                    onImagesPicked(byteArray)
                }
                byteArray = null
            }
        )
    }

    fun pickMultiImage() {
        getMultiVisualMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}
