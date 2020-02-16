package com.luckyom.imagesearch.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.luckyom.imagesearch.model.Photo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_layout.view.*

class ImageViewholder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bindViews(photo: Photo) {
        val imageUrl =
            "http://farm${photo.farm}.static.flickr.com/${photo.server}/${photo.id}_${photo.secret}.jpg"
        Picasso.get()
            .load(imageUrl)
            .into(view.img_view)
    }

}