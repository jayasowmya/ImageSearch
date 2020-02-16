package com.luckyom.imagesearch.ui

import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.luckyom.imagesearch.R
import com.luckyom.imagesearch.adapter.PhotoAdapter
import com.luckyom.imagesearch.model.ImageInfo
import com.luckyom.imagesearch.model.Photo
import com.luckyom.imagesearch.service.FlickrApi
import com.luckyom.imagesearch.service.NetworkModule
import com.luckyom.imagesearch.viewmodel.GalleryViewModel
import kotlinx.android.synthetic.main.gallery_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


const val BASE_URL = "https://api.flickr.com"
const val API_KEY = "96358825614a5d3b1a1c3fd87fca2b47"

const val METHOD = "flickr.photos.search"
const val FORMAT = "json"
const val CALLBACK_NUM = 1
const val SPAN_COUNT = 3

class GalleryFragment : Fragment() {
    private lateinit var photosAdapter: PhotoAdapter

    private lateinit var viewModel: GalleryViewModel
    private var searchTerm: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.gallery_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        et_search_term.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                searchTerm = s.toString()
                getPhotosFromApi()
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) = Unit

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) = Unit
        })

        val gridLayoutManager =
            GridLayoutManager(this.requireContext(), SPAN_COUNT)

        image_rcv.apply {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
        }
    }

    private fun getPhotosFromApi() {
        pg_bar.visibility = View.VISIBLE
        val responseList =
            searchTerm?.let {
                NetworkModule.createService(FlickrApi::class.java, BASE_URL)
                    .getPhotos(METHOD, API_KEY, it, FORMAT, CALLBACK_NUM)
            }
        responseList?.enqueue(object : Callback<ImageInfo> {
            override fun onResponse(call: Call<ImageInfo>, response: Response<ImageInfo>) {
                if (response.isSuccessful) {
                    handleResult(response)
                } else {
                    showAlert(response.message())
                }
            }

            override fun onFailure(call: Call<ImageInfo>, throwable: Throwable) {
                throwable.message?.let { showAlert(it) }
            }
        })
    }

    private fun handleResult(response: Response<ImageInfo>) {
        pg_bar.visibility = View.GONE
        val photos = ArrayList<Photo>()
        val photoList = response.body()?.photos?.photo
        if (photoList != null) {
            for (item in photoList) {
                photos.add(item)
            }
            photosAdapter = PhotoAdapter(photos)
            photosAdapter.apply {
                setHasStableIds(true)
            }
            image_rcv.adapter = photosAdapter
            viewModel.hideKeyboard(this.requireActivity())
        }
    }

    private fun showAlert(message: String) {
        pg_bar.visibility = View.GONE
        val builder: AlertDialog.Builder = AlertDialog.Builder(this.requireContext())
        builder.setTitle(getString(R.string.error_title))
            .setMessage(message)
            .setPositiveButton(getString(R.string.error_positive_title)) { _, _ -> }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

}
