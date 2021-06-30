package com.petapp.capybara.di.features

import com.petapp.capybara.core.navigation.NavControllerProvider
import com.petapp.capybara.di.app.AppComponent
import com.petapp.capybara.di.core.CoreComponent
import com.petapp.capybara.presentation.main.MainActivity
import com.petapp.capybara.presentation.calendar.CalendarFragment
import com.petapp.capybara.presentation.profile.ProfileFragment
import com.petapp.capybara.presentation.profiles.ProfilesFragment
import com.petapp.capybara.presentation.survey.SurveyFragment
import com.petapp.capybara.presentation.surveys.SurveysFragment
import com.petapp.capybara.presentation.types.TypesFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    dependencies = [
        AppComponent::class,
        CoreComponent::class
    ],
    modules = [
        VmModule::class,
        DataModule::class,
        NavigationModule::class
    ]
)
@FeaturesScope
interface FeaturesComponent {

    @Component.Builder
    interface Builder {
        fun bindAppComponent(appComponent: AppComponent): Builder
        fun bindCoreComponent(coreComponent: CoreComponent): Builder
        @BindsInstance
        fun bindMainActivity(activity: MainActivity): Builder
        fun build(): FeaturesComponent
    }

    fun inject(target: NavControllerProvider)
    fun inject(target: SurveyFragment)
    fun inject(target: SurveysFragment)
    fun inject(target: TypesFragment)
    fun inject(target: CalendarFragment)
    fun inject(target: ProfileFragment)
    fun inject(target: ProfilesFragment)
}
