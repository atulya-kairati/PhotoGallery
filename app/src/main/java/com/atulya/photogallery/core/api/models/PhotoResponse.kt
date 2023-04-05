package com.atulya.photogallery.core.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoResponse(
    @Json(name = "photo")
    val photoList: List<GalleryItem>
)
