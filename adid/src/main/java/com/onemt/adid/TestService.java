package com.onemt.adid;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import java.io.FileDescriptor;

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2019/1/21 15:18
 * @see
 */
public class TestService extends Service {

    ITestInterface.Stub binder = new ITestInterface.Stub() {
        @Override
        public void connect(String mes) throws RemoteException {
            Log.e("connect", "message:" + mes);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
