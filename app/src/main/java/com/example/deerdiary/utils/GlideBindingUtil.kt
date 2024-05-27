package com.example.deerdiary.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

class GlideBindingUtil {
    companion object{
        fun setImageUrl(imageView: ImageView, url: String){
            Glide.with(imageView.context)
                .load(url)
                .into(imageView)
        }
    }
}