package com.techhaus.techhausandroid;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.techhaus.techhausandroid.Adapter.MyAdapter;
import com.techhaus.techhausandroid.Models.TitleParent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmNotificationReceiver extends BroadcastReceiver{
    private RequestQueue mQueue;
    ArrayList<String> myNotif;


    @Override
    public void onReceive(Context context, Intent intent) {

        context.startService(new Intent(context, MyService.class));
        Log.d("mytag", "its new");
        /*SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(myNotif);
        editor.putString("notifList", json);
        editor.apply();*/



        /*while(true){
            if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
                //Intent i = new Intent();
                //content

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API.getDeviceTypes(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("mytag","A VER FLACO");
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
                Log.d("mytag","OK IM HERE EVERYONE");
                createNotificationChannel(context);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder.setContentTitle("HEY I WAS INITIALIZED!");
                builder.setContentText("Good luck");
                builder.setSmallIcon(R.drawable.alert_icon);
                builder.setAutoCancel(true);
                builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
                builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.notif_red));

                //notification manager
                Notification notification = builder.build();
                NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                manager.notify(1234, notification);
            }
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

    }

    private void createNotificationChannel(final Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "ChannelName";
            String description = "Channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1000", "El_channel", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
                Log.d("mytag","typeName es" + typeName);
                typeId = type.getString("id");
                if(typeName.equals(searchId)){
                    Log.d("mytag", "rarara");
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
            String url = API.getDevices()+ id + "/events";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.d("mytag", "Ok genialll");
                        processEvent(response, name, id);
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
            }) { //no semicolon or coma
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    return params;
                }
            };
            mQueue.add(request);
        }

    }

    private void processEvent(JSONObject response, String name, String id) throws JSONException {
       // String event = response.getString("event");
       // String args = response.getString("event");
       // Log.d("mytag", "Name es: " + name);
       // Log.d("mytag", "Id es: " + id);
       // Log.d("mytag", "Event es: " + event);
       // Log.d("mytag", "Args es: " + args);
    }
}
