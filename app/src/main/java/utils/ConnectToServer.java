package utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConnectToServer {

    public static String baseUri = "http://178.238.226.60/";
    public static String ConnectToServerDebugTag = "ConnectToServer";

    //POST
    public static void post(final Context context, final boolean withProgressDialog, final String title, final String message, final String url, final Map<String,String> parameters, final boolean attach_user_data, final PostListener postListener){
        final ProgressDialog pd;
        if(withProgressDialog){
            pd = ProgressDialog.show(context, title, message, true);
            pd.setCancelable(false);
        }else
            pd = null;

            RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                postListener.ResponseListener(response);
                if (pd != null) {
                    pd.dismiss();
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Session session = new Session(context);
                    session.insertOrUpdateUserLevel(jsonObject.getInt("level"));
                    session.insertOrUpdateUserPoint(jsonObject.getInt("points"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d(ConnectToServerDebugTag, "Successful: " + title + " - " + message);
                Log.d(ConnectToServerDebugTag, "response:\n" + response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                postListener.ErrorListener(error);
                if (pd != null) {
                    pd.dismiss();
                }
                Log.d(ConnectToServerDebugTag, "Error: " + title + " - " + message);
                error.printStackTrace();
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,listener,errorListener){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.putAll(parameters);
                Log.d("params post to " + url, params.toString());
                return params;
            }
        };

        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    public interface PostListener{
        public void ResponseListener(String response);
        public void ErrorListener(VolleyError error);
    }

    //GET
    public static void get(final Context context, final boolean withProgressDialog, final String title, final String message, final String url, final boolean attach_user_data, final GetListener getListener){
        final ProgressDialog pd;
        if(withProgressDialog){
            pd = ProgressDialog.show(context, title, message, true);
            pd.setCancelable(false);
        }else
            pd = null;

        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getListener.ResponseListener(response);
                if (pd != null) {
                    pd.dismiss();
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Session session = new Session(context);
                    session.insertOrUpdateUserLevel(jsonObject.getInt("level"));
                    session.insertOrUpdateUserPoint(jsonObject.getInt("points"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d(ConnectToServerDebugTag, "Successful: " + title + " - " + message);
                Log.d(ConnectToServerDebugTag, "response:\n" + response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getListener.ErrorListener(error);
                if (pd != null) {
                    pd.dismiss();
                }
                Log.d(ConnectToServerDebugTag, "Error: " + title + " - " + message);
                error.printStackTrace();
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,listener,errorListener);
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    public interface GetListener{
        public void ResponseListener(String response);
        public void ErrorListener(VolleyError error);
    }

    //Is Online
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
