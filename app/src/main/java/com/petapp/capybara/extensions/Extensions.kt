package com.petapp.capybara.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun View.visible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun Context?.toast(text: CharSequence, duration: Int = Toast.LENGTH_LONG) = this?.let { Toast.makeText(it, text, duration).show() }

fun Context.createImageFile(): File {
    @SuppressLint("SimpleDateFormat")
    val timeStamp = SimpleDateFormat("yyyy.MM.dd_hh:mm:ss").format(Date())
    val imageFileName = "capybara_${timeStamp}.jpg"
    val storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File(storageDir, imageFileName)
}


/**
 * Functions "argument", "extractFromBundle" and class "BundleExtractorDelegate"
 * from https://gitlab.com/terrakok/gitlab-client
 */

inline fun <reified T> argument(
    key: String,
    defaultValue: T? = null
): ReadWriteProperty<Fragment, T> =
    BundleExtractorDelegate { thisRef ->
        extractFromBundle(
            bundle = thisRef.arguments,
            key = key,
            defaultValue = defaultValue
        )
    }

inline fun <reified T> extractFromBundle(
    bundle: Bundle?,
    key: String,
    defaultValue: T? = null
): T {
    val result = bundle?.get(key) ?: defaultValue
    if (result != null && result !is T) {
        throw ClassCastException("Property $key has different class type")
    }
    return result as T
}

class BundleExtractorDelegate<R, T>(private val initializer: (R) -> T) : ReadWriteProperty<R, T> {

    private object EMPTY

    private var value: Any? = EMPTY

    override fun setValue(thisRef: R, property: KProperty<*>, value: T) {
        this.value = value
    }

    override fun getValue(thisRef: R, property: KProperty<*>): T {
        if (value == EMPTY) {
            value = initializer(thisRef)
        }
        @Suppress("UNCHECKED_CAST")
        return value as T
    }
}