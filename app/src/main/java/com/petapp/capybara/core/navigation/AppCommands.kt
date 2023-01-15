package com.petapp.capybara.core.navigation

import android.net.Uri
import com.github.terrakok.cicerone.Command
import com.github.terrakok.cicerone.Screen


// Сommand - описывают переходы, виды coomand: forward - переход вперед, back - переход назад,
// backTo - возвращаемся на любой из экранов в цепочке, replace - заменяет активнй экран на новый
// можно делать свои кастомные переходы и реализовать в applyCommand в навигаторе

class ReplaceScreenWithoutBackStack(val screen: Screen) : Command

class SendResultAndFinish(val uri: Uri) : Command

class BackToOrNewRoot(val screen: Screen) : Command


