package com.syntia.moviecatalogue.core.network.response.data.trending

import com.google.gson.annotations.SerializedName

data class TrendingItem(

    val adult: Boolean? = false,

    @SerializedName("first_air_date")
    val firstAirDate: String?,

    val id: Int,

    @SerializedName("media_type")
    val mediaType: String,

    val name: String?,

    @SerializedName("poster_path")
    val posterPath: String? = null,

    @SerializedName("release_date")
    val releaseDate: String?,

    val title: String?,

    @SerializedName("vote_average")
    val voteAverage: Double
)