package servando.contreras.buscadordecompaerosdeactividades.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import servando.contreras.buscadordecompaerosdeactividades.screens.ExploreScreen
import servando.contreras.buscadordecompaerosdeactividades.screens.ForgotPasswordScreen
import servando.contreras.buscadordecompaerosdeactividades.screens.HomeScreen
import servando.contreras.buscadordecompaerosdeactividades.screens.LoginScreen
import servando.contreras.buscadordecompaerosdeactividades.screens.NewActivityScreen
import servando.contreras.buscadordecompaerosdeactividades.screens.RegisterScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT = "forgot_password"
    const val EXPLORE = "explore"
    const val NEW_ACTIVITY = "new_activity"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onLogin = {
                    navController.navigate(Routes.EXPLORE) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onForgotPassword = { navController.navigate(Routes.FORGOT) },
                onBiometric = {  },
                onRegister = { navController.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onBack = { navController.popBackStack() },
                onRegister = {
                    navController.navigate(Routes.EXPLORE) {
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

        composable(Routes.EXPLORE) {
            ExploreScreen(
                onNewActivity = { navController.navigate(Routes.NEW_ACTIVITY) }
            )
        }

        composable(Routes.NEW_ACTIVITY) {
            NewActivityScreen(
                onBack = { navController.popBackStack() },
                onPublish = { navController.popBackStack() }
            )
        }
    }
}