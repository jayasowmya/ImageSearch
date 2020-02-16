package com.luckyom.imagesearch

import com.luckyom.imagesearch.model.ImageInfo
import com.luckyom.imagesearch.model.Images
import com.luckyom.imagesearch.model.Photo
import com.luckyom.imagesearch.viewmodel.GalleryViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Test

class GalleryViewModelTest {

    private val viewModel = GalleryViewModel()
    @Test
    fun verifyUpdateData() {
        val photos: List<Photo> = listOf(
            Photo("id1", "server1", "farm1", "secret1"),
            Photo("id2", "server2", "farm2", "secret2")
        )
        val imageInfo = ImageInfo(photos = Images(photos))
        viewModel.updateData(imageInfo)
        assertEquals(viewModel.getPhotos().size, 2)
        assertEquals(viewModel.getPhotos()[0].id, "id1")
        assertEquals(viewModel.getPhotos()[1].secret, "secret2")
    }
}