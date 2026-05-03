package com.example.split.data

import com.example.split.R
import com.example.split.model.GroupStatus
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
}
