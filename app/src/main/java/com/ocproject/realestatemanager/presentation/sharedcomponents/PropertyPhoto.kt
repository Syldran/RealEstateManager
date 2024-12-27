package com.ocproject.realestatemanager.presentation.sharedcomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
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
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.presentation.utils.rememberBitmapFromBytes

@Composable
fun PropertyPhoto(
    propertyWithPhotos: Property?,
    modifier: Modifier = Modifier,
    iconSize: Dp = 25.dp
) {
    val bitmap = if (propertyWithPhotos?.photoList.isNullOrEmpty()){
        null
    } else rememberBitmapFromBytes(byteArray = propertyWithPhotos.photoList[0].photoBytes)

    val photoModifier = modifier.clip(RoundedCornerShape(35))
    if (bitmap != null) {
        Image(
            bitmap = bitmap,
            contentDescription = propertyWithPhotos?.address,
            modifier = photoModifier,
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = photoModifier.background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = propertyWithPhotos?.address,
                modifier = Modifier.size(iconSize),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}