package com.onemt.skin;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Method;

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/17 22:16
 * @see
 */
public class ResourceLoaders {
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static Resources get(File file, Context context) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, file.getAbsolutePath());
            Resources superRes = context.getResources();
            return new Resources(assetManager, superRes.getDisplayMetrics(),
                    superRes.getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
