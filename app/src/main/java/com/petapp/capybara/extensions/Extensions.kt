package com.petapp.capybara.extensions

import android.view.View
import retrofit2.HttpException
import java.io.IOException

fun Throwable.errorMessage() = when (this) {
    is HttpException -> when (this.code()) {
        304 -> "304 Not Modified"
        400 -> "400 Bad Request"
        401 -> "401 Unauthorized"
        403 -> "403 Forbidden"
        404 -> "404 Not Found"
        405 -> "405 Method Not Allowed"
        409 -> "409 Conflict"
        422 -> "422 Unprocessable"
        500 -> "500 Server NoConnectivityException"
        else -> "Неизвестная ошибка"
    }
    is IOException -> "Ошибка соединения"
    else -> "Неизвестная ошибка"
}

fun View.visible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}


