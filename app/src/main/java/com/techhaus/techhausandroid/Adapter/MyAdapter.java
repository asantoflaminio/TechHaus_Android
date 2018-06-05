package com.techhaus.techhausandroid.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.techhaus.techhausandroid.ACActivity;
import com.techhaus.techhausandroid.AlarmActivity;
import com.techhaus.techhausandroid.BlindActivity;
import com.techhaus.techhausandroid.DoorActivity;
import com.techhaus.techhausandroid.LampActivity;
import com.techhaus.techhausandroid.Models.TitleChild;
import com.techhaus.techhausandroid.Models.TitleParent;
import com.techhaus.techhausandroid.OvenActivity;
import com.techhaus.techhausandroid.R;
import com.techhaus.techhausandroid.RefrigeratorActivity;
import com.techhaus.techhausandroid.RoutActivity;
import com.techhaus.techhausandroid.ViewHolders.TitleChildViewHolder;
import com.techhaus.techhausandroid.ViewHolders.TitleParentViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MyAdapter extends ExpandableRecyclerAdapter<TitleParentViewHolder, TitleChildViewHolder> {

    LayoutInflater inflater;
    private RequestQueue mQueue;
    private RequestQueue mQueue2;
    private RequestQueue mQueue3;

    public MyAdapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        inflater = LayoutInflater.from(context);

    }

    @Override
    public TitleParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.list_devices, viewGroup, false);
        final ImageView heartIcon = (ImageView) view.findViewById(R.id.faveIcon);
        final String url = "http://10.0.2.2:8080/api/devices";
        final TextView devNameView = (TextView) view.findViewById(R.id.parentTitle);

        mQueue = Volley.newRequestQueue(view.getContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String devName = devNameView.getText().toString();
                    JSONArray jsonArray = response.getJSONArray("devices");
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject device = jsonArray.getJSONObject(i);
                        if(devName.equals(device.getString("name"))){
                            String meta = device.getString("meta");
                            List<String> elephantList = Arrays.asList(meta.replace("{","").replace("}","").split(","));

                            if(elephantList.size() > 1 && elephantList.get(1).equals(" faved")){
                              heartIcon.setImageResource(R.drawable.heart_filled);
                              heartIcon.setTag(Integer.valueOf(R.drawable.heart_filled));
                            }else{
                                heartIcon.setTag(Integer.valueOf(R.drawable.heart_unfilled));
                            }
                        }
                    }
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

        final ImageView faveIcon = (ImageView) view.findViewById(R.id.faveIcon);

        faveIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                if(((Integer) faveIcon.getTag()).equals(R.drawable.heart_filled)) {
                    faveIcon.setImageResource(R.drawable.heart_unfilled);
                    heartIcon.setTag(Integer.valueOf(R.drawable.heart_unfilled));
                    //desfaveo en API
                    //hago request de devices hast encontrar el nombre coincidente
                    //llamo a unfave con el string id del device, el string type id, string name
                    // y el string en meta que lo q hace es
                    // hacer un request con el update


                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                String devName = devNameView.getText().toString();
                                JSONArray jsonArray = response.getJSONArray("devices");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject device = jsonArray.getJSONObject(i);
                                    if (devName.equals(device.getString("name"))) {
                                        String meta = device.getString("meta");
                                        String devId = device.getString("id");
                                        String typeId = device.getString("typeId");
                                        String name = device.getString("name");
                                        unfaveDevice(name, meta, devId, typeId);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("mytag", "Error unfaving");
                                error.printStackTrace();
                            }
                    });

                    mQueue.add(request);

                }else{
                    faveIcon.setImageResource(R.drawable.heart_filled);
                    heartIcon.setTag(Integer.valueOf(R.drawable.heart_filled));
                    //faveo

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                String devName = devNameView.getText().toString();
                                JSONArray jsonArray = response.getJSONArray("devices");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject device = jsonArray.getJSONObject(i);
                                    if (devName.equals(device.getString("name"))) {
                                        String meta = device.getString("meta");
                                        String devId = device.getString("id");
                                        String typeId = device.getString("typeId");
                                        String name = device.getString("name");
                                        faveDevice(name, meta, devId, typeId);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("mytag", "Error unfaving");
                            error.printStackTrace();
                        }
                    });

                    mQueue.add(request);
                }

            }
        });






        ImageView expandIcon = (ImageView) view.findViewById(R.id.expandArrow);
        expandIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                final String devName = devNameView.getText().toString();



                mQueue2 = Volley.newRequestQueue(v.getContext());
                String url = "http://10.0.2.2:8080/api/devices/";
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("devices");
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject device = jsonArray.getJSONObject(i);
                                if(devName.equals(device.getString("name"))){
                                    String typeId = device.getString("typeId");
                                    String name = device.getString("name");
                                    String devId = device.getString("id");
                                    openActivity(v, name, devId, typeId);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("mytag", "Error de response en expand");
                        error.printStackTrace();
                    }
                });

                mQueue2.add(request);







            }
        });

        return new TitleParentViewHolder(view);
    }

    private void unfaveDevice(final String name, final String meta, String devId, final String typeId) {
        String url = "http://10.0.2.2:8080/api/devices/" + devId;
        HashMap<String, String> mRequestParams = new HashMap<String, String>();
        mRequestParams.put("typeId",typeId);
        mRequestParams.put("name", name);


        String newMeta = "{" + meta.replace("{","").replace("}","").split(",")[0] + "}";
        mRequestParams.put("meta", newMeta);
        final JSONObject jsonObject = new JSONObject(mRequestParams);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // ok I guess
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

    private void faveDevice(final String name, final String meta, String devId, final String typeId) {
        String url = "http://10.0.2.2:8080/api/devices/" + devId;
        HashMap<String, String> mRequestParams = new HashMap<String, String>();
        mRequestParams.put("typeId",typeId);
        mRequestParams.put("name", name);

        String newMeta = "{" + meta.replace("{","").replace("}","").split(",")[0] + ", faved}";
        
        mRequestParams.put("meta", newMeta);
        final JSONObject jsonObject = new JSONObject(mRequestParams);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // ok I guess
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

    private void openActivity(final View v, final String name, final String devId, final String typeId) {
        mQueue3 = Volley.newRequestQueue(v.getContext());
        String url = "http://10.0.2.2:8080/api/devicetypes";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = response.getJSONArray("devices");
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject type = jsonArray.getJSONObject(i);
                        if(typeId.equals(type.getString("id"))){
                            String typeName = type.getString("name");
                            if(typeName.equals("blind")){
                                Intent intent2 = new Intent(v.getContext(), BlindActivity.class);
                                intent2.putExtra("devName", name);
                                intent2.putExtra("typeId", typeId);
                                intent2.putExtra("devId", devId);
                                v.getContext().startActivity(intent2);
                            }else if(typeName.equals("alarm")){
                                Intent intent2 = new Intent(v.getContext(), AlarmActivity.class);
                                intent2.putExtra("devName", name);
                                intent2.putExtra("typeId", typeId);
                                intent2.putExtra("devId", devId);
                                v.getContext().startActivity(intent2);
                            }else if(typeName.equals("refrigerator")){
                                Intent intent2 = new Intent(v.getContext(), RefrigeratorActivity.class);
                                intent2.putExtra("devName", name);
                                intent2.putExtra("typeId", typeId);
                                intent2.putExtra("devId", devId);
                                v.getContext().startActivity(intent2);
                            }else if(typeName.equals("lamp")){
                                Intent intent2 = new Intent(v.getContext(), LampActivity.class);
                                intent2.putExtra("devName", name);
                                intent2.putExtra("typeId", typeId);
                                intent2.putExtra("devId", devId);
                                v.getContext().startActivity(intent2);
                            }else if(typeName.equals("ac")){
                                Intent intent2 = new Intent(v.getContext(), ACActivity.class);
                                intent2.putExtra("devName", name);
                                intent2.putExtra("typeId", typeId);
                                intent2.putExtra("devId", devId);
                                v.getContext().startActivity(intent2);
                            }else if(typeName.equals("door")){
                                Intent intent2 = new Intent(v.getContext(), DoorActivity.class);
                                intent2.putExtra("devName", name);
                                intent2.putExtra("typeId", typeId);
                                intent2.putExtra("devId", devId);
                                v.getContext().startActivity(intent2);
                            }else{
                                //es oven
                                Intent intent2 = new Intent(v.getContext(), OvenActivity.class);
                                intent2.putExtra("devName", name);
                                intent2.putExtra("typeId", typeId);
                                intent2.putExtra("devId", devId);
                                v.getContext().startActivity(intent2);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("mytag", "Error de response en openActivity");
                error.printStackTrace();
            }
        });
        mQueue3.add(request);

    }

    @Override
    public TitleChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.list_child, viewGroup, false);
        return new TitleChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(TitleParentViewHolder titleParentViewHolder, int i, Object o) {
        TitleParent title = (TitleParent) o;
        titleParentViewHolder._textView.setText(title.getTitle());
    }

    @Override
    public void onBindChildViewHolder(TitleChildViewHolder titleChildViewHolder, int i, Object o) {
        TitleChild title = (TitleChild) o;
        titleChildViewHolder.option1.setText(title.getOption1());
        titleChildViewHolder.option2.setText(title.getOption2());
    }
}
