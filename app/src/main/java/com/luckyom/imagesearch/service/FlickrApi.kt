package com.luckyom.imagesearch.service

import com.luckyom.imagesearch.model.ImageInfo
import com.luckyom.imagesearch.model.Images
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FlickrApi {


    @GET("services/rest")
    fun getPhotos(@Query("method") method:String,
                  @Query("api_key") apiKey:String,
                  @Query("text") searchTerm:String,
                  @Query("format") format:String,
                  @Query("nojsoncallback") callbackNum:Int): Call<ImageInfo>
}