package com.serhankhan.mviexample.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.serhankhan.mviexample.util.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


abstract class NetworkBoundResource<ResponseObject,ViewStateType>{

    //The reason why MediatoLive data is used to manipulate the data in
    protected val result = MediatorLiveData<DataState<ViewStateType>>()


    init{
        result.value = DataState.loading(isLoading = true)
        GlobalScope.launch(IO){

            withContext(Main){
               val apiResponse = createCall()
               //Access the live data value manipulate the result
               result.addSource(apiResponse){response->
                   result.removeSource(apiResponse)
                   handleNetworkCall(response)
               }
            }
        }
    }


    fun handleNetworkCall(response:GenericApiResponse<ResponseObject>){
        when(response){
            is ApiSuccessResponse-> {
                handleApiSuccessResponse(response)
            }

            is ApiErrorResponse->{
               println("DEBUG: NetworkBoundResource: ${response.errorMessage}")
               onReturnError(response.errorMessage)
            }

            is ApiEmptyResponse->{
                println("DEBUG: NetworkBoundResource: HTTP 204. Returned NOTHING.")
                onReturnError("HTTP 204. Returned NOTHING.")
            }
        }
    }

    fun onReturnError(message:String){
        result.value = DataState.error(message)
    }

    abstract fun handleApiSuccessResponse(response:ApiSuccessResponse<ResponseObject>)

    abstract fun createCall():LiveData<GenericApiResponse<ResponseObject>>

    fun asLiveData() = result as LiveData<DataState<ViewStateType>>

}