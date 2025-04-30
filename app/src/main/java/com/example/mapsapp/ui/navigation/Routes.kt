package com.example.mapsapp.ui.navigation

sealed class Destination {
    @Serializable
    object Home: Destination()

    @Serializable
    object Settings: Destination()

    @Serializable
    object About: Destination()
}
