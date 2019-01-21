package com.onemt.adid;

import android.os.IBinder;
import android.os.IInterface;

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2019/1/21 14:25
 * @see
 */
public abstract class zzf extends zzb implements zze {
    public static zze getInterface(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
        return queryLocalInterface instanceof zze ? (zze) queryLocalInterface : new zzg(iBinder);
    }
}
