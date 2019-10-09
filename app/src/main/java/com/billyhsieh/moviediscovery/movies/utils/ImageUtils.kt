package com.billyhsieh.moviediscovery.movies.utils

import android.widget.ImageView
import com.billyhsieh.moviediscovery.R
import com.bumptech.glide.Glide

fun loadImage(view: ImageView, url: String?) {
    Glide.with(view)
        .load(
            if (url.isNullOrEmpty())
                // load image holder
                R.drawable.ic_broken_image
            else url
        )
        .into(view)
}