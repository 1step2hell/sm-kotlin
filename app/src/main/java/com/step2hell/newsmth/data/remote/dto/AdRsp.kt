package com.step2hell.newsmth.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AdRsp(
        @SerializedName("url") val article: String,
        @SerializedName("file") val image: String
) {
    val wholeImageUrl: String
        get() = "http://images.newsmth.net/nForum".plus(image)
}