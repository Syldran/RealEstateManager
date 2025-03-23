package com.ocproject.realestatemanager.presentation.scene.listdetails

sealed interface ListDetailsEvent {
    data class OnClickMapMode(val value: Boolean): ListDetailsEvent
}