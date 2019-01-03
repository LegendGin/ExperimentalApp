package com.onemt.adid;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/27 15:36
 * @see
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        findViewById(R.id.btn_adid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private String getAdid() {
        try {
            Context remoteContext = createPackageContext("com.google.android.gms", CONTEXT_INCLUDE_CODE | CONTEXT_IGNORE_SECURITY);
            SharedPreferences sp = remoteContext == null ? null : remoteContext.getSharedPreferences("google_ads_flags", MODE_PRIVATE);
            if (sp != null) {
                boolean ad_id_app_context = sp.getBoolean("gads:ad_id_app_context:enabled", false);
                float ping_ratio = sp.getFloat("gads:ad_id_app_context:ping_ratio", 0.0F);
                String experiment_id = sp.getString("gads:ad_id_use_shared_preference:experiment_id", "");
                boolean use_persistent_service = sp.getBoolean("gads:ad_id_use_persistent_service:enabled", false);
            }

            /*long elapsedRealtime = SystemClock.elapsedRealtime();
            var6.zza(false);
            AdvertisingIdClient.Info var9 = var6.getInfo();
            long var10 = SystemClock.elapsedRealtime();
            var6.zza(var9, var2, var3, var10 - var7, var4, (Throwable)null);
            var12 = var9;*/

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
