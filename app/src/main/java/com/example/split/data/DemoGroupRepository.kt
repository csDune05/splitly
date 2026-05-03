package com.example.split.data

import com.example.split.R
import com.example.split.model.FriendConnectionStatus
import com.example.split.model.FriendProfile
import com.example.split.model.GroupBalance
import com.example.split.model.GroupExpense
import com.example.split.model.GroupMember
import com.example.split.model.GroupMemberRole
import com.example.split.model.GroupSettlement
import com.example.split.model.GroupStatus
import com.example.split.model.MemberPaymentProfile
import com.example.split.model.SettlementStatus
import com.example.split.model.SplitGroup

object DemoGroupRepository {
    fun groups(): List<SplitGroup> =
        listOf(
            SplitGroup(
                id = "group_1",
                name = "Nha Trang trip",
                status = GroupStatus.PaymentProcess,
                memberAvatarResIds = listOf(
                    R.drawable.user_avatar,
                    R.drawable.user2_avatar,
                    R.drawable.user3_avatar,
                    R.drawable.user4_avatar,
                ),
                totalMemberCount = 4,
            ),
            SplitGroup(
                id = "group_2",
                name = "Breakfast at school",
                status = GroupStatus.Active,
                memberAvatarResIds = listOf(
                    R.drawable.user_avatar,
                    R.drawable.user3_avatar,
                ),
                totalMemberCount = 2,
            ),
            SplitGroup(
                id = "group_3",
                name = "Soc Son Camping",
                status = GroupStatus.Done,
                memberAvatarResIds = listOf(
                    R.drawable.user4_avatar,
                    R.drawable.user3_avatar,
                    R.drawable.user2_avatar,
                    R.drawable.user_avatar,
                ),
                totalMemberCount = 12,
            ),
            SplitGroup(
                id = "group_4",
                name = "Weekend Hiking",
                status = GroupStatus.Done,
                memberAvatarResIds = listOf(
                    R.drawable.user2_avatar,
                    R.drawable.user4_avatar,
                    R.drawable.user_avatar,
                    R.drawable.user3_avatar,
                ),
                totalMemberCount = 5,
            ),
        )

    fun members(groupId: String): List<GroupMember> =
        when (groupId) {
            "group_1" -> listOf(
                member("member_dung", "Dung", R.drawable.user_avatar, GroupMemberRole.Owner),
                member("member_minh", "Minh", R.drawable.user2_avatar),
                member("member_an", "An", R.drawable.user3_avatar),
                member("member_linh", "Linh", R.drawable.user4_avatar),
            )

            "group_2" -> listOf(
                member("member_dung", "Dung", R.drawable.user_avatar, GroupMemberRole.Owner),
                member("member_an", "An", R.drawable.user3_avatar),
            )

            "group_3" -> listOf(
                member("member_linh", "Linh", R.drawable.user4_avatar, GroupMemberRole.Owner),
                member("member_an", "An", R.drawable.user3_avatar),
                member("member_minh", "Minh", R.drawable.user2_avatar),
                member("member_dung", "Dung", R.drawable.user_avatar),
            )

            "group_4" -> listOf(
                member("member_minh", "Minh", R.drawable.user2_avatar, GroupMemberRole.Owner),
                member("member_linh", "Linh", R.drawable.user4_avatar),
                member("member_dung", "Dung", R.drawable.user_avatar),
                member("member_an", "An", R.drawable.user3_avatar),
            )

            else -> emptyList()
        }

