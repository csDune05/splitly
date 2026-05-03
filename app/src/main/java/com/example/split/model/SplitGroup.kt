package com.example.split.model

import androidx.annotation.DrawableRes

enum class GroupStatus {
    Active,
    PaymentProcess,
    Done,
}

enum class GroupMemberRole {
    Owner,
    Member,
}

data class GroupMember(
    val id: String,
    val name: String,
    @param:DrawableRes val avatarResId: Int,
    val role: GroupMemberRole = GroupMemberRole.Member,
)

data class GroupExpense(
    val id: String,
    val title: String,
    val amount: Long,
    val paidByMemberId: String,
    val participantMemberIds: List<String>,
    val dateLabel: String,
)

data class GroupBalance(
    val fromMemberId: String,
    val toMemberId: String,
    val amount: Long,
)

enum class SettlementStatus {
    Unpaid,
    WaitingForReceiverConfirmation,
    Confirmed,
    Rejected,
}

data class GroupSettlement(
    val id: String,
    val fromMemberId: String,
    val toMemberId: String,
    val amount: Long,
    val status: SettlementStatus,
    val receiptLabel: String? = null,
)

data class MemberPaymentProfile(
    val memberId: String,
    val bankName: String,
    val accountName: String,
    val accountNumber: String,
    val hasQr: Boolean,
)

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
