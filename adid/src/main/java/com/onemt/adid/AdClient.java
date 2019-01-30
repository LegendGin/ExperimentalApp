package com.onemt.adid;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.GuardedBy;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.stats.ConnectionTracker;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2019/1/21 13:23
 * @see
 */
public class AdClient {
    private final Object zzh;
    private final Context mContext;
    @GuardedBy("this")
    private boolean zzg;
    private final long zzk;
    private final boolean persistent;
    @GuardedBy("this")
    @Nullable
    private zze zzf;
    @GuardedBy("this")
    @Nullable
    private BlockingServiceConnection serviceConnection;

    @GuardedBy("mAutoDisconnectTaskLock")
    @Nullable
    private AdClient.zza zzi;

    static class zza extends Thread {
        private WeakReference<AdClient> zzm;
        private long zzn;
        CountDownLatch zzo;
        boolean zzp;

        public zza(AdClient var1, long var2) {
            this.zzm = new WeakReference(var1);
            this.zzn = var2;
            this.zzo = new CountDownLatch(1);
            this.zzp = false;
            this.start();
        }

        private final void disconnect() {
            AdClient var1;
            if ((var1 = (AdClient)this.zzm.get()) != null) {
                var1.finish();
                this.zzp = true;
            }

        }

        @Override
        public final void run() {
            try {
                if (!this.zzo.await(this.zzn, TimeUnit.MILLISECONDS)) {
                    this.disconnect();
                }

            } catch (InterruptedException var1) {
                this.disconnect();
            }
        }
    }

    private AdClient(Context context, long j, boolean z, boolean z2) {
        this.zzh = new Object();
        Preconditions.checkNotNull(context);
        if (z) {
            Context applicationContext = context.getApplicationContext();
            if (applicationContext != null) {
                context = applicationContext;
            }
            this.mContext = context;
        } else {
            this.mContext = context;
        }
        this.zzg = false;
        this.zzk = j;
        this.persistent = z2;
    }


    public static AdvertisingIdClient.Info getAdvertisingIdInfo(Context context) throws IOException, IllegalStateException, GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        SpUtil spUtil = new SpUtil(context);
        boolean enabled = spUtil.getBoolean("gads:ad_id_app_context:enabled", false);
        float ping_ratio = spUtil.getFloat("gads:ad_id_app_context:ping_ratio", 0.0f);
        String experiment_id = spUtil.getString("gads:ad_id_use_shared_preference:experiment_id", "");
        AdClient advertisingIdClient = new AdClient(context, -1, enabled, spUtil.getBoolean("gads:ad_id_use_persistent_service:enabled", false));
        try {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            // 连接gms服务
            advertisingIdClient.zza(false);
            AdvertisingIdClient.Info info = advertisingIdClient.getInfo();
            advertisingIdClient.zza(info, enabled, ping_ratio, SystemClock.elapsedRealtime() - elapsedRealtime, experiment_id, null);
            advertisingIdClient.finish();
            return info;
        } catch (Throwable th) {
            advertisingIdClient.finish();
        }
        return null;
    }

    private final boolean zza(AdvertisingIdClient.Info info, boolean z, float f, long j, String str, Throwable th) {
        if (Math.random() > ((double) f)) {
            return false;
        }
        Map hashMap = new HashMap();
        hashMap.put("app_context", z ? "1" : "0");
        if (info != null) {
            hashMap.put("limit_ad_tracking", info.isLimitAdTrackingEnabled() ? "1" : "0");
        }
        if (!(info == null || info.getId() == null)) {
            hashMap.put("ad_id_size", Integer.toString(info.getId().length()));
        }
        if (th != null) {
            hashMap.put("error", th.getClass().getName());
        }
        if (!(str == null || str.isEmpty())) {
            hashMap.put("experiment_id", str);
        }
        hashMap.put("tag", "AdvertisingIdClient");
        hashMap.put("time_spent", Long.toString(j));
        new com.onemt.adid.zza(this, hashMap).start();
        return true;
    }

    public AdvertisingIdClient.Info getInfo() throws IOException {
//        Preconditions.checkNotMainThread("Calling this from your main thread can lead to deadlock");
        AdvertisingIdClient.Info var1;
        synchronized(this) {
            if (!this.zzg) {
                Object var3 = this.zzh;
                synchronized(this.zzh) {
                    if (this.zzi == null || !this.zzi.zzp) {
                        throw new IOException("AdvertisingIdClient is not connected.");
                    }
                }

                try {
                    this.zza(false);
                } catch (Exception var7) {
                    throw new IOException("AdvertisingIdClient cannot reconnect.", var7);
                }

                if (!this.zzg) {
                    throw new IOException("AdvertisingIdClient cannot reconnect.");
                }
            }

            Preconditions.checkNotNull(this.serviceConnection);
            Preconditions.checkNotNull(this.zzf);

            try {
                var1 = new AdvertisingIdClient.Info(this.zzf.getId(), this.zzf.zzb(true));
            } catch (RemoteException var6) {
                Log.i("AdvertisingIdClient", "GMS remote exception ", var6);
                throw new IOException("Remote exception");
            }
        }

        this.zza();
        return var1;
    }

    private final void zza() {
        synchronized (this.zzh) {
            if (this.zzi != null) {
                this.zzi.zzo.countDown();
                try {
                    this.zzi.join();
                } catch (InterruptedException e) {
                }
            }
            if (this.zzk > 0) {
                this.zzi = new zza(this, this.zzk);
            }
        }

    }

    private final void zza(boolean z) throws IOException, IllegalStateException, GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
