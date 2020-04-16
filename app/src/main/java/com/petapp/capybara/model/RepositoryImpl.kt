package com.petapp.capybara.model

import com.petapp.capybara.entity.Dataset
import com.petapp.capybara.extensions.errorMessage
import com.petapp.capybara.net.Network
import com.petapp.capybara.view.MainViewImpl.Companion.id
import kotlinx.coroutines.delay
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class RepositoryImpl : Repository {

    override suspend fun getData(data: (Dataset) -> Unit, error: (String) -> Unit, inProgress: (Boolean) -> Unit) {

        val result = apiResult(
            apiCall = { Network.api.getDataset(id) },
            inProgress = { inProgress.invoke(it) }
        )

        when (result) {
            is Result.Success -> data.invoke(result.data)
            is Result.Error -> error.invoke(result.exception.errorMessage())
        }
    }

    private suspend fun <T : Any> apiResult(apiCall: suspend () -> Response<T>, inProgress: (Boolean) -> Unit): Result<T> {

        return try {
            val response = apiCall.invoke()
            val model = response.body()
            inProgress(true)
            delay(2000)
            if (response.isSuccessful && model != null) {
                Result.Success(model)
            } else {
                Result.Error(HttpException(response))
            }
        } catch (e: IOException) {
            Result.Error(e)
        } finally {
            inProgress(false)
        }
    }
}