    fun expenses(groupId: String): List<GroupExpense> =
        when (groupId) {
            "group_1" -> listOf(
                expense(
                    id = "expense_1",
                    title = "Seafood dinner",
                    amount = 1_200_000,
                    paidByMemberId = "member_minh",
                    participantMemberIds = listOf("member_dung", "member_minh", "member_an", "member_linh"),
                    dateLabel = "Today",
                ),
                expense(
                    id = "expense_2",
                    title = "Hotel deposit",
                    amount = 900_000,
                    paidByMemberId = "member_dung",
                    participantMemberIds = listOf("member_dung", "member_minh", "member_an"),
                    dateLabel = "Yesterday",
                ),
                expense(
                    id = "expense_3",
                    title = "Taxi to beach",
                    amount = 350_000,
                    paidByMemberId = "member_linh",
                    participantMemberIds = listOf("member_dung", "member_minh", "member_an", "member_linh"),
                    dateLabel = "Yesterday",
                ),
            )

            "group_2" -> listOf(
                expense(
                    id = "expense_4",
                    title = "Breakfast combo",
                    amount = 120_000,
                    paidByMemberId = "member_dung",
                    participantMemberIds = listOf("member_dung", "member_an"),
                    dateLabel = "Today",
                ),
            )

            "group_3" -> listOf(
                expense(
                    id = "expense_5",
                    title = "Camping food",
                    amount = 1_600_000,
                    paidByMemberId = "member_linh",
                    participantMemberIds = listOf("member_linh", "member_an", "member_minh", "member_dung"),
                    dateLabel = "Apr 24",
                ),
                expense(
                    id = "expense_6",
                    title = "Tent rental",
                    amount = 800_000,
                    paidByMemberId = "member_minh",
                    participantMemberIds = listOf("member_linh", "member_an", "member_minh", "member_dung"),
                    dateLabel = "Apr 23",
                ),
            )

            "group_4" -> listOf(
                expense(
                    id = "expense_7",
                    title = "Trail snacks",
                    amount = 420_000,
                    paidByMemberId = "member_minh",
                    participantMemberIds = listOf("member_minh", "member_linh", "member_dung"),
                    dateLabel = "Last week",
                ),
                expense(
                    id = "expense_8",
                    title = "Fuel",
                    amount = 300_000,
                    paidByMemberId = "member_dung",
                    participantMemberIds = listOf("member_minh", "member_linh", "member_dung", "member_an"),
                    dateLabel = "Last week",
                ),
            )

            else -> emptyList()
        }

    fun friends(): List<GroupMember> = allFriends()

    fun friendProfiles(): List<FriendProfile> =
        listOf(
            friendProfile(
                id = "member_minh",
                name = "Minh Tran",
                email = "minh.tran@example.com",
                avatarResId = R.drawable.user2_avatar,
                mutualFriendCount = 8,
                initialStatus = FriendConnectionStatus.Friend,
            ),
            friendProfile(
                id = "member_linh",
                name = "Linh Pham",
                email = "linh.pham@example.com",
                avatarResId = R.drawable.user4_avatar,
                mutualFriendCount = 5,
                initialStatus = FriendConnectionStatus.Friend,
            ),
            friendProfile(
                id = "member_an",
                name = "An Le",
                email = "an.le@example.com",
                avatarResId = R.drawable.user3_avatar,
                mutualFriendCount = 6,
                initialStatus = FriendConnectionStatus.IncomingRequest,
            ),
            friendProfile(
                id = "member_huy",
                name = "Huy Hoang",
                email = "huy.hoang@example.com",
                avatarResId = R.drawable.user2_avatar,
                mutualFriendCount = 3,
                initialStatus = FriendConnectionStatus.Suggested,
            ),
            friendProfile(
                id = "member_mai",
                name = "Mai Do",
                email = "mai.do@example.com",
                avatarResId = R.drawable.user3_avatar,
                mutualFriendCount = 4,
                initialStatus = FriendConnectionStatus.Suggested,
            ),
            friendProfile(
                id = "member_khoa",
                name = "Khoa Nguyen",
                email = "khoa.nguyen@example.com",
                avatarResId = R.drawable.user_avatar,
                mutualFriendCount = 2,
                initialStatus = FriendConnectionStatus.Suggested,
            ),
            friendProfile(
                id = "member_bao",
                name = "Bao Vu",
                email = "bao.vu@example.com",
                avatarResId = R.drawable.user4_avatar,
                mutualFriendCount = 1,
                initialStatus = FriendConnectionStatus.PendingSent,
            ),
        )

    fun availableFriends(groupId: String): List<GroupMember> {
        val existingIds = members(groupId).map { it.id }.toSet()
        return allFriends().filterNot { it.id in existingIds }
    }

    fun settlements(groupId: String): List<GroupSettlement> =
        when (groupId) {
            "group_1" -> listOf(
                GroupSettlement(
                    id = "settlement_1",
                    fromMemberId = "member_an",
                    toMemberId = "member_dung",
                    amount = 100_000,
                    status = SettlementStatus.WaitingForReceiverConfirmation,
                    receiptLabel = "An_receipt_demo.jpg",
                ),
            )

            else -> emptyList()
        }

