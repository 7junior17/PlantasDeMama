package com.example.plantasdemam

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

val supabase = createSupabaseClient(
    supabaseUrl = "https://xaydbhcmyacqvxuacgmr.supabase.co",
    supabaseKey = "sb_publishable_CO62T9c5VvtoOqRvZov4mg_ZUB9j5oo"
) {
    install(Postgrest)
}