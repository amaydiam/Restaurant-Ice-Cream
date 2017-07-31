package com.ad.restauranticecream.utils;

import android.content.Context;
import android.util.Log;

import com.ad.restauranticecream.RestaurantIceCream;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class CustomVolley {

    public OnCallbackResponse OnCallbackResponse;
    public int statusCode;
    private Context activity;

    public CustomVolley(Context c) {
        activity = c;
    }

    public void setOnCallbackResponse(OnCallbackResponse onCallbackResponse) {
        OnCallbackResponse = onCallbackResponse;
    }

    public RequestQueue Rest(int METHOD, String URL, final Map<String, String> jsonParams, final String TAG) {
        Log.v("URL " + TAG, URL);
        if (OnCallbackResponse != null) {
            OnCallbackResponse.onVolleyStart(TAG);
        }
        if (jsonParams != null) {
            for (Map.Entry<String, String> entry : jsonParams.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                Log.v(key + " : ", value);
            }
        }
        RequestQueue queue = Volley.newRequestQueue(activity);
        StringRequest sr = new StringRequest(METHOD, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("onVolleySuccessResponse URL " + TAG, response);
                        if (OnCallbackResponse != null) {
                            OnCallbackResponse.onVolleySuccessResponse(TAG, response);
                        }
                        if (OnCallbackResponse != null) {
                            OnCallbackResponse.onVolleyEnd(TAG);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                String resp = "error";
                if (response != null && response.data != null) {
                    resp = new String(response.data);
                }
                if (OnCallbackResponse != null) {
                    Log.v("onVolleyErrorResponse URL " + TAG, resp);
                    OnCallbackResponse.onVolleyErrorResponse(TAG, resp);
                }
                if (OnCallbackResponse != null) {
                    OnCallbackResponse.onVolleyEnd(TAG);
                }
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                statusCode = response.statusCode;
                //Prefs.putDeviceId(activity, deviceid);
                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() {
                return jsonParams;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(
                RestaurantIceCream.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        sr.setShouldCache(false);
        sr.setTag(TAG);
        queue.add(sr);
        return queue;
    }

    public interface OnCallbackResponse {
        void onVolleyStart(String TAG);

        void onVolleyEnd(String TAG);

        void onVolleySuccessResponse(String TAG, String response);

        void onVolleyErrorResponse(String TAG, String response);
    }
}
