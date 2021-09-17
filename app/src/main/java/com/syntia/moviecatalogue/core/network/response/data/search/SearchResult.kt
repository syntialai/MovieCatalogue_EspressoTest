package com.syntia.moviecatalogue.core.network.response.data.search

import com.google.gson.annotations.SerializedName

data class SearchResult(

    val adult: Boolean? = false,

    @SerializedName("first_air_date")
    val firstAirDate: String?,

    val id: Int,

    @SerializedName("known_for")
    val knownFor: List<KnownFor>?,

    @SerializedName("media_type")
    val mediaType: String,

    val name: String?,

    @SerializedName("poster_path")
    val posterPath: String?,

    @SerializedName("profile_path")
    val profilePath: String?,

    @SerializedName("release_date")

    val releaseDate: String?,

    val title: String?,

    @SerializedName("vote_average")
    val voteAverage: Double
)