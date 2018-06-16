package com.techhaus.techhausandroid.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.techhaus.techhausandroid.API;
import com.techhaus.techhausandroid.Models.TitleChild;
import com.techhaus.techhausandroid.Models.TitleParent;
import com.techhaus.techhausandroid.NotificationsActivity;
import com.techhaus.techhausandroid.R;
import com.techhaus.techhausandroid.RoutActivity;
import com.techhaus.techhausandroid.RoutinesActivity;
import com.techhaus.techhausandroid.ViewHolders.TitleChildViewHolder;
import com.techhaus.techhausandroid.ViewHolders.TitleParentViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;



public class MyAdapterRoutines extends ExpandableRecyclerAdapter<TitleParentViewHolder, TitleChildViewHolder> {

    LayoutInflater inflater;
    private RequestQueue mQueue;
   // private RequestQueue mQueue2;

    public MyAdapterRoutines(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        inflater = LayoutInflater.from(context);

    }

    @Override
    public TitleParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {


        View view = inflater.inflate(R.layout.list_routines, viewGroup, false);
        ImageView playIcon = (ImageView) view.findViewById(R.id.playRoutine);
        final TextView rutNameView = (TextView) view.findViewById(R.id.parentTitle);
        final RelativeLayout relativeLay = (RelativeLayout) view.findViewById(R.id.rel_lay);

        playIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                final String url = API.getRoutines();
                final String rutName = rutNameView.getText().toString();
                Toast.makeText(v.getContext(), rutName + " was played",
                        Toast.LENGTH_LONG).show();
                mQueue = Volley.newRequestQueue(v.getContext());
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String routID = "";
                            JSONArray jsonArray = response.getJSONArray("routines");
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject routine = jsonArray.getJSONObject(i);
                                if(rutName.equals(routine.getString("name"))){
                                    routID = routine.getString("id");
                                    executeRoutine(v, routID);
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

            }
        });

        relativeLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rutName = rutNameView.getText().toString();
                Intent intent2 = new Intent(v.getContext(), RoutActivity.class);
                intent2.putExtra("rutName", rutName);
                v.getContext().startActivity(intent2);
            }
        });


        return new TitleParentViewHolder(view);
    }

    private void executeRoutine(View v, String routID) {
       // mQueue2 = Volley.newRequestQueue(v.getContext());
        String url = API.getRoutines() + routID + "/execute";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("mytag", "Executed!");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("mytag", "Error de response en execute");
                error.printStackTrace();
            }
        });

        mQueue.add(request);

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
