package com.example.split.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.split.R
import com.example.split.auth.AuthValidation
import com.example.split.auth.LoginValidationResult
import com.example.split.ui.components.BouncyButton
import com.example.split.ui.components.SplitlyFilledButton
import com.example.split.ui.components.SplitlyTextField
import com.example.split.ui.components.SplitlyTextStyles
import com.example.split.ui.theme.SplitlyColors

@Composable
fun LoginScreen(
    onSignedIn: () -> Unit,
    onGoogleSignIn: () -> Unit,
    onSignUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var validation by remember { mutableStateOf(LoginValidationResult()) }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding(),
    ) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight
        val isSmallScreen = screenWidth < 600.dp
        val horizontalPadding = if (isSmallScreen) 24.dp else screenWidth * 0.1f
        val maxContentWidth = if (isSmallScreen) screenWidth else 420.dp
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = horizontalPadding)
                .padding(top = screenHeight * 0.025f, bottom = screenHeight * 0.025f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = maxContentWidth)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SplitlyBrandHeader(screenHeight = screenHeight)

                Spacer(modifier = Modifier.height(screenHeight * 0.045f))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    SplitlyTextField(
                        label = "",
                        value = email,
                        onValueChange = {
                            email = it
                            validation = validation.copy(emailError = null)
                        },
                        hint = "Enter your email",
                        leadingIcon = Icons.Filled.Email,
                        error = validation.emailError,
                        keyboardType = KeyboardType.Email,
                    )

                    Spacer(modifier = Modifier.height(screenHeight * 0.028f))

                    SplitlyTextField(
                        label = "",
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

                    Spacer(modifier = Modifier.height(screenHeight * 0.04f))

                    SplitlyFilledButton(
                        text = "Login",
                        onClick = {
                            val result = AuthValidation.validateLogin(email, password)
                            validation = result
                            if (result.isValid) {
                                onSignedIn()
                            }
                        },
                    )

                    Spacer(modifier = Modifier.height(screenHeight * 0.022f))

                    LoginDivider()

                    Spacer(modifier = Modifier.height(screenHeight * 0.022f))

                    GoogleSignInButton(
                        onClick = onGoogleSignIn,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(screenHeight * 0.028f))

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

                    Spacer(modifier = Modifier.height(screenHeight * 0.006f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        BouncyButton(onClick = {}, pressedScale = 0.88f) {
                            Text(
                                text = "Forgot password?",
                                style = SplitlyTextStyles.link,
                                modifier = Modifier.padding(4.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SplitlyBrandHeader(screenHeight: Dp) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Splitly logo",
            modifier = Modifier
                .fillMaxWidth(0.32f)
                .height(screenHeight * 0.13f),
            contentScale = ContentScale.Fit,
        )
        Text(text = "SPLITLY", style = SplitlyTextStyles.bigTitle)
        Text(
            text = "Manage your event finances easily and accurately.",
            style = SplitlyTextStyles.caption,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
        )
    }
}

@Composable
private fun LoginDivider(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = SplitlyColors.FieldBorder)
        Text(
            text = "or",
            style = SplitlyTextStyles.caption,
            modifier = Modifier.padding(horizontal = 16.dp),
            textAlign = TextAlign.Center,
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = SplitlyColors.FieldBorder)
    }
}

@Composable
private fun GoogleSignInButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BouncyButton(
        onClick = onClick,
        pressedScale = 0.88f,
        modifier = modifier,
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = SplitlyColors.White,
            border = BorderStroke(1.dp, SplitlyColors.FieldBorder),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 18.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "G",
                        color = Color(0xFF4285F4),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Spacer(modifier = Modifier.size(12.dp))
                Text(
                    text = "Sign in with Google",
                    style = SplitlyTextStyles.buttonSecondary.copy(color = SplitlyColors.TextPrimary),
                )
            }
        }
    }
}
