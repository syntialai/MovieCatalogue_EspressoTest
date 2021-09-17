package com.syntia.moviecatalogue.core.network.response.data.movie

import com.google.gson.annotations.SerializedName

data class Movie(

    val adult: Boolean,

    @SerializedName("backdrop_path")
    val backdropPath: String? = null,

    val id: Int,

    @SerializedName("poster_path")
    val posterPath: String? = null,

    @SerializedName("release_date")
    val releaseDate: String? = null,

    val title: String,

    @SerializedName("vote_average")
    val voteAverage: Double
)