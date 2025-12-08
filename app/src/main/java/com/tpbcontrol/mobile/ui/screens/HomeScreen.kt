package com.tpbcontrol.mobile.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tpbcontrol.mobile.data.remote.dto.UserDto
import com.tpbcontrol.mobile.domain.AppContainer
import com.tpbcontrol.mobile.ui.state.DashboardViewModel
import com.tpbcontrol.mobile.ui.state.FarmsViewModel
import com.tpbcontrol.mobile.ui.state.RequestsViewModel
import com.tpbcontrol.mobile.ui.state.TasksViewModel

private enum class HomeTab { Dashboard, Requests, Tasks, Farms, Profile }

@Composable
fun HomeScreen(
    appContainer: AppContainer,
    user: UserDto?,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(HomeTab.Dashboard) }

    val dashboardViewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModel.provideFactory(appContainer.repository)
    )
    val requestsViewModel: RequestsViewModel = viewModel(
        factory = RequestsViewModel.provideFactory(appContainer.repository)
    )
    val farmsViewModel: FarmsViewModel = viewModel(
        factory = FarmsViewModel.provideFactory(appContainer.repository)
    )
    val tasksViewModel: TasksViewModel = viewModel(
        factory = TasksViewModel.provideFactory(appContainer.repository)
    )

    val dashboardState by dashboardViewModel.uiState.collectAsState()
    val requestsState by requestsViewModel.uiState.collectAsState()
    val farmsState by farmsViewModel.uiState.collectAsState()
    val tasksState by tasksViewModel.uiState.collectAsState()

    LaunchedEffect(user?.userType) {
        dashboardViewModel.load(user?.userType)
        requestsViewModel.load()
        farmsViewModel.load()
        if (user?.userType == "funcionario") {
            tasksViewModel.load()
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == HomeTab.Dashboard,
                    onClick = { selectedTab = HomeTab.Dashboard },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
                    label = { Text("Dashboard") }
                )
                NavigationBarItem(
                    selected = selectedTab == HomeTab.Requests,
                    onClick = { selectedTab = HomeTab.Requests },
                    icon = { Icon(Icons.Default.List, contentDescription = "Solicitacoes") },
                    label = { Text("Solic.") }
                )
                NavigationBarItem(
                    selected = selectedTab == HomeTab.Tasks,
                    onClick = { selectedTab = HomeTab.Tasks },
                    icon = { Icon(Icons.Default.Task, contentDescription = "Tarefas") },
                    label = { Text("Tarefas") }
                )
                NavigationBarItem(
                    selected = selectedTab == HomeTab.Farms,
                    onClick = { selectedTab = HomeTab.Farms },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Fazendas") },
                    label = { Text("Fazendas") }
                )
                NavigationBarItem(
                    selected = selectedTab == HomeTab.Profile,
                    onClick = { selectedTab = HomeTab.Profile },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
                HomeTab.Dashboard -> DashboardScreen(
                    state = dashboardState,
                    userName = user?.fullName
                )

                HomeTab.Requests -> RequestsScreen(
                    state = requestsState
                )

                HomeTab.Tasks -> TasksScreen(
                    state = tasksState,
                    userType = user?.userType
                )

                HomeTab.Farms -> FarmsScreen(
                    state = farmsState
                )

                HomeTab.Profile -> ProfileScreen(
                    user = user,
                    onLogout = onLogout
                )
            }
        }
    }
}
