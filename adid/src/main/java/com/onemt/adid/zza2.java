package com.onemt.adid;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2019/1/21 14:28
 * @see
 */
public class zza2 implements IInterface {
    private final IBinder zza;
    private final String zzb;

    protected zza2(IBinder var1, String var2) {
        this.zza = var1;
        this.zzb = var2;
    }

    @Override
    public IBinder asBinder() {
        return this.zza;
    }

    protected final Parcel obtainAndWriteInterfaceToken() {
        Parcel var1;
        (var1 = Parcel.obtain()).writeInterfaceToken(this.zzb);
        return var1;
    }

    protected final Parcel transactAndReadException(int var1, Parcel var2) throws RemoteException {
        Parcel var3 = Parcel.obtain();

        try {
            this.zza.transact(var1, var2, var3, 0);
            var3.readException();
        } catch (RuntimeException var8) {
            var3.recycle();
            throw var8;
        } finally {
            var2.recycle();
        }

        return var3;
    }
}