//        Preconditions.checkNotMainThread("Calling this from your main thread can lead to deadlock");
        synchronized (this) {
            if (this.zzg) {
                finish();
            }
            this.serviceConnection = getConnection(this.mContext, this.persistent);
            /*switch (GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(mContext, 12451000)) {
                case 0:
                case 2:
                    String action = persistent ? "com.google.android.gms.ads.identifier.service.PERSISTENT_START" : "com.google.android.gms.ads.identifier.service.START";
                    BlockingServiceConnection blockingServiceConnection = new BlockingServiceConnection();
                    Intent intent = new Intent(action);
                    intent.setPackage("com.google.android.gms");
                    mContext.bindService(intent, new ServiceConnection() {
                        @Override
                        public void onServiceConnected(ComponentName name, IBinder service) {

                        }

                        @Override
                        public void onServiceDisconnected(ComponentName name) {

                        }
                    }, Context.BIND_AUTO_CREATE);
                default:
                    throw new IOException("Google Play services not available");
            }*/
            this.zzf = zza(this.mContext, this.serviceConnection);
            this.zzg = true;
            if (z) {
                zza();
            }
        }
    }

    private void finish() {
//        Preconditions.checkNotMainThread("Calling this from your main thread can lead to deadlock");
        synchronized(this) {
            if (this.mContext != null && this.serviceConnection != null) {
                try {
                    if (this.zzg) {
                        ConnectionTracker.getInstance().unbindService(this.mContext, this.serviceConnection);
                    }
                } catch (Throwable var4) {
                    Log.i("AdvertisingIdClient", "AdvertisingIdClient unbindService failed.", var4);
                }

                this.zzg = false;
                this.zzf = null;
                this.serviceConnection = null;
            }
        }
    }

    private static zze zza(Context context, BlockingServiceConnection blockingServiceConnection) throws IOException {
        try {
            return com.onemt.adid.zzf.getInterface(blockingServiceConnection.getServiceWithTimeout(10000, TimeUnit.MILLISECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static BlockingServiceConnection getConnection(Context context, boolean persistent) throws IOException, GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        try {
            context.getPackageManager().getPackageInfo("com.android.vending", 0);
            switch (GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(context, 12451000)) {
                case 0:
                case 2:
                    String action = persistent ? "com.google.android.gms.ads.identifier.service.PERSISTENT_START" : "com.google.android.gms.ads.identifier.service.START";
                    BlockingServiceConnection blockingServiceConnection = new BlockingServiceConnection();
                    Intent intent = new Intent(action);
                    intent.setPackage("com.google.android.gms");
                    try {
                        if (ConnectionTracker.getInstance().bindService(context, intent, blockingServiceConnection, 1)) {
                            return blockingServiceConnection;
                        }
                        throw new IOException("Connection failure");
                    } catch (Throwable th) {
                        IOException iOException = new IOException(th);
                    }
                default:
                    throw new IOException("Google Play services not available");
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new GooglePlayServicesNotAvailableException(9);
        }
    }



}
