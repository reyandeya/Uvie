package com.example.uvie.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserMovie(
    val id: String? = null, // UUID from Supabase
    @SerialName("user_id") val userId: String? = null,
    @SerialName("movie_id") val movieId: Long,
    val title: String,
    @SerialName("poster_path") val posterPath: String?,
    val rating: Float?,
    @SerialName("release_year") val releaseYear: String?,
    val status: String, // 'watchlist', 'watching', 'watched'
    @SerialName("created_at") val createdAt: String? = null
)

@Serializable
data class UserProfile(
    val id: String,
    val name: String?,
    val username: String?,
    @SerialName("avatar_url") val avatarUrl: String?,
    @SerialName("created_at") val createdAt: String? = null
)
