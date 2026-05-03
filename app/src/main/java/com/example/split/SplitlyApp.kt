package com.example.split

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.split.navigation.SplitRoute
import com.example.split.ui.screens.FriendScreen
import com.example.split.ui.screens.GroupScreen
import com.example.split.ui.screens.HomeScreen
import com.example.split.ui.screens.LoginScreen
import com.example.split.ui.screens.SignupScreen
import com.example.split.ui.screens.WelcomeScreen
import com.example.split.ui.theme.SplitTheme

@Composable
fun SplitlyApp() {
    SplitTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val backStack = remember { mutableStateListOf<SplitRoute>(SplitRoute.Login) }
            val currentRoute = backStack.last()

            fun navigate(route: SplitRoute) {
                backStack.add(route)
            }

            fun replaceAll(route: SplitRoute) {
                backStack.clear()
                backStack.add(route)
            }

            fun goBack() {
                if (backStack.size > 1) {
                    backStack.removeAt(backStack.lastIndex)
                }
            }

            BackHandler(enabled = backStack.size > 1) {
                goBack()
            }

            when (currentRoute) {
                SplitRoute.Welcome -> WelcomeScreen(
                    onCreateAccount = { navigate(SplitRoute.Signup) },
                    onSignIn = { navigate(SplitRoute.Login) },
                )

                SplitRoute.Login -> LoginScreen(
                    onSignedIn = { replaceAll(SplitRoute.Home) },
                    onGoogleSignIn = { replaceAll(SplitRoute.Home) },
                    onSignUp = { navigate(SplitRoute.Signup) },
                )

                SplitRoute.Signup -> SignupScreen(
                    onBack = ::goBack,
                    onSignIn = { navigate(SplitRoute.Login) },
                )

                SplitRoute.Home -> HomeScreen(
                    onGroupSelected = { navigate(SplitRoute.GroupDetail(it)) },
                    onFriendsSelected = { replaceAll(SplitRoute.Friends) },
                )

                SplitRoute.Friends -> FriendScreen(
                    onHomeSelected = { replaceAll(SplitRoute.Home) },
                )

                SplitRoute.Stats -> HomeScreen(
                    onGroupSelected = { navigate(SplitRoute.GroupDetail(it)) },
                    onFriendsSelected = { replaceAll(SplitRoute.Friends) },
                )

                SplitRoute.Profile -> HomeScreen(
                    onGroupSelected = { navigate(SplitRoute.GroupDetail(it)) },
                    onFriendsSelected = { replaceAll(SplitRoute.Friends) },
                )

                is SplitRoute.GroupDetail -> GroupScreen(
                    group = currentRoute.group,
                    onBack = ::goBack,
                    onHomeSelected = { replaceAll(SplitRoute.Home) },
                    onFriendsSelected = { replaceAll(SplitRoute.Friends) },
                )
            }
        }
    }
}
