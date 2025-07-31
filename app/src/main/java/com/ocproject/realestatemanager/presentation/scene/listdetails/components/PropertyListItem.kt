package com.ocproject.realestatemanager.presentation.scene.listdetails.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ocproject.realestatemanager.core.InterestPoint
import com.ocproject.realestatemanager.core.utils.UtilsKotlin
import com.ocproject.realestatemanager.domain.models.Property
import com.ocproject.realestatemanager.presentation.scene.listdetails.ListDetailsEvent
import com.ocproject.realestatemanager.presentation.sharedcomponents.PropertyPhoto

@Composable
fun PropertyListItem(
//    viewModel: ListDetailsViewModel,
    property: Property,
    modifier: Modifier = Modifier,
    onEvent: (ListDetailsEvent) -> Unit,
) {
    PropertyListItemContent(
        modifier = modifier,
        propertyWithPhotos = property,
        onEvent = onEvent
    )
}

@Composable
fun PropertyListItemContent(
    modifier: Modifier,
    propertyWithPhotos: Property,
    onEvent: (ListDetailsEvent) -> Unit,
) {
    Row(
        modifier = modifier
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PropertyPhoto(
            propertyWithPhotos = propertyWithPhotos,
            modifier = Modifier.size(50.dp),

            )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "id: ${propertyWithPhotos.id}, ",
            )
            Text("$: ${propertyWithPhotos.price}, ")
            Text(
                "date: ${
                    UtilsKotlin.datePresentation(
                        propertyWithPhotos
                    )
                }, "
            )
            Text("surface: ${propertyWithPhotos.surfaceArea}")
        }

        Spacer(modifier = Modifier.width(16.dp))
//        IconButton(
//            onClick = { onEvent(ListDetailsEvent.DeleteProperty(propertyWithPhotos)) },
//        ) {
//            Icon(
//                imageVector = Icons.Rounded.Delete,
//                contentDescription = "Delete",
//
//                )
//        }
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
        onEvent = {}
    )
}