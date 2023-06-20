package com.petapp.capybara.profile.state

import com.petapp.capybara.core.mvi.SideEffect

sealed class ProfilesEffect : SideEffect {
    object Ready : ProfilesEffect()
    data class NavigateToProfile(val profileId: Long) : ProfilesEffect()
}
