package com.syntia.moviecatalogue.core.network.response.data.detail

import com.google.gson.annotations.SerializedName
import com.syntia.moviecatalogue.core.model.detail.Genre

data class Detail(

    val id: Int,

    @SerializedName("poster_path")
    val posterPath: String? = null,

    val name: String? = null,

    val title: String? = null,

    @SerializedName("release_date")
    val releaseDate: String? = null,

    @SerializedName("first_air_date")
    val firstAirDate: String? = null,

    @SerializedName("original_language")
    val originalLanguage: String,

    val runtime: Int? = null,

    @SerializedName("number_of_episodes")
    val numberOfEpisodes: Int? = null,

    @SerializedName("vote_average")
    val voteAverage: Double,

    val adult: Boolean? = false,

    val genres: List<Genre>,

    val overview: String?
)
