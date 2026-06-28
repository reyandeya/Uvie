package com.example.uvie.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uvie.ui.components.MovieCard
import com.example.uvie.ui.viewmodels.GenresViewModel

@Composable
fun GenresScreen(
    viewModel: GenresViewModel,
    onNavigateToDetails: (Long) -> Unit
) {
    val genres by viewModel.genres.collectAsState()
    val selectedGenre by viewModel.selectedGenre.collectAsState()
    val moviesByGenre by viewModel.moviesByGenre.collectAsState()

    // Spotify-like colors
    val genreColors = remember {
        listOf(
            Color(0xFFE13300), Color(0xFF1E3264), Color(0xFFE8115B), Color(0xFF148A08),
            Color(0xFF8D67AB), Color(0xFF7358FF), Color(0xFFD84000), Color(0xFFE91429),
            Color(0xFF477D95), Color(0xFF509BF5), Color(0xFF8C1932), Color(0xFFBA5D07),
            Color(0xFFB02897), Color(0xFFA5673F), Color(0xFF0D73EC), Color(0xFF477D95)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .padding(top = 24.dp)
    ) {
        if (selectedGenre == null) {
            Text(
                text = "Search",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
            )
            
            Text(
                text = "Browse all",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3), // Changed to 3 columns to make boxes smaller
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 80.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(genres.size) { index ->
                    val genre = genres[index]
                    val bgColor = genreColors[index % genreColors.size]
                    
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth()
                            .aspectRatio(1f) // Square cards like Spotify
                            .clip(RoundedCornerShape(8.dp))
                            .background(bgColor)
                            .clickable { viewModel.selectGenre(genre) }
                            .padding(12.dp)
                    ) {
                        Text(
                            text = genre.name,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color.White,
                            modifier = Modifier.align(Alignment.TopStart)
                        )
                    }
                }
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
                IconButton(onClick = { viewModel.selectGenre(null) }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = selectedGenre?.name ?: "",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            LazyColumn(
                contentPadding = PaddingValues(bottom = 80.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(moviesByGenre) { movie ->
                    MovieCard(
                        movie = movie,
                        onClick = { onNavigateToDetails(movie.id) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}
