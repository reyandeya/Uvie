package com.example.uvie.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvie.data.models.UserMovie
import com.example.uvie.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WatchlistViewModel : ViewModel() {
    private val repository = MovieRepository()

    private val _movies = MutableStateFlow<List<UserMovie>>(emptyList())
    val movies: StateFlow<List<UserMovie>> = _movies.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _currentTab = MutableStateFlow("watchlist")
    val currentTab: StateFlow<String> = _currentTab.asStateFlow()

    fun setTabAndLoad(tab: String) {
        _currentTab.value = tab
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _movies.value = repository.getUserMovies(_currentTab.value)
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun removeMovie(movieId: Long) {
        viewModelScope.launch {
            try {
                repository.removeUserMovie(movieId)
                loadMovies() // Refresh
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
