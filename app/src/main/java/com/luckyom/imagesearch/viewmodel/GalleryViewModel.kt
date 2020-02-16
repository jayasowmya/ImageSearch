package com.luckyom.imagesearch.viewmodel

import androidx.lifecycle.ViewModel
import com.luckyom.imagesearch.model.ImageInfo
import com.luckyom.imagesearch.model.Photo
import com.luckyom.imagesearch.service.FlickrApi
import com.luckyom.imagesearch.service.NetworkModule
import io.reactivex.Observable

const val BASE_URL = "https://api.flickr.com"
const val API_KEY = "96358825614a5d3b1a1c3fd87fca2b47"

const val METHOD = "flickr.photos.search"
const val FORMAT = "json"
const val CALLBACK_NUM = 1

class GalleryViewModel : ViewModel() {
    private val photos = ArrayList<Photo>()

    fun getPhotos(term: String): Observable<ImageInfo> {
        return NetworkModule.createService(FlickrApi::class.java, BASE_URL)
            .fetchPhotos(METHOD, API_KEY, term, FORMAT, CALLBACK_NUM)
    }

    fun updateData(imageInfo: ImageInfo) {
        photos.clear()
        val photoList = imageInfo.photos.photo
        for (item in photoList) {
            photos.add(item)
        }
    }

    fun getPhotos(): MutableList<Photo> {
        return photos
    }
}
