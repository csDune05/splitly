package com.example.split.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.split.data.DemoGroupRepository
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onGroupSelected: (SplitGroup) -> Unit,
    onFriendsSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val groups = remember { DemoGroupRepository.groups() }
    var currentFilter by rememberSaveable { mutableStateOf(FilterOption.All) }
    var showAddSheet by rememberSaveable { mutableStateOf(false) }
    var showFilterSheet by rememberSaveable { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val filteredGroups = remember(groups, currentFilter) {
        groups.filterBy(currentFilter)
    }

    Scaffold(
        modifier = modifier,
        containerColor = SplitlyColors.PageBackground,
        topBar = { HomeHeader(listState = listState) },
        bottomBar = {
            SplitlyBottomBar(
                selectedDestination = BottomNavDestination.Home,
                onHomeClick = {},
                onFriendsClick = onFriendsSelected,
                onAddClick = { showAddSheet = true },
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

    if (showAddSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddSheet = false },
            containerColor = Color.White,
            dragHandle = null,
        ) {
            AddOptionsSheet(
                onCreateGroup = {
                    showAddSheet = false
                },
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
private fun AddOptionsSheet(onCreateGroup: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
    ) {
        Box(
            modifier = Modifier
                .padding(top = 10.dp)
                .size(width = 40.dp, height = 4.dp)
                .background(Color(0xFFE0E0E0), RoundedCornerShape(2.dp)),
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "New",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(20.dp))
        BouncyButton(
            onClick = onCreateGroup,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        SplitlyColors.Primary.copy(alpha = 0.1f),
                        RoundedCornerShape(12.dp),
                    )
                    .border(
                        1.dp,
                        SplitlyColors.Primary.copy(alpha = 0.2f),
                        RoundedCornerShape(12.dp),
                    )
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(SplitlyColors.Primary, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.GroupAdd,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp),
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                ) {
                    Text(
                        text = "Create new group",
                        color = Color(0xFF424242),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Start a new group with friends",
                        color = Color(0xFF757575),
                        fontSize = 13.sp,
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color(0xFFBDBDBD),
                    modifier = Modifier.size(20.dp),
                )
            }
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
