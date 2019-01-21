package com.onemt.adid;

import android.net.Uri;
import android.util.Log;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.zzc;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2019/1/21 14:12
 * @see
 */
final class zza extends Thread {
    private final Map zzl;

    zza(AdClient var1, Map var2) {
        this.zzl = var2;
    }

    @Override
    public final void run() {
        new zzc();
        Map var1 = this.zzl;
        Uri.Builder var2 = Uri.parse("https://pagead2.googlesyndication.com/pagead/gen_204?id=gmob-apps").buildUpon();
        Iterator var3 = var1.keySet().iterator();

        while(var3.hasNext()) {
            String var4 = (String)var3.next();
            var2.appendQueryParameter(var4, (String)var1.get(var4));
        }

        String var5 = var2.build().toString();

        String var7;
        try {
            HttpURLConnection var6 = (HttpURLConnection)(new URL(var5)).openConnection();

            try {
                int var16;
                if ((var16 = var6.getResponseCode()) < 200 || var16 >= 300) {
                    Log.w("HttpUrlPinger", (new StringBuilder(65 + String.valueOf(var5).length())).append("Received non-success response code ").append(var16).append(" from pinging URL: ").append(var5).toString());
                }
            } finally {
                var6.disconnect();
            }

        } catch (IndexOutOfBoundsException var14) {
            var7 = var14.getMessage();
            Log.w("HttpUrlPinger", (new StringBuilder(32 + String.valueOf(var5).length() + String.valueOf(var7).length())).append("Error while parsing ping URL: ").append(var5).append(". ").append(var7).toString(), var14);
        } catch (RuntimeException | IOException var15) {
            var7 = var15.getMessage();
            Log.w("HttpUrlPinger", (new StringBuilder(27 + String.valueOf(var5).length() + String.valueOf(var7).length())).append("Error while pinging URL: ").append(var5).append(". ").append(var7).toString(), var15);
        }
    }
}

