package com.example.split.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.split.navigation.BottomNavDestination
import com.example.split.ui.components.SplitlyBottomBar
import com.example.split.ui.theme.SplitlyColors

@Composable
fun FriendScreen(
    onHomeSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = SplitlyColors.PageBackground,
        bottomBar = {
            SplitlyBottomBar(
                selectedDestination = BottomNavDestination.Friends,
                onHomeClick = onHomeSelected,
                onFriendsClick = {},
                onAddClick = {},
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .safeDrawingPadding(),
        ) {
            Text(text = "friends")
        }
    }
}
