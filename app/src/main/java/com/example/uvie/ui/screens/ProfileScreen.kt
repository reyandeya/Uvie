package com.example.uvie.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uvie.ui.theme.UviePurple
import com.example.uvie.ui.viewmodels.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onSignOut: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onToggleTheme: () -> Unit
) {
    val profileStats by viewModel.profileStats.collectAsState()
    var showSignOutDialog by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(UviePurple),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = profileStats.name?.take(1)?.uppercase() ?: "U",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = (profileStats.username ?: "USERNAME").uppercase(),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "${profileStats.username ?: "email"}@email.com", // Mock email since we don't have it in profileStats yet
            color = UviePurple,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            StatCard(title = "Watchlist", count = profileStats.watchlistCount, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            StatCard(title = "Watching", count = profileStats.watchingCount, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            StatCard(title = "Watched", count = profileStats.watchedCount, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))
        androidx.compose.material3.Divider(color = MaterialTheme.colorScheme.onBackground.copy(alpha=0.3f), thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(16.dp))

        ProfileMenuItem(title = "Theme", onClick = onToggleTheme)
        Spacer(modifier = Modifier.height(8.dp))
        ProfileMenuItem(title = "Account Settings", onClick = onNavigateToSettings)
        Spacer(modifier = Modifier.height(8.dp))
        ProfileMenuItem(title = "About", onClick = onNavigateToAbout)

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { showSignOutDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(48.dp)
        ) {
            Text("Sign Out")
        }
    }

    if (showSignOutDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showSignOutDialog = false },
            title = { Text(text = "Sign Out") },
            text = { Text("Are you sure you want to sign out?") },
            confirmButton = {
                Button(
                    onClick = {
                        showSignOutDialog = false
                        viewModel.signOut(onSuccess = onSignOut)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Yes, Sign Out")
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showSignOutDialog = false }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurface)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun ProfileMenuItem(title: String, onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier.fillMaxWidth().height(48.dp).clickable { onClick() },
        contentAlignment = Alignment.CenterStart
    ) {
        Text(text = title, color = MaterialTheme.colorScheme.onBackground.copy(alpha=0.7f), fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun StatCard(title: String, count: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = UviePurple,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(modifier = Modifier.height(1.dp).fillMaxWidth().background(MaterialTheme.colorScheme.onBackground))
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = count.toString(),
            color = UviePurple,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
