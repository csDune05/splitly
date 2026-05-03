package com.example.split.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.split.navigation.BottomNavDestination
import com.example.split.ui.theme.SplitlyColors

@Composable
fun SplitlyBottomBar(
    selectedDestination: BottomNavDestination?,
    onHomeClick: () -> Unit,
    onFriendsClick: () -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 12.dp)
            .border(width = 0.5.dp, color = Color(0xFFE5E7EB))
            .background(SplitlyColors.PageBackground)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .height(95.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BottomBarItem(
                icon = Icons.Filled.Home,
                isActive = selectedDestination == BottomNavDestination.Home,
                onClick = onHomeClick,
                contentDescription = "Home",
            )
            BottomBarItem(
                icon = Icons.Filled.People,
                isActive = selectedDestination == BottomNavDestination.Friends,
                onClick = onFriendsClick,
                contentDescription = "Friends",
            )
            AddButton(onClick = onAddClick)
            BottomBarItem(
                icon = Icons.Filled.Analytics,
                isActive = selectedDestination == BottomNavDestination.Stats,
                onClick = {},
                contentDescription = "Stats",
            )
            BottomBarItem(
                icon = Icons.Filled.AccountCircle,
                isActive = selectedDestination == BottomNavDestination.Profile,
                onClick = {},
                contentDescription = "Profile",
            )
        }
    }
}

@Composable
private fun AddButton(onClick: () -> Unit) {
    BouncyButton(onClick = onClick) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .shadow(
                        elevation = 12.dp,
                        shape = CircleShape,
                        ambientColor = Color(0x660084FF),
                        spotColor = Color(0x660084FF),
                    )
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(SplitlyColors.Primary, SplitlyColors.Accent),
                        ),
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add",
                    tint = SplitlyColors.White,
                    modifier = Modifier.size(30.dp),
                )
            }
        }
    }
}

@Composable
private fun BottomBarItem(
    icon: ImageVector,
    isActive: Boolean,
    onClick: () -> Unit,
    contentDescription: String,
) {
    BouncyButton(onClick = onClick) {
        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 10.dp)
                .size(45.dp)
                .background(
                    color = if (isActive) Color.White.copy(alpha = 0.2f) else Color.Transparent,
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = if (isActive) SplitlyColors.Primary else Color(0xFF616161),
                modifier = Modifier.size(30.dp),
            )
        }
    }
}
