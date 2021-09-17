package com.syntia.moviecatalogue.core.model.detail

data class DetailUiModel(

    val id: Int,

    val image: String,

    val title: String,

    val releaseOrFirstAirDate: String,

    val language: String,

    val runtime: String?,

    val episodeCount: Int,

    val rating: Pair<String, Float>,

    val isAdult: Boolean = false,

    val genres: List<Genre>,

    val overview: String?
)
