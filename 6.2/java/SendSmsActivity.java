package com.example.williamrudwall.smsappen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendSmsActivity extends AppCompatActivity {

    private Button sendSmsButton;
    private Button goToInbox;
    private EditText phoneNumber;
    private EditText smsMessageET;
    private final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);

        goToInbox = (Button) findViewById(R.id.btnInbox);
        sendSmsButton = (Button) findViewById(R.id.btnSendMessage);
        phoneNumber = (EditText) findViewById(R.id.editTextPhoneNR);
        smsMessageET = (EditText) findViewById(R.id.editTextMessage);

        goToInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendSmsActivity.this, ReceiveSmsActivity.class);
                startActivity(intent);
            }
        });

        sendSmsButton.setEnabled(false);
        if(checkPermission(Manifest.permission.SEND_SMS)){
            sendSmsButton.setEnabled(true);
        }else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }

        sendSmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms();
            }
        });

    }

// Skickar iväg meddelandet
    private void sendSms() {

            String toPhone = phoneNumber.getText().toString();
            String smsMessage = smsMessageET.getText().toString();



                if (checkPermission(Manifest.permission.SEND_SMS)) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(toPhone, null, smsMessage, null, null);
                    Toast.makeText(this, "hej", Toast.LENGTH_SHORT).show();

                    phoneNumber.setText(" ");
                    smsMessageET.setText(" ");
                }


            }


    // Metod för att kolla tillåtelse
    private boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

}


