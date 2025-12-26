package com.sword.cats.data.api.models

import com.google.gson.annotations.SerializedName

data class FavouriteApiRequest(
    @SerializedName("image_id") val imageId: String,
    @SerializedName("sub_id") val subId: String
)