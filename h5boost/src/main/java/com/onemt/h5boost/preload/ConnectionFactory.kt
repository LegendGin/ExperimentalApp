package com.onemt.h5boost.preload

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/22 22:03
 * @see
 */
interface ConnectionFactory {
    fun getConn(session: Session): AbsConnection
}