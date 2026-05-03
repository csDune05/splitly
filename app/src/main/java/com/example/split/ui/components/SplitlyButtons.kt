package com.example.split.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.split.ui.theme.SplitlyColors

@Composable
fun SplitlyFilledButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BouncyButton(
        onClick = onClick,
        pressedScale = 0.82f,
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SplitlyColors.Primary, RoundedCornerShape(12.dp))
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = text, style = SplitlyTextStyles.buttonPrimary)
        }
    }
}

@Composable
fun SplitlySoftButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = SplitlyColors.SoftBlue,
) {
    BouncyButton(
        onClick = onClick,
        pressedScale = 0.88f,
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(10.dp))
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = text, style = SplitlyTextStyles.buttonSecondary)
        }
    }
}
