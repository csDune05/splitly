package com.example.split.navigation

import com.example.split.model.SplitGroup

sealed interface SplitRoute {
    data object Welcome : SplitRoute
    data object Login : SplitRoute
    data object Signup : SplitRoute
    data object Home : SplitRoute
    data object Friends : SplitRoute
    data object Stats : SplitRoute
    data object Profile : SplitRoute
    data class GroupDetail(val group: SplitGroup) : SplitRoute
}

enum class BottomNavDestination {
    Home,
    Friends,
    Stats,
    Profile,
}
