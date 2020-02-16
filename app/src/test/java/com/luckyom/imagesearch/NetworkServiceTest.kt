package com.luckyom.imagesearch

import com.luckyom.imagesearch.service.FlickrApi
import com.luckyom.imagesearch.viewmodel.*
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class NetworkServiceTest {
    private val mockWebServer = MockWebServer()
    private lateinit var retrofit: Retrofit
    private lateinit var exp: FlickrApi
    private val invalidApiKey = "dummyapikey"

    @Before
    fun setup() {
        retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url(BASE_URL))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        mockWebServer.enqueue(MockResponse())
        exp = retrofit.create(FlickrApi::class.java)
    }

    @Test
    fun validateForEmptyInputParams() {
        val errorResp = exp.fetchPhotos(
            "",
            "",
            "",
            "",
            0

        )
        val testObserver = errorResp.test()
        testObserver.assertNoValues()
        testObserver.assertNotComplete()
    }

    @Test
    fun validateSuccessResponse() {
        val testObserver = exp.fetchPhotos(
            METHOD,
            API_KEY,
            "test",
            FORMAT,
            CALLBACK_NUM
        ).test()
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
    }

    @Test
    fun validateErrorResponse() {
        val testObserver = exp.fetchPhotos(
            METHOD,
            invalidApiKey,
            "test",
            FORMAT,
            CALLBACK_NUM
        ).test()
        testObserver.isCancelled
        testObserver.assertComplete()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}