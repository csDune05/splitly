package com.example.split.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.split.R
import com.example.split.data.DemoGroupRepository
import com.example.split.model.GroupMember
import com.example.split.model.GroupMemberRole
import com.example.split.model.GroupStatus
import com.example.split.model.SplitGroup
import com.example.split.navigation.BottomNavDestination
import com.example.split.ui.components.BouncyButton
import com.example.split.ui.components.GroupCard
import com.example.split.ui.components.HomeHeader
import com.example.split.ui.components.SplitlyBottomBar
import com.example.split.ui.components.SplitlyTextStyles
import com.example.split.ui.theme.SplitlyColors

private enum class FilterOption {
    All,
    Active,
    PaymentProcess,
    Done,
}

private const val CurrentUserId = "member_dung"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    groups: List<SplitGroup>,
    onGroupSelected: (SplitGroup) -> Unit,
    onCreateGroup: (SplitGroup, List<GroupMember>) -> Unit,
    onFriendsSelected: () -> Unit,
    onStatsSelected: () -> Unit,
    onProfileSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var currentFilter by rememberSaveable { mutableStateOf(FilterOption.All) }
    var showCreateGroupSheet by rememberSaveable { mutableStateOf(false) }
    var showFilterSheet by rememberSaveable { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val filteredGroups = groups.filterBy(currentFilter)

    Scaffold(
        modifier = modifier,
        containerColor = SplitlyColors.PageBackground,
        topBar = { HomeHeader(listState = listState) },
        bottomBar = {
            SplitlyBottomBar(
                selectedDestination = BottomNavDestination.Home,
                onHomeClick = {},
                onFriendsClick = onFriendsSelected,
                onAddClick = { showCreateGroupSheet = true },
                onStatsClick = onStatsSelected,
                onProfileClick = onProfileSelected,
            )
        },
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier.padding(innerPadding),
        ) {
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = " Groups",
                            color = SplitlyColors.Primary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        BouncyButton(
                            onClick = { showFilterSheet = true },
                            pressedScale = 0.88f,
                        ) {
                            Row(
                                modifier = Modifier
                                    .background(
                                        SplitlyColors.Primary.copy(alpha = 0.1f),
                                        RoundedCornerShape(20.dp),
                                    )
                                    .border(
                                        1.dp,
                                        SplitlyColors.Primary.copy(alpha = 0.3f),
                                        RoundedCornerShape(20.dp),
                                    )
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "View by",
                                    color = SplitlyColors.Primary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                )
                                Icon(
                                    imageVector = Icons.Filled.ExpandMore,
                                    contentDescription = null,
                                    tint = SplitlyColors.Primary,
                                    modifier = Modifier.size(16.dp),
                                )
                            }
                        }
                    }

                    if (currentFilter != FilterOption.All) {
                        Box(
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .background(Color(0xFFF5F5F5), RoundedCornerShape(16.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                        ) {
                            Text(
                                text = "Showing: ${currentFilter.label} (${filteredGroups.size})",
                                style = SplitlyTextStyles.caption,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }
            }

            items(
                count = filteredGroups.size,
                key = { filteredGroups[it].id },
            ) { index ->
                GroupCard(
                    group = filteredGroups[index],
                    onClick = { onGroupSelected(filteredGroups[index]) },
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }

            if (filteredGroups.isEmpty()) {
                item {
                    EmptyGroupsMessage()
                }
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }

    if (showCreateGroupSheet) {
        ModalBottomSheet(
            onDismissRequest = { showCreateGroupSheet = false },
            containerColor = Color.White,
        ) {
            CreateGroupSheet(
                onCreateGroup = { group, members ->
                    onCreateGroup(group, members)
                    currentFilter = FilterOption.All
                    showCreateGroupSheet = false
                },
                onCancel = { showCreateGroupSheet = false },
            )
        }
    }

    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            containerColor = Color.White,
        ) {
            FilterOptionsSheet(
                selectedFilter = currentFilter,
                onFilterSelected = {
                    currentFilter = it
                    showFilterSheet = false
                },
            )
        }
    }
}

@Composable
private fun EmptyGroupsMessage() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
    ) {
        Icon(
            imageVector = Icons.Filled.Group,
            contentDescription = null,
            tint = Color(0xFFE0E0E0),
            modifier = Modifier.size(64.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No groups found",
            color = Color(0xFF9E9E9E),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Try changing the filter to see more groups",
            color = Color(0xFFBDBDBD),
            fontSize = 14.sp,
        )
    }
}

