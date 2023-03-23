package com.atulya.photogallery.core.singletons

import com.atulya.photogallery.core.api.FlickerApi
import com.atulya.photogallery.core.interceptor.PhotoInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

class FlickerApiSingleton {


    companion object{

        var INSTANCE: FlickerApi? = null

        fun init(){
            /**
             * Retrofit's default response type is [okhttp3.ResponseBody],
             * To deserialize it into usable data we need to use
             * converter to change it into desired type.
             *
             * Ex: we can specify a converter such as
             * [ScalarsConverterFactory.create], which among other
             * type will let retrofit to deserialize data to string
             * Or
             * [MoshiConverterFactory.create] to convert into kotlin
             * classes.
             */

            // Creating a client to add interceptor
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(PhotoInterceptor())
                .build()

            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("https://api.flickr.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .client(okHttpClient)
                .build()

            /**
             *  When we call `retrofit.create`, retrofit
             *  generate code using the interface we passed to it
             *  (here it is inferred).
             *  It generates an Anonymous class containing
             *  all the methods we need.
             *
             *  This code generation happens at runtime.
             */
            INSTANCE = retrofit.create() // inferred that FlickerApi interface should be used
        }

        fun get() = checkNotNull(INSTANCE) {
            "Initialize the FLickerApiSingleton instance first"
        }
    }
}