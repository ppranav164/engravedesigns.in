package com.shopping.giveaway4u;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class GettingDeviceTokenService extends FirebaseInstanceIdService {


    SharedPreferences.Editor editor ;


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        editor = getSharedPreferences("firebase",MODE_PRIVATE).edit();
        String DeviceToken = FirebaseInstanceId.getInstance().getToken();
        editor.putString("fbtoken",DeviceToken);
        editor.apply();

        Log.e("GettinToken",DeviceToken);

    }

}
