package com.example.magmarket.data.remote

import com.example.magmarket.data.remote.model.ProductError
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.net.ssl.SSLException

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Error<out T>(val message: String?) : ResultWrapper<T>()
    object Loading : ResultWrapper<Nothing>()
}

suspend inline fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    crossinline apiCall: suspend () -> Response<T>
) = flow {
    emit(ResultWrapper.Loading)
    try {
        val response = apiCall()
        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            emit(ResultWrapper.Success(responseBody))
        } else {
            val errorBody = response.errorBody()
            if (errorBody != null) {
                val type = object : TypeToken<ProductError>() {}.type
                val responseError = Gson().fromJson<ProductError>(errorBody.charStream(), type)
                emit(ResultWrapper.Error(responseError.message))
            } else {
                emit(ResultWrapper.Error("Some Thing Went Wrong"))
            }
        }
    } catch (e: SSLException) {
        emit(ResultWrapper.Error(e.message))
    } catch (e: IOException) {
        emit(ResultWrapper.Error(e.message))
    } catch (e: HttpException) {
        emit(ResultWrapper.Error(e.message))
    } catch (e: Throwable) {
        emit(ResultWrapper.Error(e.message))
    } finally {

    }
}