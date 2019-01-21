package com.onemt.adid;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2019/1/21 14:06
 * @see
 */
public class BlockingServiceConnection implements ServiceConnection {
    private boolean zze = false;
    private final BlockingQueue<IBinder> queue = new LinkedBlockingQueue<>();

    public BlockingServiceConnection() {
    }

    @Override
    public void onServiceConnected(ComponentName var1, IBinder binder) {
        // 拿到google ad Service的binder
        this.queue.add(binder);
    }

    @Override
    public void onServiceDisconnected(ComponentName var1) {
    }

    @KeepForSdk
    public IBinder getServiceWithTimeout(long timeout, TimeUnit timeUnit) throws InterruptedException, TimeoutException {
        Preconditions.checkNotMainThread("BlockingServiceConnection.getServiceWithTimeout() called on main thread");
        if (this.zze) {
            throw new IllegalStateException("Cannot call get on this connection more than once");
        } else {
            this.zze = true;
            IBinder var4;
            if ((var4 = (IBinder)this.queue.poll(timeout, timeUnit)) == null) {
                throw new TimeoutException("Timed out waiting for the service connection");
            } else {
                return var4;
            }
        }
    }

    @KeepForSdk
    public IBinder getService() throws InterruptedException {
        Preconditions.checkNotMainThread("BlockingServiceConnection.getService() called on main thread");
        if (this.zze) {
            throw new IllegalStateException("Cannot call get on this connection more than once");
        } else {
            this.zze = true;
            return (IBinder)this.queue.take();
        }
    }
}
