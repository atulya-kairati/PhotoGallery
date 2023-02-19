package com.atulya.photogallery.core.api

import com.atulya.photogallery.core.models.FlickerResponse
import retrofit2.http.GET
import retrofit2.http.Query

const val interestingPhotoApiUrl = "/services/rest/?method=flickr.interestingness.getList"
const val searchPhotoApiUrl = "/services/rest/?method=flickr.photos.search"
interface FlickerApi {
    /**
     * Retrofit will use this interface to
     * generate code for api calls
     */

    @GET(interestingPhotoApiUrl) // the string is addition to the base url we provide retrofit
    suspend fun fetchPhotos(): FlickerResponse

    @GET(searchPhotoApiUrl)
    suspend fun searchPhotos(
        @Query("text") query: String
    ): FlickerResponse
}