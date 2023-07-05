package com.example.musicplayerandroidapp

sealed class Screen(val route: String) {
    object Home: Screen(route = "home")
    object Detail: Screen(route = "detail")
}