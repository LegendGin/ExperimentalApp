package com.onemt.adid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/27 15:36
 * @see
 */
public class MainActivity extends AppCompatActivity {

    private TextView tv_adid;
    String adid = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        tv_adid = findViewById(R.id.tv_adid);
        findViewById(R.id.btn_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
            }
        });
        findViewById(R.id.btn_adid_sync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdidTask t = new AdidTask();
//                Executors.newSingleThreadExecutor().submit(t);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        /*try {
                            AdvertisingIdClient.Info info = AdvertisingIdClient.getAdvertisingIdInfo(MainActivity.this);
                            adid = info.getId();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (GooglePlayServicesNotAvailableException e) {
                            e.printStackTrace();
                        } catch (GooglePlayServicesRepairableException e) {
                            e.printStackTrace();
                        }*/
                        try {
                            final AdvertisingIdClient.Info info = AdClient.getAdvertisingIdInfo(MainActivity.this);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_adid.append("playid custom:" + info.getId() + "\n");
                                }
                            });
                            Log.e("info", info.getId());
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (GooglePlayServicesNotAvailableException e) {
                            e.printStackTrace();
                        } catch (GooglePlayServicesRepairableException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
//                    thread.join();
//                tv_adid.append(adid);
                /*try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tv_adid.append("cost:" + (System.currentTimeMillis() - start));*/
            }
        });
        findViewById(R.id.btn_adid_async).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_adid.append("serial:" + getSerialId());
                tv_adid.append("\n");
                tv_adid.append("android_id:" + getAndroidId());
                tv_adid.append("\n");
                tv_adid.append("ro.build.id:" + getBuildId());
                tv_adid.append("\n");
                tv_adid.append("ro.debuggable:" + getDebuggable());
                tv_adid.append("\n");
                tv_adid.append("RawMacAddress:" + getRawMacAddress(MainActivity.this));
                tv_adid.append("\n");
                tv_adid.append("getMacFromHardware:" + getMacFromHardware());
                tv_adid.append("\n");
                tv_adid.append("getMacViaMgr:" + getMacViaMgr());
                tv_adid.append("\n");
                tv_adid.append("getLocalMacAddressFromBusybox:" + getLocalMacAddressFromBusybox());
                tv_adid.append("\n");
                getAdid();
                getBuildIdByCmd();
            }
        });
