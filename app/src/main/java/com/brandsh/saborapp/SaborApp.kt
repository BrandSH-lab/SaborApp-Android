package com.brandsh.saborapp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.brandsh.saborapp.designsystem.SaborTheme
import com.brandsh.saborapp.designsystem.SaborToast
import com.brandsh.saborapp.features.CartScreen
import com.brandsh.saborapp.features.CheckoutScreen
import com.brandsh.saborapp.features.FavoritesScreen
import com.brandsh.saborapp.features.HomeScreen
import com.brandsh.saborapp.features.MenuScreen
import com.brandsh.saborapp.features.NotificationsScreen
import com.brandsh.saborapp.features.OnboardingScreen
import com.brandsh.saborapp.features.OrderConfirmedScreen
import com.brandsh.saborapp.features.OrdersScreen
import com.brandsh.saborapp.features.ProfileScreen
import com.brandsh.saborapp.features.SettingsScreen
import com.brandsh.saborapp.features.AddressesScreen
import com.brandsh.saborapp.features.TrackingScreen
import com.brandsh.saborapp.features.DishDetailScreen
import com.brandsh.saborapp.features.RegisterScreen
import com.brandsh.saborapp.features.SignInScreen
import com.brandsh.saborapp.models.AppTab
import com.brandsh.saborapp.stores.AppState
import com.brandsh.saborapp.stores.AppUiState
import kotlinx.coroutines.delay

private val tabs = listOf(AppTab.HOME, AppTab.MENU, AppTab.FAVORITES, AppTab.ORDERS, AppTab.PROFILE)

@Composable
fun SaborApp() {
    val appState = remember { AppState() }
    val state by appState.state.collectAsState()
    var showRegister by remember { mutableStateOf(false) }

    SaborTheme(darkTheme = state.appearance.isDark ?: isSystemInDarkTheme()) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Box(Modifier.fillMaxSize()) {
                when {
                    state.showOnboarding -> OnboardingScreen(
                        onSkip = {
                            showRegister = false
                            appState.updateState { it.copy(showOnboarding = false) }
                        },
                        onSignIn = {
                            showRegister = false
                            appState.updateState { it.copy(showOnboarding = false) }
                        },
                        onRegister = {
                            showRegister = true
                            appState.updateState { it.copy(showOnboarding = false) }
                        },
                    )

                    !state.isAuthenticated && showRegister -> RegisterScreen(
                        onBack = { showRegister = false },
                        onSignIn = { showRegister = false },
                        onRegistered = {
                            appState.signIn()
                            showRegister = false
                        },
                    )

                    !state.isAuthenticated -> SignInScreen(
                        onBack = { appState.updateState { it.copy(showOnboarding = true) } },
                        onRegister = { showRegister = true },
                        onSignedIn = appState::signIn,
                    )

                    else -> SaborMainNavigation(appState = appState, state = state)
                }

                state.toast?.let { message ->
                    SaborToast(
                        message = message,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 86.dp),
                    )
                }
            }
        }
    }

    LaunchedEffect(state.toast) {
        val toast = state.toast ?: return@LaunchedEffect
        delay(1_800)
        appState.updateState { current ->
            if (current.toast == toast) current.copy(toast = null) else current
        }
    }
}

