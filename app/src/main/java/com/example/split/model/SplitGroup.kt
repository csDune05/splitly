package com.example.split.model

import androidx.annotation.DrawableRes

enum class GroupStatus {
    Active,
    PaymentProcess,
    Done,
}

data class SplitGroup(
    val id: String,
    val name: String,
    val status: GroupStatus,
    @param:DrawableRes val memberAvatarResIds: List<Int> = emptyList(),
    val totalMemberCount: Int = 0,
) {
    val memberCount: Int = totalMemberCount
    val displayAvatarResIds: List<Int> = memberAvatarResIds.take(4)
}
