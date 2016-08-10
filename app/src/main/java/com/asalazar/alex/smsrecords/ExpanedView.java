package com.asalazar.alex.smsrecords;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.graphics.Color;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sjand on 2/17/2016.
 */
public class ExpanedView extends ActionBarActivity{

    public SMSDictionary records;
    public Context context;
    Utilities utilities;
    ArrayList<String> days;
    ArrayList<String> months;

    List<String> selectedDates;

    public TextView hash,phone_number,contactName, textSent, textReceived;
    public BarChart chart;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);
        utilities = new Utilities();
        chart = (BarChart) findViewById(R.id.chart);
        hash = (TextView)findViewById(R.id.DETAILS_number_hash);
        contactName = (TextView) findViewById(R.id.DETAILS_contact_name);
        phone_number = (TextView)findViewById(R.id.DETAILS_number);
        textReceived = (TextView)findViewById(R.id.DETAILS_received);
        textSent = (TextView)findViewById(R.id.DETAILS_sent);
        context = this;
        records = getIntent().getExtras().getParcelable("object");
        String number = getIntent().getExtras().getString("num");
        String received = getIntent().getExtras().getString("received");
        String sent = getIntent().getExtras().getString("sent");
        String myNum = getIntent().getExtras().getString("myNum");
        //dates = getIntent().getExtras().getString("records");
        selectedDates = records.theseDates(number);

        PrivateData hashNumber = new PrivateData(number.substring(3, number.length()));
        hash.setText(hashNumber.getNumber().substring(1,10));

        HashMap<String,String> contacts = utilities.ReadPhoneContacts(this);
        String contact = contacts.get(number);
        //System.out.println(contact);
        if(contact == null)
            contact = "NOT_IN CONTACTS";

        if(contact.equals("NOT_IN CONTACTS"))
            contactName.setText(number);
        else
            contactName.setText(contact);

        phone_number.setText(number);
        textReceived.setText(received);
        textSent.setText(sent);
        try {
            getDays();
            getMonth();
        }
        catch (Exception ex){
            System.out.println(ex);
        }
        setChart(number);
        setRecyclerView(number, myNum);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action) {
            BarData data = new BarData(getXAxisValuesMonths(), getDataSet("month"));
            chart.setData(data);
            chart.setDescription("");
            //chart.animateXY(2000, 2000);
            chart.invalidate();
            chart.getAxisLeft().setDrawGridLines(false);
            chart.getAxisRight().setDrawGridLines(false);
            chart.setScaleYEnabled(false);
            return true;
        }
        if (id == R.id.action_day){
            BarData data = new BarData(getXAxisValuesDays(), getDataSet("days"));
            chart.setData(data);
            chart.setDescription("");
            //chart.animateXY(2000, 2000);
            chart.invalidate();
            chart.getAxisLeft().setDrawGridLines(false);
            chart.getAxisRight().setDrawGridLines(false);
            chart.setScaleYEnabled(false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setRecyclerView(String num, String myNum){
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        rv.setAdapter(new MessageLogs(selectedDates, context, num, myNum));
    }

    private int getCount(){
        return 0;
    }

    private void setChart(String myNum){
        RelativeLayout topLayout = (RelativeLayout) findViewById(R.id.topContainer);
        BarData data = new BarData(getXAxisValuesDays(), getDataSet("days"));
        chart.setData(data);
        chart.setDescription("");
        chart.animateXY(2000, 2000);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.setScaleYEnabled(false);
        //chart.getXAxis().setDrawGridLines(false);
        chart.setVisibleXRangeMaximum(20);
        //chart.moveViewToX(7);


        XAxis xAxis = chart.getXAxis();
        xAxis.setTextColor(Color.parseColor("#ffffff"));
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(Color.parseColor("#ffffff"));
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setTextColor(Color.parseColor("#ffffff"));
        Legend l = chart.getLegend();
        l.setTextColor(Color.parseColor("#ffffff"));
        String color;
        String darkColor;
        int oval;
        ImageButton ib = (ImageButton) findViewById(R.id.DETAILS_circle);
        if(Character.isDigit(myNum.substring(myNum.length() - 1).toCharArray()[0])){
            color = utilities.chooseColor(Integer.parseInt(myNum.substring(myNum.length() - 1)));
            darkColor = utilities.chooseDarkColor(Integer.parseInt(myNum.substring(myNum.length() - 1)));
            oval =  utilities.getOval(Integer.parseInt(myNum.substring(myNum.length() - 1)), context);
        }
        else {
            color = utilities.chooseColor(-1);
            darkColor = utilities.chooseDarkColor(-1);
            oval =  utilities.getOval(-1, context);
        }

        chart.setBackgroundColor(Color.parseColor(color));
        topLayout.setBackgroundColor(Color.parseColor(color));

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setElevation(0);


        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.parseColor(darkColor));
            bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
            //ib.setBackground(oval);
            ib.setBackgroundResource(oval);
        }

        chart.invalidate();
    }


    private ArrayList<IBarDataSet> getDataSet(String type) {
        ArrayList<String> data;
        if(type.equals("days"))
            data = days;
        else
            data = months;
        ArrayList<IBarDataSet> dataSets = null;
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        ArrayList<BarEntry> valueSet2 = new ArrayList<>();

        for(int i = 0; i < data.size(); ++i){
            String[] temp = data.get(i).split(",");

            BarEntry v1e1 = new BarEntry(Integer.parseInt(temp[1]), i);
            valueSet1.add(v1e1);

            BarEntry v2e1 = new BarEntry(Integer.parseInt(temp[2]), i);
            valueSet2.add(v2e1);
        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Received");
        barDataSet1.setColor(Color.rgb(0, 204, 0));
        //barDataSet1.setValueTextColor(Color.parseColor("#ffffff"));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Sent");
        barDataSet2.setColor(Color.rgb(25, 118, 210));
        //barDataSet2.setValueTextColor(Color.parseColor("#ffffff"));

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        return dataSets;
    }

    private List<String> getXAxisValuesDays() {
        List<String> xAxis = new ArrayList<>();
        for(int i = 0; i < days.size(); ++i){
            xAxis.add(days.get(i).split(",")[0]);
        }
        return xAxis;
    }

    private List<String> getXAxisValuesMonths() {
        List<String> xAxis = new ArrayList<>();
        for(int i = 0; i < months.size(); ++i){
            xAxis.add(months.get(i).split(",")[0]);
        }
        return xAxis;
    }

    private void getDays(){
        days = new ArrayList<>();
        int receivedCount = 0, sentCount = 0;
        int count = 0;

        String currentDay = utilities.returnDate(selectedDates.get(0).split(",")[1]).split(" ")[1] +" "+ utilities.returnDate(selectedDates.get(0).split(",")[1]).split(" ")[2];

        for(String day : selectedDates){
            String[] infoParts = day.split(",");
            String nextDay = utilities.returnDate(infoParts[1]).split(" ")[1] + " " + utilities.returnDate(infoParts[1]).split(" ")[2];

            if(!currentDay.equals(nextDay)){
                days.add(count,currentDay+","+receivedCount+","+sentCount);
                currentDay = nextDay;
                receivedCount = 0; sentCount = 0;
                count++;
            }

            if(infoParts[2].equals("RECEIVED"))
                receivedCount++;
            else if(infoParts[2].equals("SENT"))
                sentCount++;

        }


            days.add(count , currentDay+","+receivedCount+","+sentCount);


    }


    private void getMonth(){
        months = new ArrayList<String>();

        int receivedCount = 0, sentCount = 0;
        int count = 0;

        String currentMonth = utilities.returnDate(selectedDates.get(0).split(",")[1]).split(" ")[1];// +" "+ utilities.returnDate(selectedDates.get(0).split(",")[1]).split(" ")[2];

        for(String day : selectedDates){
            String[] infoParts = day.split(",");
            String nextMonth = utilities.returnDate(infoParts[1]).split(" ")[1];

            if(!currentMonth.equals(nextMonth)){
                months.add(count,currentMonth+","+receivedCount+","+sentCount);
                currentMonth = nextMonth;
                receivedCount = 0; sentCount = 0;
                count++;
            }

            if(infoParts[2].equals("RECEIVED"))
                receivedCount++;
            else if(infoParts[2].equals("SENT"))
                sentCount++;

        }

        months.add(count , currentMonth+","+receivedCount+","+sentCount);
    }

    public void onClickFAB(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage("Do you want to create a csv log for " + phone_number.getText().toString() + "?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
            utilities.writeTimeCVS(phone_number.getText().toString().replaceAll("[+()\\s-]+", ""), records);
            utilities.toastMsg("Logs for the number " + phone_number.getText().toString() + " have been created.", context);
        }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        }).show();
    }

}
