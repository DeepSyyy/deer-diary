package com.example.deerdiary.ui.detailScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.deerdiary.ViewModelFactory
import com.example.deerdiary.databinding.ActivityDetailBinding
import com.example.deerdiary.utils.GlideBindingUtil

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(this) }

    private val detailViewModel: DetailViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpObserver()
        setupAppBar()
        callEvent()
    }

    private fun callEvent(){
        Log.d("GetStories", "callEvent: ")
        detailViewModel.processEvent(DetailEvent.getStory(intent.getStringExtra(EXTRA_ID).toString(), this))
    }

    private fun setUpObserver() {
        detailViewModel.story.observe(this) { _ ->
            setDetailStory()
        }
        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.isEmpty.observe(this) {
            showEmpty(it)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDetailStory() {
        binding.apply {
            GlideBindingUtil.setImageUrl(ivDetail, detailViewModel.story.value?.photoUrl.toString())
            tvPenulisDetail.text = "Penulis: ${detailViewModel.story.value?.name}"
            tvDeskripsiDetail.text = detailViewModel.story.value?.description
        }
    }

    private fun setupAppBar() {
        binding.apply {
            toolbar.toolbar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun showLoading(it: Boolean) {
        binding.apply {
            if (it) {
                pbDetail.visibility = View.VISIBLE
                ivDetail.visibility = View.GONE
                tvPenulisDetail.visibility = View.GONE
                tvDeskripsiDetail.visibility = View.GONE
                tvTitleDetail.visibility = View.GONE
                tvTitleDeskripsi.visibility = View.GONE
                tvEmpty.visibility = View.GONE
                ivEmptyDetail.visibility = View.GONE
            } else {
                pbDetail.visibility = View.GONE
                ivDetail.visibility = View.VISIBLE
                tvPenulisDetail.visibility = View.VISIBLE
                tvDeskripsiDetail.visibility = View.VISIBLE
                tvTitleDetail.visibility = View.VISIBLE
                tvTitleDeskripsi.visibility = View.VISIBLE
                tvEmpty.visibility = View.GONE
                ivEmptyDetail.visibility = View.GONE
            }
        }
    }

    private fun showEmpty(it: Boolean) {
        binding.apply {
            if (it) {
                binding.apply {
                    pbDetail.visibility = View.GONE
                    ivDetail.visibility = View.GONE
                    tvPenulisDetail.visibility = View.GONE
                    tvDeskripsiDetail.visibility = View.GONE
                    tvTitleDetail.visibility = View.GONE
                    tvTitleDeskripsi.visibility = View.GONE
                    tvEmpty.visibility = View.VISIBLE
                    ivEmptyDetail.visibility = View.VISIBLE
                }
            }
        }
    }


    companion object {
        const val EXTRA_ID = "id"
    }
}