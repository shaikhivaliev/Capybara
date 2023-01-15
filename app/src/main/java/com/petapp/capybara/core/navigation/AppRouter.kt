package com.petapp.capybara.core.navigation

import com.github.terrakok.cicerone.BackTo
import com.github.terrakok.cicerone.Forward
import com.github.terrakok.cicerone.Replace
import com.github.terrakok.cicerone.Router


// высокоуровневый объект, с которым вы непосредственно взаимодействуете в коде, вызывая его методы.
// Любой из методов Router превращается в некоторый набор базовых команд.
// Роутер может работать только с одним навигатором
class AppRouter : Router() {

    fun navigateTo(screen: BaseScreen) {
        executeCommands(Forward(screen.toFragmentScreen()))
    }

    fun backTo(screen: BaseScreen?) {
        executeCommands(BackTo(screen?.toFragmentScreen()))
    }

    fun newRootScreen(screen: BaseScreen) {
        executeCommands(BackTo(null), Replace(screen.toFragmentScreen()))
    }

    fun replaceScreen(screen: BaseScreen) {
        executeCommands(Replace(screen.toFragmentScreen()))
    }

    fun newRootChain(vararg screens: BaseScreen) {
        val commands = screens.mapIndexed { index, screen ->
            if (index == 0) {
                Replace(screen.toFragmentScreen())
            } else {
                Forward(screen.toFragmentScreen())
            }
        }
        executeCommands(BackTo(null), *commands.toTypedArray())
    }

//    fun multiScreensBack(amountOfBackSteps: Int) {
//        executeCommands(*Array<Command>(amountOfBackSteps) { Back() })
//    }
//
//    fun replaceScreenWithoutBackStack(screen: BaseScreen) {
//        executeCommands(ReplaceScreenWithoutBackStack(screen.toFragmentScreen()))
//    }
//
//    fun backToOrNewRoot(screen: BaseScreen) {
//        executeCommands(BackToOrNewRoot(screen.toFragmentScreen()))
//    }
//
//    fun backAndReplace(screen: BaseScreen) {
//        executeCommands(Back(), Replace(screen.toFragmentScreen()))
//    }
//
//    fun backToAndReplace(baseScreen: BaseScreen, replacedScreen: BaseScreen) {
//        executeCommands(BackTo(baseScreen.toFragmentScreen()), Replace(replacedScreen.toFragmentScreen()))
//    }
//
//    fun sendAuthResultToSideApp(uri: Uri) {
//        executeCommands(SendResultAndFinish(uri))
//    }
}
