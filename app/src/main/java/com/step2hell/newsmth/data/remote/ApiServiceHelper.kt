package com.step2hell.newsmth.data.remote

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class ApiServiceHelper private constructor() {

    private val retrofitMap = HashMap<String, Retrofit>()
    private val serviceMap = HashMap<String, Any>()

    fun <T> createService(baseUrl: String, service: Class<T>): T {
        var t = serviceMap[baseUrl.plus(service.name)] as T?
        if (t == null) {
            t = buildRetrofit(baseUrl).create(service)
            serviceMap[baseUrl.plus(service.name)] = t as Any
        }
        return t
    }

    private fun buildRetrofit(baseUrl: String): Retrofit {
        lateinit var retrofit: Retrofit
        if (retrofitMap.contains(baseUrl)) {
            retrofit = retrofitMap[baseUrl] as Retrofit
        } else {
            retrofit = Retrofit.Builder()
//                    .client()
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                            .setDateFormat("yyyy-MM-dd hh:mm:ss")
                            .setPrettyPrinting()
                            .create() // custom Gson
                    ))
                    .build()
            retrofitMap[baseUrl] = retrofit
        }
        return retrofit
    }

    companion object {
        val INSTANCE by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ApiServiceHelper()
        }
    }

}