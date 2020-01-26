package com.serhankhan.mviexample.ui.main.state


sealed class MainStateEvent {

    class GetBlogPostsEvent:MainStateEvent()

    class GetUserEvent (val userId:String):MainStateEvent()

    //none for reset or restart
    class None:MainStateEvent()
}