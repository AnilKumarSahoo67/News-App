package com.example.samplenewsapp.ui.fragment

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.samplenewsapp.R
import com.example.samplenewsapp.ui.NewsActivity
import com.example.samplenewsapp.viewmodel.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class ArticleFragment : Fragment(R.layout.fragment_article) {

    lateinit var viewModel: MainViewModel
    lateinit var webView: WebView
    val args :ArticleFragmentArgs by navArgs()
    lateinit var fab : FloatingActionButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView = view.findViewById(R.id.webView)
        fab = view.findViewById(R.id.floatingAction)
        viewModel = (activity as NewsActivity).viewModel

        val article = args.article
        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }
        if (article.isSaved){
            fab.setImageResource(R.drawable.ic_save_filled)
        }

        fab.setOnClickListener{
            if (article.isSaved){
                viewModel.deleteArticle(article)
                Snackbar.make(view, "Removed Successfully", Snackbar.LENGTH_SHORT).show()
                article.isSaved = false
                fab.setImageResource(R.drawable.ic_save_border)
            }else {
                article.isSaved = true
                viewModel.saveArticle(article)
                Snackbar.make(view, "Article Saved Successfully", Snackbar.LENGTH_SHORT).show()
                fab.setImageResource(R.drawable.ic_save_filled)
            }
        }
    }
}