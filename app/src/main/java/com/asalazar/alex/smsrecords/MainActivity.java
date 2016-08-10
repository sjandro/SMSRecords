package com.asalazar.alex.smsrecords;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.os.Handler;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

//    private SendToDatabaseBackgroundService mService = null;
//
//    private boolean mIsBound;

    private PrivateData myNumberHash;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapterNav;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    SMSDictionary records;
    Utilities utilities;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    Context context;

    SwipeRefreshLayout mSwipeRefreshLayout;
    SharedPreferences sharedpref;
    public static final String mypreference = "mypref";


    //ArrayList<SMSRecordObj> mySMSRecordObjs = new ArrayList<>();

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message message){
            myDialog myDialog = new myDialog();
            Bundle args = new Bundle();
            args.putString("message", message.getData().getString("msg"));
            args.putString("hash", myNumberHash.getNumber());
            myDialog.setArguments(args);
            myDialog.setCancelable(false);
            myDialog.show(getSupportFragmentManager(), "My Dialog");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        context = this;
        utilities = new Utilities();
        myNumberHash = new PrivateData(utilities.getMyNumber(context).substring(3,utilities.getMyNumber(context).length()));
        getPermission();
        populateCardView();

        //nav drawer
        mDrawerList = (ListView)findViewById(R.id.navList); mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        addDrawerItems(); setupDrawer();
        //nav drawer end

        //Recylcer view
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright);//,
//                android.R.color.holo_red_light);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateCardView();
                refresh();
