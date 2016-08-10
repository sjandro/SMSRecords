package com.asalazar.alex.smsrecords;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;


/**
 * Created by sjand on 12/14/2015.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    List<String> mItems;
    SMSDictionary records;
    DateSort dateSort;
    Context context;
    HashMap<String, String> CONTACTS;
    Utilities utilities;
    Activity activity;

    public View view;

    public CardAdapter(SMSDictionary texts, Context con, Activity activity) {
        super();
        context = con;
        this.activity = activity;
        utilities = new Utilities();
        records = texts;
        dateSort = new DateSort(records.getRecords(), 2);
        CONTACTS = utilities.ReadPhoneContacts(context);
        mItems = dateSort.getSortedDates();
    }

    public String getItemString(int i){
        return mItems.get(i);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_card_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Utilities utilities = new Utilities();
        String[] parts = mItems.get(i).split(",");
        String color;

        String contact = CONTACTS.get(parts[0]);
        //System.out.println(contact);
        if(contact == null)
            contact = "NOT_IN CONTACTS";

        if(contact.equals("NOT_IN CONTACTS"))
            viewHolder.contactName.setText(parts[0]);
        else
            viewHolder.contactName.setText(contact);

        if(Character.isDigit(parts[0].substring(parts[0].length() - 1).toCharArray()[0])){
            color = utilities.chooseColor(Integer.parseInt(parts[0].substring(parts[0].length() - 1)));
        }
        else
            color = utilities.chooseColor(-1);
        viewHolder.myLayout.setBackgroundColor(Color.parseColor(color));
        if(parts[0] != null) {
            PrivateData hash = new PrivateData(parts[0].substring(3, parts[0].length()));
            viewHolder.phoneNumberHash.setText(hash.getNumber().substring(1,10));
            viewHolder.phoneNumber.setText(parts[0]);//.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1)-$2-$3"));

        }
        if(parts[1] != null)
            viewHolder.Received.setText(parts[1]);
        if(parts[2] != null)
            viewHolder.sent.setText(parts[2]);

    }

    @Override
    public int getItemCount() {return mItems.size();}

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView phoneNumberHash;
        public TextView phoneNumber;
        public TextView Received;
        public TextView sent;
        public TextView contactName;
        public RelativeLayout myLayout;
        public ImageButton infoButtn;

        public ViewHolder(View itemView) {
            super(itemView);
            phoneNumberHash = (TextView)itemView.findViewById(R.id.phoneNumberLableHash);
            phoneNumber = (TextView)itemView.findViewById(R.id.phoneNumberLable);
            Received = (TextView)itemView.findViewById(R.id.received);
            sent = (TextView)itemView.findViewById(R.id.sent);
            myLayout = (RelativeLayout) itemView.findViewById(R.id.topHalf);
            contactName = (TextView)itemView.findViewById(R.id.contactNameLable);
            infoButtn = (ImageButton) itemView.findViewById(R.id.infoBttn);

            view = itemView;
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    newActivity(v);
                }
            });

            infoButtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    utilities.toastMsg("Server Sees: " + phoneNumberHash.getText(), context);
                }
            });



        }
    }

    public void newActivity(View view){
        final TextView phoneNumber, received, sent;
        phoneNumber = (TextView) view.findViewById(R.id.phoneNumberLable);
        received = (TextView) view.findViewById(R.id.received);
        sent = (TextView) view.findViewById(R.id.sent);
        final String[] temp = phoneNumber.getText().toString().split(" ");
        Bundle objectBundle = new Bundle();
        Bundle number = new Bundle();
        Bundle receivedText = new Bundle();
        Bundle sentText = new Bundle();
        Bundle myNumber = new Bundle();
        objectBundle.putParcelable("object", records);

        number.putString("num", phoneNumber.getText().toString());
        receivedText.putString("received", received.getText().toString());
        sentText.putString("sent", sent.getText().toString());
        myNumber.putString("myNum", utilities.getMyNumber(context));


        Intent intent = new Intent(activity, ExpanedView.class);
        intent.putExtras(objectBundle);
        intent.putExtras(number);
        intent.putExtras(receivedText);
        intent.putExtras(sentText);
        intent.putExtras(myNumber);
        //intent.putExtras(recordsBundle);
        animateTransition(view, intent);
        //startActivity(intent);
    }

    public void animateTransition(View view, Intent intent){
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,

                new Pair<View, String>(view.findViewById(R.id.topHalf),
                        activity. getString(R.string.transition_name_top)),
//                new Pair<View, String>(view.findViewById(R.id.phoneNumberLable),
//                        getString(R.string.transition_name_number)),
                new Pair<View, String>(view.findViewById(R.id.received),
                        activity. getString(R.string.transition_name_received)),
                new Pair<View, String>(view.findViewById(R.id.sent),
                        activity. getString(R.string.transition_name_sent)),
                new Pair<View, String>(view.findViewById(R.id.receivedLabel),
                        activity. getString(R.string.transition_name_received_label)),
                new Pair<View, String>(view.findViewById(R.id.sentLabel),
                        activity. getString(R.string.transition_name_sent_label))
        );
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }
}



