package com.app.movieexplorer2.data.remote.models

data class GuestSessionResponse(

    val success: Boolean,
    val guest_session_id: String,
    val expires_at: String
)
