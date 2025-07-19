package com.ocproject.realestatemanager.presentation.scene.propertydetails.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.ocproject.realestatemanager.R
import com.ocproject.realestatemanager.domain.models.PhotoProperty
import com.ocproject.realestatemanager.domain.models.Property
import kotlin.math.absoluteValue

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
                if (propertyWithPhotos.photoList.size == 1) Arrangement.spacedBy(8000.dp)
                else Arrangement.spacedBy(8.dp),
            columns = GridCells.Adaptive(minSize = 128.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            userScrollEnabled = false,
        ) {
            items(propertyWithPhotos.photoList) { photo: PhotoProperty ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box {
                        Image(
                            bitmap = BitmapFactory.decodeByteArray(
                                photo.photoBytes,
                                0,
                                photo.photoBytes.size
                            ).asImageBitmap(),
                            contentDescription = photo.name,
                            modifier = Modifier
                                .size(150.dp)
//                                .align(Alignment.CenterHorizontally)
                            ,
                            contentScale = ContentScale.Crop
                        )
//                        if (propertyWithPhotos.sold != null) {
//                            Image(
//                                painter = painterResource(id = R.drawable.sold_png_transparent),
//                                contentDescription = "Your Image",
//                                contentScale = ContentScale.Crop,
//                                modifier = Modifier.size(150.dp)
//                            )
//                        }
                    }
                    Text(
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .width(150.dp)
                            .align(Alignment.CenterHorizontally),
                        text = propertyWithPhotos.photoList[propertyWithPhotos.photoList.indexOf(
                            photo
                        )].name,
                    )
                }
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
                imageVector = Icons.Rounded.Home,
                contentDescription = "Add Photo",
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(40.dp)
            )
            if (propertyWithPhotos?.sold != null) {
                Image(
                    painter = painterResource(id = R.drawable.sold_png_transparent),
                    contentDescription = "Your Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun PagerPhotoDetails(property: Property) {
    val pagerState = rememberPagerState(pageCount = {
        property.photoList?.size ?: 0
    })
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .height(250.dp),
//            .background(Color.Yellow),
        contentPadding = PaddingValues(horizontal = 64.dp),
    ) { page ->

        CardContent(page, pagerState, property)

    }
    Row(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(16.dp)
            )
        }
    }
}

@Composable
fun CardContent(page: Int, pagerState: PagerState, property: Property) {
    val pageOffset = (
            (pagerState.currentPage - page) + pagerState
                .currentPageOffsetFraction
            ).absoluteValue
    val scale = lerp(
        start = 0.8f,
        stop = 1f,
        fraction = 1f - pageOffset.coerceIn(0f, 1f)
    )
    Card(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier.wrapContentHeight()/*.background(Color.Green)*/,
                text = property.photoList?.get(page)?.name ?: "",
            )
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(property.photoList?.get(page)?.photoBytes)
                    .crossfade(true)
                    .scale(Scale.FILL)
                    .build(),
                contentDescription = "image",
                contentScale = ContentScale.Crop
            )

        }
    }
}


@Composable
fun PhotosComposable(
    property: Property,
    modifier: Modifier = Modifier,
    iconSize: Dp = 25.dp,
) {

    if (!property.photoList.isEmpty()) {
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
            }
        }
        // Champ de texte pour le nom de la photo (en dessous)
        Text(
            modifier = Modifier
                .width(150.dp)
                .padding(top = 4.dp),
            text = photo.name,
        )
    }
}