package com.onemt.h5boost.preload

import retrofit2.Retrofit

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/21 16:36
 * @see
 */
object HttpClient {

    fun get(url: String): MyService {
        val service = Retrofit.Builder().baseUrl(url).build()
        return service.create(MyService::class.java)
    }
}