//        getAdid();
    }

    private class AdidTask implements Callable<String> {

        private String adid;

        private ReentrantLock lock = new ReentrantLock(true);

        private long start = System.currentTimeMillis();

        private volatile boolean isComplete = false;

        public String getAdid() {
            while (!isComplete) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return adid;
        }

        @Override
        public String call() throws Exception {
            try {
                Log.e("call", "start");
                final AdvertisingIdClient.Info info = AdvertisingIdClient.getAdvertisingIdInfo(MainActivity.this);
                Log.e("call", "finish");
                StringBuilder sb = new StringBuilder();
                long cost = System.currentTimeMillis() - start;
                sb.append("playid async:" + info.getId() + "\n")
                        .append("cost:" + cost)
                        .append("\n");
                this.adid = sb.toString();
                isComplete = true;
                return this.adid;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } finally {
            }
            return null;
        }
    }

    private void getAdid() {
        final long start = System.currentTimeMillis();
        /*Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                final String adid = getPlayAdId(MainActivity.this);
                tv_adid.post(new Runnable() {
                    @Override
                    public void run() {
                        long cost = System.currentTimeMillis() - start;
                        tv_adid.append("playid reflect:" + adid + "\n");
                        tv_adid.append("cost:" + cost);
                        tv_adid.append("\n");
                    }
                });
            }
        });*/
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final AdvertisingIdClient.Info info = AdvertisingIdClient.getAdvertisingIdInfo(MainActivity.this);
                    tv_adid.post(new Runnable() {
                        @Override
                        public void run() {
                            long cost = System.currentTimeMillis() - start;
                            tv_adid.append("playid async:" + info.getId() + "\n");
                            tv_adid.append("cost:" + cost);
                            tv_adid.append("\n");
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String getAndroidId() {
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private String getMeid() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getImei();
    }

    private String getSerialId() {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");

            Method get = c.getMethod("get", String.class, String.class);

            String serialnum = (String) (get.invoke(c, "ro.serialno", "unknown"));
            return serialnum + " | " + Build.SERIAL;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Build.SERIAL;
    }

    private String getBuildId() {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");

            Method get = c.getMethod("get", String.class, String.class);

            String serialnum = (String) (get.invoke(c, "ro.build.id", "unknown"));
            return serialnum + " | " + Build.ID;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getDebuggable() {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");

            Method get = c.getMethod("get", String.class, String.class);

            String serialnum = (String) (get.invoke(c, "ro.debuggable", "unknown"));
            return serialnum;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getBuildIdByCmd() {
        try {
            Process process = Runtime.getRuntime().exec("cmd /c adb shell");
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            process.getOutputStream().write("cat build.prop".getBytes());
            String line = null;
            while ((line = input.readLine()) != null) {
                Log.e("cat build.prop", line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getRawMacAddress(Context context) {
        // android devices should have a wlan address
        final String wlanAddress = loadAddress("wlan0");
        if (wlanAddress != null) {
            return wlanAddress;
        }

        // emulators should have an ethernet address
        final String ethAddress = loadAddress("eth0");
        if (ethAddress != null) {
            return ethAddress;
        }

        // query the wifi manager (requires the ACCESS_WIFI_STATE permission)
        try {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final String wifiAddress = wifiManager.getConnectionInfo().getMacAddress();
            if (wifiAddress != null) {
                return wifiAddress;
            }
        } catch (Exception e) {
            /* no-op */
        }

        return null;
    }

    private String loadAddress(final String interfaceName) {
        try {
            final String filePath = "/sys/class/net/" + interfaceName + "/address";
            final StringBuilder fileData = new StringBuilder(1000);
            final BufferedReader reader = new BufferedReader(new FileReader(filePath), 1024);
            final char[] buf = new char[1024];
            int numRead;

            String readData;
            while ((numRead = reader.read(buf)) != -1) {
                readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
            }

            reader.close();
            return fileData.toString();
        } catch (IOException e) {
            return null;
        }
    }

    public String getPlayAdId(Context context) {
        try {
            Object AdvertisingInfoObject = getAdvertisingInfoObject(context);
            String playAdid = (String) invokeInstanceMethod(AdvertisingInfoObject, "getId", null);
            return playAdid;
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    public Object invokeInstanceMethod(Object instance, String methodName, Class[] cArgs, Object... args)
            throws Exception {
        Class classObject = instance.getClass();
        return invokeMethod(classObject, methodName, instance, cArgs, args);
    }

    private Object getAdvertisingInfoObject(Context context) throws Exception {
        return invokeStaticMethod("com.google.android.gms.ads.identifier.AdvertisingIdClient", "getAdvertisingIdInfo", new Class[]{Context.class}, context);
    }

    public Object invokeStaticMethod(String className, String methodName, Class[] cArgs, Object... args)
            throws Exception {
        Class classObject = Class.forName(className);
        return invokeMethod(classObject, methodName, null, cArgs, args);
    }

    public Object invokeMethod(Class classObject, String methodName, Object instance, Class[] cArgs, Object... args)
            throws Exception {
        @SuppressWarnings("unchecked")
        Method methodObject = classObject.getMethod(methodName, cArgs);
        if (methodObject == null) {
            return null;
        }

        Object resultObject = methodObject.invoke(instance, args);
        return resultObject;
    }

    /**
     * 遍历循环所有的网络接口，找到接口是 wlan0
     * 必须的权限 <uses-permission android:name="android.permission.INTERNET" />
     * @return
     */
    private String getMacFromHardware() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) {
                    continue;
                }

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    continue;
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    private String getMacViaMgr() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager == null ? null : wifiManager.getConnectionInfo();
        return wifiInfo == null ? "" : wifiInfo.getMacAddress();
    }

    /**
     * 根据busybox获取本地Mac
     *
     * @return
     */
    public String getLocalMacAddressFromBusybox() {
        String result = "";
        String Mac = "";
        result = callCmd("busybox ifconfig", "HWaddr");
        // 如果返回的result == null，则说明网络不可取
        if (result == null) {
            return "网络异常";
        }
        // 对该行数据进行解析
        // 例如：eth0 Link encap:Ethernet HWaddr 00:16:E8:3E:DF:67
        if (result.length() > 0 && result.contains("HWaddr") == true) {
            Mac = result.substring(result.indexOf("HWaddr") + 6,
                    result.length() - 1);
            result = Mac;
        }
        return result;
    }

    private String callCmd(String cmd, String filter) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);

            while ((line = br.readLine()) != null
                    && line.contains(filter) == false) {
                result += line;
            }

            result = line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
