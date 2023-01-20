package com.petapp.capybara.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.os.Environment
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Profile
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun Context?.toast(stringRes: Int, duration: Int = Toast.LENGTH_LONG) =
    this?.let { Toast.makeText(it, getString(stringRes), duration).show() }

fun Context.createImageFile(): File {
    @SuppressLint("SimpleDateFormat")
    val timeStamp = SimpleDateFormat("yyyy.MM.dd_hh:mm:ss").format(Date())
    val imageFileName = "capybara_$timeStamp.jpg"
    val storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File(storageDir, imageFileName)
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
