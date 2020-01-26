package com.serhankhan.mviexample.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.serhankhan.mviexample.model.BlogPost
import com.serhankhan.mviexample.model.User
import com.serhankhan.mviexample.repository.Repository
import com.serhankhan.mviexample.ui.main.state.MainStateEvent
import com.serhankhan.mviexample.ui.main.state.MainStateEvent.*
import com.serhankhan.mviexample.ui.main.state.MainViewState
import com.serhankhan.mviexample.util.AbsentLiveData
import com.serhankhan.mviexample.util.DataState

class MainViewModel : ViewModel() {

    private val _stateEvent: MutableLiveData<MainStateEvent> = MutableLiveData()
    private val _viewState: MutableLiveData<MainViewState> = MutableLiveData()

    //The google sample structure
    val viewState: LiveData<MainViewState>
        get() = _viewState

    //State event are responsible to trigger the different actions
    //such as posting data, getting data, etc.
    val dataState: LiveData<DataState<MainViewState>> = Transformations
        .switchMap(_stateEvent) { stateEvent ->
            stateEvent?.let {
                handleStateEvent(it)
            }
        }


    //Handle state event is fired depending on stateEvent changes
    fun handleStateEvent(stateEvent: MainStateEvent): LiveData<DataState<MainViewState>> {
        println("DEBUG: New StateEvent detected:$stateEvent")
        when (stateEvent) {
            is GetBlogPostsEvent -> {
                return Repository.getBlogPosts()
            }

            is GetUserEvent -> {
                return Repository.getUser(stateEvent.userId)
            }

            is None -> {
                return AbsentLiveData.create()
            }
        }
    }

    fun setBlogListData(blogPosts:List<BlogPost>){
        val update = getCurrentViewStateorNew()
        update.blogPosts = blogPosts
        _viewState.value = update
    }


    fun setUser(user:User){
        val update = getCurrentViewStateorNew()
        update.user = user
        _viewState.value = update
    }

    fun getCurrentViewStateorNew():MainViewState{
        val value = viewState.value?.let {
            it
        }?: MainViewState()
        return value
    }

    fun setStateEvent(event:MainStateEvent){
        _stateEvent.value = event
    }
}
