package com.example.williamrudwall.smsappen;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ReceiveSmsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private final int RECEIVE_SMS_PERMISSION_REQUEST_CODE = 2;
    private final int READ_SMS_PERMISSION_REQUEST_CODE = 3;
    private Button buttonCom;
    private static ReceiveSmsActivity inst;
    private ArrayList<String> smsMessageList = new ArrayList<>();
    private ListView smsListView;
    private ArrayAdapter arrayAdapter;

    public static ReceiveSmsActivity instance() {
        return inst;
    }

    @Override
    protected void onStart() {
        super.onStart();
        inst = this;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_sms);


        smsListView = (ListView) findViewById(R.id.SMSList);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, smsMessageList);
        smsListView.setAdapter(arrayAdapter);
        smsListView.setOnItemClickListener(this);

        buttonCom = (Button) findViewById(R.id.btnCompose);

        buttonCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceiveSmsActivity.this, SendSmsActivity.class);
                startActivity(intent);
            }
        });

        if(checkPermission(Manifest.permission.RECEIVE_SMS)){
            refreshSmsInbox();
        }
        if (checkPermission(Manifest.permission.READ_SMS)){
            refreshSmsInbox();
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, RECEIVE_SMS_PERMISSION_REQUEST_CODE);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, READ_SMS_PERMISSION_REQUEST_CODE);
        }

    }

    // Lägger till meddelanden i inboxen
    public void refreshSmsInbox() {
        if(ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
            ContentResolver contentResolver = getContentResolver();


            Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
            int indexBody = smsInboxCursor.getColumnIndex("body");
            int indexAdress = smsInboxCursor.getColumnIndex("adress");
            int timeMillis = smsInboxCursor.getColumnIndex("date");
            Date date = new Date(timeMillis);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
            String dateText = format.format(date);

            if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
            arrayAdapter.clear();
            while (smsInboxCursor.moveToNext()) {
                String str = getString(indexAdress) + " at " +
                        "\n" + smsInboxCursor.getString(indexBody) + dateText + "\n";
                arrayAdapter.add(str);

            }

        }
    }

    //Uppdaterar "inboxen"
    public void updateList(final String smsMessage) {
        arrayAdapter.insert(smsMessage, 0);
        arrayAdapter.notifyDataSetChanged();
    }

    // Visar en toast med info om meddelandet när man klickar på meddelandet
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            try {
                String[] smsMessages = smsMessageList.get(position).split("\n");
                String adress = smsMessages[0];
                String smsMessage = "";

                for (int i = 1; i < smsMessages.length; i++) {
                    smsMessage = smsMessages[i];
                }

                String smsMessageStr = adress + "\n";
                smsMessageStr += smsMessage;
                Toast.makeText(this, smsMessageStr, Toast.LENGTH_SHORT).show();


            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    // Metod för att kolla tillåtelse
    private boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

}
