package com.example.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.ConsentScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SplashScreen

object TogetherRoutes {
  const val SPLASH = "splash"
  const val CONSENT = "consent"
  const val HOME = "home"
}

@Composable
fun TogetherApp() {
  val navController = rememberNavController()

  NavHost(
    navController = navController,
    startDestination = TogetherRoutes.SPLASH,
    enterTransition = { fadeIn() },
    exitTransition = { fadeOut() },
    popEnterTransition = { EnterTransition.None },
    popExitTransition = { ExitTransition.None },
  ) {
    composable(route = TogetherRoutes.SPLASH) {
      SplashScreen(
        onSplashFinished = {
          navController.navigate(TogetherRoutes.CONSENT) {
            popUpTo(TogetherRoutes.SPLASH) { inclusive = true }
          }
        }
      )
    }

    composable(route = TogetherRoutes.CONSENT) {
      ConsentScreen(
        onPermissionAllowed = {
          navController.navigate(TogetherRoutes.HOME) {
            popUpTo(TogetherRoutes.CONSENT) { inclusive = true }
          }
        }
      )
    }

    composable(route = TogetherRoutes.HOME) { HomeScreen() }
  }
}
