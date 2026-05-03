package com.example.split.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.split.R
import com.example.split.data.DemoGroupRepository
import com.example.split.model.FriendConnectionStatus
import com.example.split.model.GroupExpense
import com.example.split.model.MemberPaymentProfile
import com.example.split.model.SplitGroup
import com.example.split.navigation.BottomNavDestination
import com.example.split.ui.components.SplitlyBottomBar
import com.example.split.ui.components.SplitlyTextStyles
import com.example.split.ui.theme.SplitlyColors
import java.text.NumberFormat
import java.util.Locale

private const val CurrentUserId = "member_dung"

@Composable
fun ProfileScreen(
    groups: List<SplitGroup>,
    groupExpensesById: Map<String, List<GroupExpense>>,
    friendStatuses: Map<String, FriendConnectionStatus>,
    onHomeSelected: () -> Unit,
    onFriendsSelected: () -> Unit,
    onStatsSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val paymentProfile = DemoGroupRepository.paymentProfiles()[CurrentUserId]
    val friendCount = friendStatuses.values.count { it == FriendConnectionStatus.Friend }
    val expenseCount = groups.sumOf { group ->
        (groupExpensesById[group.id] ?: DemoGroupRepository.expenses(group.id)).size
    }
    val totalSpent = groups.sumOf { group ->
        (groupExpensesById[group.id] ?: DemoGroupRepository.expenses(group.id)).sumOf { it.amount }
    }

    Scaffold(
        modifier = modifier,
        containerColor = SplitlyColors.PageBackground,
        bottomBar = {
            SplitlyBottomBar(
                selectedDestination = BottomNavDestination.Profile,
                onHomeClick = onHomeSelected,
                onFriendsClick = onFriendsSelected,
                onAddClick = {},
                onStatsClick = onStatsSelected,
                onProfileClick = {},
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            item {
                ProfileHeader(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
                )
            }

            item {
                ProfileStatsGrid(
                    groupCount = groups.size,
                    friendCount = friendCount,
                    expenseCount = expenseCount,
                    totalSpent = totalSpent,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }

            item {
                SectionHeader(
                    title = "Payment",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                )
                PaymentProfileCard(
                    profile = paymentProfile,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }

            item {
                SectionHeader(
                    title = "Account",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                )
                AccountCard(
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }

            item {
                SectionHeader(
                    title = "Settings",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                )
                SettingsCard(
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
private fun ProfileHeader(modifier: Modifier = Modifier) {
    SectionCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = R.drawable.user_avatar),
                contentDescription = null,
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .border(3.dp, Color.White, CircleShape),
                contentScale = ContentScale.Crop,
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
            ) {
                Text(
                    text = "Dung Nguyen",
                    color = SplitlyColors.TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "dung.nguyen@example.com",
                    style = SplitlyTextStyles.caption,
                    modifier = Modifier.padding(top = 4.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "Owner account",
                    color = SplitlyColors.Primary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .background(SplitlyColors.Primary.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                )
            }
        }
    }
}

@Composable
private fun ProfileStatsGrid(
    groupCount: Int,
    friendCount: Int,
    expenseCount: Int,
    totalSpent: Long,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ProfileMetricCard(
                title = "Groups",
                value = groupCount.toString(),
                icon = Icons.Filled.Group,
                color = SplitlyColors.Primary,
                modifier = Modifier.weight(1f),
            )
            ProfileMetricCard(
                title = "Friends",
                value = friendCount.toString(),
                icon = Icons.Filled.People,
                color = SplitlyColors.Done,
                modifier = Modifier.weight(1f),
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ProfileMetricCard(
                title = "Expenses",
                value = expenseCount.toString(),
                icon = Icons.Filled.Payments,
                color = SplitlyColors.Active,
                modifier = Modifier.weight(1f),
            )
            ProfileMetricCard(
                title = "Spent",
                value = formatMoney(totalSpent),
                icon = Icons.Filled.Analytics,
                color = SplitlyColors.OnProcess,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun ProfileMetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
) {
    SectionCard(modifier = modifier, contentPadding = 14) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(color.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(19.dp),
                )
            }
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Text(
                    text = title,
                    style = SplitlyTextStyles.caption,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = value,
                    color = SplitlyColors.TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }
    }
}

@Composable
private fun PaymentProfileCard(
    profile: MemberPaymentProfile?,
    modifier: Modifier = Modifier,
) {
    SectionCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = profile?.accountName ?: "Dung Nguyen",
                    color = SplitlyColors.TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = profile?.bankName ?: "No bank connected",
                    style = SplitlyTextStyles.caption,
                    modifier = Modifier.padding(top = 4.dp),
                )
                Text(
                    text = profile?.accountNumber ?: "Account number not set",
                    color = SplitlyColors.TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            QrStatusPill(hasQr = profile?.hasQr == true)
        }
        Spacer(modifier = Modifier.height(14.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MiniQrPlaceholder()
            Text(
                text = if (profile?.hasQr == true) "Receiving QR ready" else "Receiving QR missing",
                color = if (profile?.hasQr == true) SplitlyColors.Done else SplitlyColors.OnProcess,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 12.dp),
            )
        }
    }
}

@Composable
private fun QrStatusPill(hasQr: Boolean) {
    val color = if (hasQr) SplitlyColors.Done else SplitlyColors.OnProcess
    Row(
        modifier = Modifier
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
            .border(1.dp, color.copy(alpha = 0.24f), RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(15.dp),
        )
        Text(
            text = if (hasQr) "QR ready" else "No QR",
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp),
        )
    }
}

@Composable
private fun MiniQrPlaceholder() {
    Column(
        modifier = Modifier
            .size(74.dp)
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, SplitlyColors.FieldBorder, RoundedCornerShape(12.dp))
            .padding(8.dp),
    ) {
        repeat(6) { row ->
            Row(modifier = Modifier.weight(1f)) {
                repeat(6) { column ->
                    val filled = row < 2 && column < 2 ||
                        row > 3 && column < 2 ||
                        row < 2 && column > 3 ||
                        (row + column) % 3 == 0
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .padding(1.5.dp)
                            .background(
                                if (filled) SplitlyColors.TextPrimary else SplitlyColors.FieldBackground,
                                RoundedCornerShape(2.dp),
                            ),
                    )
                }
            }
        }
    }
}

@Composable
private fun AccountCard(modifier: Modifier = Modifier) {
    SectionCard(modifier = modifier, contentPadding = 14) {
        AccountRow(
            icon = Icons.Filled.AccountCircle,
            title = "Display name",
            value = "Dung Nguyen",
        )
        AccountDivider()
        AccountRow(
            icon = Icons.Filled.CheckCircle,
            title = "Email",
            value = "dung.nguyen@example.com",
        )
        AccountDivider()
        AccountRow(
            icon = Icons.Filled.AccountBalanceWallet,
            title = "Default currency",
            value = "VND",
        )
    }
}

@Composable
private fun SettingsCard(modifier: Modifier = Modifier) {
    SectionCard(modifier = modifier, contentPadding = 14) {
        AccountRow(
            icon = Icons.Filled.Payments,
            title = "Payment methods",
            value = "1 connected",
        )
        AccountDivider()
        AccountRow(
            icon = Icons.Filled.People,
            title = "Friend requests",
            value = "Enabled",
        )
        AccountDivider()
        AccountRow(
            icon = Icons.Filled.Analytics,
            title = "Monthly summary",
            value = "Enabled",
        )
    }
}

@Composable
private fun AccountRow(
    icon: ImageVector,
    title: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(SplitlyColors.Primary.copy(alpha = 0.08f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SplitlyColors.Primary,
                modifier = Modifier.size(20.dp),
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
        ) {
            Text(
                text = title,
                color = SplitlyColors.TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = value,
                style = SplitlyTextStyles.caption,
                modifier = Modifier.padding(top = 2.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun AccountDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .height(1.dp)
            .background(SplitlyColors.FieldBorder),
    )
}

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        color = SplitlyColors.TextPrimary,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
private fun SectionCard(
    modifier: Modifier = Modifier,
    contentPadding: Int = 18,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(18.dp))
            .border(1.dp, Color(0xFFEDEFF3), RoundedCornerShape(18.dp))
            .padding(contentPadding.dp),
        content = content,
    )
}

private fun formatMoney(amount: Long): String =
    "${NumberFormat.getNumberInstance(Locale.US).format(amount)} VND"
