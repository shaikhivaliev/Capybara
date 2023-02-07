package com.petapp.capybara.core.navigation

import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.data.model.Survey
import kotlinx.parcelize.Parcelize

const val EXTRA_NAVIGATION_DTO = "EXTRA_NAVIGATION_DTO"

interface BaseNavDto : Parcelable

@Parcelize
data class LongNavDto(val value: Long) : BaseNavDto

@Parcelize
data class SurveyNavDto(val survey: Survey) : BaseNavDto

@Parcelize
data class ProfileNavDto(val profile: Profile) : BaseNavDto

inline fun <reified T : BaseNavDto> Bundle?.getNavDto(): T? {
    if (this == null) return null

    return getParcelable(EXTRA_NAVIGATION_DTO)
}

inline fun <reified T : BaseNavDto> Fragment.getNavDto(): T? {
    if (arguments == null) return null

    return arguments.getNavDto()
}

inline fun <reified T : BaseNavDto> Fragment.navDto(): Lazy<T?> = lazy {
    getNavDto()
}

fun NavController.navigate(@IdRes resId: Int, navDto: BaseNavDto?, navOptions: NavOptions? = null) {
    return navigate(resId, navDto?.toBundle(), navOptions)
}

fun BaseNavDto.toBundle(): Bundle {
    return bundleOf(EXTRA_NAVIGATION_DTO to this)
}
