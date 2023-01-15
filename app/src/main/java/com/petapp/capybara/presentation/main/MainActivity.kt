package com.petapp.capybara.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.petapp.capybara.R
import com.petapp.capybara.core.navigation.AppRouter
import com.petapp.capybara.databinding.ActivityMainBinding
import com.petapp.capybara.di.features.FeaturesComponentHolder
import javax.inject.Inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    @Inject
    lateinit var router: AppRouter

//    NavigationHolder нам нужен для того, чтобы передавать и удалять из Cicerone наш текущий навигатор,
//    который описывается интерфейсом Navigator. В стандартном подходе это будет происходить в callback-ах onPause() и onResume()
//    вашей Activity или Fragment-а — все зависит от того, что вы выбираете как контейнер.
//    Именно удаление Navigator-a позволяет CommandBuffer понять, когда приложение находится в background-е
    @Inject
    lateinit var navigatorHolder: NavigatorHolder

//    navigator - это ничто иное, как обертка над всем знакомыми Context и FragmentManager,
//    но в отличие от стандартного подхода работа с ними происходит где-то в отдельном от View месте.
//    navigator - реализация переключения экранов, отвечает за выполнение command
//    всегда будет там, где находится контейнер для переключаемых экранов(в actvity для переключения fragment)
    private val navigator = AppNavigator(
        activity = this,
        containerId = R.id.main_content
    )

    private val viewBinding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FeaturesComponentHolder.getComponent(this)?.inject(this)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
         navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        FeaturesComponentHolder.clearComponent()
    }
}
