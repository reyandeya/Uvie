package com.example.uvie.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvie.data.remote.Movie
import com.example.uvie.data.repository.MovieRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val repository = MovieRepository()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Movie>>(emptyList())
    val searchResults: StateFlow<List<Movie>> = _searchResults.asStateFlow()

    private val _recentSearches = MutableStateFlow<List<String>>(listOf("Avatar", "Action", "Toy Story", "Marvel", "Horror"))
    val recentSearches: StateFlow<List<String>> = _recentSearches.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
        searchJob?.cancel()
        
        if (newQuery.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        searchJob = viewModelScope.launch {
            delay(500) // Debounce
            _isLoading.value = true
            try {
                val results = repository.searchMovies(newQuery)
                _searchResults.value = results
                // Add to recent searches if there are results
                if (results.isNotEmpty() && newQuery.length > 2) {
                    addToRecentSearches(newQuery)
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun addToRecentSearches(query: String) {
        val current = _recentSearches.value.toMutableList()
        current.remove(query) // Remove if exists to put it at the front
        current.add(0, query)
        if (current.size > 10) current.removeLast() // Keep max 10
        _recentSearches.value = current
    }

    fun clearQuery() {
        _query.value = ""
        _searchResults.value = emptyList()
        searchJob?.cancel()
    }
}
