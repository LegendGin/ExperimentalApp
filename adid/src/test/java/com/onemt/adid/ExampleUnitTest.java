package com.onemt.adid;

import android.net.Uri;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        String url = "https://apicosdkbeta.onemt.co/?gameid=100006001&action=social&version=3.0.1_20190107_1";
        Uri uri = Uri.parse(url);
        uri = uri.buildUpon().scheme(uri.getScheme())
                .authority(uri.getAuthority())
                .path(uri.getPath()).build();
        System.out.println(uri.toString());
    }
}