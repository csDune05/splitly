package com.example.split.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PeopleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.split.model.GroupStatus
import com.example.split.model.SplitGroup
import com.example.split.ui.theme.SplitlyColors

@Composable
fun GroupCard(
    group: SplitGroup,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BouncyButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = Color(0x1F9E9E9E),
                    spotColor = Color(0x1F9E9E9E),
                )
                .background(Color.White, RoundedCornerShape(20.dp))
                .padding(20.dp),
        ) {
            Text(
                text = group.name,
                style = SplitlyTextStyles.groupTitle,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.PeopleOutline,
                    contentDescription = null,
                    tint = Color(0xFF616161),
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${group.memberCount} members",
                    style = SplitlyTextStyles.caption,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StatusPill(status = group.status)
                AvatarStack(group = group)
            }
        }
    }
}

@Composable
private fun StatusPill(status: GroupStatus) {
    val colors = statusColors(status)
    Box(
        modifier = Modifier
            .width(200.dp)
            .background(colors.background, RoundedCornerShape(20.dp))
            .border(1.dp, colors.border, RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = status.label,
            color = colors.foreground,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun AvatarStack(group: SplitGroup) {
    if (group.memberAvatarResIds.isEmpty()) {
        return
    }

    val displayCount = if (group.totalMemberCount > 4) 3 else group.displayAvatarResIds.size
    val visibleItems = if (group.totalMemberCount > 4) displayCount + 1 else displayCount
    val width = (((visibleItems - 1).coerceAtLeast(0) * 20) + 32).dp

    Box(
        modifier = Modifier
            .width(width)
            .height(32.dp),
    ) {
        group.displayAvatarResIds.take(displayCount).forEachIndexed { index, avatarResId ->
            AvatarImage(
                avatarResId = avatarResId,
                modifier = Modifier.offset(x = (index * 20).dp),
            )
        }

        if (group.totalMemberCount > 4) {
            Box(
                modifier = Modifier
                    .offset(x = (displayCount * 20).dp)
                    .size(32.dp)
                    .background(Color(0xFF9E9E9E), CircleShape)
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "+${group.totalMemberCount - 4}",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun AvatarImage(
    @DrawableRes avatarResId: Int,
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(id = avatarResId),
        contentDescription = null,
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
            .border(2.dp, Color.White, CircleShape),
        contentScale = ContentScale.Crop,
    )
}

private val GroupStatus.label: String
    get() = when (this) {
        GroupStatus.Active -> "Active"
        GroupStatus.PaymentProcess -> "Bill Sharing"
        GroupStatus.Done -> "Done"
    }

private data class StatusColors(
    val foreground: Color,
    val background: Color,
    val border: Color,
)

private fun statusColors(status: GroupStatus): StatusColors {
    val foreground = when (status) {
        GroupStatus.Active -> SplitlyColors.Active
        GroupStatus.PaymentProcess -> SplitlyColors.OnProcess
        GroupStatus.Done -> SplitlyColors.Done
    }
    return StatusColors(
        foreground = foreground,
        background = foreground.copy(alpha = 0.1f),
        border = foreground.copy(alpha = 0.3f),
    )
}
