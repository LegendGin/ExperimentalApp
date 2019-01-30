package com.lemonyakult.handyproxy;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.ProxyInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ProxySelector;

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2019/1/30 11:13
 * @see
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{"android.permission.CONNECTIVITY_INTERNAL"}, 0);
                }
                boolean b = saveToDb();
                if (b) {
                    Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    boolean saveToDb() {
        EditText edt_ip = findViewById(R.id.edt_ip);
        String hostname = edt_ip.getText().toString();
        EditText edt_port = findViewById(R.id.edt_port);
        int port = Integer.parseInt(edt_port.getText().toString());
        Class clz = null;
        try {
            clz = Class.forName("android.net.ProxyInfo");
            Constructor constructor = clz.getConstructor(String.class, int.class, String.class);
            ProxyInfo p = (ProxyInfo) constructor.newInstance(hostname, port, null);
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            Method method = cm.getClass().getMethod("setGlobalProxy", ProxyInfo.class);
            method.setAccessible(true);
            method.invoke(cm, p);
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

}
