package com.sword.cats.data.api.favourites.models

import com.google.gson.annotations.SerializedName
import com.sword.cats.data.api.common.models.CatImageDto
import java.util.Date

data class CatFavouriteDto(
    val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("image_id") val imageId: String,
    @SerializedName("sub_id") val subId: String,
    @SerializedName("created_at") val createdAt: Date,
    val image: CatImageDto?
)