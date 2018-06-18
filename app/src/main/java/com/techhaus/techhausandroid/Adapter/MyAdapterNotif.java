package com.techhaus.techhausandroid.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.techhaus.techhausandroid.R;
import com.techhaus.techhausandroid.RoutActivity;
import com.techhaus.techhausandroid.ViewHolders.TitleChildViewHolder;
import com.techhaus.techhausandroid.ViewHolders.TitleParentViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class MyAdapterNotif  extends ExpandableRecyclerAdapter<TitleParentViewHolder, TitleChildViewHolder> {
    Set<String> myNotif;
    LayoutInflater inflater;
    private RequestQueue mQueue;

    public MyAdapterNotif(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        inflater = LayoutInflater.from(context);

    }

    @Override
    public TitleParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {



        View view = inflater.inflate(R.layout.list_notifications, viewGroup, false);

        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        myNotif = sharedPreferences.getStringSet("notifications", null);

        ImageView tacho = (ImageView) view.findViewById(R.id.deleteNotif);
        final TextView notiTitle = (TextView) view.findViewById(R.id.parentTitle);

        tacho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mytag", "notititle es " + notiTitle.getText());
                String[] mytext = notiTitle.getText().toString().replace("\nUpdate", "").replace("\nActualización", "").split(":");
                for(String s: myNotif){
                    if(s.contains(mytext[0]) && s.contains(mytext[1]) && s.contains(mytext[2]) && s.contains(mytext[3].replace(" ", "")) ){
                        Log.d("mytag", "Match!");
                        myNotif.remove(s);
                        editor.putStringSet("notifications", myNotif);
                        editor.commit();

                    }

                }
            }
        });

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
