package com.billyhsieh.moviediscovery.movies.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

private const val API_KEY = "64b6f3a69e5717b13ed8a56fe4417e71"

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