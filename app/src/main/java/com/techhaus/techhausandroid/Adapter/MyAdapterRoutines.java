package com.techhaus.techhausandroid.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.techhaus.techhausandroid.Models.TitleChild;
import com.techhaus.techhausandroid.Models.TitleParent;
import com.techhaus.techhausandroid.NotificationsActivity;
import com.techhaus.techhausandroid.R;
import com.techhaus.techhausandroid.RoutActivity;
import com.techhaus.techhausandroid.RoutinesActivity;
import com.techhaus.techhausandroid.ViewHolders.TitleChildViewHolder;
import com.techhaus.techhausandroid.ViewHolders.TitleParentViewHolder;

import java.util.List;



public class MyAdapterRoutines extends ExpandableRecyclerAdapter<TitleParentViewHolder, TitleChildViewHolder> {

    LayoutInflater inflater;

    public MyAdapterRoutines(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        inflater = LayoutInflater.from(context);

    }

    @Override
    public TitleParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.list_routines, viewGroup, false);
        ImageView playIcon = (ImageView) view.findViewById(R.id.playRoutine);
        final TextView rutNameView = (TextView) view.findViewById(R.id.parentTitle);
        playIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String rutName = rutNameView.getText().toString();
                Toast.makeText(v.getContext(), rutName + " was played",
                        Toast.LENGTH_LONG).show();

            }
        });

        ImageView expandIcon = (ImageView) view.findViewById(R.id.expandArrow);
        expandIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String rutName = rutNameView.getText().toString();
                Intent intent2 = new Intent(v.getContext(), RoutActivity.class);
                intent2.putExtra("rutName", rutName);
                v.getContext().startActivity(intent2);
                //aca abro un nuevo intent activity etc

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
