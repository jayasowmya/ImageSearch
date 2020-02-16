package com.luckyom.imagesearch.service

import com.luckyom.imagesearch.model.ImageInfo
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {

    @GET("services/rest")
    fun fetchPhotos(
        @Query("method") method: String,
        @Query("api_key") apiKey: String,
        @Query("text") searchTerm: String,
        @Query("format") format: String,
        @Query("nojsoncallback") callbackNum: Int
    ): Observable<ImageInfo>
}