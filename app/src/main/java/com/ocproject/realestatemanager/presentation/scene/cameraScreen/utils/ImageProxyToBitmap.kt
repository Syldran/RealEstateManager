package com.ocproject.realestatemanager.presentation.scene.cameraScreen.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.camera.core.ImageProxy
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

fun imageProxyToBitmapWithRotation(image: ImageProxy): Bitmap {
    val planeProxy = image.planes[0]
    val buffer: ByteBuffer = planeProxy.buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    
    val originalBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    
    // Get the rotation degrees from the image
    val rotationDegrees = image.imageInfo.rotationDegrees
    
    // If no rotation is needed, return the original bitmap
    if (rotationDegrees == 0) {
        return originalBitmap
    }
    
    // Create a matrix for rotation
    val matrix = Matrix()
    matrix.postRotate(rotationDegrees.toFloat())
    
    // Create the rotated bitmap
    val rotatedBitmap = Bitmap.createBitmap(
        originalBitmap,
        0,
        0,
        originalBitmap.width,
        originalBitmap.height,
        matrix,
        true
    )
    
    // Recycle the original bitmap if it's different from the rotated one
    if (originalBitmap != rotatedBitmap) {
        originalBitmap.recycle()
    }
    
    return rotatedBitmap
}

fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
    return stream.toByteArray()
}