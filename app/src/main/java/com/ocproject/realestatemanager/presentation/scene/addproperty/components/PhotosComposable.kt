package com.ocproject.realestatemanager.presentation.scene.addproperty.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

    if (!property.photoList.isNullOrEmpty()) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(property.photoList) { photo: PhotoProperty ->
                PhotoItem(
                    photo = photo,
                    viewModel = viewModel,
                    modifier = Modifier
                )
            }
        }
    } else {
        Box(
            modifier = Modifier
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

@Composable
fun PhotoItem(
    photo: PhotoProperty,
    viewModel: AddPropertyViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.size(150.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Afficher l'image
                Image(
                    bitmap = BitmapFactory.decodeByteArray(
                        photo.photoBytes,
                        0,
                        photo.photoBytes.size
                    ).asImageBitmap(),
                    contentDescription = photo.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Indicateur si c'est la photo principale
                if (photo.isMain) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Photo principale",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                            .padding(4.dp),
                        tint = Color.White
                    )
                }
                // Bouton de suppression
                IconButton(
                    onClick = { viewModel.removePhoto(photo) },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp),
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
        // Champ de texte pour le nom de la photo (en dessous)
        OutlinedTextField(
            modifier = Modifier
                .width(150.dp)
                .padding(top = 4.dp),
            value = photo.name,
            onValueChange = {
                viewModel.onEvent(
                    AddPropertyEvent.OnPhotoNameChanged(
                        photoProperty = photo,
                        value = it
                    )
                )
            },
            singleLine = true,
            placeholder = { Text("Nom de la photo") },
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            textStyle = MaterialTheme.typography.bodySmall,
            enabled = true
        )
    }
}