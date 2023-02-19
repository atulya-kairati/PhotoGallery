package com.atulya.photogallery.core.api

import com.atulya.photogallery.core.models.FlickerResponse
import retrofit2.http.GET
import retrofit2.http.Query

const val photoApiPath = "/services/rest/?method=flickr.interestingness.getList"
//const val photoApiPath = "/services/rest/?method=flickr.photos.search&api_key=$API_KEY&format=json&nojsoncallback=1&extras=url_s"
interface FlickerApi {
    /**
     * Retrofit will use this interface to
     * generate code for api calls
     */

    @GET(photoApiPath) // the string is addition to the base url we provide retrofit
    suspend fun fetchPhotos(
    ): FlickerResponse
}