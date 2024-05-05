package com.ocproject.realestatemanager.ui.scenes.propertylist

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.rememberCoroutineScope
import com.ocproject.realestatemanager.models.PropertyWithPictures
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext


data class PropertyListState @OptIn(ExperimentalMaterial3Api::class) constructor(
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val properties: List<PropertyWithPictures> = emptyList(),
        val sortType: SortType = SortType.PRICE_ASC,
        val openFilterState : Boolean = false,

){}

enum class SortType {
        PRICE_ASC,
        PRICE_DESC,
        PRICE_RANGE,
        TAGS,
}
