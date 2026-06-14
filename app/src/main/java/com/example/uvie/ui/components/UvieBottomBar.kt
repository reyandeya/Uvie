package com.example.uvie.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class UvieDestination(val route: String, val icon: ImageVector, val label: String) {
    HOME("home", Icons.Default.Home, "Home"),
    SEARCH("search", Icons.Default.Search, "Search"),
    GENRES("genres", Icons.Default.GridView, "Genres"),
    WATCHLIST("watchlist", Icons.Default.Favorite, "Watchlist"),
    PROFILE("profile", Icons.Default.AccountCircle, "Profile")
}

@Composable
fun UvieBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = Color(0xFF141414)
    ) {
        UvieDestination.entries.forEach { destination ->
            NavigationBarItem(
                selected = currentRoute == destination.route,
                onClick = { onNavigate(destination.route) },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.label
                    )
                },
                label = null,
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = Color(0xFF767676),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
