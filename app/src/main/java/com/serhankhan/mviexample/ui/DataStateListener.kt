package com.serhankhan.mviexample.ui

import com.serhankhan.mviexample.util.DataState

interface DataStateListener {

    fun onDataStateChanged(dataState:DataState<*>?)
}