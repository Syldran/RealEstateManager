package com.ocproject.realestatemanager.presentation.sharedcomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ocproject.realestatemanager.models.PropertyWithPhotos
import com.ocproject.realestatemanager.presentation.utils.rememberBitmapFromBytes

@Composable
fun PropertyPhoto(
    propertyWithPhotos: PropertyWithPhotos?,
    modifier: Modifier = Modifier,
    iconSize: Dp = 25.dp
) {
    val bitmap: ImageBitmap?
    if (propertyWithPhotos?.photoList.isNullOrEmpty()){
        bitmap = null
    } else bitmap = rememberBitmapFromBytes(byteArray = propertyWithPhotos?.photoList?.get(0)?.photoBytes)

    val photoModifier = modifier.clip(RoundedCornerShape(35))
    if (bitmap != null) {
        Image(
            bitmap = bitmap,
            contentDescription = propertyWithPhotos?.property?.address,
            modifier = photoModifier,
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = photoModifier.background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = propertyWithPhotos?.property?.address,
                modifier = Modifier.size(iconSize),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}