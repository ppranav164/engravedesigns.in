package com.shopping.giveaway4u;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class GettingDeviceTokenService extends FirebaseInstanceIdService {



    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String DeviceToken = FirebaseInstanceId.getInstance().getToken();

        Log.e("GettinToken",DeviceToken);

    }
}
