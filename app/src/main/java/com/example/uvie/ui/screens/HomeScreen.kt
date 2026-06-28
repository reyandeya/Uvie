package com.example.uvie.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.uvie.R
import com.example.uvie.ui.components.MovieCard
import com.example.uvie.ui.components.TrendingCarousel
import com.example.uvie.ui.viewmodels.HomeViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToDetails: (Long) -> Unit
) {
    val trendingMovies by viewModel.trendingMovies.collectAsState()
    val popularMovies by viewModel.popularMovies.collectAsState()
    val upcomingMovies by viewModel.upcomingMovies.collectAsState()
    val recommendedMovies by viewModel.recommendedMovies.collectAsState()
    val recSource by viewModel.recommendationSource.collectAsState()

    // Filter upcoming to only movies that haven't released yet
    val today = LocalDate.now()
    val endOfMonth = today.withDayOfMonth(today.lengthOfMonth())
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val filteredUpcoming = upcomingMovies.filter { movie ->
        try {
            val releaseDate = LocalDate.parse(movie.releaseDate ?: "", formatter)
            releaseDate.isAfter(today) && !releaseDate.isAfter(endOfMonth)
        } catch (e: Exception) {
            false
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.logo_uvie),
                contentDescription = "Uvie Logo",
                modifier = Modifier
                    .padding(start = 16.dp, top = 48.dp, bottom = 16.dp)
                    .height(32.dp)
            )
            
            Text(
                text = "TRENDING",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            TrendingCarousel(
                movies = trendingMovies,
                onMovieClick = onNavigateToDetails
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "POPULAR",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
                items(popularMovies) { movie ->
                    Box(modifier = Modifier.padding(end=12.dp).width(120.dp).clickable{ onNavigateToDetails(movie.id) }) {
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.height(180.dp).clip(RoundedCornerShape(8.dp))
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            if (filteredUpcoming.isNotEmpty()) {
                Text(
                    text = "UPCOMING THIS MONTH",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
                    items(filteredUpcoming) { movie ->
                        Box(modifier = Modifier.padding(end=12.dp).width(120.dp).clickable{ onNavigateToDetails(movie.id) }) {
                            AsyncImage(
                                model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.height(180.dp).clip(RoundedCornerShape(8.dp))
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            if (recommendedMovies.isNotEmpty()) {
                Text(
                    text = recSource?.uppercase() ?: "RECOMMENDED FOR YOU",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        
        if (recommendedMovies.isNotEmpty()) {
            items(recommendedMovies) { movie ->
                MovieCard(
                    movie = movie,
                    onClick = { onNavigateToDetails(movie.id) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}
