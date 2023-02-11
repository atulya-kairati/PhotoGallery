package com.atulya.photogallery.core.api

import retrofit2.http.GET


interface FlickerApi {
    /**
     * Retrofit will use this interface to
     * generate code for api calls
     */

    @GET("/") // the string is addition to the base url we provide retrofit
    suspend fun fetchContents(): String
}