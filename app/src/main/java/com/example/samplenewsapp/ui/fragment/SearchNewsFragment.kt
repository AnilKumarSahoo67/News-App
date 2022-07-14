package com.example.samplenewsapp.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.samplenewsapp.R
import com.example.samplenewsapp.adapter.NewsAdapter
import com.example.samplenewsapp.api.Resource
import com.example.samplenewsapp.ui.NewsActivity
import com.example.samplenewsapp.viewmodel.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    lateinit var viewModel: MainViewModel
    lateinit var progressBar: ProgressBar
    lateinit var recyclerView: RecyclerView
    lateinit var newsAdapter: NewsAdapter
    private val TAG ="SearchFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewSearchNews)
        progressBar = view.findViewById<ProgressBar>(R.id.paginationProgress)
        viewModel = (activity as NewsActivity).viewModel

        var txtSearch = view.findViewById<EditText>(R.id.txtSearchNews)
        setUpRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment2,
                bundle
            )
        }

        var job:Job?=null
        txtSearch.addTextChangedListener { editable ->
            job?.cancel()
            job= MainScope().launch {
                delay(500L)
                if (editable.toString().isNotEmpty())
                    viewModel.getSearchNews(editable.toString())
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer {response ->
            when(response){
                is Resource.Success->{
                    hideProgress()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Failure->{
                    hideProgress()
                    response.message?.let {message ->
                        Log.e(TAG, "An error occured :$message" )

                    }
                }
                is Resource.Loading->{
                    showProgress()
                }
            }
        })
    }
    private fun hideProgress(){
        progressBar.visibility = View.INVISIBLE
    }
    private fun showProgress(){
        progressBar.visibility = View.VISIBLE
    }
    private fun setUpRecyclerView(){
        newsAdapter = NewsAdapter()
        recyclerView.apply {
            adapter = newsAdapter
        }
    }
}