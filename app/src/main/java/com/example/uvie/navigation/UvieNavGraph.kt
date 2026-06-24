package com.example.uvie.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.uvie.ui.components.UvieBottomBar
import com.example.uvie.ui.screens.AboutScreen
import com.example.uvie.ui.screens.AccountSettingsScreen
import com.example.uvie.ui.screens.GenresScreen
import com.example.uvie.ui.screens.HomeScreen
import com.example.uvie.ui.screens.LoginScreen
import com.example.uvie.ui.screens.MovieDetailScreen
import com.example.uvie.ui.screens.ProfileScreen
import com.example.uvie.ui.screens.SearchScreen
import com.example.uvie.ui.screens.SignUpScreen
import com.example.uvie.ui.screens.SplashScreen
import com.example.uvie.ui.screens.WatchlistScreen
import com.example.uvie.ui.viewmodels.AuthViewModel
import com.example.uvie.ui.viewmodels.GenresViewModel
import com.example.uvie.ui.viewmodels.HomeViewModel
import com.example.uvie.ui.viewmodels.MovieDetailViewModel
import com.example.uvie.ui.viewmodels.ProfileViewModel
import com.example.uvie.ui.viewmodels.SearchViewModel
import com.example.uvie.ui.viewmodels.WatchlistViewModel

@Composable
fun UvieApp(onToggleTheme: () -> Unit = {}) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarRoutes = listOf("home", "search", "genres", "watchlist", "profile")
    val showBottomBar = currentRoute in bottomBarRoutes

    // ViewModels scoped to activity level so they don't recreate on bottom tab switch
    val authViewModel: AuthViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()
    val searchViewModel: SearchViewModel = viewModel()
    val genresViewModel: GenresViewModel = viewModel()
    val watchlistViewModel: WatchlistViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                UvieBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.fillMaxSize().padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            composable("splash") {
                SplashScreen(
                    onNavigateToHome = {
                        navController.navigate("home") {
                            popUpTo("splash") { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigate("login") {
                            popUpTo("splash") { inclusive = true }
                        }
                    },
                    isLoggedIn = authViewModel.authState.value is com.example.uvie.ui.viewmodels.AuthState.Success
                )
                authViewModel.checkSession()
            }
            
            composable("login") {
                LoginScreen(
                    viewModel = authViewModel,
                    onNavigateToHome = {
                        navController.navigate("home") { popUpTo("login") { inclusive = true } }
                    },
                    onNavigateToSignUp = { navController.navigate("signup") }
                )
            }
            
            composable("signup") {
                SignUpScreen(
                    viewModel = authViewModel,
                    onNavigateToHome = {
                        navController.navigate("home") { popUpTo("login") { inclusive = true } }
                    },
                    onNavigateToLogin = { navController.popBackStack() }
                )
            }
            
            composable("home") {
                HomeScreen(
                    viewModel = homeViewModel,
                    onNavigateToDetails = { id -> navController.navigate("movieDetail/$id") }
                )
            }
            
            composable("search") {
                SearchScreen(
                    viewModel = searchViewModel,
                    onNavigateToDetails = { id -> navController.navigate("movieDetail/$id") }
                )
            }
            
            composable("genres") {
                GenresScreen(
                    viewModel = genresViewModel,
                    onNavigateToDetails = { id -> navController.navigate("movieDetail/$id") }
                )
            }
            
            composable("watchlist") {
                WatchlistScreen(
                    viewModel = watchlistViewModel,
                    onNavigateToDetails = { id -> navController.navigate("movieDetail/$id") }
                )
            }
            
            composable("profile") {
                ProfileScreen(
                    viewModel = profileViewModel,
                    onSignOut = {
                        navController.navigate("login") { popUpTo(0) }
                    },
                    onNavigateToSettings = { navController.navigate("account_settings") },
                    onNavigateToAbout = { navController.navigate("about") },
                    onToggleTheme = onToggleTheme
                )
            }
            
            composable("account_settings") {
                AccountSettingsScreen(
                    viewModel = authViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onAccountDeleted = {
                        navController.navigate("login") { popUpTo(0) }
                    }
                )
            }
            
            composable("about") {
                AboutScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            composable("movieDetail/{movieId}") { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId")?.toLongOrNull() ?: return@composable
                // We use a fresh viewmodel instance for each detail screen
                val detailViewModel: MovieDetailViewModel = viewModel()
                MovieDetailScreen(
                    viewModel = detailViewModel,
                    movieId = movieId,
                    onNavigateToSimilar = { id -> navController.navigate("movieDetail/$id") },
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
