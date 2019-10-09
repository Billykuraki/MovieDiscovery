package com.billyhsieh.moviediscovery.movies.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.billyhsieh.moviediscovery.BuildConfig
import java.time.LocalDate

// TODO Move out key
private const val API_KEY = BuildConfig.tmdbapikey

const val TMDB_DICOVERY_PAGE_SIZE = 20

@RequiresApi(Build.VERSION_CODES.O)
fun getDefaultQueryOptions(): MutableMap<String, String> {

    // default period three months
    val now = LocalDate.now()
    val before = now.minusMonths(3)
    return mutableMapOf(
        "api_key" to API_KEY,
        "page" to "1",
        "language" to "en-US",
        "sort_by" to "release_date.desc",
        "include_adult" to "false",
        "include_video" to "false",
        "release_date.gte" to before.toString(),
        "release_date.lte" to now.toString()
    )
}