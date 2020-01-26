package com.serhankhan.mviexample.ui.main

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.serhankhan.mviexample.R
import com.serhankhan.mviexample.model.BlogPost
import com.serhankhan.mviexample.model.User
import com.serhankhan.mviexample.ui.DataStateListener
import com.serhankhan.mviexample.ui.main.state.MainStateEvent
import com.serhankhan.mviexample.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_main.*
import java.lang.ClassCastException

class MainFragment : Fragment(),BlogListAdapter.Interaction {



    lateinit var viewModel: MainViewModel

    lateinit var dataStateHandler:DataStateListener

    lateinit var blogListAdapter: BlogListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = activity?.run {
            ViewModelProvider(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        subscribeObservers()
        initRecyclerView()
    }

    private  fun initRecyclerView(){
        recycler_view.apply {
           layoutManager = LinearLayoutManager(activity)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            blogListAdapter = BlogListAdapter(this@MainFragment)
            adapter = blogListAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_get_user -> triggerGetUserEvent()
            R.id.action_get_blogs -> triggerGetBlogsEvent()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun triggerGetUserEvent() {
        viewModel.setStateEvent(MainStateEvent.GetUserEvent("1"))
    }

    private fun triggerGetBlogsEvent() {
        viewModel.setStateEvent(MainStateEvent.GetBlogPostsEvent())
    }


    private fun setUserProperties(user: User){
        email.text = user.email
        username.text = user.username

        view?.let{
            Glide.with(it.context).load(user.image).into(image)
        }
    }

    //subscribe by using viewLifecycleOwner
    //fragment can be destroyed or recreated
    fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { datastate ->
            println("DEBUG: DataState: ${datastate}")

            //handle loading and message
            dataStateHandler.onDataStateChanged(datastate)

            //Handle Data<T>
            datastate.data?.let {event ->

                event.getContentIfNotHandled()?.let {mainViewState->
                    println("DEBUG: DataState: ${mainViewState} ")

                    mainViewState.blogPosts?.let { blogPosts ->
                        //set BlogPost data
                        viewModel.setBlogListData(blogPosts)
                    }

                    mainViewState.user?.let { user ->
                        //set User data
                        viewModel.setUser(user)
                    }
                }
            }

        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

            viewState.blogPosts?.let {list->
                println("DEBUG: Setting blog posts to RecyclerView: ${list}")
                blogListAdapter.submitList(list)
            }

            viewState.user?.let {
                println("DEBUG: Setting user data: ${it}")
                setUserProperties(it)
            }

        })


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dataStateHandler = context as DataStateListener
        }catch (e:ClassCastException){
            println("DEBUG: $context must implement DataStateListener")
        }
    }


    override fun onItemSelected(position: Int, item: BlogPost) {
        println("DEBUG: CLICKED $position")
        println("DEBUG: CLICKED $item")

    }
}