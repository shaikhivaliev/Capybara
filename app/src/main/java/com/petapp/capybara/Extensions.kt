package com.petapp.capybara

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import retrofit2.HttpException
import retrofit2.Response
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

fun Activity.showSnackMessage(message: String) {
    val ssb = SpannableStringBuilder().apply {
        append(message)
        setSpan(
            ForegroundColorSpan(Color.WHITE),
            0,
            message.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    Snackbar.make(this.findViewById(android.R.id.content), ssb, Snackbar.LENGTH_LONG).show()
}

fun Context.toast(message: CharSequence, duration: Int = Toast.LENGTH_LONG) {
    val toast = Toast.makeText(this, message, duration)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
}

suspend fun <T : Any> apiResult(apiCall: suspend () -> Response<T>, inProgress: (Boolean) -> Unit): Result<T> {
    return try {
        val response = apiCall.invoke()
        val model = response.body()
        inProgress(true)
        if (response.isSuccessful && model != null) {
            inProgress(false)
            Result.Success(model)
        } else {
            inProgress(false)
            Result.Error(HttpException(response))
        }
    } catch (e: IOException) {
        inProgress(false)
        Result.Error(e)
    }
}