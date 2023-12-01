package com.example.readersapplicationn.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.readersapplicationn.screens.ReaderSplashScreen
import com.example.readersapplicationn.screens.RecoverPassword
import com.example.readersapplicationn.screens.details.ReaderDetailScreen
import com.example.readersapplicationn.screens.home.HomeScreenViewModel

import com.example.readersapplicationn.screens.home.ReaderHomeScreen
import com.example.readersapplicationn.screens.login.ReaderLoginScreen
import com.example.readersapplicationn.screens.search.BookSearchViewModel
import com.example.readersapplicationn.screens.search.ReaderSearchScreen
import com.example.readersapplicationn.screens.stats.ReaderStatsScreen
import com.example.readersapplicationn.screens.update.ReaderUpdateScreen


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ReaderNavigation() {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ReaderScreens.SplashScreen.name){

        composable(ReaderScreens.SplashScreen.name){
            ReaderSplashScreen(navController=navController)
        }
        composable(ReaderScreens.ReaderHomeScreen.name){
            val homeViewModel= hiltViewModel<HomeScreenViewModel>()

            ReaderHomeScreen(navController=navController,homeViewModel)
        }
        composable(ReaderScreens.LoginScreen.name){

            ReaderLoginScreen(navController=navController)
        }
        composable(ReaderScreens.RecoverPassword.name){
            RecoverPassword(navController=navController)
        }
        composable(ReaderScreens.ReaderStatsScreen.name){
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            ReaderStatsScreen(navController=navController, viewModel = homeViewModel)
        }
        composable(ReaderScreens.SearchScreen.name){
            val searchViewModel = hiltViewModel<BookSearchViewModel>()
            ReaderSearchScreen(navController=navController,searchViewModel)
        }
        val detailName= ReaderScreens.DetailScreen.name
        composable("$detailName/{bookId}", arguments = listOf(navArgument("bookId"){
            type= NavType.StringType
        })){backStackEntry->
            backStackEntry.arguments?.getString("bookId").let {


                ReaderDetailScreen(navController = navController, bookId =it.toString())
            }
        }
        val updateName = ReaderScreens.UpdateScreen.name
        composable("$updateName/{bookItemId}", arguments = listOf(navArgument("bookItemId"){
            type=NavType.StringType
        })){navBackStackEntry ->
            navBackStackEntry.arguments?.getString("bookItemId").let {
                if (it != null) {
                    ReaderUpdateScreen(navController=navController,bookItemId=it)
                }
            }

        }


    }
}