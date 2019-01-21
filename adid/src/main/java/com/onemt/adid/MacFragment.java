package com.onemt.adid;

import android.os.Bundle;
import android.support.v14.preference.PreferenceFragment;
import android.support.v7.preference.Preference;

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2019/1/7 18:12
 * @see
 */
public class MacFragment extends PreferenceFragment {

    private static final String KEY_WIFI_MAC_ADDRESS = "wifi_mac_address";
    private Preference mWifiMacAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWifiMacAddress = findPreference(KEY_WIFI_MAC_ADDRESS);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

    }
}
