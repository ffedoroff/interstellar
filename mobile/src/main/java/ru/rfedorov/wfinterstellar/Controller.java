package ru.rfedorov.wfinterstellar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class Controller implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "Controller";
    private static final String msgPathWearable = "/rfedorov_watch";
    public Boolean ServerEnabled = false;

    GoogleApiClient googleClient;
    private static Controller ourInstance = new Controller();

    private ru.rfedorov.wfinterstellar.ModelRFHome model;
    public LoginActivity mainActivity;

    public static Controller getInstance() {
        return ourInstance;
    }

    private Controller() {
        Log.v(TAG, "Controller created");
        model = new ModelRFHome();

        // Register the local broadcast receiver (listens wearable)
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(InterstellarApplication.getAppContext()).registerReceiver(messageReceiver, messageFilter);

        googleClient = new GoogleApiClient.Builder(InterstellarApplication.getAppContext())
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleClient.connect();

        new APIConnector().execute(AsyncApiCall.API_GET_JSON, "0");
    }

    private void sendInitResponseToWearable() {
        String message = getModel().getEnabled() ? getModel().getMessage() : "";
//        for (ModelUnit unit: getModel().getPrimeUnits().values()) {
//            message += ","+unit.getName();
//        }
        new WearableConnector(message).start();
        Log.v(TAG, "init response " + message);
    }

    public void PostUnitUpdate(String unitName, String newValue) {
        new APIConnector().execute(AsyncApiCall.API_POST_MESSAGE, unitName, newValue);
    }
    public void onModelChanged() {
        Log.i(TAG, "onModelChanged "+getModel().getMessage());
        sendInitResponseToWearable();
        if (mainActivity != null) mainActivity.reCreateUnits();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "mobile onConnectionSuspended cause:"+i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "mobile onConnectionFailed result:"+connectionResult);
    }

    public ModelRFHome getModel() {
        return model;
    }

    private class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra("wearable", false)) {
                String[] data = intent.getStringExtra("data").split(",");
                Log.v(TAG, "MessageReceiver onReceive "+intent.getStringExtra("data"));
                if (data.length > 0) {
                    if (data.length == 1 && "init".equals(data[0])) {
                        sendInitResponseToWearable();
                    }
                }
            }
        }
    }

    class WearableConnector extends Thread {
        String message;

        // Constructor to send a message to the data layer
        WearableConnector(String msg) {
            message = msg;
        }
        public void run() {
            NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(googleClient).await();
            for (Node node : nodes.getNodes()) {
                MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(googleClient, node.getId(), msgPathWearable, message.getBytes()).await();
                if (result.getStatus().isSuccess()) {
                    Log.v(TAG, "Message: {" + message + "} sent to: " + node.getDisplayName());
                }
                else {
                    Log.e(TAG, "ERROR: failed to send Message to watch: {" + message + "} watch: " + node.getDisplayName());
                }
            }
        }
    }

    class APIConnector extends AsyncApiCall {
        @Override
        public void onResult(String command, Boolean result, String data) {
            Log.i(TAG, "onResult "+command+" "+result);
            if (result) {
                if (API_GET_JSON.equals(command)) {
                    getModel().setMessage(data);
                    onModelChanged();
                } else if (API_POST_MESSAGE.equals(command)) {
//                    new APIConnector().execute(AsyncApiCall.API_GET_JSON, "0");
                }
            }
        }
    }
}
