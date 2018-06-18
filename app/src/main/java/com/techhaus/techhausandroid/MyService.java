package com.techhaus.techhausandroid;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyService extends Service {
    private RequestQueue mQueue;
    public MyService() {
       // Log.d("mytag","SERVICIO INICIADO");
        //mQueue = Volley.newRequestQueue(getApplicationContext());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mQueue = Volley.newRequestQueue(getApplicationContext());
        Log.d("mytag","SERVICIO INICIADO   2!!!");

            Log.d("mytag","en el true");
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API.getDeviceTypes(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("mytag", "A VER FLACO");
                        try {
                            processResponse(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("mytag", "Error de response");
                        error.printStackTrace();
                    }
                });

                mQueue.add(request);



        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void processResponse(JSONObject response) throws JSONException {


        String searchId = "alarm";
        JSONArray jsonArray = response.getJSONArray("devices");
        Log.d("mytag","A VER FLACO2");

        String typeName = "";
        String typeId = "";
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject type = jsonArray.getJSONObject(i);
            typeName = type.getString("name");
            typeId = type.getString("id");
            if(typeName.equals(searchId)){
                getDevicesForType(typeId);
            }

        }
    }

    private void getDevicesForType(String typeId) {
        String urlDevicesForType = API.getDevices() + "devicetypes/" + typeId;
        //mQueue2 = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlDevicesForType, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    processDevices(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("mytag", "Error de response");
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    private void processDevices(JSONObject response) throws JSONException {
        JSONArray jsonArray = response.getJSONArray("devices");

        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject device = jsonArray.getJSONObject(i);
            final String name = device.getString("name");
            final String id = device.getString("id");
            Log.d("mytag", "Procesando: " + name);
            Log.d("mytag", "Id es: " + id);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API.getDevices()+ id + "/events", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        processEvent(response, name, id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("mytag", "Error de response ACA");
                    error.printStackTrace();
                }
            });
            mQueue.add(request);
        }

    }

    private void processEvent(JSONObject response, String name, String id) throws JSONException {
        String event = response.getString("event");
        String args = response.getString("event");
        Log.d("mytag", "Name es: " + name);
        Log.d("mytag", "Id es: " + id);
        Log.d("mytag", "Event es: " + event);
        Log.d("mytag", "Args es: " + args);
    }
}
