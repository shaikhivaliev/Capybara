package com.petapp.capybara.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.os.Environment
import android.view.ContextThemeWrapper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.google.android.material.chip.Chip
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Profile
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun NavDirections.navigateWith(navController: NavController, navOptions: NavOptions? = null) {
    if (navController.currentDestination?.getAction(actionId) != null) {
        navController.navigate(this, navOptions)
    }
}

fun Context?.toast(stringRes: Int, duration: Int = Toast.LENGTH_LONG) =
    this?.let { Toast.makeText(it, getString(stringRes), duration).show() }

fun Context.createImageFile(): File {
    @SuppressLint("SimpleDateFormat")
    val timeStamp = SimpleDateFormat("yyyy.MM.dd_hh:mm:ss").format(Date())
    val imageFileName = "capybara_$timeStamp.jpg"
    val storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File(storageDir, imageFileName)
}

fun currentDateFull(date: Date): String {
    return SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).format(date)
}

fun currentDateMonthYear(date: Date): String {
    return SimpleDateFormat("LLLL yyyy", Locale("ru")).format(date).capitalize()
}

fun createChip(context: Context, profile: Profile, padding: Float): Chip {
    val chip = Chip(context)
    chip.apply {
        chipEndPadding = padding
        chipStartPadding = padding
        text = profile.name
        setTextColor(ContextCompat.getColor(context, android.R.color.black))
        isCheckable = true
        checkedIcon = ContextCompat.getDrawable(context, R.drawable.ic_done_black)
        chipBackgroundColor = ColorStateList.valueOf(profile.color)
    }
    return chip
}

fun View.showKeyboard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_FORCED)
}

fun View.hideKeyboard() {
    val windowToken = (activity.currentFocus ?: this).applicationWindowToken
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

val View.activity: Activity
    get() {
        var context = context
        while (true) {
            when (context) {
                is Activity -> return context
                is ContextThemeWrapper -> context = context.baseContext
            }
        }
    }