@Composable
private fun CreateGroupSheet(
    onCreateGroup: (SplitGroup, List<GroupMember>) -> Unit,
    onCancel: () -> Unit,
) {
    val friends = DemoGroupRepository.friends()
    val currentUser = friends.firstOrNull { it.id == CurrentUserId }
        ?: GroupMember(
            id = CurrentUserId,
            name = "Dung",
            avatarResId = R.drawable.user_avatar,
            role = GroupMemberRole.Owner,
        )
    val candidates = friends.filterNot { it.id == CurrentUserId }
    var groupName by rememberSaveable { mutableStateOf("") }
    val selectedFriendIds = remember {
        mutableStateListOf<String>().apply {
            addAll(candidates.take(2).map { it.id })
        }
    }
    val canCreate = groupName.trim().isNotEmpty()

    fun toggleFriend(memberId: String) {
        if (memberId in selectedFriendIds) {
            selectedFriendIds.remove(memberId)
        } else {
            selectedFriendIds.add(memberId)
        }
    }

    fun createGroup() {
        if (!canCreate) return
        val selectedFriends = candidates.filter { it.id in selectedFriendIds }
        val members = listOf(currentUser.copy(role = GroupMemberRole.Owner)) +
            selectedFriends.map { it.copy(role = GroupMemberRole.Member) }
        val group = SplitGroup(
            id = "demo_group_${System.currentTimeMillis()}",
            name = groupName.trim(),
            status = GroupStatus.Active,
            memberAvatarResIds = members.map { it.avatarResId },
            totalMemberCount = members.size,
        )
        onCreateGroup(group, members)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 28.dp),
    ) {
        Text(
            text = "Create group",
            color = SplitlyColors.TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "Name the group and choose who will join it.",
            style = SplitlyTextStyles.caption,
            modifier = Modifier.padding(top = 6.dp),
        )
        Spacer(modifier = Modifier.height(18.dp))
        CreateGroupTextField(
            value = groupName,
            onValueChange = { groupName = it },
            placeholder = "Group name",
        )
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "Members",
            color = SplitlyColors.TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(10.dp))
        GroupMemberPickRow(
            member = currentUser.copy(role = GroupMemberRole.Owner),
            selected = true,
            locked = true,
            subtitle = "You - Owner",
            onClick = {},
        )
        candidates.forEach { friend ->
            GroupMemberPickRow(
                member = friend,
                selected = friend.id in selectedFriendIds,
                locked = false,
                subtitle = "Friend",
                onClick = { toggleFriend(friend.id) },
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        CreateGroupButton(
            enabled = canCreate,
            onClick = ::createGroup,
        )
        BouncyButton(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth(),
            pressedScale = 0.9f,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Cancel",
                    color = SplitlyColors.TextSecondary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
        Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
    }
}

@Composable
private fun CreateGroupTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        placeholder = {
            Text(text = placeholder, style = SplitlyTextStyles.hint)
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            keyboardType = KeyboardType.Text,
        ),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = SplitlyColors.FieldBackground,
            unfocusedContainerColor = SplitlyColors.FieldBackground,
            focusedBorderColor = SplitlyColors.PrimaryLight,
            unfocusedBorderColor = SplitlyColors.FieldBorder,
            cursorColor = SplitlyColors.Primary,
        ),
    )
}

@Composable
private fun GroupMemberPickRow(
    member: GroupMember,
    selected: Boolean,
    locked: Boolean,
    subtitle: String,
    onClick: () -> Unit,
) {
    BouncyButton(
        onClick = onClick,
        enabled = !locked,
        pressedScale = 0.9f,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (selected) SplitlyColors.Primary.copy(alpha = 0.08f) else Color.Transparent,
                    RoundedCornerShape(12.dp),
                )
                .border(
                    1.dp,
                    if (selected) SplitlyColors.Primary.copy(alpha = 0.25f) else SplitlyColors.FieldBorder,
                    RoundedCornerShape(12.dp),
                )
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = if (selected) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (selected) SplitlyColors.Primary else SplitlyColors.TextSecondary,
                modifier = Modifier.size(20.dp),
            )
            Image(
                painter = painterResource(id = member.avatarResId),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(34.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, Color.White, CircleShape),
                contentScale = ContentScale.Crop,
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp),
            ) {
                Text(
                    text = member.name,
                    color = SplitlyColors.TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = subtitle,
                    style = SplitlyTextStyles.caption,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }
    }
}

@Composable
private fun CreateGroupButton(
    enabled: Boolean,
    onClick: () -> Unit,
) {
    BouncyButton(
        onClick = onClick,
        enabled = enabled,
        pressedScale = 0.86f,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (enabled) SplitlyColors.Primary else SplitlyColors.TextSecondary.copy(alpha = 0.25f),
                    RoundedCornerShape(12.dp),
                )
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Create group",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun FilterOptionsSheet(
    selectedFilter: FilterOption,
    onFilterSelected: (FilterOption) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 24.dp),
    ) {
        Text(
            text = "View Groups By",
            color = Color(0xFF424242),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(16.dp))
        FilterOption.entries.forEach { filter ->
            FilterRow(
                filter = filter,
                isSelected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
            )
        }
        Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
    }
}

@Composable
private fun FilterRow(
    filter: FilterOption,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    BouncyButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (isSelected) SplitlyColors.Primary.copy(alpha = 0.1f) else Color.Transparent,
                    RoundedCornerShape(12.dp),
                )
                .border(
                    1.5.dp,
                    if (isSelected) SplitlyColors.Primary else Color(0xFFE5E7EB),
                    RoundedCornerShape(12.dp),
                )
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = if (isSelected) {
                    Icons.Filled.CheckCircle
                } else {
                    Icons.Filled.RadioButtonUnchecked
                },
                contentDescription = null,
                tint = if (isSelected) SplitlyColors.Primary else Color(0xFFBDBDBD),
                modifier = Modifier.size(20.dp),
            )
            Text(
                text = filter.label,
                color = if (isSelected) SplitlyColors.Primary else Color(0xFF616161),
                fontSize = 16.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                modifier = Modifier.padding(start = 12.dp),
            )
        }
    }
}

private fun List<SplitGroup>.filterBy(filter: FilterOption): List<SplitGroup> =
    when (filter) {
        FilterOption.All -> this
        FilterOption.Active -> filter { it.status == GroupStatus.Active }
        FilterOption.PaymentProcess -> filter { it.status == GroupStatus.PaymentProcess }
        FilterOption.Done -> filter { it.status == GroupStatus.Done }
    }

private val FilterOption.label: String
    get() = when (this) {
        FilterOption.All -> "All Groups"
        FilterOption.Active -> "Active"
        FilterOption.PaymentProcess -> "Payment Process"
        FilterOption.Done -> "Done"
    }
