package com.techhaus.techhausandroid;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MyService extends Service {
    private RequestQueue mQueue;
    Set<String> myNotif;
    public MyService() {
       // Log.d("mytag","SERVICIO INICIADO");
        //mQueue = Volley.newRequestQueue(getApplicationContext());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mQueue = Volley.newRequestQueue(getApplicationContext());


            Log.d("mytag","en el true");
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API.getDeviceTypes(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

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
            StringRequest request = new StringRequest(Request.Method.GET, API.getDevices()+ id + "/events",  new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
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

    private void processEvent(String response, String name, String id) throws JSONException {
        //Log.d("mytag", "Response es: " + response);
        String[] events = response.split(",");
        if(events.length > 1){
            String[] eventAux = events[2].split(":");
            String[] argsAux = events[3].split(":");
            // String args = response.getString("event");

            String event = eventAux[2].replace("\"", "");
            String arg =  argsAux[3].replace("\"", "").replace("}", "").replace("data", "");

            //mando notif
            createNotificationChannel(this);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setContentTitle(name + ": " + event);
            builder.setContentText(getString(R.string.NewStatus)+ " " + arg);
            builder.setSmallIcon(R.drawable.alert_icon);
            builder.setAutoCancel(true);
            builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
            builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.notif_red));

            //notification manager
            Notification notification = builder.build();
            NotificationManager manager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
            manager.notify(1234, notification);

            //agrego a listado

            SharedPreferences sharedPreferences = this.getSharedPreferences("shared preferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            myNotif = sharedPreferences.getStringSet("notifications", null);
            if(myNotif == null){
                myNotif = new HashSet<String>();
            }
            String addS = "Alerta: " + name + " " +  arg;
            myNotif.add(addS);
            Log.d("mytag", "En myNotif puse " + addS);
            editor.putStringSet("notifications", myNotif);
            editor.commit();
        }

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
}
