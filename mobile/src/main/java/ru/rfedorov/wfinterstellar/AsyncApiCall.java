package ru.rfedorov.wfinterstellar;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.webkit.URLUtil;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public abstract class AsyncApiCall extends AsyncTask<String, Void, Boolean> {
    private static final String TAG = "LongOperation";
//    private Context context;
    private String ApiJsonText;
    private String ApiCommand;

    static final String API_POST_MESSAGE = "api/message";
    static final String API_GET_JSON = "api/json";

    private Boolean ValidateConfig() {
//        String serverUrl = getConfigParameter("prefServerurl");
//        String userName = getConfigParameter("prefUsername");
//        String password = getConfigParameter("prefUserpassword");
//        Log.i(TAG, "prefServerurl: " + getConfigParameter("prefServerurl"));
        return URLUtil.isValidUrl(getConfigParameter("prefServerurl"));
    }

    private String getConfigParameter(String name) {
        return PreferenceManager.getDefaultSharedPreferences(ru.rfedorov.wfinterstellar.InterstellarApplication.getAppContext()).getString(name, "");
    }

    private Boolean api_message(String... params) {
        if (!ValidateConfig() || params.length != 3) {
            return false;
        }
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(getConfigParameter("prefServerurl") + API_POST_MESSAGE);
        String outp = "[{\"key\":\"" + params[1] + "\", \"value\" : \"" + params[2] + "\"}]";
        try {
            String userName = getConfigParameter("prefUsername");
            if (userName != null && !userName.isEmpty()) {
                String auth = "basic ";
                auth += new String(Base64.encode((userName + ":" + getConfigParameter("prefUserpassword")).getBytes(), Base64.NO_WRAP));
                httppost.setHeader("Authorization", auth);
            }

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("updates", outp));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            //new LongOperation2().execute("0");
            return true;
        } catch (ClientProtocolException e) {
            Log.e(TAG, "ClientProtocolException", e);
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        }
        return false;
    }

    private Boolean api_json(String... params) {
        if (!ValidateConfig() || params.length != 2) {
            return false;
        }
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet http = new HttpGet(getConfigParameter("prefServerurl") + API_GET_JSON + "?export_time="+params[1]);
        try {
            String userName = getConfigParameter("prefUsername");
            if (userName != null && !userName.isEmpty()) {
                String auth = "basic ";
                auth += new String(Base64.encode((userName + ":" + getConfigParameter("prefUserpassword")).getBytes(), Base64.NO_WRAP));
                http.setHeader("Authorization", auth);
            }

            HttpResponse response = httpclient.execute(http);
            if (response.getStatusLine().getStatusCode() == 200) {
                ApiJsonText = EntityUtils.toString(response.getEntity());
                return true;
            } else {
                Log.e(TAG, "error getting data from server, status code:" + response.getStatusLine().getStatusCode());
            }
        } catch (ClientProtocolException e) {
            Log.e(TAG, "ClientProtocolException", e);
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        }
        return false;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        if (params.length > 0) {
            ApiJsonText = null;
            ApiCommand = params[0];
            if (params[0].equals(API_POST_MESSAGE))
                return api_message(params);
            if (params[0].equals(API_GET_JSON))
                return api_json(params);
        }
        Log.e(TAG, "wrong api params:" + params);
        return false;
    }

    public abstract void onResult(String command, Boolean result, String data);

    @Override
    protected void onPostExecute(Boolean result) {
        onResult(ApiCommand, result, ApiJsonText);
        // execution of result of Long time consuming operation
        //reCreateUnits(result);
    }
}