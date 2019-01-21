package com.onemt.adid;

import android.os.IInterface;
import android.os.RemoteException;

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2019/1/21 14:18
 * @see
 */
public interface zze extends IInterface {
    String getId() throws RemoteException;

    boolean zzb(boolean z) throws RemoteException;

    boolean zzc() throws RemoteException;
}
