package com.onemt.h5boost.preload

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/22 22:04
 * @see
 */
class DefaultConnFactory: ConnectionFactory {

    override fun getConn(session: Session): AbsConnection {
        return HttpUrlConnectionImpl(session)
    }
}