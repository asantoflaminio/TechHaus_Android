package com.techhaus.techhausandroid.ViewHolders;

import android.media.Image;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.techhaus.techhausandroid.R;

public class TitleParentViewHolder extends ParentViewHolder{

    public TextView _textView;
    public ImageButton _imageView;


    public TitleParentViewHolder(View itemView) {
        super(itemView);
        _textView = (TextView) itemView.findViewById(R.id.parentTitle);
        //_imageView = (ImageView) itemView.findViewById(R.id.expandArrow);


    }
}
