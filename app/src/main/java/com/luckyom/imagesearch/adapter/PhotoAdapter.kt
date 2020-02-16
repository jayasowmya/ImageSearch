package com.luckyom.imagesearch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luckyom.imagesearch.R
import com.luckyom.imagesearch.model.Photo
import com.luckyom.imagesearch.ui.ImageViewholder

class PhotoAdapter (private val newsList: MutableList<Photo>) :
    RecyclerView.Adapter<ImageViewholder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageViewholder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_layout, parent, false)
        return ImageViewholder(view)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: ImageViewholder, position: Int) {
        holder.bindViews(newsList[position])
    }

}

