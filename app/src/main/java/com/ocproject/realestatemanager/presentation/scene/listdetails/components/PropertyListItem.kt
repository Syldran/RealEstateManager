package com.ocproject.realestatemanager.presentation.scene.listdetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ocproject.realestatemanager.core.InterestPoint
import com.ocproject.realestatemanager.core.utils.UtilsKotlin
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsEvent

@Composable
fun PropertyListItem(
//    viewModel: ListDetailsViewModel,
    property: Property,
    modifier: Modifier = Modifier,
    onEvent: (ListDetailsEvent) -> Unit,
    selectedProperty: Property? = null,
) {
    PropertyListItemContent(
        modifier = modifier,
        propertyWithPhotos = property,
        onEvent = onEvent,
        selectedProperty = selectedProperty
    )
}

@Composable
fun PropertyListItemContent(
    modifier: Modifier,
    propertyWithPhotos: Property,
    onEvent: (ListDetailsEvent) -> Unit,
    selectedProperty: Property? = null,
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    val isSelected = selectedProperty?.id == propertyWithPhotos.id
    
    val backgroundColor = if (isLandscape && isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        Color.Transparent
    }
    
    Row(
        modifier = modifier
            .background(backgroundColor)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PropertyPhoto(
            propertyWithPhotos = propertyWithPhotos,
            modifier = Modifier.size(50.dp),

            )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.wrapContentWidth()) {

            Text("$: ${propertyWithPhotos.price}")
            Text(
                "City: ${propertyWithPhotos.town}"
            )
            Text("surface: ${propertyWithPhotos.surfaceArea}")
        }

        Spacer(modifier = Modifier.width(16.dp))
        IconButton(
            onClick = { onEvent(ListDetailsEvent.DeleteProperty(propertyWithPhotos)) },
        ) {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = "Delete",

                )
        }
    }
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
}

@Preview
@Composable
fun PreviewItem() {
    PropertyListItemContent(
        modifier = Modifier,
        propertyWithPhotos = Property(
            id = 3L,
            photoList = emptyList(),
            interestPoints = listOf(InterestPoint.SCHOOL),
            description = "A property for preview.",
            address = "123 Main St",
            town = "SampleTown",
            lat = 37.7749,
            lng = -122.4194,
            country = "SampleCountry",
            createdDate = 0L,
            price = 150000,
            surfaceArea = 150,
            areaCode = 18290,
            sold = 2500L,
        ),
        onEvent = {},
        selectedProperty = null
    )
}