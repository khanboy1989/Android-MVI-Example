package com.serhankhan.mviexample.ui.main.state

import com.serhankhan.mviexample.model.BlogPost
import com.serhankhan.mviexample.model.User

data  class MainViewState (

    var blogPosts:List<BlogPost>? = null,
    var user:User? = null
)