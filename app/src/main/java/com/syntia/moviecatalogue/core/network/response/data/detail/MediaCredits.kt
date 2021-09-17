package com.syntia.moviecatalogue.core.network.response.data.detail

import com.syntia.moviecatalogue.core.model.detail.Cast

data class MediaCredits(

    val id: Int,

    val cast: List<Cast>)
