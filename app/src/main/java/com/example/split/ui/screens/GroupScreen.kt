package com.example.split.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.split.model.SplitGroup
import com.example.split.ui.components.SplitlyBottomBar
import com.example.split.ui.components.SplitlyGradientTopBar
import com.example.split.ui.theme.SplitlyColors

@Composable
fun GroupScreen(
    group: SplitGroup,
    onBack: () -> Unit,
    onHomeSelected: () -> Unit,
    onFriendsSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
                onAddClick = {},
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "Group Page, ${group.name}")
        }
    }
}
