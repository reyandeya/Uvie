package com.example.uvie.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvie.data.models.UserMovie
import com.example.uvie.data.remote.CastMember
import com.example.uvie.data.remote.Movie
import com.example.uvie.data.remote.MovieDetails
import com.example.uvie.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailViewModel : ViewModel() {
    private val repository = MovieRepository()

    private val _movieDetails = MutableStateFlow<MovieDetails?>(null)
    val movieDetails: StateFlow<MovieDetails?> = _movieDetails.asStateFlow()

    private val _cast = MutableStateFlow<List<CastMember>>(emptyList())
    val cast: StateFlow<List<CastMember>> = _cast.asStateFlow()

    private val _similarMovies = MutableStateFlow<List<Movie>>(emptyList())
    val similarMovies: StateFlow<List<Movie>> = _similarMovies.asStateFlow()

    private val _movieStatus = MutableStateFlow<String?>(null)
    val movieStatus: StateFlow<String?> = _movieStatus.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadMovieDetails(movieId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _movieDetails.value = repository.getMovieDetails(movieId)
                _cast.value = repository.getMovieCredits(movieId)
                _similarMovies.value = repository.getSimilarMovies(movieId)
                _movieStatus.value = repository.getUserMovieStatus(movieId)
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateMovieStatus(status: String) {
        viewModelScope.launch {
            val details = _movieDetails.value ?: return@launch
            try {
                if (_movieStatus.value == status) {
                    // Remove if toggling same status
                    repository.removeUserMovie(details.id)
                    _movieStatus.value = null
                } else {
                    val userMovie = UserMovie(
                        movieId = details.id,
                        title = details.title,
                        posterPath = details.posterPath,
                        rating = details.voteAverage,
                        releaseYear = details.releaseDate?.take(4),
                        status = status
                    )
                    repository.saveUserMovie(userMovie)
                    _movieStatus.value = status
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
