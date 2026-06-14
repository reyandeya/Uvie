package com.example.uvie.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest

object SupabaseInstance {
    private const val SUPABASE_URL = "https://ittkmuxsmnyjfailxgym.supabase.co"
    private const val SUPABASE_KEY = "sb_publishable_w9CymvxYwqjR1wvtL7-UAQ_UFsn4V0y"

    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_KEY
        ) {
            install(Auth)
            install(Postgrest)
        }
    }
}
