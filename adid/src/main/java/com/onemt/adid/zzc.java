package com.onemt.adid;

import android.os.Parcel;

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2019/1/21 14:29
 * @see
 */
public class zzc {
    private static final ClassLoader zzd = com.google.android.gms.internal.ads_identifier.zzc.class.getClassLoader();

    private zzc() {
    }

    public static boolean zza(Parcel var0) {
        return var0.readInt() != 0;
    }

    public static void zza(Parcel var0, boolean var1) {
        var0.writeInt(1);
    }
}
