package com.example.split

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.split.data.DemoGroupRepository
import com.example.split.model.FriendConnectionStatus
import com.example.split.model.GroupExpense
import com.example.split.model.GroupMember
import com.example.split.model.SplitGroup
import com.example.split.navigation.SplitRoute
import com.example.split.ui.screens.FriendScreen
import com.example.split.ui.screens.GroupScreen
import com.example.split.ui.screens.HomeScreen
import com.example.split.ui.screens.LoginScreen
import com.example.split.ui.screens.ProfileScreen
import com.example.split.ui.screens.SignupScreen
import com.example.split.ui.screens.StatsScreen
import com.example.split.ui.screens.WelcomeScreen
import com.example.split.ui.theme.SplitTheme

@Composable
fun SplitlyApp() {
    SplitTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val backStack = remember { mutableStateListOf<SplitRoute>(SplitRoute.Login) }
            val groups = remember {
                mutableStateListOf<SplitGroup>().apply {
                    addAll(DemoGroupRepository.groups())
                }
            }
            val groupMembersById = remember {
                mutableStateMapOf<String, List<GroupMember>>().apply {
                    groups.forEach { group ->
                        this[group.id] = DemoGroupRepository.members(group.id)
                    }
                }
            }
            val groupExpensesById = remember {
                mutableStateMapOf<String, List<GroupExpense>>().apply {
                    groups.forEach { group ->
                        this[group.id] = DemoGroupRepository.expenses(group.id)
                    }
                }
            }
            val friendStatuses = remember {
                mutableStateMapOf<String, FriendConnectionStatus>().apply {
                    DemoGroupRepository.friendProfiles().forEach { profile ->
                        this[profile.id] = profile.initialStatus
                    }
                }
            }
            val currentRoute = backStack.last()

            fun navigate(route: SplitRoute) {
                backStack.add(route)
            }

            fun replaceAll(route: SplitRoute) {
                backStack.clear()
                backStack.add(route)
            }

            fun goBack() {
                if (backStack.size > 1) {
                    backStack.removeAt(backStack.lastIndex)
                }
            }

            fun addGroup(group: SplitGroup, members: List<GroupMember>) {
                groups.add(0, group)
                groupMembersById[group.id] = members
                groupExpensesById[group.id] = emptyList()
            }

            fun updateGroupMembers(groupId: String, members: List<GroupMember>) {
                groupMembersById[groupId] = members
                val groupIndex = groups.indexOfFirst { it.id == groupId }
                if (groupIndex >= 0) {
                    groups[groupIndex] = groups[groupIndex].copy(
                        memberAvatarResIds = members.map { it.avatarResId },
                        totalMemberCount = members.size,
                    )
                }
            }

            fun updateGroupExpenses(groupId: String, expenses: List<GroupExpense>) {
                groupExpensesById[groupId] = expenses
            }

            BackHandler(enabled = backStack.size > 1) {
                goBack()
            }

            when (currentRoute) {
                SplitRoute.Welcome -> WelcomeScreen(
                    onCreateAccount = { navigate(SplitRoute.Signup) },
                    onSignIn = { navigate(SplitRoute.Login) },
                )

                SplitRoute.Login -> LoginScreen(
                    onSignedIn = { replaceAll(SplitRoute.Home) },
                    onGoogleSignIn = { replaceAll(SplitRoute.Home) },
                    onSignUp = { navigate(SplitRoute.Signup) },
                )

                SplitRoute.Signup -> SignupScreen(
                    onBack = ::goBack,
                    onSignIn = { navigate(SplitRoute.Login) },
                )

                SplitRoute.Home -> HomeScreen(
                    groups = groups,
                    onGroupSelected = { navigate(SplitRoute.GroupDetail(it)) },
                    onCreateGroup = ::addGroup,
                    onFriendsSelected = { replaceAll(SplitRoute.Friends) },
                    onStatsSelected = { replaceAll(SplitRoute.Stats) },
                    onProfileSelected = { replaceAll(SplitRoute.Profile) },
                )

                SplitRoute.Friends -> FriendScreen(
                    friendStatuses = friendStatuses,
                    onFriendStatusChanged = { profileId, status ->
                        friendStatuses[profileId] = status
                    },
                    onHomeSelected = { replaceAll(SplitRoute.Home) },
                    onStatsSelected = { replaceAll(SplitRoute.Stats) },
                    onProfileSelected = { replaceAll(SplitRoute.Profile) },
                )

                SplitRoute.Stats -> StatsScreen(
                    groups = groups,
                    groupMembersById = groupMembersById,
                    groupExpensesById = groupExpensesById,
                    onHomeSelected = { replaceAll(SplitRoute.Home) },
                    onFriendsSelected = { replaceAll(SplitRoute.Friends) },
                    onProfileSelected = { replaceAll(SplitRoute.Profile) },
                )

                SplitRoute.Profile -> ProfileScreen(
                    groups = groups,
                    groupExpensesById = groupExpensesById,
                    friendStatuses = friendStatuses,
                    onHomeSelected = { replaceAll(SplitRoute.Home) },
                    onFriendsSelected = { replaceAll(SplitRoute.Friends) },
                    onStatsSelected = { replaceAll(SplitRoute.Stats) },
                )

                is SplitRoute.GroupDetail -> GroupScreen(
                    group = currentRoute.group,
                    initialMembers = groupMembersById[currentRoute.group.id]
                        ?: DemoGroupRepository.members(currentRoute.group.id),
                    initialExpenses = groupExpensesById[currentRoute.group.id]
                        ?: DemoGroupRepository.expenses(currentRoute.group.id),
                    onMembersChanged = { members ->
                        updateGroupMembers(currentRoute.group.id, members)
                    },
                    onExpensesChanged = { expenses ->
                        updateGroupExpenses(currentRoute.group.id, expenses)
                    },
                    onBack = ::goBack,
                    onHomeSelected = { replaceAll(SplitRoute.Home) },
                    onFriendsSelected = { replaceAll(SplitRoute.Friends) },
                    onStatsSelected = { replaceAll(SplitRoute.Stats) },
                    onProfileSelected = { replaceAll(SplitRoute.Profile) },
                )
            }
        }
    }
}
