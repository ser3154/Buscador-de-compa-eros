package servando.contreras.buscadordecompaerosdeactividades.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import servando.contreras.buscadordecompaerosdeactividades.screens.ForgotPasswordScreen
import servando.contreras.buscadordecompaerosdeactividades.screens.HomeScreen
import servando.contreras.buscadordecompaerosdeactividades.screens.LoginScreen
import servando.contreras.buscadordecompaerosdeactividades.screens.RegisterScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT = "forgot_password"
    const val HOME = "home"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onLogin = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onForgotPassword = { navController.navigate(Routes.FORGOT) },
                onBiometric = { /* TODO */ },
                onRegister = { navController.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onBack = { navController.popBackStack() },
                onRegister = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.FORGOT) {
            ForgotPasswordScreen(
                onBack = { navController.popBackStack() },
                onSend = { navController.popBackStack() }
            )
        }

        composable(Routes.HOME) {
            HomeScreen()
        }
    }
}