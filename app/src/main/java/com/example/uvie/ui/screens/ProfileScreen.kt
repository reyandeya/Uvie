package com.example.uvie.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
    var showSignOutDialog by remember { mutableStateOf(false) }

    val bgColor = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surfaceVariant
    val textColor = MaterialTheme.colorScheme.onBackground
    val secondaryTextColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(top = 48.dp), // Added more top padding
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Centered Avatar
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(UviePurple, Color(0xFF9C27B0))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = profileStats.name?.take(1)?.uppercase() ?: "U",
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = (profileStats.name ?: profileStats.username ?: "David Saito").replaceFirstChar { it.uppercase() },
            color = textColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.5).sp
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = "${profileStats.username ?: "email"}@email.com",
            color = UviePurple.copy(alpha = 0.8f),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Stats Row - underlined text style
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            UnderlinedStat(title = "Watchlist", count = profileStats.watchlistCount)
            UnderlinedStat(title = "Watching", count = profileStats.watchingCount)
            UnderlinedStat(title = "Watched", count = profileStats.watchedCount)
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Divider
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(surfaceColor.copy(alpha=0.5f)))

        Spacer(modifier = Modifier.height(16.dp))

        // Menu Items
        UberMenuItem(
            title = "Account Settings",
            icon = Icons.Default.Settings,
            textColor = textColor,
            onClick = onNavigateToSettings
        )
        
        UberMenuItem(
            title = "App Theme",
            icon = Icons.Default.BrightnessMedium,
            textColor = textColor,
            onClick = onToggleTheme
        )
        
        UberMenuItem(
            title = "About",
            icon = Icons.Default.Info,
            textColor = textColor,
            onClick = onNavigateToAbout
        )

        UberMenuItem(
            title = "Log Out",
            icon = Icons.Default.ExitToApp,
            textColor = Color(0xFFE50914),
            onClick = { showSignOutDialog = true }
        )

        Spacer(modifier = Modifier.height(80.dp))
    }

    if (showSignOutDialog) {
        AlertDialog(
            onDismissRequest = { showSignOutDialog = false },
            title = {
                Text(
                    text = "Log Out",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "Are you sure you want to log out?",
                    color = secondaryTextColor
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSignOutDialog = false
                        viewModel.signOut(onSuccess = onSignOut)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Log Out", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showSignOutDialog = false },
                ) {
                    Text("Cancel", color = textColor)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp),
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun UberMenuItem(title: String, icon: ImageVector, textColor: Color, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = title,
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun UnderlinedStat(title: String, count: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = UviePurple,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .drawBehind {
                    val strokeWidth = 2.dp.toPx()
                    val y = size.height + 4.dp.toPx() - strokeWidth / 2
                    drawLine(
                        color = UviePurple,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = strokeWidth
                    )
                }
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = count.toString(),
            color = UviePurple,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
