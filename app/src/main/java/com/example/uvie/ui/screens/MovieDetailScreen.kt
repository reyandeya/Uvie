package com.example.uvie.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.uvie.ui.components.MovieCard
import com.example.uvie.ui.theme.UviePurple
import com.example.uvie.ui.viewmodels.MovieDetailViewModel

@Composable
fun MovieDetailScreen(
    viewModel: MovieDetailViewModel,
    movieId: Long,
    onNavigateToSimilar: (Long) -> Unit,
    onNavigateBack: () -> Unit
) {
    val movie by viewModel.movieDetails.collectAsState()
    val cast by viewModel.cast.collectAsState()
    val similarMovies by viewModel.similarMovies.collectAsState()
    val movieStatus by viewModel.movieStatus.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.loadMovieDetails(movieId)
    }

    if (isLoading || movie == null) {
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = UviePurple)
        }
        return
    }

    val details = movie!!

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(250.dp)) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w780${details.backdropPath}",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
            )
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.align(Alignment.TopStart).padding(top = 48.dp, start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = androidx.compose.ui.graphics.Color.White
                )
            }
        }

        Row(modifier = Modifier.padding(16.dp).offset(y = (-60).dp)) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${details.posterPath}",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(120.dp)
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.padding(top = 60.dp)) {
                Text(
                    text = details.title,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${details.releaseDate?.take(4)} • ${details.runtime} min",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "⭐ ${details.voteAverage}",
                    color = UviePurple,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .horizontalScroll(rememberScrollState())
        ) {
            Button(
                onClick = { viewModel.updateMovieStatus("watchlist") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (movieStatus == "watchlist") UviePurple else MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.padding(end = 8.dp)
            ) { Text("Watchlist") }
            
            Button(
                onClick = { viewModel.updateMovieStatus("watching") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (movieStatus == "watching") UviePurple else MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.padding(end = 8.dp)
            ) { Text("Watching") }
            
            Button(
                onClick = { viewModel.updateMovieStatus("watched") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (movieStatus == "watched") UviePurple else MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.padding(end = 8.dp)
            ) { Text("Watched") }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Overview",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = details.overview,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Cast",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
            items(cast.take(10)) { actor ->
                Column(
                    modifier = Modifier.width(80.dp).padding(end = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = "https://image.tmdb.org/t/p/w185${actor.profilePath}",
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(60.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surface)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = actor.name,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 12.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Similar Movies",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
            items(similarMovies) { simMovie ->
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500${simMovie.posterPath}",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(120.dp)
                        .height(180.dp)
                        .padding(end = 12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onNavigateToSimilar(simMovie.id) }
                )
            }
        }
    }
}
