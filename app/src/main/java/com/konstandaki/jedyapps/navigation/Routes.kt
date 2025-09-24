package com.konstandaki.jedyapps.navigation

import android.net.Uri
import com.konstandaki.jedyapps.domain.entity.Movie

object Routes {
    const val HOME = "home"
    const val DETAILS = "details?id={id}&title={title}&year={year}&type={type}&poster={poster}"

    fun detailsOf(movie: Movie): String =
        "details?id=${Uri.encode(movie.id)}" +
                "&title=${Uri.encode(movie.title)}" +
                "&year=${Uri.encode(movie.year)}" +
                "&type=${Uri.encode(movie.type)}" +
                "&poster=${Uri.encode(movie.poster ?: "")}"
}