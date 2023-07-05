package com.example.musicplayerandroidapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.musicplayerandroidapp.ui.detail.DetailScreen
import com.example.musicplayerandroidapp.ui.home.HomeScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                onItemClicked = { id ->
                    navController.navigate("${Screen.Detail.route}/${id}")
                }
            )
        }
        composable(
            route = "${Screen.Detail.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            DetailScreen()
        }
    }
}