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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.split.data.DemoGroupRepository
import com.example.split.model.GroupBalance
import com.example.split.model.GroupExpense
import com.example.split.model.GroupMember
import com.example.split.model.GroupMemberRole
import com.example.split.model.GroupSettlement
import com.example.split.model.MemberPaymentProfile
import com.example.split.model.SettlementStatus
import com.example.split.model.GroupStatus
import com.example.split.model.SplitGroup
import com.example.split.ui.components.BouncyButton
import com.example.split.ui.components.SplitlyBottomBar
import com.example.split.ui.components.SplitlyFilledButton
import com.example.split.ui.components.SplitlyGradientTopBar
import com.example.split.ui.components.SplitlyTextStyles
import com.example.split.ui.theme.SplitlyColors
import java.text.NumberFormat
import java.util.Locale

private enum class GroupSheet {
    None,
    Actions,
    AddFriend,
    AddExpense,
    ExpenseDetail,
    EditExpense,
    BalanceDetail,
}

private enum class SplitSelectionMode {
    Everyone,
    EveryoneExcept,
    Custom,
}

private const val CurrentUserId = "member_dung"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupScreen(
    group: SplitGroup,
    initialMembers: List<GroupMember> = DemoGroupRepository.members(group.id),
    initialExpenses: List<GroupExpense> = DemoGroupRepository.expenses(group.id),
    onMembersChanged: (List<GroupMember>) -> Unit = {},
    onExpensesChanged: (List<GroupExpense>) -> Unit = {},
    onBack: () -> Unit,
    onHomeSelected: () -> Unit,
    onFriendsSelected: () -> Unit,
    onStatsSelected: () -> Unit,
    onProfileSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val members = remember(group.id) {
        mutableStateListOf<GroupMember>().apply {
            addAll(initialMembers)
        }
    }
    val expenses = remember(group.id) {
        mutableStateListOf<GroupExpense>().apply {
            addAll(initialExpenses)
        }
    }
    val settlements = remember(group.id) {
        mutableStateListOf<GroupSettlement>().apply {
            addAll(DemoGroupRepository.settlements(group.id))
        }
    }
    val paymentProfiles = remember {
        mutableStateMapOf<String, MemberPaymentProfile>().apply {
            putAll(DemoGroupRepository.paymentProfiles())
        }
    }
    var activeSheet by rememberSaveable { mutableStateOf(GroupSheet.None) }
    var selectedExpenseId by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedBalanceFromId by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedBalanceToId by rememberSaveable { mutableStateOf<String?>(null) }
    val balances = DemoGroupRepository.balances(members, expenses, settlements)
    val totalSpent = expenses.sumOf { it.amount }

    Scaffold(
        modifier = modifier,
        containerColor = SplitlyColors.PageBackground,
        topBar = {
            SplitlyGradientTopBar(title = group.name, onBack = onBack)
        },
        bottomBar = {
            SplitlyBottomBar(
                selectedDestination = null,
                onHomeClick = onHomeSelected,
                onFriendsClick = onFriendsSelected,
                onAddClick = { activeSheet = GroupSheet.Actions },
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
                GroupSummaryCard(
                    group = group,
                    memberCount = members.size,
                    totalSpent = totalSpent,
                    balances = balances,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                )
            }

            item {
                SectionHeader(
                    title = "Members",
                    actionText = "Add friend",
                    onAction = { activeSheet = GroupSheet.AddFriend },
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }

            items(
                items = members,
                key = { it.id },
            ) { member ->
                MemberRow(
                    member = member,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }

            item {
                SectionHeader(
                    title = "Payment QR",
                    actionText = null,
                    onAction = {},
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                )
                CurrentPaymentProfileCard(
                    profile = paymentProfiles[CurrentUserId],
                    currentUserName = members.nameFor(CurrentUserId),
                    onUploadQr = {
                        val currentProfile = paymentProfiles[CurrentUserId] ?: MemberPaymentProfile(
                            memberId = CurrentUserId,
                            bankName = "Demo Bank",
                            accountName = members.nameFor(CurrentUserId),
                            accountNumber = "Not set",
                            hasQr = false,
                        )
                        paymentProfiles[CurrentUserId] = currentProfile.copy(hasQr = true)
                    },
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }

            item {
                SectionHeader(
                    title = "Your balance",
                    actionText = null,
                    onAction = {},
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                )
                YourBalanceList(
                    balances = balances,
                    settlements = settlements,
                    members = members,
                    onBalanceClick = { balance ->
                        selectedBalanceFromId = balance.fromMemberId
                        selectedBalanceToId = balance.toMemberId
                        activeSheet = GroupSheet.BalanceDetail
                    },
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }

            item {
                SectionHeader(
                    title = "Expenses",
                    actionText = "Add expense",
                    onAction = { activeSheet = GroupSheet.AddExpense },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                )
            }

            items(
                items = expenses,
                key = { it.id },
            ) { expense ->
                ExpenseRow(
                    expense = expense,
                    paidByName = members.nameFor(expense.paidByMemberId),
                    onClick = {
                        selectedExpenseId = expense.id
                        activeSheet = GroupSheet.ExpenseDetail
                    },
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }

            item {
                SectionHeader(
                    title = "Balances",
                    actionText = null,
                    onAction = {},
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                )
                BalanceList(
                    balances = balances,
                    members = members,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }

    when (activeSheet) {
        GroupSheet.Actions -> {
            ModalBottomSheet(
                onDismissRequest = { activeSheet = GroupSheet.None },
                containerColor = Color.White,
            ) {
                AddActionSheet(
                    onAddFriend = { activeSheet = GroupSheet.AddFriend },
                    onAddExpense = { activeSheet = GroupSheet.AddExpense },
                )
            }
        }

        GroupSheet.AddFriend -> {
            ModalBottomSheet(
                onDismissRequest = { activeSheet = GroupSheet.None },
                containerColor = Color.White,
            ) {
                AddFriendSheet(
                    candidates = DemoGroupRepository.availableFriends(group.id)
                        .filterNot { candidate -> members.any { it.id == candidate.id } },
                    onAddFriend = { member ->
                        members.add(member.copy(role = GroupMemberRole.Member))
                        onMembersChanged(members.toList())
                    },
                    onDone = { activeSheet = GroupSheet.None },
                )
            }
        }

        GroupSheet.AddExpense -> {
            ModalBottomSheet(
                onDismissRequest = { activeSheet = GroupSheet.None },
                containerColor = Color.White,
            ) {
                ExpenseFormSheet(
                    title = "Add expense",
                    buttonText = "Save expense",
                    members = members,
                    initialExpense = null,
                    onSaveExpense = { title, amount, paidById, participantIds ->
                        val newExpense = GroupExpense(
                            id = "demo_expense_${System.currentTimeMillis()}",
                            title = title,
                            amount = amount,
                            paidByMemberId = paidById,
                            participantMemberIds = participantIds,
                            dateLabel = "Just now",
                        )
                        expenses.add(0, newExpense)
                        onExpensesChanged(expenses.toList())
                        activeSheet = GroupSheet.None
                    },
                )
            }
        }

        GroupSheet.ExpenseDetail -> {
            val selectedExpense = expenses.firstOrNull { it.id == selectedExpenseId }
            if (selectedExpense == null) {
                activeSheet = GroupSheet.None
            } else {
                ModalBottomSheet(
                    onDismissRequest = { activeSheet = GroupSheet.None },
                    containerColor = Color.White,
                ) {
                    ExpenseDetailSheet(
                        expense = selectedExpense,
                        members = members,
                        onEdit = { activeSheet = GroupSheet.EditExpense },
                        onDelete = {
                            expenses.remove(selectedExpense)
                            onExpensesChanged(expenses.toList())
                            selectedExpenseId = null
                            activeSheet = GroupSheet.None
                        },
                    )
                }
            }
        }

        GroupSheet.EditExpense -> {
            val selectedExpense = expenses.firstOrNull { it.id == selectedExpenseId }
            if (selectedExpense == null) {
                activeSheet = GroupSheet.None
            } else {
                ModalBottomSheet(
                    onDismissRequest = { activeSheet = GroupSheet.None },
                    containerColor = Color.White,
                ) {
                    ExpenseFormSheet(
                        title = "Edit expense",
                        buttonText = "Save changes",
                        members = members,
                        initialExpense = selectedExpense,
                        onSaveExpense = { title, amount, paidById, participantIds ->
                            val expenseIndex = expenses.indexOfFirst { it.id == selectedExpense.id }
                            if (expenseIndex >= 0) {
                                expenses[expenseIndex] = selectedExpense.copy(
                                    title = title,
                                    amount = amount,
                                    paidByMemberId = paidById,
                                    participantMemberIds = participantIds,
                                )
                                onExpensesChanged(expenses.toList())
                            }
                            activeSheet = GroupSheet.ExpenseDetail
                        },
                    )
                }
            }
        }

        GroupSheet.BalanceDetail -> {
            val selectedBalance = balances.firstOrNull {
                it.fromMemberId == selectedBalanceFromId && it.toMemberId == selectedBalanceToId
            }
            if (selectedBalance == null) {
                activeSheet = GroupSheet.None
            } else {
                ModalBottomSheet(
                    onDismissRequest = { activeSheet = GroupSheet.None },
                    containerColor = Color.White,
                ) {
                    BalanceDetailSheet(
                        balance = selectedBalance,
                        members = members,
                        paymentProfiles = paymentProfiles,
                        settlement = settlements.matching(selectedBalance),
                        onUploadReceipt = { settlement ->
                            settlements.upsertSettlement(settlement.copy(receiptLabel = "receipt_demo.jpg"))
                        },
                        onTransferred = { settlement ->
                            settlements.upsertSettlement(
                                settlement.copy(
                                    status = SettlementStatus.WaitingForReceiverConfirmation,
                                ),
                            )
                        },
                        onConfirmReceived = { settlement ->
                            settlements.upsertSettlement(settlement.copy(status = SettlementStatus.Confirmed))
                            activeSheet = GroupSheet.None
                        },
                        onReject = { settlement ->
                            settlements.upsertSettlement(settlement.copy(status = SettlementStatus.Rejected))
                        },
                    )
                }
            }
        }

        GroupSheet.None -> Unit
    }
}

@Composable
private fun GroupSummaryCard(
    group: SplitGroup,
    memberCount: Int,
    totalSpent: Long,
    balances: List<GroupBalance>,
    modifier: Modifier = Modifier,
) {
    val currentUserBalance = balances.currentUserBalance(currentUserId = CurrentUserId)
    val balanceText = when {
        currentUserBalance < 0L -> "You owe ${formatMoney(-currentUserBalance)}"
        currentUserBalance > 0L -> "You should receive ${formatMoney(currentUserBalance)}"
        else -> "You are settled"
    }
    val balanceColor = when {
        currentUserBalance < 0L -> SplitlyColors.OnProcess
        currentUserBalance > 0L -> SplitlyColors.Done
        else -> SplitlyColors.TextSecondary
    }

    SectionCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StatusPill(status = group.status)
            Text(
                text = "$memberCount members",
                style = SplitlyTextStyles.caption,
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "Total spent",
            style = SplitlyTextStyles.captionTitle,
        )
        Text(
            text = formatMoney(totalSpent),
            color = SplitlyColors.TextPrimary,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(balanceColor.copy(alpha = 0.1f), RoundedCornerShape(14.dp))
                .border(1.dp, balanceColor.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.AccountBalanceWallet,
                    contentDescription = null,
                    tint = balanceColor,
                    modifier = Modifier.size(22.dp),
                )
                Text(
                    text = balanceText,
                    color = balanceColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 10.dp),
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    actionText: String?,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 14.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            color = SplitlyColors.TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        if (actionText != null) {
            BouncyButton(onClick = onAction, pressedScale = 0.9f) {
                Row(
                    modifier = Modifier
                        .background(SplitlyColors.Primary.copy(alpha = 0.1f), RoundedCornerShape(18.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        tint = SplitlyColors.Primary,
                        modifier = Modifier.size(16.dp),
                    )
                    Text(
                        text = actionText,
                        color = SplitlyColors.Primary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 4.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun MemberRow(
    member: GroupMember,
    modifier: Modifier = Modifier,
) {
    SectionCard(
        modifier = modifier.padding(bottom = 10.dp),
        contentPadding = 14,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Avatar(member = member)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
            ) {
                Text(
                    text = member.name,
                    color = SplitlyColors.TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = member.role.label,
                    style = SplitlyTextStyles.caption,
                )
            }
        }
    }
}

@Composable
private fun CurrentPaymentProfileCard(
    profile: MemberPaymentProfile?,
    currentUserName: String,
    onUploadQr: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hasQr = profile?.hasQr == true
    val title = if (hasQr) "Your receiving QR is ready" else "Add your receiving QR"
    val subtitle = if (hasQr) {
        "${profile?.bankName.orEmpty()} - ${profile?.accountNumber.orEmpty()}"
    } else {
        "People who owe you will see it when they settle their balance."
    }

    SectionCard(
        modifier = modifier,
        contentPadding = 16,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(SplitlyColors.Primary.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountBalanceWallet,
                    contentDescription = null,
                    tint = SplitlyColors.Primary,
                    modifier = Modifier.size(24.dp),
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
                    text = subtitle.ifBlank { currentUserName },
                    style = SplitlyTextStyles.caption,
                    modifier = Modifier.padding(top = 3.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            BouncyButton(onClick = onUploadQr, pressedScale = 0.9f) {
                Row(
                    modifier = Modifier
                        .background(SplitlyColors.Primary.copy(alpha = 0.1f), RoundedCornerShape(18.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Filled.UploadFile,
                        contentDescription = null,
                        tint = SplitlyColors.Primary,
                        modifier = Modifier.size(16.dp),
                    )
                    Text(
                        text = if (hasQr) "Replace" else "Upload",
                        color = SplitlyColors.Primary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 4.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpenseRow(
    expense: GroupExpense,
    paidByName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BouncyButton(onClick = onClick, modifier = modifier.padding(bottom = 10.dp)) {
        SectionCard(contentPadding = 16) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = expense.title,
                        color = SplitlyColors.TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Paid by $paidByName - Split with ${expense.participantMemberIds.size} people",
                        style = SplitlyTextStyles.caption,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = formatMoney(expense.amount),
                        color = SplitlyColors.Primary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = expense.dateLabel,
                        style = SplitlyTextStyles.caption,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun BalanceList(
    balances: List<GroupBalance>,
    members: List<GroupMember>,
    modifier: Modifier = Modifier,
) {
    SectionCard(modifier = modifier) {
        if (balances.isEmpty()) {
            Text(
                text = "Everyone is settled.",
                style = SplitlyTextStyles.caption,
                modifier = Modifier.fillMaxWidth(),
            )
        } else {
            balances.forEachIndexed { index, balance ->
                BalanceRow(
                    fromName = members.nameFor(balance.fromMemberId),
                    toName = members.nameFor(balance.toMemberId),
                    amount = balance.amount,
                )
                if (index < balances.lastIndex) {
                    HorizontalDivider(
                        color = SplitlyColors.FieldBorder,
                        modifier = Modifier.padding(vertical = 12.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun BalanceRow(
    fromName: String,
    toName: String,
    amount: Long,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.Payments,
            contentDescription = null,
            tint = SplitlyColors.Done,
            modifier = Modifier.size(22.dp),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
        ) {
            Text(
                text = "$fromName owes $toName",
                color = SplitlyColors.TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = formatMoney(amount),
                style = SplitlyTextStyles.caption,
            )
        }
    }
}

@Composable
private fun YourBalanceList(
    balances: List<GroupBalance>,
    settlements: List<GroupSettlement>,
    members: List<GroupMember>,
    onBalanceClick: (GroupBalance) -> Unit,
    modifier: Modifier = Modifier,
) {
    val personalBalances = balances.filter { balance ->
        balance.fromMemberId == CurrentUserId || balance.toMemberId == CurrentUserId
    }

    if (personalBalances.isEmpty()) {
        SectionCard(modifier = modifier) {
            Text(
                text = "You are settled.",
                style = SplitlyTextStyles.caption,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    } else {
        Column(modifier = modifier) {
            personalBalances.forEach { balance ->
                YourBalanceRow(
                    balance = balance,
                    settlement = settlements.matching(balance),
                    members = members,
                    onClick = { onBalanceClick(balance) },
                )
            }
        }
    }
}

@Composable
private fun YourBalanceRow(
    balance: GroupBalance,
    settlement: GroupSettlement?,
    members: List<GroupMember>,
    onClick: () -> Unit,
) {
    val isPayer = balance.fromMemberId == CurrentUserId
    val otherName = members.nameFor(if (isPayer) balance.toMemberId else balance.fromMemberId)
    val statusText = settlement?.status?.label ?: if (isPayer) "Pay" else "Review"
    val statusColor = settlement?.status?.color ?: if (isPayer) SplitlyColors.OnProcess else SplitlyColors.Done

    BouncyButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
    ) {
        SectionCard(contentPadding = 16) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.Payments,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(22.dp),
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp),
                ) {
                    Text(
                        text = if (isPayer) "You owe $otherName" else "$otherName owes you",
                        color = SplitlyColors.TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = if (isPayer) {
                            "View recipient QR and confirm transfer"
                        } else {
                            "Review receipt and confirm payment"
                        },
                        style = SplitlyTextStyles.caption,
                        modifier = Modifier.padding(top = 3.dp),
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = formatMoney(balance.amount),
                        color = SplitlyColors.TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = statusText,
                        color = statusColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .background(statusColor.copy(alpha = 0.1f), RoundedCornerShape(14.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun BalanceDetailSheet(
    balance: GroupBalance,
    members: List<GroupMember>,
    paymentProfiles: Map<String, MemberPaymentProfile>,
    settlement: GroupSettlement?,
    onUploadReceipt: (GroupSettlement) -> Unit,
    onTransferred: (GroupSettlement) -> Unit,
    onConfirmReceived: (GroupSettlement) -> Unit,
    onReject: (GroupSettlement) -> Unit,
) {
    val isPayer = balance.fromMemberId == CurrentUserId
    val payerName = members.nameFor(balance.fromMemberId)
    val receiverName = members.nameFor(balance.toMemberId)
    val newSettlement = remember(balance.fromMemberId, balance.toMemberId, balance.amount) {
        balance.toNewSettlement()
    }
    val activeSettlement = settlement ?: newSettlement
    val paymentAmount = if (settlement == null || activeSettlement.status == SettlementStatus.Unpaid) {
        balance.amount
    } else {
        activeSettlement.amount
    }
    val actionableSettlement = activeSettlement.copy(amount = paymentAmount)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 28.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isPayer) "Pay $receiverName" else "$payerName owes you",
                    color = SplitlyColors.TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "Open balance ${formatMoney(balance.amount)}",
                    style = SplitlyTextStyles.caption,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            SettlementStatusPill(status = activeSettlement.status)
        }

        Spacer(modifier = Modifier.height(18.dp))
        DetailInfoRow(
            title = "Settlement amount",
            value = formatMoney(paymentAmount),
        )

        if (isPayer) {
            Spacer(modifier = Modifier.height(18.dp))
            Text(text = "Recipient QR", style = SplitlyTextStyles.captionTitle)
            Spacer(modifier = Modifier.height(10.dp))
            PaymentQrCard(
                profile = paymentProfiles[balance.toMemberId],
                memberName = receiverName,
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(text = "Receipt", style = SplitlyTextStyles.captionTitle)
            Spacer(modifier = Modifier.height(10.dp))
            ReceiptPreviewCard(receiptLabel = activeSettlement.receiptLabel)
            Spacer(modifier = Modifier.height(10.dp))
            ActionRow(
                icon = Icons.Filled.UploadFile,
                title = if (activeSettlement.receiptLabel == null) "Upload receipt" else "Replace receipt",
                subtitle = "Optional demo attachment before you confirm transfer",
                onClick = { onUploadReceipt(actionableSettlement) },
            )
            Spacer(modifier = Modifier.height(10.dp))
            SplitlyFilledButton(
                text = "I have transferred",
                onClick = { onTransferred(actionableSettlement) },
            )
            if (activeSettlement.status == SettlementStatus.WaitingForReceiverConfirmation) {
                Text(
                    text = "Waiting for $receiverName to confirm this payment.",
                    style = SplitlyTextStyles.caption,
                    modifier = Modifier.padding(top = 10.dp),
                )
            }
        } else {
            Spacer(modifier = Modifier.height(18.dp))
            Text(text = "Receipt from $payerName", style = SplitlyTextStyles.captionTitle)
            Spacer(modifier = Modifier.height(10.dp))
            ReceiptPreviewCard(receiptLabel = activeSettlement.receiptLabel)
            Spacer(modifier = Modifier.height(14.dp))
            when (activeSettlement.status) {
                SettlementStatus.WaitingForReceiverConfirmation -> {
                    SplitlyFilledButton(
                        text = "Confirm received",
                        onClick = { onConfirmReceived(actionableSettlement) },
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    SheetOutlineButton(
                        text = "Reject payment",
                        color = SplitlyColors.OnProcess,
                        onClick = { onReject(actionableSettlement) },
                    )
                }

                SettlementStatus.Rejected -> {
                    Text(
                        text = "This payment was rejected. Wait for $payerName to submit a new transfer.",
                        color = SplitlyColors.OnProcess,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }

                SettlementStatus.Confirmed -> {
                    Text(
                        text = "Payment confirmed.",
                        color = SplitlyColors.Done,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }

                SettlementStatus.Unpaid -> {
                    Text(
                        text = "Waiting for $payerName to transfer money.",
                        style = SplitlyTextStyles.caption,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
    }
}

@Composable
private fun PaymentQrCard(
    profile: MemberPaymentProfile?,
    memberName: String,
) {
    SectionCard(contentPadding = 14) {
        if (profile?.hasQr == true) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                QrPlaceholder()
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 14.dp),
                ) {
                    Text(
                        text = memberName,
                        color = SplitlyColors.TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = profile.bankName,
                        style = SplitlyTextStyles.caption,
                        modifier = Modifier.padding(top = 5.dp),
                    )
                    Text(
                        text = profile.accountNumber,
                        color = SplitlyColors.TextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 3.dp),
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.UploadFile,
                    contentDescription = null,
                    tint = SplitlyColors.TextSecondary,
                    modifier = Modifier.size(24.dp),
                )
                Text(
                    text = "$memberName has not uploaded a receiving QR yet.",
                    style = SplitlyTextStyles.caption,
                    modifier = Modifier.padding(start = 12.dp),
                )
            }
        }
    }
}

@Composable
private fun QrPlaceholder() {
    Column(
        modifier = Modifier
            .size(116.dp)
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, SplitlyColors.FieldBorder, RoundedCornerShape(12.dp))
            .padding(10.dp),
    ) {
        repeat(7) { row ->
            Row(modifier = Modifier.weight(1f)) {
                repeat(7) { column ->
                    val filled = row < 2 && column < 2 ||
                        row < 2 && column > 4 ||
                        row > 4 && column < 2 ||
                        (row + column) % 3 == 0
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .padding(2.dp)
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
private fun ReceiptPreviewCard(receiptLabel: String?) {
    SectionCard(contentPadding = 14) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(SplitlyColors.SoftBlue, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.UploadFile,
                    contentDescription = null,
                    tint = SplitlyColors.Primary,
                    modifier = Modifier.size(22.dp),
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
            ) {
                Text(
                    text = receiptLabel ?: "No receipt uploaded",
                    color = SplitlyColors.TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = if (receiptLabel == null) "Receipt is optional." else "Demo receipt attached.",
                    style = SplitlyTextStyles.caption,
                    modifier = Modifier.padding(top = 3.dp),
                )
            }
        }
    }
}

@Composable
private fun SettlementStatusPill(status: SettlementStatus) {
    Text(
        text = status.label,
        color = status.color,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .background(status.color.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
            .border(1.dp, status.color.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 7.dp),
    )
}

@Composable
private fun SheetOutlineButton(
    text: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BouncyButton(onClick = onClick, pressedScale = 0.9f, modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                .background(color.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                .padding(vertical = 15.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                color = color,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun AddActionSheet(
    onAddFriend: () -> Unit,
    onAddExpense: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 28.dp),
    ) {
        Text(
            text = "New",
            color = SplitlyColors.TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(16.dp))
        ActionRow(
            icon = Icons.Filled.PersonAdd,
            title = "Add friend",
            subtitle = "Invite a friend into this group",
            onClick = onAddFriend,
        )
        Spacer(modifier = Modifier.height(10.dp))
        ActionRow(
            icon = Icons.AutoMirrored.Filled.ReceiptLong,
            title = "Add expense",
            subtitle = "Record a bill and split it",
            onClick = onAddExpense,
        )
        Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
    }
}

@Composable
private fun AddFriendSheet(
    candidates: List<GroupMember>,
    onAddFriend: (GroupMember) -> Unit,
    onDone: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 28.dp),
    ) {
        Text(
            text = "Add friends",
            color = SplitlyColors.TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "New friends are only included in future expenses unless you edit older bills.",
            style = SplitlyTextStyles.caption,
            modifier = Modifier.padding(top = 6.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (candidates.isEmpty()) {
            Text(
                text = "All demo friends are already in this group.",
                style = SplitlyTextStyles.caption,
                modifier = Modifier.padding(vertical = 20.dp),
            )
        } else {
            candidates.forEach { friend ->
                FriendCandidateRow(
                    friend = friend,
                    onAddFriend = onAddFriend,
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        SplitlyFilledButton(text = "Done", onClick = onDone)
        Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
    }
}

@Composable
private fun ExpenseDetailSheet(
    expense: GroupExpense,
    members: List<GroupMember>,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    val participantShare = if (expense.participantMemberIds.isNotEmpty()) {
        expense.amount / expense.participantMemberIds.size
    } else {
        0L
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 28.dp),
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
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = expense.dateLabel,
                    style = SplitlyTextStyles.caption,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            Text(
                text = formatMoney(expense.amount),
                color = SplitlyColors.Primary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        DetailInfoRow(
            title = "Paid by",
            value = members.nameFor(expense.paidByMemberId),
        )
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "Shared with",
            style = SplitlyTextStyles.captionTitle,
        )
        Spacer(modifier = Modifier.height(10.dp))
        expense.participantMemberIds.forEach { memberId ->
            ExpenseParticipantRow(
                memberName = members.nameFor(memberId),
                amount = participantShare,
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        ActionRow(
            icon = Icons.Filled.Edit,
            title = "Edit expense",
            subtitle = "Change amount, payer, or shared members",
            onClick = onEdit,
        )
        Spacer(modifier = Modifier.height(10.dp))
        ActionRow(
            icon = Icons.Filled.RemoveCircleOutline,
            title = "Delete expense",
            subtitle = "Remove this bill from the demo group",
            onClick = onDelete,
        )
        Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
    }
}

@Composable
private fun ExpenseFormSheet(
    title: String,
    buttonText: String,
    members: List<GroupMember>,
    initialExpense: GroupExpense?,
    onSaveExpense: (
        title: String,
        amount: Long,
        paidByMemberId: String,
        participantMemberIds: List<String>,
    ) -> Unit,
) {
    val allMemberIds = members.map { it.id }
    var expenseTitle by rememberSaveable(initialExpense?.id) {
        mutableStateOf(initialExpense?.title.orEmpty())
    }
    var amountText by rememberSaveable(initialExpense?.id) {
        mutableStateOf(initialExpense?.amount?.toString().orEmpty())
    }
    var paidByMemberId by rememberSaveable(initialExpense?.id, members.size) {
        mutableStateOf(initialExpense?.paidByMemberId ?: members.firstOrNull()?.id.orEmpty())
    }
    var selectionMode by rememberSaveable(initialExpense?.id) {
        mutableStateOf(
            if (initialExpense == null || initialExpense.participantMemberIds.toSet() == allMemberIds.toSet()) {
                SplitSelectionMode.Everyone
            } else {
                SplitSelectionMode.Custom
            },
        )
    }
    val selectedParticipantIds = remember(initialExpense?.id, members.map { it.id }.joinToString()) {
        mutableStateListOf<String>().apply {
            addAll(initialExpense?.participantMemberIds ?: allMemberIds)
        }
    }

    fun setEveryone() {
        selectionMode = SplitSelectionMode.Everyone
        selectedParticipantIds.clear()
        selectedParticipantIds.addAll(allMemberIds)
    }

    fun setEveryoneExcept() {
        selectionMode = SplitSelectionMode.EveryoneExcept
        if (selectedParticipantIds.isEmpty()) {
            selectedParticipantIds.addAll(allMemberIds)
        }
    }

    fun setCustom() {
        selectionMode = SplitSelectionMode.Custom
        if (selectedParticipantIds.isEmpty()) {
            paidByMemberId.takeIf { it.isNotBlank() }?.let { selectedParticipantIds.add(it) }
        }
    }

    fun toggleParticipant(memberId: String) {
        if (memberId in selectedParticipantIds) {
            if (selectedParticipantIds.size > 1) {
                selectedParticipantIds.remove(memberId)
            }
        } else {
            selectedParticipantIds.add(memberId)
        }
        if (selectedParticipantIds.toSet() == allMemberIds.toSet()) {
            selectionMode = SplitSelectionMode.Everyone
        }
    }

    val amount = amountText.filter { it.isDigit() }.toLongOrNull() ?: 0L
    val canSave = expenseTitle.isNotBlank() &&
        amount > 0L &&
        paidByMemberId.isNotBlank() &&
        selectedParticipantIds.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 28.dp),
    ) {
        Text(
            text = title,
            color = SplitlyColors.TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(16.dp))
        DemoOutlinedField(
            value = expenseTitle,
            onValueChange = { expenseTitle = it },
            placeholder = "Expense title",
        )
        Spacer(modifier = Modifier.height(12.dp))
        DemoOutlinedField(
            value = amountText,
            onValueChange = { amountText = it.filter(Char::isDigit) },
            placeholder = "Amount",
            keyboardType = KeyboardType.Number,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Paid by",
            style = SplitlyTextStyles.captionTitle,
        )
        Spacer(modifier = Modifier.height(10.dp))
        members.forEach { member ->
            PayerRow(
                member = member,
                selected = member.id == paidByMemberId,
                onClick = { paidByMemberId = member.id },
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Shared with",
            style = SplitlyTextStyles.captionTitle,
        )
        Spacer(modifier = Modifier.height(10.dp))
        SplitModeRow(
            title = "Everyone",
            subtitle = "Split with all current members",
            selected = selectionMode == SplitSelectionMode.Everyone,
            onClick = ::setEveryone,
        )
        SplitModeRow(
            title = "Everyone except...",
            subtitle = "Start from everyone, then remove people",
            selected = selectionMode == SplitSelectionMode.EveryoneExcept,
            onClick = ::setEveryoneExcept,
        )
        SplitModeRow(
            title = "Choose manually",
            subtitle = "Tick only people who shared this bill",
            selected = selectionMode == SplitSelectionMode.Custom,
            onClick = ::setCustom,
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (selectionMode != SplitSelectionMode.Everyone) {
            members.forEach { member ->
                ParticipantSelectorRow(
                    member = member,
                    selected = member.id in selectedParticipantIds,
                    onClick = {
                        if (selectionMode == SplitSelectionMode.Everyone) {
                            selectionMode = SplitSelectionMode.Custom
                        }
                        toggleParticipant(member.id)
                    },
                )
            }
        }
        if (paidByMemberId !in selectedParticipantIds) {
            Text(
                text = "Payer is not included in this bill. This is allowed for paid-on-behalf cases.",
                color = SplitlyColors.OnProcess,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
            )
        } else {
            Spacer(modifier = Modifier.height(12.dp))
        }
        SplitlyFilledButton(
            text = buttonText,
            onClick = {
                if (canSave) {
                    onSaveExpense(
                        expenseTitle.trim(),
                        amount,
                        paidByMemberId,
                        selectedParticipantIds.toList(),
                    )
                }
            },
        )
        Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
    }
}

@Composable
private fun DetailInfoRow(
    title: String,
    value: String,
) {
    SectionCard(contentPadding = 14) {
        Text(text = title, style = SplitlyTextStyles.captionTitle)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            color = SplitlyColors.TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun ExpenseParticipantRow(
    memberName: String,
    amount: Long,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .background(Color(0xFFFAFAFA), RoundedCornerShape(12.dp))
            .border(1.dp, SplitlyColors.FieldBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = memberName,
            color = SplitlyColors.TextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = formatMoney(amount),
            color = SplitlyColors.TextSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun SplitModeRow(
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    SelectableRow(
        title = title,
        subtitle = subtitle,
        selected = selected,
        onClick = onClick,
    )
}

@Composable
private fun ParticipantSelectorRow(
    member: GroupMember,
    selected: Boolean,
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
            Avatar(member = member, size = 30)
            Text(
                text = member.name,
                color = SplitlyColors.TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 10.dp),
            )
        }
    }
}

@Composable
private fun SelectableRow(
    title: String,
    subtitle: String,
    selected: Boolean,
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
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Text(
                    text = title,
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
private fun FriendCandidateRow(
    friend: GroupMember,
    onAddFriend: (GroupMember) -> Unit,
) {
    SectionCard(
        modifier = Modifier.padding(bottom = 10.dp),
        contentPadding = 14,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Avatar(member = friend)
            Text(
                text = friend.name,
                color = SplitlyColors.TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
            )
            BouncyButton(onClick = { onAddFriend(friend) }) {
                Text(
                    text = "Add",
                    color = SplitlyColors.Primary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(SplitlyColors.Primary.copy(alpha = 0.1f), RoundedCornerShape(18.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                )
            }
        }
    }
}

@Composable
private fun ActionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    BouncyButton(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SplitlyColors.Primary.copy(alpha = 0.08f), RoundedCornerShape(14.dp))
                .border(1.dp, SplitlyColors.Primary.copy(alpha = 0.18f), RoundedCornerShape(14.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(SplitlyColors.Primary, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp),
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp),
            ) {
                Text(
                    text = title,
                    color = SplitlyColors.TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = subtitle,
                    style = SplitlyTextStyles.caption,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = SplitlyColors.TextSecondary,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun PayerRow(
    member: GroupMember,
    selected: Boolean,
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
                    if (selected) SplitlyColors.Primary.copy(alpha = 0.1f) else Color.Transparent,
                    RoundedCornerShape(12.dp),
                )
                .border(
                    1.dp,
                    if (selected) SplitlyColors.Primary.copy(alpha = 0.3f) else SplitlyColors.FieldBorder,
                    RoundedCornerShape(12.dp),
                )
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Avatar(member = member, size = 30)
            Text(
                text = member.name,
                color = SplitlyColors.TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 10.dp),
            )
        }
    }
}

@Composable
private fun DemoOutlinedField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        placeholder = {
            Text(text = placeholder, style = SplitlyTextStyles.hint)
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
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
private fun Avatar(
    member: GroupMember,
    size: Int = 38,
) {
    Image(
        painter = painterResource(id = member.avatarResId),
        contentDescription = null,
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .border(2.dp, Color.White, CircleShape),
        contentScale = ContentScale.Crop,
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
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(18.dp))
            .border(1.dp, color.copy(alpha = 0.28f), RoundedCornerShape(18.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
    )
}

private fun List<GroupMember>.nameFor(memberId: String): String =
    firstOrNull { it.id == memberId }?.name ?: "Unknown"

private fun List<GroupBalance>.currentUserBalance(currentUserId: String): Long {
    val receivable = filter { it.toMemberId == currentUserId }.sumOf { it.amount }
    val payable = filter { it.fromMemberId == currentUserId }.sumOf { it.amount }
    return receivable - payable
}

private fun List<GroupSettlement>.matching(balance: GroupBalance): GroupSettlement? =
    lastOrNull { settlement ->
        settlement.fromMemberId == balance.fromMemberId &&
            settlement.toMemberId == balance.toMemberId &&
            settlement.status != SettlementStatus.Confirmed
    }

private fun MutableList<GroupSettlement>.upsertSettlement(settlement: GroupSettlement) {
    val index = indexOfFirst { it.id == settlement.id }
    if (index >= 0) {
        this[index] = settlement
    } else {
        add(settlement)
    }
}

private fun GroupBalance.toNewSettlement(): GroupSettlement =
    GroupSettlement(
        id = "settlement_${fromMemberId}_${toMemberId}_${System.currentTimeMillis()}",
        fromMemberId = fromMemberId,
        toMemberId = toMemberId,
        amount = amount,
        status = SettlementStatus.Unpaid,
    )

private val GroupMemberRole.label: String
    get() = when (this) {
        GroupMemberRole.Owner -> "Owner"
        GroupMemberRole.Member -> "Member"
    }

private val GroupStatus.label: String
    get() = when (this) {
        GroupStatus.Active -> "Active"
        GroupStatus.PaymentProcess -> "Bill Sharing"
        GroupStatus.Done -> "Done"
    }

private val SettlementStatus.label: String
    get() = when (this) {
        SettlementStatus.Unpaid -> "Unpaid"
        SettlementStatus.WaitingForReceiverConfirmation -> "Waiting"
        SettlementStatus.Confirmed -> "Confirmed"
        SettlementStatus.Rejected -> "Rejected"
    }

private val SettlementStatus.color: Color
    get() = when (this) {
        SettlementStatus.Unpaid -> SplitlyColors.TextSecondary
        SettlementStatus.WaitingForReceiverConfirmation -> SplitlyColors.OnProcess
        SettlementStatus.Confirmed -> SplitlyColors.Done
        SettlementStatus.Rejected -> SplitlyColors.OnProcess
    }

private fun formatMoney(amount: Long): String =
    "${NumberFormat.getNumberInstance(Locale.US).format(amount)} VND"
