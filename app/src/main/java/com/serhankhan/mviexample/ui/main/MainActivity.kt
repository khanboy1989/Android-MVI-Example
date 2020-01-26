package com.serhankhan.mviexample.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.serhankhan.mviexample.R
import com.serhankhan.mviexample.ui.DataStateListener
import com.serhankhan.mviexample.util.DataState

class MainActivity : AppCompatActivity(), DataStateListener {


    lateinit var viewModel: MainViewModel
    lateinit var progressBar:ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        showMainFragment()
        initViewData()
    }

    fun showMainFragment() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                MainFragment(), "MainFragment"
            )
            .commit()
    }

    fun initViewData(){
        progressBar = findViewById(R.id.progress_bar)
    }

    override fun onDataStateChanged(dataState: DataState<*>?) {
        handleDataStateChange(dataState)
    }

    private fun handleDataStateChange(dataState: DataState<*>?) {
        dataState?.let {

            //Handle loading
            it.loading?.let {loading->
                showProgressBar(dataState.loading)
            }

            //Handle message
            it.message?.let { event ->
                event.getContentIfNotHandled()?.let{message->
                    showToast(message)
                }
            }
        }
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun showProgressBar(isVisible:Boolean){
        if (isVisible){
            progressBar.visibility = View.VISIBLE
        }else {
            progressBar.visibility = View.GONE
        }
    }


}
