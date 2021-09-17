package com.syntia.moviecatalogue.core.network.response.error

import com.google.gson.annotations.SerializedName

data class ErrorResponse(

    @SerializedName("status_message")
    val message: String
)
