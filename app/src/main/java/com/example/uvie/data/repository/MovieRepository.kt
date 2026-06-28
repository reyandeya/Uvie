package com.example.uvie.data.repository

import com.example.uvie.data.models.UserMovie
import com.example.uvie.data.remote.RetrofitInstance
import com.example.uvie.data.remote.SupabaseInstance
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MovieRepository {
    private val tmdbApi = RetrofitInstance.api
    private val supabase = SupabaseInstance.client

    // TMDB API calls
    suspend fun getTrendingMovies() = tmdbApi.getTrendingMovies().results
    suspend fun getPopularMovies() = tmdbApi.getPopularMovies().results
    suspend fun getUpcomingMovies() = tmdbApi.getUpcomingMovies().results
    suspend fun getMovieDetails(id: Long) = tmdbApi.getMovieDetails(id)
    suspend fun getMovieCredits(id: Long) = tmdbApi.getMovieCredits(id).cast
    suspend fun getSimilarMovies(id: Long) = tmdbApi.getSimilarMovies(id).results
    suspend fun searchMovies(query: String) = tmdbApi.searchMovies(query).results
    suspend fun getGenres() = tmdbApi.getGenres().genres
    suspend fun getMoviesByGenre(genreId: Int) = tmdbApi.getMoviesByGenre(genreId).results

    // Supabase DB calls
    suspend fun getUserMovies(status: String? = null): List<UserMovie> {
        val user = supabase.auth.currentUserOrNull() ?: return emptyList()
        val query = supabase.postgrest["user_movies"].select {
            filter {
                eq("user_id", user.id)
                if (status != null) {
                    eq("status", status)
                }
            }
        }
        return query.decodeList<UserMovie>()
    }

    suspend fun saveUserMovie(movie: UserMovie) {
        val user = supabase.auth.currentUserOrNull() ?: return
        val movieWithUser = movie.copy(userId = user.id)
        
        supabase.postgrest["user_movies"].upsert(movieWithUser) {
            onConflict = "user_id, movie_id"
        }
    }

    suspend fun removeUserMovie(movieId: Long) {
        val user = supabase.auth.currentUserOrNull() ?: return
        supabase.postgrest["user_movies"].delete {
            filter {
                eq("user_id", user.id)
                eq("movie_id", movieId)
            }
        }
    }

    suspend fun getUserMovieStatus(movieId: Long): String? {
        val user = supabase.auth.currentUserOrNull() ?: return null
        val result = supabase.postgrest["user_movies"].select() {
            filter {
                eq("user_id", user.id)
                eq("movie_id", movieId)
            }
        }
        val rows = result.decodeList<UserMovie>()
        return rows.firstOrNull()?.status
    }
}
