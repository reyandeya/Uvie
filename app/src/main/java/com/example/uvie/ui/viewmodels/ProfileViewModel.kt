package com.example.uvie.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uvie.data.remote.SupabaseInstance
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileStats(
    val name: String? = null,
    val username: String? = null,
    val watchlistCount: Int = 0,
    val watchingCount: Int = 0,
    val watchedCount: Int = 0
)

class ProfileViewModel : ViewModel() {
    private val supabase = SupabaseInstance.client

    private val _profileStats = MutableStateFlow(UserProfileStats())
    val profileStats: StateFlow<UserProfileStats> = _profileStats.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = supabase.auth.currentUserOrNull()
                if (user != null) {
                    val name = user.userMetadata?.get("name")?.toString()?.replace("\"", "")
                    val username = user.userMetadata?.get("username")?.toString()?.replace("\"", "")
                    
                    // Note: Supabase Kotlin SDK count query is a bit complex, 
                    // we'll fetch all and count for simplicity here, or just mock stats if needed.
                    val movies = supabase.postgrest["user_movies"].select {
                        filter { eq("user_id", user.id) }
                    }.decodeList<com.example.uvie.data.models.UserMovie>()

                    _profileStats.value = UserProfileStats(
                        name = name,
                        username = username,
                        watchlistCount = movies.count { it.status == "watchlist" },
                        watchingCount = movies.count { it.status == "watching" },
                        watchedCount = movies.count { it.status == "watched" }
                    )
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signOut(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                supabase.auth.signOut()
                onSuccess()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
