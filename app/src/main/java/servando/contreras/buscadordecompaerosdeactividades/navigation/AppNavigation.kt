package servando.contreras.buscadordecompaerosdeactividades.navigation

import ActivityDetailScreen
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import servando.contreras.buscadordecompaerosdeactividades.componenets.CoincideNavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import servando.contreras.buscadordecompaerosdeactividades.screens.CalendarScreen
import servando.contreras.buscadordecompaerosdeactividades.screens.ParticipationRequestsScreen
import servando.contreras.buscadordecompaerosdeactividades.screens.ParticipationDecision
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import servando.contreras.buscadordecompaerosdeactividades.screens.ExploreScreen
import servando.contreras.buscadordecompaerosdeactividades.screens.ForgotPasswordScreen
import servando.contreras.buscadordecompaerosdeactividades.screens.LoginScreen
import servando.contreras.buscadordecompaerosdeactividades.screens.NewActivityScreen
import servando.contreras.buscadordecompaerosdeactividades.screens.RegisterScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import servando.contreras.buscadordecompaerosdeactividades.data.UserProfile
import servando.contreras.buscadordecompaerosdeactividades.screens.ProfileScreen
import servando.contreras.buscadordecompaerosdeactividades.screens.SettingsScreen
import servando.contreras.buscadordecompaerosdeactividades.screens.StatisticsScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT = "forgot_password"
    const val EXPLORE = "explore"
    const val NEW_ACTIVITY = "new_activity"
    const val CALENDAR = "calendar"
    const val PARTICIPATION_REQUESTS = "participation_requests"
    const val ACTIVITY_DETAIL = "activity_detail/{activityId}"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"
    const val STATISTICS = "statistics"

    fun activityDetail(id: Int) = "activity_detail/$id"
}

@Composable
fun AppNavigation(
    darkTheme: Boolean = false,
    onDarkThemeChange: (Boolean) -> Unit = {}
) {
    val navController = rememberNavController()
    var elenaDecision by rememberSaveable { mutableStateOf(ParticipationDecision.PENDING) }
    var diegoDecision by rememberSaveable { mutableStateOf(ParticipationDecision.PENDING) }
    var profile by rememberSaveable(stateSaver = UserProfile.Saver) { mutableStateOf(UserProfile.Sample) }
    var biometricEnabled by rememberSaveable { mutableStateOf(true) }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val route = backStackEntry?.destination?.route
    val mainRoutes = setOf(Routes.EXPLORE, Routes.CALENDAR, Routes.PARTICIPATION_REQUESTS, Routes.NEW_ACTIVITY, Routes.PROFILE)
    var unavailableSection by rememberSaveable { mutableStateOf<String?>(null) }

    fun openMainRoute(destination: String) {
        navController.navigate(destination) {
            popUpTo(Routes.EXPLORE) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (route in mainRoutes) {
                CoincideNavigationBar(
                    selectedTab = when (route) {
                        Routes.CALENDAR, Routes.PARTICIPATION_REQUESTS -> 1
                        Routes.PROFILE -> 3
                        else -> 0
                    },
                    onSelect = { index ->
                        when (index) {
                            0 -> openMainRoute(Routes.EXPLORE)
                            1 -> openMainRoute(Routes.CALENDAR)
                            2 -> unavailableSection = "Mensajes"
                            3 -> openMainRoute(Routes.PROFILE)
                        }
                    }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.LOGIN,
            modifier = Modifier.padding(padding).consumeWindowInsets(padding)
        ) {

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

            composable(Routes.CALENDAR) {
                CalendarScreen(
                    onBack = { navController.popBackStack() },
                    onRequests = { navController.navigate(Routes.PARTICIPATION_REQUESTS) { launchSingleTop = true } }
                )
            }

            composable(Routes.PARTICIPATION_REQUESTS) {
                ParticipationRequestsScreen(
                    decisions = listOf(elenaDecision, diegoDecision),
                    onDecision = { index, decision ->
                        if (index == 0) elenaDecision = decision else diegoDecision = decision
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Routes.PROFILE) {
                ProfileScreen(
                    profile = profile,
                    onEditProfile = { navController.navigate(Routes.SETTINGS) { launchSingleTop = true } },
                    onSettings = { navController.navigate(Routes.SETTINGS) { launchSingleTop = true } },
                    onStatistics = { navController.navigate(Routes.STATISTICS) { launchSingleTop = true } },
                    onCalendar = { openMainRoute(Routes.CALENDAR) },
                    onNewActivity = { navController.navigate(Routes.NEW_ACTIVITY) },
                    onUnavailable = { section -> unavailableSection = section }
                )
            }

            composable(Routes.SETTINGS) {
                SettingsScreen(
                    profile = profile,
                    darkTheme = darkTheme,
                    biometricEnabled = biometricEnabled,
                    onDarkThemeChange = onDarkThemeChange,
                    onBiometricChange = { biometricEnabled = it },
                    onSave = { updated ->
                        profile = updated
                        navController.popBackStack()
                    },
                    onChangePassword = { navController.navigate(Routes.FORGOT) },
                    onLogout = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Routes.STATISTICS) {
                StatisticsScreen(onBack = { navController.popBackStack() })
            }

            composable(
                route = Routes.ACTIVITY_DETAIL,
                arguments = listOf(navArgument("activityId") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("activityId") ?: return@composable
                ActivityDetailScreen(activityId = id, onBack = { navController.popBackStack() })
            }
        }
    }

    unavailableSection?.let { section ->
        AlertDialog(
            onDismissRequest = { unavailableSection = null },
            title = { Text(section) },
            text = { Text("Esta sección todavía no está disponible.") },
            confirmButton = {
                TextButton(onClick = { unavailableSection = null }) { Text("Cerrar") }
            }
        )
    }
}
