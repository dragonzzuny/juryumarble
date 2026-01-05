package com.manus.juryumarble.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.manus.juryumarble.presentation.ui.screen.*
import com.manus.juryumarble.presentation.viewmodel.GameViewModel

/**
 * Navigation routes
 */
sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object GameSetup : Screen("game_setup")
    data object CustomCardList : Screen("custom_card_list")
    data object CardPackList : Screen("card_pack_list")
    data object CardPackDetail : Screen("card_pack_detail/{packId}") {
        fun createRoute(packId: String) = "card_pack_detail/$packId"
    }
    data object GameBoard : Screen("game_board")
    data object GameResult : Screen("game_result")
}

/**
 * Main Navigation Host
 * Uses shared ViewModel for game state across screens
 */
@Composable
fun JuryumarbleNavHost(
    navController: NavHostController = rememberNavController()
) {
    // Shared ViewModel across game screens
    val gameViewModel: GameViewModel = hiltViewModel()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onStartGame = {
                    gameViewModel.resetGame()
                    navController.navigate(Screen.GameSetup.route)
                },
                onLoadGame = {
                    navController.navigate(Screen.GameBoard.route) {
                        popUpTo(Screen.Home.route)
                    }
                },
                viewModel = gameViewModel
            )
        }
        
        composable(Screen.GameSetup.route) {
            GameSetupScreen(
                onStartGame = {
                    navController.navigate(Screen.GameBoard.route) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onBack = {
                    navController.popBackStack()
                },
                onNavigateToCustomCards = {
                    navController.navigate(Screen.CustomCardList.route)
                },
                onNavigateToCardPacks = {
                    navController.navigate(Screen.CardPackList.route)
                },
                viewModel = gameViewModel
            )
        }

        composable(Screen.CustomCardList.route) {
            CustomCardListScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.CardPackList.route) {
            CardPackListScreen(
                onBack = {
                    navController.popBackStack()
                },
                onPackDetails = { packId ->
                    navController.navigate(Screen.CardPackDetail.createRoute(packId))
                }
            )
        }

        composable(
            route = Screen.CardPackDetail.route,
            arguments = listOf(navArgument("packId") { type = NavType.StringType })
        ) { backStackEntry ->
            val packId = backStackEntry.arguments?.getString("packId") ?: ""
            CardPackDetailScreen(
                packId = packId,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.GameBoard.route) {
            GameBoardScreen(
                onGameEnd = {
                    navController.navigate(Screen.GameResult.route) {
                        popUpTo(Screen.Home.route)
                    }
                },
                viewModel = gameViewModel
            )
        }
        
        composable(Screen.GameResult.route) {
            GameResultScreen(
                onPlayAgain = {
                    navController.navigate(Screen.GameSetup.route) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onGoHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                viewModel = gameViewModel
            )
        }
    }
}
