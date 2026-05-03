package com.example.split.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.split.data.DemoGroupRepository
import com.example.split.model.GroupExpense
import com.example.split.model.GroupMember
import com.example.split.model.GroupStatus
import com.example.split.model.SplitGroup
import com.example.split.navigation.BottomNavDestination
import com.example.split.ui.components.SplitlyBottomBar
import com.example.split.ui.components.SplitlyTextStyles
import com.example.split.ui.theme.SplitlyColors
import java.text.NumberFormat
import java.util.Locale

private const val CurrentUserId = "member_dung"

@Composable
fun StatsScreen(
    groups: List<SplitGroup>,
    groupMembersById: Map<String, List<GroupMember>>,
    groupExpensesById: Map<String, List<GroupExpense>>,
    onHomeSelected: () -> Unit,
    onFriendsSelected: () -> Unit,
    onProfileSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val summaries = groups.map { group ->
        val members = groupMembersById[group.id] ?: DemoGroupRepository.members(group.id)
        val expenses = groupExpensesById[group.id] ?: DemoGroupRepository.expenses(group.id)
        val balances = DemoGroupRepository.balances(
            members = members,
            expenses = expenses,
            settlements = DemoGroupRepository.settlements(group.id),
        )
        GroupStatsSummary(
            group = group,
            memberCount = members.size,
            expenseCount = expenses.size,
            totalSpent = expenses.sumOf { it.amount },
            payable = balances.filter { it.fromMemberId == CurrentUserId }.sumOf { it.amount },
            receivable = balances.filter { it.toMemberId == CurrentUserId }.sumOf { it.amount },
        )
    }
    val totalSpent = summaries.sumOf { it.totalSpent }
    val totalExpenses = summaries.sumOf { it.expenseCount }
    val totalPayable = summaries.sumOf { it.payable }
    val totalReceivable = summaries.sumOf { it.receivable }
    val recentExpenses = summaries.flatMap { summary ->
        val expenses = groupExpensesById[summary.group.id] ?: DemoGroupRepository.expenses(summary.group.id)
        expenses.map { expense ->
            RecentExpense(
                groupName = summary.group.name,
                title = expense.title,
                amount = expense.amount,
                dateLabel = expense.dateLabel,
            )
        }
    }.take(6)

    Scaffold(
        modifier = modifier,
        containerColor = SplitlyColors.PageBackground,
        bottomBar = {
            SplitlyBottomBar(
                selectedDestination = BottomNavDestination.Stats,
                onHomeClick = onHomeSelected,
                onFriendsClick = onFriendsSelected,
                onAddClick = {},
                onStatsClick = {},
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
                StatsHeader(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
                )
            }

            item {
                MetricsGrid(
                    totalSpent = totalSpent,
                    totalExpenses = totalExpenses,
                    activeGroups = groups.count { it.status == GroupStatus.Active },
                    sharingGroups = groups.count { it.status == GroupStatus.PaymentProcess },
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }

            item {
                BalanceOverviewCard(
                    payable = totalPayable,
                    receivable = totalReceivable,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                )
            }

            item {
                SectionHeader(
                    title = "Group spending",
                    subtitle = "${summaries.size} groups",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                )
            }
            items(
                items = summaries,
                key = { it.group.id },
            ) { summary ->
                GroupSpendRow(
                    summary = summary,
                    maxSpent = summaries.maxOfOrNull { it.totalSpent } ?: 0L,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }

            item {
                SectionHeader(
                    title = "Recent expenses",
                    subtitle = "$totalExpenses records",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                )
            }
            if (recentExpenses.isEmpty()) {
                item {
                    EmptyStatsCard(
                        text = "No expenses yet.",
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }
            } else {
                items(
                    items = recentExpenses,
                    key = { "${it.groupName}_${it.title}_${it.amount}_${it.dateLabel}" },
                ) { expense ->
                    RecentExpenseRow(
                        expense = expense,
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
private fun StatsHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Stats",
                color = SplitlyColors.TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Track spending, balances, and group activity.",
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
                imageVector = Icons.Filled.Analytics,
                contentDescription = null,
                tint = SplitlyColors.Primary,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Composable
private fun MetricsGrid(
    totalSpent: Long,
    totalExpenses: Int,
    activeGroups: Int,
    sharingGroups: Int,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricCard(
                title = "Total spent",
                value = formatMoney(totalSpent),
                icon = Icons.AutoMirrored.Filled.ReceiptLong,
                color = SplitlyColors.Primary,
                modifier = Modifier.weight(1f),
            )
            MetricCard(
                title = "Expenses",
                value = totalExpenses.toString(),
                icon = Icons.Filled.Payments,
                color = SplitlyColors.Done,
                modifier = Modifier.weight(1f),
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricCard(
                title = "Active",
                value = activeGroups.toString(),
                icon = Icons.Filled.Analytics,
                color = SplitlyColors.Active,
                modifier = Modifier.weight(1f),
            )
            MetricCard(
                title = "Bill sharing",
                value = sharingGroups.toString(),
                icon = Icons.Filled.AccountBalanceWallet,
                color = SplitlyColors.OnProcess,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun MetricCard(
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
private fun BalanceOverviewCard(
    payable: Long,
    receivable: Long,
    modifier: Modifier = Modifier,
) {
    val net = receivable - payable
    val netText = when {
        net > 0L -> "You should receive ${formatMoney(net)}"
        net < 0L -> "You owe ${formatMoney(-net)}"
        else -> "You are settled"
    }
    val netColor = when {
        net > 0L -> SplitlyColors.Done
        net < 0L -> SplitlyColors.OnProcess
        else -> SplitlyColors.TextSecondary
    }

    SectionCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Your balance",
                    color = SplitlyColors.TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = netText,
                    color = netColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            Icon(
                imageVector = Icons.Filled.AccountBalanceWallet,
                contentDescription = null,
                tint = netColor,
                modifier = Modifier.size(26.dp),
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            BalanceMiniCard(
                title = "Need to pay",
                amount = payable,
                color = SplitlyColors.OnProcess,
                modifier = Modifier.weight(1f),
            )
            BalanceMiniCard(
                title = "Need to receive",
                amount = receivable,
                color = SplitlyColors.Done,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun BalanceMiniCard(
    title: String,
    amount: Long,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(color.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
            .border(1.dp, color.copy(alpha = 0.18f), RoundedCornerShape(12.dp))
            .padding(12.dp),
    ) {
        Text(text = title, style = SplitlyTextStyles.caption)
        Text(
            text = formatMoney(amount),
            color = color,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Composable
private fun GroupSpendRow(
    summary: GroupStatsSummary,
    maxSpent: Long,
    modifier: Modifier = Modifier,
) {
    val progress = if (maxSpent > 0L) {
        summary.totalSpent.toFloat() / maxSpent.toFloat()
    } else {
        0f
    }

    SectionCard(
        modifier = modifier.padding(bottom = 10.dp),
        contentPadding = 14,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = summary.group.name,
                    color = SplitlyColors.TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "${summary.expenseCount} expenses - ${summary.memberCount} members",
                    style = SplitlyTextStyles.caption,
                    modifier = Modifier.padding(top = 3.dp),
                )
            }
            StatusPill(status = summary.group.status)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(SplitlyColors.FieldBorder, RoundedCornerShape(10.dp)),
        ) {
            if (progress > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress.coerceIn(0.06f, 1f))
                        .height(8.dp)
                        .background(SplitlyColors.Primary, RoundedCornerShape(10.dp)),
                )
            }
        }
        Text(
            text = formatMoney(summary.totalSpent),
            color = SplitlyColors.Primary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 10.dp),
        )
    }
}

@Composable
private fun RecentExpenseRow(
    expense: RecentExpense,
    modifier: Modifier = Modifier,
) {
    SectionCard(
        modifier = modifier.padding(bottom = 10.dp),
        contentPadding = 14,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = expense.title,
                    color = SplitlyColors.TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "${expense.groupName} - ${expense.dateLabel}",
                    style = SplitlyTextStyles.caption,
                    modifier = Modifier.padding(top = 3.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                text = formatMoney(expense.amount),
                color = SplitlyColors.TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun SectionHeader(
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
        Text(text = subtitle, style = SplitlyTextStyles.caption)
    }
}

@Composable
private fun EmptyStatsCard(
    text: String,
    modifier: Modifier = Modifier,
) {
    SectionCard(modifier = modifier) {
        Text(text = text, style = SplitlyTextStyles.caption)
    }
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

@Composable
private fun StatusPill(status: GroupStatus) {
    val color = when (status) {
        GroupStatus.Active -> SplitlyColors.Active
        GroupStatus.PaymentProcess -> SplitlyColors.OnProcess
        GroupStatus.Done -> SplitlyColors.Done
    }
    Text(
        text = status.label,
        color = color,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
            .border(1.dp, color.copy(alpha = 0.24f), RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
    )
}

private data class GroupStatsSummary(
    val group: SplitGroup,
    val memberCount: Int,
    val expenseCount: Int,
    val totalSpent: Long,
    val payable: Long,
    val receivable: Long,
)

private data class RecentExpense(
    val groupName: String,
    val title: String,
    val amount: Long,
    val dateLabel: String,
)

private val GroupStatus.label: String
    get() = when (this) {
        GroupStatus.Active -> "Active"
        GroupStatus.PaymentProcess -> "Bill Sharing"
        GroupStatus.Done -> "Done"
    }

private fun formatMoney(amount: Long): String =
    "${NumberFormat.getNumberInstance(Locale.US).format(amount)} VND"
