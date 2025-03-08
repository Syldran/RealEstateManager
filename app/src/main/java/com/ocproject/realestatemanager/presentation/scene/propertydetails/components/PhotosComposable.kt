package com.ocproject.realestatemanager.presentation.scene.propertydetails.components

import com.ocproject.realestatemanager.R
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ocproject.realestatemanager.domain.models.PhotoProperty
import com.ocproject.realestatemanager.domain.models.Property

@Composable
fun PhotosRow(modifier: Modifier = Modifier.horizontalScroll(state = rememberScrollState(), true)) {

}


@Composable
fun PhotosDetailsComposable(
    propertyWithPhotos: Property?,
) {
    if (!propertyWithPhotos?.photoList.isNullOrEmpty()) {
        LazyVerticalGrid(
            modifier = Modifier
                .heightIn(max = 1000.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement =
            if(propertyWithPhotos.photoList.size == 1) Arrangement.spacedBy(8000.dp)
            else Arrangement.spacedBy(8.dp)
            ,
            columns = GridCells.Adaptive(minSize = 128.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            userScrollEnabled = false,
        ) {
            items(propertyWithPhotos.photoList) { photo: PhotoProperty ->
                Column{
                    Image(
                        bitmap = BitmapFactory.decodeByteArray(
                            photo.photoBytes,
                            0,
                            photo.photoBytes.size
                        ).asImageBitmap(),
                        contentDescription = photo.name,
                        modifier = Modifier .size(150.dp).align(Alignment.CenterHorizontally),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        modifier = Modifier.width(150.dp).align(Alignment.CenterHorizontally),
                        text =  propertyWithPhotos.photoList[propertyWithPhotos.photoList.indexOf(photo)].name,
                    )
                }
            }

        }
    } else {
        Box(modifier = Modifier
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
                imageVector = Icons.Rounded.Home,
                contentDescription = "Add Photo",
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(40.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.sold_png_transparent),
                contentDescription = "Your Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
