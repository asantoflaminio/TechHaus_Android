package com.techhaus.techhausandroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class ColorPicker extends AppCompatDialogFragment {
    private EditText editTextCode;
    private ColorPickerListener listener;
    Spinner color_spinner;
    String selectedColor;

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
    public void onResume() {
        super.onResume();

        final AlertDialog dialog = (AlertDialog)getDialog();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedColor.length() != 6 || !selectedColor.matches("[0-9A-F]+")){
                    dialog.setTitle(getString(R.string.MustChoose));


                } else{
                    listener.applyHex(selectedColor);
                    dialog.dismiss();
                }
            }
        });
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        selectedColor = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.color_picker, null);
        builder.setView(view);
        builder.setTitle(R.string.ChooseColor);
        builder.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

       builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               /* String code = editTextCode.getText().toString();
                if(code.length() != 6 && !code.matches("[0-9A-F]+")){
                    Log.d("mytag", "WRONG LISTENER inside if");
                }
                listener.applyHex(code);*/
            }
        });


        color_spinner = (Spinner) view.findViewById(R.id.edit_color);
        final List<SpinnerData> customList1 = new ArrayList<>();
        customList1.add(new SpinnerData(R.drawable.lapiz, getString(R.string.ChooseColor)));
        customList1.add(new SpinnerData(R.drawable.red, getString(R.string.Red)));
        customList1.add(new SpinnerData(R.drawable.pink, getString(R.string.Pink)));
        customList1.add(new SpinnerData(R.drawable.green, getString(R.string.Green)));
        customList1.add(new SpinnerData(R.drawable.blue, getString(R.string.Blue)));
        customList1.add(new SpinnerData(R.drawable.violet, getString(R.string.Violet)));
        customList1.add(new SpinnerData(R.drawable.yellow, getString(R.string.Yellow)));
        customList1.add(new SpinnerData(R.drawable.orange, getString(R.string.Orange)));
        customList1.add(new SpinnerData(R.drawable.white, getString(R.string.White)));

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(view.getContext(), R.layout.spinner_layout, customList1);
        color_spinner.setAdapter(customSpinnerAdapter);

        color_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                String code = "";
                Log.d("mytag", "bueno el icon name es " + customList1.get(i).getIconName());
                if(customList1.get(i).getIconName().equals(getString(R.string.Red))){
                    code = "FF0000";
                }else if(customList1.get(i).getIconName().equals(getString(R.string.Pink))){
                    Log.d("mytag", "Ok flaco estoy aca");
                    code = "FF69B4";
                }else if(customList1.get(i).getIconName().equals(getString(R.string.Green))){
                    code = "00FF00";
                }else if(customList1.get(i).getIconName().equals(getString(R.string.Blue))){
                    code = "0000FF";
                }else if(customList1.get(i).getIconName().equals(getString(R.string.Violet))){
                    code = "8A2BE2";
                }else if(customList1.get(i).getIconName().equals(getString(R.string.Yellow))){
                    code = "FFFF00";
                }else if(customList1.get(i).getIconName().equals(getString(R.string.Orange))){
                    code = "FF8C00";
                }else if (customList1.get(i).getIconName().equals(getString(R.string.White))){
                    //white
                    code = "FFFFFF";
                }
                selectedColor = code;
                 //   ImageView circ = (ImageView) view.getRootView().findViewById(R.id.circle);
                   // GradientDrawable drawable = (GradientDrawable) circ.getDrawable();
                  //  drawable.setColor(Color.parseColor("#F0" + code));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return builder.create();
    }

    public interface ColorPickerListener{
        void applyHex(String hexcode);
    }
}
