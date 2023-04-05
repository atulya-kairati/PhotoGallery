package com.atulya.photogallery.core.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FlickerResponse(
    @Json(name = "photos") val response: PhotoResponse
)
