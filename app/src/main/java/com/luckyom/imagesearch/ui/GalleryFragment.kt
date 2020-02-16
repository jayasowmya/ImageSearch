package com.luckyom.imagesearch.ui

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
import com.luckyom.imagesearch.viewmodel.GalleryViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.gallery_fragment.*

const val SPAN_COUNT = 3

class GalleryFragment : Fragment() {
    private lateinit var photosAdapter: PhotoAdapter

    private lateinit var viewModel: GalleryViewModel
    private val disposable: CompositeDisposable = CompositeDisposable()

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
                disposable.add(viewModel.getPhotos(s.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this@GalleryFragment::handleResult)
                    { throwable -> showAlert(throwable) }
                )
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

    private fun handleResult(imageInfo: ImageInfo) {
        pg_bar.visibility = View.GONE
        viewModel.updateData(imageInfo)
        photosAdapter = PhotoAdapter(viewModel.getPhotos())
        photosAdapter.apply {
            setHasStableIds(true)
        }
        image_rcv.adapter = photosAdapter
    }

    private fun showAlert(throwable: Throwable) {
        pg_bar.visibility = View.GONE
        val builder: AlertDialog.Builder = AlertDialog.Builder(this.requireContext())
        builder.setTitle(getString(R.string.error_title))
            .setMessage(throwable.message)
            .setPositiveButton(getString(R.string.error_positive_title)) { _, _ -> }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

}
