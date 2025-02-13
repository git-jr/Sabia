package com.alura.sabia.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alura.sabia.ui.endgame.EndGameScreen
import com.alura.sabia.ui.gamemodes.complete.CompleteScreen
import com.alura.sabia.ui.gamemodes.selecttheme.SelectThemeScreen
import com.alura.sabia.ui.gamemodes.sendImage.SendImageScreen

@Composable
fun NavHost(
    navController: NavHostController
) {
    Scaffold(
        modifier = Modifier,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            NavHost(
                navController = navController,
                startDestination = Routes.SendImage
            ) {
                composable<Routes.SelectTheme> {
                    SelectThemeScreen(
                        onGoToNextScreen = {
                            navController.navigate(Routes.Complete)
                        }
                    )
                }

                composable<Routes.Complete> {
                    CompleteScreen(
                        onGoToNextScreen = {
                            navController.navigate(Routes.SendImage)
                        }
                    )
                }

                composable<Routes.SendImage> {
                    SendImageScreen(
                        onGoToNextScreen = {
                            navController.navigate(Routes.EndGame)
                        }
                    )
                }

                composable<Routes.EndGame> {
                    EndGameScreen(
                        onGoHome = {
                            navController.navigateClean(Routes.SelectTheme)
                        }
                    )
                }
            }
        }
    }
}

fun NavHostController.navigateClean(route: Routes) {
    this.navigate(route) {
        popUpTo(Routes.SelectTheme) {
            inclusive = true
        }
    }
}