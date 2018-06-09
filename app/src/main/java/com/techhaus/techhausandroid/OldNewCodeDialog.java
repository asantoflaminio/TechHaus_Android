package com.techhaus.techhausandroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class OldNewCodeDialog extends AppCompatDialogFragment {

    private EditText editOldCode;
    private EditText editNewCode;
    private OldNewCodeDialog.OldNewCodeDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OldNewCodeDialog.OldNewCodeDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement OldNewCodeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog2, null);
        builder.setView(view);
        builder.setTitle("Change code");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String code1 = editOldCode.getText().toString();
                String code2 = editNewCode.getText().toString();
                listener.applyCodes(code1, code2);
            }
        });
        editOldCode = view.findViewById(R.id.edit_oldcode);
        editNewCode = view.findViewById(R.id.edit_newcode);
        return builder.create();
    }

    public interface OldNewCodeDialogListener{
        void applyCodes(String oldcode, String newcode);
    }
}
