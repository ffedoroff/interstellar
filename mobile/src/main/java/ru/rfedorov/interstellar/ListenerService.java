package ru.rfedorov.interstellar;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.TreeMap;


public class ListenerService extends WearableListenerService {
    private static final String TAG = "ListenerServiceMobile";
    private static final String msgPathMobile = "/rfedorov_mobile";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        if (messageEvent.getPath().equals(msgPathMobile)) {
            final String message = new String(messageEvent.getData());

            Intent messageIntent = new Intent();
            messageIntent.setAction(Intent.ACTION_SEND);
            messageIntent.putExtra("wearable", true);
            messageIntent.putExtra("data", message);
            Log.v(TAG, "onMessageReceived: " + message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
        }
        else {
            Log.e(TAG, "onMessageReceived not processed: " + messageEvent.getPath());
            super.onMessageReceived(messageEvent);
        }
    }
}
