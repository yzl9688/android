package com.mad.trafficclient.utilhttp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mad.trafficclient.util.LoadingDialog;

import org.json.JSONObject;

public class RequestNetwork {
    /**
     *
     * @param context  上下文
     * @param url  请求地址
     * @param data  请求数据
     * @param callback  执行的操作
     */
    public static void request(final Context context, String url, JSONObject data, final Callback callback) {
        RequestQueue mQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                callback.invoke(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    public interface Callback {
        void invoke(JSONObject jsonObject);
    }
}
