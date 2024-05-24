package com.ocproject.realestatemanager.presentation.scene.propertylist.components

import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ocproject.realestatemanager.models.Property
import com.ocproject.realestatemanager.models.PropertyWithPhotos
import com.ocproject.realestatemanager.presentation.scene.propertylist.PropertyListEvent
import com.ocproject.realestatemanager.presentation.scene.propertylist.PropertyListViewModel
import com.ocproject.realestatemanager.presentation.sharedcomponents.PropertyPhoto

@Composable
fun PropertyListItem(
    viewModel: PropertyListViewModel,
    propertyWithPhotos: PropertyWithPhotos,
    modifier: Modifier = Modifier,
    onEvent: (PropertyListEvent) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PropertyPhoto(
            propertyWithPhotos = propertyWithPhotos,
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "id: ${propertyWithPhotos.property.id}, $: ${propertyWithPhotos.property.price}, date: ${viewModel.datePresentation(propertyWithPhotos.property)}",
            modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        IconButton(
            onClick = { onEvent(PropertyListEvent.DeleteProperty(propertyWithPhotos.property)) },
//            modifier = Modifier.align(Alignment.End)
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
   PropertyListItem(
       viewModel = viewModel<PropertyListViewModel>(),
       onEvent = {},
       modifier = Modifier.background(color = MaterialTheme.colorScheme.surface),
        propertyWithPhotos = PropertyWithPhotos( Property(
            id = 500L,
            price = 150000,
            createdDate = Calendar.getInstance().timeInMillis,
            address = "8 Route de Vierzon",
            areaCode = 18290,
            lat = 0.0,
            lng = 0.0,
            town = "Poisieux",
            country = "France",
            surfaceArea = 110,
            interestPoints = null,
        ),
            null,
        )
    )
}