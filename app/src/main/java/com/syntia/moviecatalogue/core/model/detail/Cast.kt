package com.syntia.moviecatalogue.core.model.detail

import com.google.gson.annotations.SerializedName

data class Cast(

    val id: Int,

    val name: String,

    @SerializedName("profile_path")
    val profilePath: String? = null,

    val character: String,
)