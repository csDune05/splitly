package com.example.split.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.split.ui.theme.SplitlyColors

object SplitlyTextStyles {
    val bigTitle: TextStyle
        @Composable get() = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = SplitlyColors.Primary,
            letterSpacing = 0.sp,
        )

    val normalTitle: TextStyle
        @Composable get() = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            color = Color.Black,
            letterSpacing = 0.sp,
        )

    val groupTitle: TextStyle
        @Composable get() = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            color = Color.Black,
            letterSpacing = 0.sp,
        )

    val highlightTitle: TextStyle
        @Composable get() = normalTitle.copy(color = SplitlyColors.Primary)

    val whiteTitle: TextStyle
        @Composable get() = normalTitle.copy(color = SplitlyColors.White)

    val body: TextStyle
        @Composable get() = TextStyle(
            fontSize = 16.sp,
            color = Color.Black,
            letterSpacing = 0.sp,
        )

    val bigBody: TextStyle
        @Composable get() = body.copy(fontSize = 18.sp)

    val captionTitle: TextStyle
        @Composable get() = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = SplitlyColors.TextSecondary,
            letterSpacing = 0.sp,
        )

    val caption: TextStyle
        @Composable get() = TextStyle(
            fontSize = 14.sp,
            color = Color(0xFF757575),
            letterSpacing = 0.sp,
        )

    val buttonPrimary: TextStyle
        @Composable get() = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = SplitlyColors.White,
            letterSpacing = 0.sp,
        )

    val buttonSecondary: TextStyle
        @Composable get() = buttonPrimary.copy(color = SplitlyColors.Primary)

    val link: TextStyle
        @Composable get() = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = SplitlyColors.PrimaryLight,
            letterSpacing = 0.sp,
        )

    val hint: TextStyle
        @Composable get() = TextStyle(
            fontSize = 16.sp,
            color = Color(0xFF9CA3AF),
            letterSpacing = 0.sp,
        )
}
