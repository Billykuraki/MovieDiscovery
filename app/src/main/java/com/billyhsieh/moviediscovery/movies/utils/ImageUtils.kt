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

private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

fun getPosterUrl(posterPath: String?): String? {
    return if (posterPath.isNullOrEmpty()) {
        null
    } else {
        "$IMAGE_BASE_URL/${posterPath}"
    }
}