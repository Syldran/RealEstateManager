package com.ocproject.realestatemanager.presentation.scene.addproperty.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ocproject.realestatemanager.models.PhotoProperty
import com.ocproject.realestatemanager.models.PropertyWithPhotos
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyEvent
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyViewModel


@Composable
fun PhotosComposable(
    viewModel: AddPropertyViewModel,
    propertyWithPhotos: PropertyWithPhotos?,
    modifier: Modifier = Modifier,
    iconSize: Dp = 25.dp
) {

val value = remember {
    mutableStateOf(viewModel.map.value.values.firstOrNull())
}
    val photoModifier = modifier
        .clip(RoundedCornerShape(35))
    if (propertyWithPhotos?.photoList != null) {
        LazyVerticalGrid(
//            userScrollEnabled = false,
            modifier = Modifier
                .height(292.dp)
                .wrapContentSize()
                .padding(horizontal = 8.dp),
            columns = GridCells.Adaptive(minSize = 128.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(propertyWithPhotos.photoList) { photo: PhotoProperty ->
                Column {
                    Image(
                        bitmap = BitmapFactory.decodeByteArray(
                            photo.photoBytes,
                            0,
                            photo.photoBytes.size
                        ).asImageBitmap(),
                        contentDescription = photo.name,
                        modifier = photoModifier.align(Alignment.CenterHorizontally),
                        contentScale = ContentScale.Crop
                    )
                    TextField(
                        value = propertyWithPhotos.photoList[propertyWithPhotos.photoList.indexOf(photo)].name,
                        onValueChange = {
                            viewModel.onEvent(AddPropertyEvent.OnPhotoNameChanged(photoProperty = photo, value = it))
                        }
                    )
                }
            }

        }
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