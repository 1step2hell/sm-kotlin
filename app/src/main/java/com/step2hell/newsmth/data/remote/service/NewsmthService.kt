package com.step2hell.newsmth.data.remote.service

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET

interface NewsmthService {

    companion object {
        const val BASE_URL = "http://www.newsmth.net/"
    }

    @GET("/")
    fun getIndexHtml(): Observable<ResponseBody>

}
