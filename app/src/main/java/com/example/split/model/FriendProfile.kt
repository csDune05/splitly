package com.example.split.model

import androidx.annotation.DrawableRes

enum class FriendConnectionStatus {
    Friend,
    IncomingRequest,
    PendingSent,
    Suggested,
}

data class FriendProfile(
    val id: String,
    val name: String,
    val email: String,
    @param:DrawableRes val avatarResId: Int,
    val mutualFriendCount: Int,
    val initialStatus: FriendConnectionStatus,
)
