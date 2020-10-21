package com.petapp.capybara.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.google.android.material.chip.Chip
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Mark
import com.petapp.capybara.survey.SurveyFragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun NavDirections.navigateWith(navController: NavController, navOptions: NavOptions? = null) {
    if (navController.currentDestination?.getAction(actionId) != null) {
        navController.navigate(this, navOptions)
    }
}

fun View.visible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
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

fun currentMonth(date: Date): String {
    return SimpleDateFormat("MMMM", Locale.ENGLISH).format(date)
}

fun currentDateMonthYear(date: Date): String {
    return SimpleDateFormat("MMMM yyyy", Locale.ENGLISH).format(date)
}

fun createChip(context: Context, mark: Mark, padding: Float): Chip {
    val chip = Chip(context)
    chip.apply {
        chipEndPadding = padding
        chipStartPadding = padding
        text = mark.name
        setTextColor(ContextCompat.getColor(context, android.R.color.black))
        isCheckable = true
        checkedIcon = ContextCompat.getDrawable(context, R.drawable.ic_done_black)
        chipBackgroundColor = AppCompatResources.getColorStateList(context, mark.color)
    }
    return chip
}

fun Context.dpTpPx(dp: Int): Float {
    return dp.toFloat() * this.resources.displayMetrics.density
}

fun Context.createRadioButton(type: String): RadioButton {
    val params = RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    params.setMargins(
        SurveyFragment.MARGIN_START_END,
        SurveyFragment.MARGIN_TOP_BOTTOM,
        SurveyFragment.MARGIN_START_END,
        SurveyFragment.MARGIN_TOP_BOTTOM
    )

    return RadioButton(this).apply {
        text = type
        tag = type
        layoutParams = params
        setPadding(SurveyFragment.PADDING_START, 0, 0, 0)
    }
}
