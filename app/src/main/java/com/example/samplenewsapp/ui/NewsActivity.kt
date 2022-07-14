package com.example.samplenewsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.samplenewsapp.R
import com.example.samplenewsapp.databinding.ActivityNewsBinding
import com.example.samplenewsapp.room.ArticleDatabase
import com.example.samplenewsapp.viewmodel.MainViewModel
import com.example.samplenewsapp.viewmodel.NewsViewModelProviderFactory
import com.example.samplenewsapp.viewmodel.NewsRepository

class NewsActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityNewsBinding
    lateinit var viewModel : MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityNewsBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        val navHostFragment= supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController= navHostFragment.navController
        mainBinding.bottomNavView.setupWithNavController(navController)

        val repository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(repository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(MainViewModel::class.java)

    }
}