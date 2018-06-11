package com.techhaus.techhausandroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class ColorPicker extends AppCompatDialogFragment {
    private EditText editTextCode;
    private ColorPickerListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ColorPickerListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement ColorPickerListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.color_picker, null);
        builder.setView(view);
        builder.setTitle("Enter HEX code");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String code = editTextCode.getText().toString();
                if(code.length() != 6 && !code.matches("[0-9A-F]+")){
                    //mostrar letras en rojo o algo
                    Log.d("mytag", "A la grande le puse CUCA");
                }
                listener.applyHex(code);
            }
        });
        editTextCode = view.findViewById(R.id.edit_color);
        editTextCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String code = s.toString().toUpperCase();
                if(code.length() == 6 && code.matches("[0-9A-F]+")) {
                    ImageView circ = (ImageView) view.findViewById(R.id.circle);
                    GradientDrawable drawable = (GradientDrawable) circ.getDrawable();
                    // drawable.setColor(Integer.parseInt("F0"+color,16));
                    drawable.setColor(Color.parseColor("#F0" + code));
                }

            }
        });

        return builder.create();
    }

    public interface ColorPickerListener{
        void applyHex(String hexcode);
    }
}
