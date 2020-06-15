package com.petapp.capybara.extensions

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun View.visible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

/**
https://stackoverflow.com/a/52075248
 */
fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}