    fun paymentProfiles(): Map<String, MemberPaymentProfile> =
        listOf(
            paymentProfile("member_dung", "Splitly Bank", "Dung Nguyen", "0123 456 789", hasQr = true),
            paymentProfile("member_minh", "Demo Bank", "Minh Tran", "0987 654 321", hasQr = true),
            paymentProfile("member_an", "Blue Wallet", "An Le", "1122 334 455", hasQr = true),
            paymentProfile("member_linh", "Pocket Bank", "Linh Pham", "7788 990 011", hasQr = true),
            paymentProfile("member_huy", "Demo Bank", "Huy Hoang", "4455 667 788", hasQr = false),
            paymentProfile("member_mai", "Blue Wallet", "Mai Do", "2233 445 566", hasQr = false),
        ).associateBy { it.memberId }

    fun balances(
        members: List<GroupMember>,
        expenses: List<GroupExpense>,
        settlements: List<GroupSettlement> = emptyList(),
    ): List<GroupBalance> {
        val paid = members.associate { member ->
            member.id to expenses
                .filter { it.paidByMemberId == member.id }
                .sumOf { it.amount }
        }
        val share = members.associate { member ->
            member.id to expenses.sumOf { expense ->
                if (member.id in expense.participantMemberIds && expense.participantMemberIds.isNotEmpty()) {
                    expense.amount / expense.participantMemberIds.size
                } else {
                    0L
                }
            }
        }
        val net = members.associate { member ->
            member.id to ((paid[member.id] ?: 0L) - (share[member.id] ?: 0L))
        }.toMutableMap()

        settlements
            .filter { it.status == SettlementStatus.Confirmed }
            .forEach { settlement ->
                net[settlement.fromMemberId] = (net[settlement.fromMemberId] ?: 0L) + settlement.amount
                net[settlement.toMemberId] = (net[settlement.toMemberId] ?: 0L) - settlement.amount
            }

        val debtors = net.filterValues { it < 0 }.toMutableMap()
        val creditors = net.filterValues { it > 0 }.toMutableMap()
        val balances = mutableListOf<GroupBalance>()

        debtors.keys.toList().forEach { debtorId ->
            var debt = -(debtors[debtorId] ?: 0L)
            creditors.keys.toList().forEach { creditorId ->
                if (debt <= 0L) return@forEach
                val credit = creditors[creditorId] ?: 0L
                if (credit <= 0L) return@forEach

                val amount = minOf(debt, credit)
                balances.add(
                    GroupBalance(
                        fromMemberId = debtorId,
                        toMemberId = creditorId,
                        amount = amount,
                    ),
                )
                debt -= amount
                creditors[creditorId] = credit - amount
            }
        }

        return balances
    }

    private fun allFriends(): List<GroupMember> =
        listOf(
            member("member_dung", "Dung", R.drawable.user_avatar),
            member("member_minh", "Minh", R.drawable.user2_avatar),
            member("member_an", "An", R.drawable.user3_avatar),
            member("member_linh", "Linh", R.drawable.user4_avatar),
            member("member_huy", "Huy", R.drawable.user2_avatar),
            member("member_mai", "Mai", R.drawable.user3_avatar),
        )

    private fun member(
        id: String,
        name: String,
        avatarResId: Int,
        role: GroupMemberRole = GroupMemberRole.Member,
    ): GroupMember =
        GroupMember(
            id = id,
            name = name,
            avatarResId = avatarResId,
            role = role,
        )

    private fun friendProfile(
        id: String,
        name: String,
        email: String,
        avatarResId: Int,
        mutualFriendCount: Int,
        initialStatus: FriendConnectionStatus,
    ): FriendProfile =
        FriendProfile(
            id = id,
            name = name,
            email = email,
            avatarResId = avatarResId,
            mutualFriendCount = mutualFriendCount,
            initialStatus = initialStatus,
        )

    private fun paymentProfile(
        memberId: String,
        bankName: String,
        accountName: String,
        accountNumber: String,
        hasQr: Boolean,
    ): MemberPaymentProfile =
        MemberPaymentProfile(
            memberId = memberId,
            bankName = bankName,
            accountName = accountName,
            accountNumber = accountNumber,
            hasQr = hasQr,
        )

    private fun expense(
        id: String,
        title: String,
        amount: Long,
        paidByMemberId: String,
        participantMemberIds: List<String>,
        dateLabel: String,
    ): GroupExpense =
        GroupExpense(
            id = id,
            title = title,
            amount = amount,
            paidByMemberId = paidByMemberId,
            participantMemberIds = participantMemberIds,
            dateLabel = dateLabel,
        )
}
