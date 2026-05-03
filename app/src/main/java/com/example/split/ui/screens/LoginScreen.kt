package com.example.split.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.split.auth.AuthValidation
import com.example.split.auth.LoginValidationResult
import com.example.split.ui.components.AuthTopBar
import com.example.split.ui.components.BouncyButton
import com.example.split.ui.components.SplitlyFilledButton
import com.example.split.ui.components.SplitlyTextField
import com.example.split.ui.components.SplitlyTextStyles

@Composable
fun LoginScreen(
    onBack: () -> Unit,
    onSignedIn: () -> Unit,
    onSignUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var validation by remember { mutableStateOf(LoginValidationResult()) }

    Scaffold(
        modifier = modifier.background(Color.White),
        topBar = {
            AuthTopBar(title = "Login", onBack = onBack)
        },
        containerColor = Color.White,
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            val screenWidth = maxWidth
            val screenHeight = maxHeight
            val isSmallScreen = screenWidth < 600.dp
            val horizontalPadding = if (isSmallScreen) 24.dp else screenWidth * 0.1f
            val maxContentWidth = if (isSmallScreen) screenWidth else 400.dp
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = horizontalPadding)
                    .padding(top = screenHeight * 0.03f, bottom = screenHeight * 0.03f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = maxContentWidth)
                        .fillMaxWidth(),
                ) {
                    Text(text = "Welcome Back", style = SplitlyTextStyles.highlightTitle)
                    Text(
                        text = "Hello there, sign in to continue!",
                        style = SplitlyTextStyles.body,
                    )

                    Spacer(modifier = Modifier.height(screenHeight * 0.05f))

                    SplitlyTextField(
                        label = "Username",
                        value = username,
                        onValueChange = {
                            username = it
                            validation = validation.copy(usernameError = null)
                        },
                        hint = "Enter your username",
                        leadingIcon = Icons.Filled.Person,
                        error = validation.usernameError,
                    )

                    Spacer(modifier = Modifier.height(screenHeight * 0.04f))

                    SplitlyTextField(
                        label = "Password",
                        value = password,
                        onValueChange = {
                            password = it
                            validation = validation.copy(passwordError = null)
                        },
                        hint = "Enter your password",
                        leadingIcon = Icons.Filled.Lock,
                        error = validation.passwordError,
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                        passwordVisible = passwordVisible,
                        onPasswordVisibilityChange = { passwordVisible = !passwordVisible },
                    )

                    Spacer(modifier = Modifier.height(screenHeight * 0.02f))

                    BouncyButton(onClick = {}, pressedScale = 0.88f) {
                        Text(
                            text = "Forgot Password?",
                            style = SplitlyTextStyles.link,
                            modifier = Modifier.padding(4.dp),
                        )
                    }

                    Spacer(modifier = Modifier.height(screenHeight * 0.08f))

                    SplitlyFilledButton(
                        text = "Sign in",
                        onClick = {
                            val result = AuthValidation.validateLogin(username, password)
                            validation = result
                            if (result.isValid) {
                                onSignedIn()
                            }
                        },
                    )

                    Spacer(modifier = Modifier.height(screenHeight * 0.02f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "Don't have an account? ",
                            style = SplitlyTextStyles.caption,
                        )
                        BouncyButton(onClick = onSignUp, pressedScale = 0.88f) {
                            Text(
                                text = "Sign up",
                                style = SplitlyTextStyles.link,
                                modifier = Modifier.padding(4.dp),
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
