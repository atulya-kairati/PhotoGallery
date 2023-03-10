package com.atulya.photogallery.core.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// The annotation will tell moshi to perform code generation during compilation
@JsonClass(generateAdapter = true)
data class GalleryItem (

    /**
     * Class represents individual photo json object
     * and only contains the attributes we need from
     * the JSON.
     */

    val id: String,
    val title: String,

    @Json(name = "url_s") // since the photo url is labeled as "url_s" in json
    val url: String
)