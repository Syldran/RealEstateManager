package com.ocproject.realestatemanager.presentation.scene.addproperty.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ocproject.realestatemanager.domain.models.PhotoProperty
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyEvent
import com.ocproject.realestatemanager.presentation.scene.addproperty.AddPropertyViewModel


@Composable
fun PhotosComposable(
    viewModel: AddPropertyViewModel,
    property: Property,
    modifier: Modifier = Modifier,
    iconSize: Dp = 25.dp
) {

    val photoModifier = modifier.size(150.dp)
//        .clip(RoundedCornerShape(35))
    if (!property.photoList.isNullOrEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 1000.dp),
            contentAlignment = Alignment.Center
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                userScrollEnabled = false,
                columns = if (property.photoList.size == 1) GridCells.Fixed(1)
                else GridCells.Adaptive(minSize = 128.dp)

            ) {
                items(property.photoList) { photo: PhotoProperty ->
                    Column {
                        Image(
                            bitmap = BitmapFactory.decodeByteArray(
                                photo.photoBytes,
                                0,
                                photo.photoBytes.size
                            ).asImageBitmap(),
                            contentDescription = photo.name,
                            modifier = photoModifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally),
                            contentScale = ContentScale.Crop
                        )
                        TextField(
                            modifier = Modifier
                                .width(150.dp)
                                .align(Alignment.CenterHorizontally),
                            value = property.photoList[property.photoList.indexOf(photo)].name,
                            onValueChange = {
                                viewModel.onEvent(
                                    AddPropertyEvent.OnPhotoNameChanged(
                                        photoProperty = photo,
                                        value = it
                                    )
                                )
                            }
                        )
                        IconButton(
                            onClick = {
                                viewModel.onEvent(AddPropertyEvent.UpdatePhotos(emptyList()))
                            },
                            colors = IconButtonColors(
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                disabledContentColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Icon(imageVector = Icons.Rounded.Close, contentDescription = "Close")
                        }
                    }
                }
            }
        }
    } else {
        Box(
            modifier = photoModifier
                .size(150.dp)
                .clip(RoundedCornerShape(40))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    shape = RoundedCornerShape(40)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Add Photo",
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}
