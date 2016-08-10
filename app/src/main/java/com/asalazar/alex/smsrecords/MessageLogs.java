package com.asalazar.alex.smsrecords;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sjand on 2/18/2016.
 */
public class MessageLogs extends RecyclerView.Adapter<MessageLogs.ViewHolder> {
    List<String> mItems;
    List<String> dateItems;
    Utilities utilities;
    Context context;
    String contactNumber;
    String myNum;
    SMSDictionary records;
    //test_branch
    //kljhsc;kldhf;klejhg;


    public View view;

    public MessageLogs(List<String> dates, Context activity, String number, String myNum) {
        super();
        this.myNum = myNum;
        contactNumber = number;
        context = activity;
        mItems = new ArrayList<>();
        dateItems = new ArrayList<>();
        utilities = new Utilities();
        for(int i = 0; i < dates.size(); ++i){
            mItems.add(dates.get(i).split(",")[2]);
            dateItems.add(utilities.returnDate(dates.get(i).split(",")[1]));
        }

//        int k = mItems.size();
//        if ( k > 100 )
//            mItems.subList(100, k).clear();
//
//        int j = dateItems.size();
//        if ( k > 100 )
//            dateItems.subList(100, j).clear();
    }

    public String getItemString(int i){
        return mItems.get(i);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_view, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.item.setText(mItems.get(i));
        viewHolder.date.setText(dateItems.get(i));
    }

    @Override
    public int getItemCount() {return mItems.size();}

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView item;
        public TextView date;
        public Button viewButton;

        public ViewHolder(final View itemView) {
            super(itemView);
            item = (TextView)itemView.findViewById(R.id.item);
            date = (TextView) itemView.findViewById(R.id.date);
            viewButton = (Button) itemView.findViewById(R.id.viewButton);

            viewButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    viewButtonClick(date.getText().toString(), item.getText().toString());
                }
            });
        }
    }

    public void viewButtonClick(String d, String inboxOrOutbox){
        //final String address, theDate;
        Uri inbox;
        if(inboxOrOutbox.equals("RECEIVED"))
            inbox = Uri.parse("content://sms/inbox");
        else if(inboxOrOutbox.equals("SENT"))
            inbox = Uri.parse("content://sms/sent");
        else
            inbox = Uri.parse("content://sms/inbox");
        String[] reqCols = new String[]{"_id","address","body","read","date"};
        final Cursor cursor = context.getContentResolver().query(inbox, reqCols,null,null,null);
        //Cursor simCur = context.getContentResolver().query(sim,reqCols,null,null,null);

        cursor.moveToFirst();
        while(true) {
            String formattedAddress = cursor.getString(cursor.getColumnIndex("address")).replaceAll("[+()\\s-]+", "");
            final String address = (formattedAddress.startsWith("1")) ? formattedAddress.substring(1) : formattedAddress;
            final String theDate = utilities.returnDate(cursor.getString(cursor.getColumnIndex("date")));

            if(address.equals(contactNumber) && theDate.equals(d)){
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                final String message = cursor.getString(cursor.getColumnIndex("body")).replace("'", "").replace("\"", "");

                builder.setMessage("Message: \n\n"+cursor.getString(cursor.getColumnIndex("body"))).setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {


                        PrivateData privateData = new PrivateData(address.substring(3, address.length()));
                        PrivateData privateData1 = new PrivateData(myNum.substring(3, myNum.length()));
                        PrivateData idHash = new PrivateData(privateData.getNumber() + theDate);
                        Runnable r = new SendTextMessageToServer(idHash.getNumber(), privateData.getNumber(), theDate, message, privateData1.getNumber());
                        new Thread(r).start();

                        utilities.toastMsg("Message Sent!", context);


                    }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            }).show();
                break;
            }

            if(!cursor.moveToNext())
                break;
        }
        cursor.close();
    }
}
