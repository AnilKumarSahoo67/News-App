package com.example.samplenewsapp.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.samplenewsapp.R
import com.example.samplenewsapp.adapter.NewsAdapter
import com.example.samplenewsapp.adapter.NowPlayingMovieAdapter
import com.example.samplenewsapp.api.Resource
import com.example.samplenewsapp.model.Result
import com.example.samplenewsapp.ui.NewsActivity
import com.example.samplenewsapp.utils.Constants
import com.example.samplenewsapp.viewmodel.MainViewModel
import com.example.samplenewsapp.viewmodel.NewsRepository

class MovieFragment : Fragment(R.layout.fragment_movie) {
    lateinit var viewModel: MainViewModel
    lateinit var nowPlayingMovieAdapter: NowPlayingMovieAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        recyclerView = view.findViewById(R.id.movieRecycler)
        progressBar = view.findViewById(R.id.paginationProgress)

        viewModel.getNowPlayingMovieApi()

        setUpRecyclerView()

        viewModel.mutableLiveDataNowPlaying.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgress()
                    val movieList = response.data?.results
                    movieList?.let {
                        nowPlayingMovieAdapter.differ.submitList(movieList)
                        val totalPages = it.size/ Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.moviePageNumber == totalPages
                    }
                }

                is Resource.Failure -> {
                    hideProgress()
                    response.message?.let {
                        Toast.makeText(context as NewsActivity, it, Toast.LENGTH_LONG).show()
                    }
                }

                is Resource.Loading -> {
                    showProgress()
                }

            }
        })
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    var scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE

            val shouldPaginate =
                isNotLoadingNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible
                        && isScrolling

            if (shouldPaginate) {
                viewModel.getNowPlayingMovieApi()
                isScrolling = false
            } else {
                recyclerView.setPadding(0, 0, 0, 0)
            }
        }
    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun setUpRecyclerView() {
        nowPlayingMovieAdapter = NowPlayingMovieAdapter()
        recyclerView.apply {
            adapter = nowPlayingMovieAdapter
            addOnScrollListener(this@MovieFragment.scrollListener)
        }
    }
}