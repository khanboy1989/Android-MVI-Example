package com.serhankhan.mviexample.api

import androidx.lifecycle.LiveData
import com.serhankhan.mviexample.model.BlogPost
import com.serhankhan.mviexample.model.User
import com.serhankhan.mviexample.util.GenericApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("placeholder/user/{userId}")
    fun getUser(@Path("userId") userId:String):LiveData<GenericApiResponse<User>>


    @GET("placeholder/blogs")
    fun getBlogPosts():LiveData<GenericApiResponse<List<BlogPost>>>
}