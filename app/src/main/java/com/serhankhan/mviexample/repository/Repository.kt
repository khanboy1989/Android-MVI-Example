package com.serhankhan.mviexample.repository

import androidx.lifecycle.LiveData
import com.serhankhan.mviexample.api.MyRetrofitBuilder
import com.serhankhan.mviexample.model.BlogPost
import com.serhankhan.mviexample.model.User
import com.serhankhan.mviexample.ui.main.state.MainViewState
import com.serhankhan.mviexample.util.ApiSuccessResponse
import com.serhankhan.mviexample.util.DataState
import com.serhankhan.mviexample.util.GenericApiResponse


object Repository {


    fun getBlogPosts(): LiveData<DataState<MainViewState>> {
        return object :NetworkBoundResource<List<BlogPost>,MainViewState>(){
            override fun handleApiSuccessResponse(response: ApiSuccessResponse<List<BlogPost>>) {
                result.value = DataState.data(
                    data = MainViewState(
                        blogPosts = response.body
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<List<BlogPost>>> {
                return MyRetrofitBuilder.apiService.getBlogPosts()
            }

        }.asLiveData()
    }


    fun getUser(userId: String): LiveData<DataState<MainViewState>> {

        return object :NetworkBoundResource<User,MainViewState>(){
            override fun handleApiSuccessResponse(response: ApiSuccessResponse<User>) {
                result.value = DataState.data(
                    data = MainViewState(
                        user = response.body
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<User>> {
                return MyRetrofitBuilder.apiService.getUser(userId)
            }

        }.asLiveData()
    }
}