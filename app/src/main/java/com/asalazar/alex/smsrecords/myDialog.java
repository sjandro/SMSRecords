package com.asalazar.alex.smsrecords;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Salazar on 8/1/15.
 */
public class myDialog extends DialogFragment {
    private EditText response;
    private View view;
    private TextView tv;
    private String message;
    private String hash;
    private int id;
    public myDialog(){}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        view=inflater.inflate(R.layout.dialog,null);
        response = (EditText)view.findViewById(R.id.response);
        builder.setView(view);
        builder.setCancelable(false);
        message = getArguments().getString("message");
        hash = getArguments().getString("hash");
        tv = (TextView) view.findViewById(R.id.titleBar);

        try {
            tv.setText(message.split("#@!sPlIt!@#")[0]);
            id = Integer.valueOf(message.split("#@!sPlIt!@#")[1]);

            builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    response = (EditText) view.findViewById(R.id.response);

                    if (response.getText().toString().length() > 0) {
                        Runnable r = new ReplyToMessage(id, hash, response.getText().toString());
                        new Thread(r).start();
                    }


                    Toast.makeText(getActivity(), "Sent!", Toast.LENGTH_LONG).show();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Runnable r = new ReplyToMessage(id, hash, "cancel");
                    new Thread(r).start();
                }
            });

            return builder.create();
        }
        catch (Exception ex){
            System.out.println("myDialog: " + ex);
            tv.setText("Database is currently disconnected...");
            response.setVisibility(View.GONE);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });


            return builder.create();
        }
    }
}
