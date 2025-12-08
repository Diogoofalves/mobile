package com.tpbcontrol.mobile.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tpbcontrol.mobile.domain.AppContainer
import com.tpbcontrol.mobile.ui.state.AuthViewModel

val LocalAppContainer = staticCompositionLocalOf<AppContainer> {
    error("AppContainer nao encontrado")
}

@Composable
fun TPBApp(
    appContainer: AppContainer,
    navController: NavHostController = rememberNavController()
) {
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModel.provideFactory(appContainer.repository)
    )
    val authState by authViewModel.uiState.collectAsState()

    LaunchedEffect(authState.isAuthenticated) {
        val target = if (authState.isAuthenticated) "home" else "login"
        val current = navController.currentDestination?.route
        if (current != target) {
            navController.navigate(target) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    CompositionLocalProvider(LocalAppContainer provides appContainer) {
        NavHost(
            navController = navController,
            startDestination = "login"
        ) {
            composable("login") {
                LoginScreen(
                    uiState = authState,
                    onLogin = { email, password -> authViewModel.login(email, password) },
                    onNavigateToRegister = { navController.navigate("register") }
                )
            }
            composable("register") {
                RegisterScreen(
                    uiState = authState,
                    onRegister = { request -> authViewModel.register(request) },
                    onNavigateToLogin = { navController.navigate("login") { popUpTo("login") { inclusive = true } } }
                )
            }
            composable("home") {
                HomeScreen(
                    appContainer = appContainer,
                    user = authState.user,
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate("login") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}
