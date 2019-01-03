package com.onemt.h5boost.preload

import retrofit2.http.GET

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/21 16:35
 * @see
 */
interface MyService {

    @GET
    fun request(): String
}