//                Runnable r = new AddRecordThread(mySMSRecordObjs);
//                new Thread(r).start();
                checkformessage();
            }
        });
        //Recycler view end

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent backgroundService = new Intent(this, SendToDatabaseBackgroundService.class);

        startService(backgroundService);
    }

    private void checkformessage() {
        Runnable r = new CheckForMessage(myNumberHash.getNumber(), mHandler);
        new Thread(r).start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        //This is the menu button. No longer needed for now -Alex(2/17/16)
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        //Navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void fetchInbox()
    {
        Uri inbox = Uri.parse("content://sms/inbox");
        Uri sim = Uri.parse("content://sms/sent");
        String[] reqCols = new String[]{"_id","address","body","read","date"};
        Cursor cursor = getContentResolver().query(inbox, reqCols,null,null,null);
        Cursor simCur = getContentResolver().query(sim,reqCols,null,null,null);
        boolean inboxCountZero = cursor.getCount() == 0;
        boolean outboxCountZero = simCur.getCount() == 0;
        if(inboxCountZero && outboxCountZero){
            records.insertSentItem("No messages", Integer.toString(0));
            cursor.close();
            simCur.close();
            return;
        }
        else{
            try {
                storeMessageCount(cursor, "received");
                storeMessageCount(simCur, "sent");
            }
            catch (Exception ex){
                System.out.println(ex);
            }

            records.sortDates();

            System.out.println("Received: ");
            System.out.println("-----------------------------");
            records.printDict("received");
            System.out.println("-----------------------------");
            System.out.println();
            System.out.println("Sent: ");
            System.out.println("-----------------------------");
            records.printDict("sent");
            System.out.println("-----------------------------");
        }
    }

    public void storeMessageCount(Cursor cursor, String type){
        String dateType;
        if(type.equals("received")) {
            dateType = "RECEIVED";
        }
        else{
            dateType = "SENT";
        }

        String address;
        cursor.moveToFirst();
        while(true) {
            int message = 1;
            String date = utilities.returnDate(cursor.getString(cursor.getColumnIndex("date")));

            address = cursor.getString(cursor.getColumnIndex("address"));
            address = address.replaceAll("[+()\\s-]+", "");
            if(address.startsWith("1"))
                address = address.substring(1);
//
//            //Data that is sent to the database
//            PrivateData privateData = new PrivateData(address.substring(3, address.length()));
//
//            SMSRecordObj mySMSRecordObj = new SMSRecordObj();
//
//            mySMSRecordObj.setNum(privateData.getNumber());
//            mySMSRecordObj.setSr(dateType);
//            mySMSRecordObj.setDate(date);
//            //System.out.println(date);
//            mySMSRecordObj.setTextLength(cursor.getString(cursor.getColumnIndex("body")).length());
//            mySMSRecordObj.setMyNum(myNumberHash.getNumber());
//            mySMSRecordObj.setMessage("");
//            mySMSRecordObj.setDateMilliseconds(cursor.getString(cursor.getColumnIndex("date")));
//
//            String dateParts = utilities.returnDate(cursor.getString(cursor.getColumnIndex("date")));
//            PrivateData idHash = new PrivateData(privateData.getNumber() + dateParts);
//            mySMSRecordObj.setId(idHash.getNumber());
//            mySMSRecordObjs.add(mySMSRecordObj);
//            //Data that is sent to the database

            records.storeDates(address + "," + cursor.getString(cursor.getColumnIndexOrThrow("date")) + "," + dateType);
            //records.storeDates(privateData.getNumber() + "," + cursor.getString(cursor.getColumnIndexOrThrow("date")) + "," + dateType);

            if (records.containThisKey(address, type)) {
                if(type.equals("received")) {
                    message += Integer.parseInt(records.getTextReceived(address));
                    records.insertReceivedItem(address, Integer.toString(message));
                }
                else if(type.equals("sent")){
                    message += Integer.parseInt(records.getTextSent(address));
                    records.insertSentItem(address, Integer.toString(message));
                }
            }
            else {
                if (type.equals("received"))
                    records.insertReceivedItem(address, Integer.toString(1));
                else
                    records.insertSentItem(address, Integer.toString(1));
            }

            if(!cursor.moveToNext())
                break;
        }
        System.out.println("---test---");
        records.test();
        System.out.println("---test---");
        cursor.close();
    }

    public void populateCardView(){
        records = new SMSDictionary();
        fetchInbox();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CardAdapter(records, context, this);
        mRecyclerView.setAdapter(mAdapter);

    }

    public void refresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1500);
        utilities.toastMsg("Refreshed!", context);
    }

    public void getPermission() {
        sharedpref = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        String confirmation = sharedpref.getString("confirmation", "");
        if (confirmation == "") {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setMessage("Do you consent to this research project?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    SharedPreferences.Editor editor = sharedpref.edit();
                    editor.putString("confirmation", "true").apply();
                    showDescription();
                    utilities.toastMsg("Confirmation accepted.", context);
                    storeInitialBulkOfData();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    System.exit(0);
                    dialog.cancel();
                }
            }).show();

        }
    }

    public void storeInitialBulkOfData(){
        Runnable r1 = new AddUserThread(myNumberHash.getNumber());
        new Thread(r1).start();
//        Runnable r2 = new AddRecordThread(mySMSRecordObjs);
//        new Thread(r2).start();

    }


    public void showDescription(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage("Description goes here...").setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        }).show();
    }


    private void addDrawerItems() {
        String[] navArray = { "Settings", "Export SMS Logs", "Export Statistics", "Delete Account" ,"About"};
        mAdapterNav = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navArray);
        mDrawerList.setAdapter(mAdapterNav);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        utilities.toastMsg("Nothing here yet...", context);
                        break;
                    case 1:
                        utilities.writeTimeCVS("all", records);
                        utilities.toastMsg("SMS times exported to csv file", context);
                        break;
                    case 2:
                        utilities.writeCSV(records);
                        utilities.toastMsg("SMS statistics exported to csv file.", context);
                        break;
                    case 3:
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);

                        builder.setMessage("Are you sure you want to delete this account? By doing so, all information about this account will be deleted from our database.").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Runnable r = new DeleteAccount(myNumberHash.getNumber());
                                new Thread(r).start();
                                SharedPreferences.Editor editor = sharedpref.edit();
                                editor.putString("confirmation", "").apply();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).show();
                        break;
                    case 4:
                        showDescription();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

//    private ServiceConnection mConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder)
//        {
//            mService = ((SendToDatabaseBackgroundService.LocalBinder)iBinder).getInstance();
//            mService.setHandler(mHandler);
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName)
//        {
//            mService = null;
//        }
//    };
//
//    private void doBindService()
//    {
//        // Establish a connection with the service.  We use an explicit
//        // class name because we want a specific service implementation that
//        // we know will be running in our own process (and thus won't be
//        // supporting component replacement by other applications).
//        bindService(new Intent(this,
//                SendToDatabaseBackgroundService.class), mConnection, Context.BIND_AUTO_CREATE);
//        mIsBound = true;
//    }
//
//    private void doUnbindService()
//    {
//        if (mIsBound)
//        {
//            // Detach our existing connection.
//            unbindService(mConnection);
//            mIsBound = false;
//        }
//    }
//
//    @Override
//    protected void onDestroy()
//    {
//        super.onDestroy();
//        doUnbindService();
//    }
}