package com.example.uvie.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvie.data.remote.Movie
import com.example.uvie.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repository = MovieRepository()

    private val _trendingMovies = MutableStateFlow<List<Movie>>(emptyList())
    val trendingMovies: StateFlow<List<Movie>> = _trendingMovies.asStateFlow()

    private val _popularMovies = MutableStateFlow<List<Movie>>(emptyList())
    val popularMovies: StateFlow<List<Movie>> = _popularMovies.asStateFlow()

    private val _upcomingMovies = MutableStateFlow<List<Movie>>(emptyList())
    val upcomingMovies: StateFlow<List<Movie>> = _upcomingMovies.asStateFlow()

    private val _recommendedMovies = MutableStateFlow<List<Movie>>(emptyList())
    val recommendedMovies: StateFlow<List<Movie>> = _recommendedMovies.asStateFlow()
    
    private val _recommendationSource = MutableStateFlow<String?>(null)
    val recommendationSource: StateFlow<String?> = _recommendationSource.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _trendingMovies.value = repository.getTrendingMovies()
                _popularMovies.value = repository.getPopularMovies()
                _upcomingMovies.value = repository.getUpcomingMovies()
                
                // Recommendation Algorithm
                val watched = repository.getUserMovies("watched")
                if (watched.isNotEmpty()) {
                    val randomWatched = watched.random()
                    _recommendationSource.value = "Because you watched a similar movie"
                    _recommendedMovies.value = repository.getSimilarMovies(randomWatched.movieId)
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
}
