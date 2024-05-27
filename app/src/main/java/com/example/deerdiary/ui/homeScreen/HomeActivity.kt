package com.example.deerdiary.ui.homeScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Display
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.hardware.display.DisplayManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.deerdiary.ViewModelFactory
import com.example.deerdiary.adapter.ListStoryAdapter
import com.example.deerdiary.databinding.ActivityHomeBinding
import com.example.deerdiary.ui.detailScreen.DetailActivity
import com.example.deerdiary.ui.homeScreen.model.StoryModel

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val listStoryAdapter: ListStoryAdapter by lazy { ListStoryAdapter() }

    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(this) }

    private val homeViewModel: HomeViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupAdapter()
        setUpObserver()
        callEvent()
    }

    private fun setupAdapter(){
        val defaultDisplay = DisplayManagerCompat.getInstance(this).getDisplay(Display.DEFAULT_DISPLAY)
        val displayContext = createDisplayContext(defaultDisplay!!)
        val screenWidth = displayContext.resources.displayMetrics.widthPixels
        val screenDensity = resources.displayMetrics.density
        val itemWidth = (152 * screenDensity).toInt()

        binding.apply {
            rvHome.layoutManager = GridLayoutManager(
                root.context,
                Integer.max(
                    2,
                    screenWidth / itemWidth
                )
            )
            rvHome.adapter = listStoryAdapter

            listStoryAdapter.setOnItemClickListener(object :ListStoryAdapter.OnItemClickListener{
                override fun onItemClick(item: StoryModel) {
                    val intent = Intent(this@HomeActivity, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_ID, item.id)
                    startActivity(intent)
                }
            })
        }
    }

    private fun callEvent(){
        Log.d("GetStories", "callEvent: ")
        homeViewModel.processEvent(HomeEvent.ListStory(this))
    }

    private fun setUpObserver(){
        homeViewModel.listStory.observe(this){ story ->
            listStoryAdapter.submitList(story)
        }
        homeViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        homeViewModel.isEmpty.observe(this) {
            showEmpty(it)
        }
    }

    private fun showLoading(it: Boolean) {
        binding.apply {
            if (it) {
                rvHome.visibility = android.view.View.GONE
                pbHome.visibility = android.view.View.VISIBLE
            } else {
                rvHome.visibility = android.view.View.VISIBLE
                pbHome.visibility = android.view.View.GONE
            }
        }
    }

    private fun showEmpty(it: Boolean) {
        binding.apply {
            if (it) {
                rvHome.visibility = android.view.View.GONE
                tvEmpty.visibility = android.view.View.VISIBLE
                ivEmpty.visibility = android.view.View.VISIBLE
            } else {
                rvHome.visibility = android.view.View.VISIBLE
                tvEmpty.visibility = android.view.View.GONE
                ivEmpty.visibility = android.view.View.GONE
            }
        }
    }
}