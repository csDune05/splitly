package com.example.split.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.split.R
import com.example.split.ui.components.BouncyButton
import com.example.split.ui.components.SplitlyFilledButton
import com.example.split.ui.components.SplitlySoftButton
import com.example.split.ui.components.SplitlyTextStyles

@Composable
fun WelcomeScreen(
    onCreateAccount: () -> Unit,
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding(),
    ) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight
        val horizontalPadding = screenWidth * 0.06f
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = horizontalPadding, vertical = screenHeight * 0.04f),
        ) {
            Spacer(modifier = Modifier.height(screenHeight * 0.06f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Splitly logo",
                    modifier = Modifier
                        .fillMaxWidth(0.45f)
                        .height(screenHeight * 0.2f),
                    contentScale = ContentScale.Fit,
                )
                Text(text = "SPLITLY", style = SplitlyTextStyles.bigTitle)
                Spacer(modifier = Modifier.height(screenHeight * 0.01f))
                Text(
                    text = "Manage your event finances easily and accurately.",
                    style = SplitlyTextStyles.body,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(0.65f),
                )
            }

            Spacer(modifier = Modifier.height(screenHeight * 0.15f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(screenHeight * 0.025f),
                modifier = Modifier.fillMaxWidth(),
            ) {
                SplitlySoftButton(
                    text = "Sign in with Google",
                    onClick = {},
                    modifier = Modifier
                        .widthIn(max = 500.dp)
                        .fillMaxWidth(),
                )
                SplitlyFilledButton(
                    text = "Create an account",
                    onClick = onCreateAccount,
                    modifier = Modifier
                        .widthIn(max = 500.dp)
                        .fillMaxWidth(),
                )
            }

            Spacer(modifier = Modifier.height(screenHeight * 0.02f))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "Already have an account? ",
                    style = SplitlyTextStyles.caption,
                )
                BouncyButton(onClick = onSignIn, pressedScale = 0.88f) {
                    Text(
                        text = "Sign in",
                        style = SplitlyTextStyles.link,
                        modifier = Modifier.padding(4.dp),
                    )
                }
            }
        }
    }
}
