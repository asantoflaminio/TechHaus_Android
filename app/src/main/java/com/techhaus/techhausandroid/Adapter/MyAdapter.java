package com.techhaus.techhausandroid.Adapter;

import android.content.Context;
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
import com.techhaus.techhausandroid.Models.TitleChild;
import com.techhaus.techhausandroid.Models.TitleParent;
import com.techhaus.techhausandroid.R;
import com.techhaus.techhausandroid.ViewHolders.TitleChildViewHolder;
import com.techhaus.techhausandroid.ViewHolders.TitleParentViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyAdapter extends ExpandableRecyclerAdapter<TitleParentViewHolder, TitleChildViewHolder> {

    LayoutInflater inflater;
    private RequestQueue mQueue;

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
                            if(elephantList.size() > 1){
                                Log.d("mytag", "Segundo elem es " + elephantList.get(1));
                            }

                            if(elephantList.size() > 1 && elephantList.get(1).equals(" faved")){
                                Log.d("mytag", "Encontre un faved");
                              heartIcon.setImageResource(R.drawable.heart_filled);
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
        return new TitleParentViewHolder(view);
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