@Composable
private fun SaborMainNavigation(appState: AppState, state: AppUiState) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val route = backStackEntry?.destination?.route
    val currentTabRoute = tabs.firstOrNull { it.route == route }

    fun selectTab(tab: AppTab) {
        appState.updateState { it.copy(tab = tab) }
        navController.navigate(tab.route) {
            popUpTo(navController.graph.startDestinationId) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigate(route: String) {
        val tab = tabs.firstOrNull { it.route == route }
        if (tab != null) {
            selectTab(tab)
        } else {
            navController.navigate(route)
        }
    }

    Scaffold(
        bottomBar = {
            if (currentTabRoute != null) {
                SaborTabBar(
                    selected = currentTabRoute,
                    cartCount = state.cartCount,
                    onSelect = ::selectTab,
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = state.tab.route,
            modifier = Modifier.padding(padding),
        ) {
            composable(AppTab.HOME.route) {
                HomeScreen(
                    state = state,
                    appState = appState,
                    onNavigate = ::navigate,
                    onSelectTab = ::selectTab,
                )
            }
            composable(AppTab.MENU.route) {
                MenuScreen(state, appState, ::navigate)
            }
            composable(AppTab.FAVORITES.route) {
                FavoritesScreen(state, appState, ::navigate)
            }
            composable(AppTab.ORDERS.route) {
                OrdersScreen(state, ::navigate)
            }
            composable(AppTab.PROFILE.route) {
                ProfileScreen(state, appState, ::navigate)
            }
            composable(
                route = "dish/{dishId}",
                arguments = listOf(navArgument("dishId") { type = NavType.StringType }),
            ) { entry ->
                val dish = state.dishes.firstOrNull { it.id == entry.arguments?.getString("dishId") }
                if (dish != null) {
                    DishDetailScreen(
                        dish = dish,
                        appState = appState,
                        onBack = { navController.popBackStack() },
                    )
                }
            }
            composable("cart") {
                CartScreen(
                    state = state,
                    appState = appState,
                    onBack = { navController.popBackStack() },
                    onCheckout = { navController.navigate("checkout") },
                    onMenu = { selectTab(AppTab.MENU) },
                )
            }
            composable("checkout") {
                CheckoutScreen(
                    state = state,
                    appState = appState,
                    onBack = { navController.popBackStack() },
                    onAddresses = { navController.navigate("addresses") },
                    onOrderPlaced = { paymentMethod, scheduledTime ->
                        appState.placeOrder(
                            paymentMethod = paymentMethod,
                            scheduledTime = scheduledTime,
                        )
                        navController.navigate("confirmed") {
                            popUpTo("checkout") { inclusive = true }
                        }
                    },
                )
            }
            composable("confirmed") {
                OrderConfirmedScreen(
                    state = state,
                    onTracking = { navController.navigate("tracking") },
                    onHome = { selectTab(AppTab.HOME) },
                )
            }
            composable("tracking") {
                TrackingScreen(state = state, onBack = { navController.popBackStack() })
            }
            composable("notifications") {
                NotificationsScreen(state = state, onBack = { navController.popBackStack() }) {
                    navController.navigate("tracking")
                }
            }
            composable("addresses") {
                AddressesScreen(state = state, appState = appState, onBack = { navController.popBackStack() })
            }
            composable("settings") {
                SettingsScreen(state = state, appState = appState, onBack = { navController.popBackStack() })
            }
        }
    }
}

@Composable
private fun SaborTabBar(selected: AppTab, cartCount: Int, onSelect: (AppTab) -> Unit) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        tabs.forEach { tab ->
            NavigationBarItem(
                selected = tab == selected,
                onClick = { onSelect(tab) },
                icon = {
                    val icon = when (tab) {
                        AppTab.HOME -> Icons.Default.Home
                        AppTab.MENU -> Icons.Default.RestaurantMenu
                        AppTab.FAVORITES -> Icons.Default.Favorite
                        AppTab.ORDERS -> Icons.Default.ReceiptLong
                        AppTab.PROFILE -> Icons.Default.Person
                    }
                    if (tab == AppTab.ORDERS && cartCount > 0) {
                        BadgedBox(badge = { Badge { androidx.compose.material3.Text(cartCount.toString()) } }) {
                            Icon(icon, contentDescription = tab.title)
                        }
                    } else {
                        Icon(icon, contentDescription = tab.title)
                    }
                },
                label = { androidx.compose.material3.Text(tab.title) },
                alwaysShowLabel = true,
            )
        }
    }
}

private val AppTab.route: String
    get() = when (this) {
        AppTab.HOME -> "home"
        AppTab.MENU -> "menu"
        AppTab.FAVORITES -> "favorites"
        AppTab.ORDERS -> "orders"
        AppTab.PROFILE -> "profile"
    }

private val AppTab.title: String
    get() = when (this) {
        AppTab.HOME -> "Inicio"
        AppTab.MENU -> "Menú"
        AppTab.FAVORITES -> "Favoritos"
        AppTab.ORDERS -> "Pedidos"
        AppTab.PROFILE -> "Perfil"
    }
