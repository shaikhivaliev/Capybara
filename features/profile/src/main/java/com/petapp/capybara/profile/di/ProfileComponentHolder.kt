package com.petapp.capybara.profile.di

import com.petapp.capybara.core.di.BaseComponentHolder
import com.petapp.capybara.core.di.CoreComponentHolder

object ProfileComponentHolder : BaseComponentHolder<ProfileComponent>() {
    override val component: ProfileComponent
        get() = _component ?: DaggerProfileComponent
            .builder()
            .bindCoreComponent(CoreComponentHolder.component)
            .build()
            .also { commonComponent ->
                _component = commonComponent
            }
}
