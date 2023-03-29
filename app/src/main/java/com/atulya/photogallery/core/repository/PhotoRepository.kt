package com.atulya.photogallery.core.repository

import com.atulya.photogallery.core.api.FlickerApi
import com.atulya.photogallery.core.interceptor.PhotoInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

class PhotoRepository {
    suspend fun fetchPhotos(flickerApi: FlickerApi) = flickerApi.fetchPhotos().response.photoList

    suspend fun searchPhotos(query: String, flickerApi: FlickerApi) = flickerApi.searchPhotos(query).response.photoList
}