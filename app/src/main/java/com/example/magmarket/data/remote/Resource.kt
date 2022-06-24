package com.example.magmarket.data.remote

import com.example.magmarket.data.remote.model.ProductError
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.net.ssl.SSLException

sealed class Resource<out T> {
    data class Success<out T>(val value: T) : Resource<T>()
    data class Error<out T>(val message: String?) : Resource<T>()
    object Loading : Resource<Nothing>()
}

 inline fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    crossinline apiCall: suspend () -> Response<T>
) = channelFlow<Resource<T>> {
    withContext(dispatcher){
        send(Resource.Loading)
        try {
            val response = apiCall()
            val responseBody = response.body()
            if (response.isSuccessful && responseBody !=null) {
               send(Resource.Success(responseBody))
            }else {
                val errorBody = response.errorBody()
                if (errorBody != null) {
                    val type = object : TypeToken<ProductError>() {}.type
                    val responseError = Gson().fromJson<ProductError>(errorBody.charStream(), type)
                    send(Resource.Error(responseError.message))
                } else {
                    send(Resource.Error("Some Thing Went Wrong"))
                }
            }
        } catch (e: SSLException) {
            send(Resource.Error(e.message))
        } catch (e: IOException) {
            send(Resource.Error(e.message))
        } catch (e: HttpException) {
            send(Resource.Error("e.message"))
        } catch (e: Throwable) {
            send(Resource.Error(e.message))
        } finally {

        }
    }

}.flowOn(dispatcher)

//sealed class Resource<T>(
//    val data: T? = null,
//    val message: String? = null
//) {
//
//    // We'll wrap our data in this 'Success'
//    // class in case of success response from api
//    class Success<T>(data: T) : Resource<T>(data = data)
//
//    // We'll pass error message wrapped in this 'Error'
//    // class to the UI in case of failure response
//    class Error<T>(errorMessage: String) : Resource<T>(message = errorMessage)
//
//    // We'll just pass object of this Loading
//    // class, just before making an api call
//    class Loading<T> : Resource<T>()
//}



    // we'll use this function in all
    // repos to handle api errors.
//    suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher,
//        apiToBeCalled: suspend () -> Response<T>): Resource<T> {
//
//        // Returning api response
//        // wrapped in Resource class
//        return withContext(dispatcher) {
//            try {
//
//                // Here we are calling api lambda
//                // function that will return response
//                // wrapped in Retrofit's Response class
//                val response: Response<T> = apiToBeCalled()
//
//                if (response.isSuccessful) {
//                    // In case of success response we
//                    // are returning Resource.Success object
//                    // by passing our data in it.
//                    Resource.Success(data = response.body()!!)
//                } else {
//                    // parsing api's own custom json error
//                    // response in ExampleErrorResponse pojo
////                    val errorResponse: ExampleErrorResponse? = convertErrorBody(response.errorBody())
//                    // Simply returning api's own failure message
//                    Resource.Error(errorMessage =  "Something went wrong")
//                }
//
//            } catch (e: HttpException) {
//                // Returning HttpException's message
//                // wrapped in Resource.Error
//                Resource.Error(errorMessage = e.message ?: "Something went wrong")
//            } catch (e: IOException) {
//                // Returning no internet message
//                // wrapped in Resource.Error
//                Resource.Error("Please check your network connection")
//            } catch (e: Exception) {
//                // Returning 'Something went wrong' in case
//                // of unknown error wrapped in Resource.Error
//                Resource.Error(errorMessage = "Something went wrong")
//            }
//        }
//    }

    // If you don't wanna handle api's own
    // custom error response then ignore this function


