package com.example.split.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.split.data.DemoGroupRepository
import com.example.split.model.FriendConnectionStatus
import com.example.split.model.FriendProfile
import com.example.split.navigation.BottomNavDestination
import com.example.split.ui.components.BouncyButton
import com.example.split.ui.components.SplitlyBottomBar
import com.example.split.ui.components.SplitlyTextStyles
import com.example.split.ui.theme.SplitlyColors

@Composable
fun FriendScreen(
    friendStatuses: Map<String, FriendConnectionStatus>,
    onFriendStatusChanged: (String, FriendConnectionStatus) -> Unit,
    onHomeSelected: () -> Unit,
    onStatsSelected: () -> Unit,
    onProfileSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val profiles = remember { DemoGroupRepository.friendProfiles() }
    var query by rememberSaveable { mutableStateOf("") }

    fun statusFor(profile: FriendProfile): FriendConnectionStatus =
        friendStatuses[profile.id] ?: profile.initialStatus

    val normalizedQuery = query.trim().lowercase()
    val searchResults = if (normalizedQuery.isBlank()) {
        emptyList()
    } else {
        profiles.filter { it.matches(normalizedQuery) }
    }
    val incomingRequests = profiles.filter { statusFor(it) == FriendConnectionStatus.IncomingRequest }
    val friends = profiles.filter { statusFor(it) == FriendConnectionStatus.Friend }
    val suggestions = profiles.filter {
        val status = statusFor(it)
        status == FriendConnectionStatus.Suggested || status == FriendConnectionStatus.PendingSent
    }

    Scaffold(
        modifier = modifier,
        containerColor = SplitlyColors.PageBackground,
        bottomBar = {
            SplitlyBottomBar(
                selectedDestination = BottomNavDestination.Friends,
                onHomeClick = onHomeSelected,
                onFriendsClick = {},
                onAddClick = {},
                onStatsClick = onStatsSelected,
                onProfileClick = onProfileSelected,
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            item {
                FriendHeader(
                    query = query,
                    onQueryChange = { query = it },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
                )
            }

            if (query.isNotBlank()) {
                item {
                    SearchResultsPanel(
                        results = searchResults,
                        statusFor = ::statusFor,
                        onAddFriend = { profile ->
                            onFriendStatusChanged(profile.id, FriendConnectionStatus.PendingSent)
                        },
                        onAcceptFriend = { profile ->
                            onFriendStatusChanged(profile.id, FriendConnectionStatus.Friend)
                        },
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }
            }

            if (incomingRequests.isNotEmpty()) {
                item {
                    FriendSectionHeader(
                        title = "Friend requests",
                        subtitle = "${incomingRequests.size} waiting",
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                    )
                }
                items(
                    items = incomingRequests,
                    key = { it.id },
                ) { profile ->
                    FriendProfileRow(
                        profile = profile,
                        status = statusFor(profile),
                        subtitle = "${profile.mutualFriendCount} mutual friends",
                        onAddFriend = {
                            onFriendStatusChanged(profile.id, FriendConnectionStatus.PendingSent)
                        },
                        onAcceptFriend = {
                            onFriendStatusChanged(profile.id, FriendConnectionStatus.Friend)
                        },
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }
            }

            item {
                FriendSectionHeader(
                    title = "Your friends",
                    subtitle = "${friends.size} friends",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                )
            }
            if (friends.isEmpty()) {
                item {
                    EmptyFriendState(
                        text = "No friends yet.",
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }
            } else {
                items(
                    items = friends,
                    key = { it.id },
                ) { profile ->
                    FriendProfileRow(
                        profile = profile,
                        status = statusFor(profile),
                        subtitle = profile.email,
                        onAddFriend = {
                            onFriendStatusChanged(profile.id, FriendConnectionStatus.PendingSent)
                        },
                        onAcceptFriend = {
                            onFriendStatusChanged(profile.id, FriendConnectionStatus.Friend)
                        },
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }
            }

            item {
                FriendSectionHeader(
                    title = "People you may know",
                    subtitle = "Based on mutual friends",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                )
            }
            if (suggestions.isEmpty()) {
                item {
                    EmptyFriendState(
                        text = "No suggestions right now.",
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }
            } else {
                items(
                    items = suggestions,
                    key = { it.id },
                ) { profile ->
                    FriendProfileRow(
                        profile = profile,
                        status = statusFor(profile),
                        subtitle = "${profile.mutualFriendCount} mutual friends",
                        onAddFriend = {
                            onFriendStatusChanged(profile.id, FriendConnectionStatus.PendingSent)
                        },
                        onAcceptFriend = {
                            onFriendStatusChanged(profile.id, FriendConnectionStatus.Friend)
                        },
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
private fun FriendHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Friends",
                    color = SplitlyColors.TextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Find people, accept requests, and add new friends.",
                    style = SplitlyTextStyles.caption,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(SplitlyColors.Primary.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.People,
                    contentDescription = null,
                    tint = SplitlyColors.Primary,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = SplitlyTextStyles.body,
            placeholder = {
                Text(text = "Search by name or email", style = SplitlyTextStyles.hint)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                    tint = SplitlyColors.TextSecondary,
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = SplitlyColors.PrimaryLight,
                unfocusedBorderColor = SplitlyColors.FieldBorder,
                cursorColor = SplitlyColors.Primary,
            ),
        )
    }
}

@Composable
private fun SearchResultsPanel(
    results: List<FriendProfile>,
    statusFor: (FriendProfile) -> FriendConnectionStatus,
    onAddFriend: (FriendProfile) -> Unit,
    onAcceptFriend: (FriendProfile) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(18.dp))
            .border(1.dp, Color(0xFFEDEFF3), RoundedCornerShape(18.dp))
            .padding(14.dp),
    ) {
        Text(
            text = "Search results",
            color = SplitlyColors.TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(10.dp))
        if (results.isEmpty()) {
            Text(
                text = "No matching people found.",
                style = SplitlyTextStyles.caption,
                modifier = Modifier.padding(vertical = 10.dp),
            )
        } else {
            results.forEach { profile ->
                FriendProfileRow(
                    profile = profile,
                    status = statusFor(profile),
                    subtitle = profile.email,
                    onAddFriend = { onAddFriend(profile) },
                    onAcceptFriend = { onAcceptFriend(profile) },
                    modifier = Modifier.padding(bottom = 8.dp),
                    compact = true,
                )
            }
        }
    }
}

@Composable
private fun FriendSectionHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            color = SplitlyColors.TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = subtitle,
            style = SplitlyTextStyles.caption,
        )
    }
}

@Composable
private fun FriendProfileRow(
    profile: FriendProfile,
    status: FriendConnectionStatus,
    subtitle: String,
    onAddFriend: () -> Unit,
    onAcceptFriend: () -> Unit,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = if (compact) 0.dp else 10.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFEDEFF3), RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = profile.avatarResId),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .border(2.dp, Color.White, CircleShape),
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
        ) {
            Text(
                text = profile.name,
                color = SplitlyColors.TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = subtitle,
                style = SplitlyTextStyles.caption,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 3.dp),
            )
            if (!compact && status != FriendConnectionStatus.Friend) {
                Text(
                    text = profile.email,
                    color = SplitlyColors.TextSecondary,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }
        FriendActionButton(
            status = status,
            onAddFriend = onAddFriend,
            onAcceptFriend = onAcceptFriend,
        )
    }
}

@Composable
private fun FriendActionButton(
    status: FriendConnectionStatus,
    onAddFriend: () -> Unit,
    onAcceptFriend: () -> Unit,
) {
    val label = when (status) {
        FriendConnectionStatus.Friend -> "Friends"
        FriendConnectionStatus.IncomingRequest -> "Accept"
        FriendConnectionStatus.PendingSent -> "Requested"
        FriendConnectionStatus.Suggested -> "Add"
    }
    val color = when (status) {
        FriendConnectionStatus.Friend -> SplitlyColors.Done
        FriendConnectionStatus.IncomingRequest -> SplitlyColors.Primary
        FriendConnectionStatus.PendingSent -> SplitlyColors.TextSecondary
        FriendConnectionStatus.Suggested -> SplitlyColors.Primary
    }
    val enabled = status == FriendConnectionStatus.IncomingRequest ||
        status == FriendConnectionStatus.Suggested
    val icon = when (status) {
        FriendConnectionStatus.Friend -> Icons.Filled.CheckCircle
        FriendConnectionStatus.IncomingRequest -> Icons.Filled.CheckCircle
        FriendConnectionStatus.PendingSent -> Icons.Filled.PersonAdd
        FriendConnectionStatus.Suggested -> Icons.Filled.PersonAdd
    }

    BouncyButton(
        onClick = {
            when (status) {
                FriendConnectionStatus.IncomingRequest -> onAcceptFriend()
                FriendConnectionStatus.Suggested -> onAddFriend()
                FriendConnectionStatus.Friend,
                FriendConnectionStatus.PendingSent -> Unit
            }
        },
        enabled = enabled,
        pressedScale = 0.9f,
    ) {
        Row(
            modifier = Modifier
                .background(color.copy(alpha = if (enabled) 0.1f else 0.08f), RoundedCornerShape(18.dp))
                .border(1.dp, color.copy(alpha = 0.24f), RoundedCornerShape(18.dp))
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(15.dp),
            )
            Text(
                text = label,
                color = color,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp),
            )
        }
    }
}

@Composable
private fun EmptyFriendState(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFEDEFF3), RoundedCornerShape(16.dp))
            .padding(18.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = SplitlyTextStyles.caption,
        )
    }
}

private fun FriendProfile.matches(query: String): Boolean =
    name.lowercase().contains(query) || email.lowercase().contains(query)
