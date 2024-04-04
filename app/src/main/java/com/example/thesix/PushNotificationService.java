package com.example.thesix;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class PushNotificationService extends FirebaseMessagingService {
    private QrCodeDB firestoreHelper;
    private String deviceID;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        firestoreHelper = new QrCodeDB();
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        firestoreHelper.saveDeviceIDToToken(deviceID,token);
        Log.e("newToken",token);

        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb",token).apply();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
    }
    public static String getToken(Context context){
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");

    }
}
