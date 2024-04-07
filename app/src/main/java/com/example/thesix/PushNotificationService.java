package com.example.thesix;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class PushNotificationService extends FirebaseMessagingService {
    //getting firestore helper
    private QrCodeDB firestoreHelper;
    private String deviceID;

    /** Retrieving New Token
     * @param token The token used for sending messages to this application instance. This token is
     *              the same as the one retrieved by {@link FirebaseMessaging#getToken()}.
     */
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        //new instance of DB
        firestoreHelper = new QrCodeDB();
        // new device ID
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        firestoreHelper.saveDeviceIDToToken(deviceID,token);
        Log.e("newToken",token);

        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb",token).apply();
    }

    /** On Recieving Message
     * @param message Remote message that has been received.
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
    }

    /** On getting token
     * @param context of Activity
     * @return String with Token
     */
    public static String getToken(Context context){
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");

    }
}
