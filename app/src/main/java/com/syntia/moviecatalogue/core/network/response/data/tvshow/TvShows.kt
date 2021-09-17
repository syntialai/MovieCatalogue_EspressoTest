package com.syntia.moviecatalogue.core.network.response.data.tvshow

import com.google.gson.annotations.SerializedName

data class TvShows(

    @SerializedName("backdrop_path")
    val backdropPath: String? = null,

    val id: Int,

    val name: String,

    @SerializedName("poster_path")
    val posterPath: String? = null,

    @SerializedName("first_air_date")
    val firstAirDate: String? = null,

    @SerializedName("vote_average")
    val voteAverage: Double
)