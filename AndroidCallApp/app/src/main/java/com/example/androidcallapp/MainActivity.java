package com.example.androidcallapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText phoneNumberInput;
    private Button callButton;
    private ListView callStatusListView;
    private ArrayList<String> callStatusList;
    private ArrayList<String> retryQueue; // Added retry queue
    private ArrayAdapter<String> adapter; // Adapter for ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumberInput = findViewById(R.id.phoneNumberInput);
        callButton = findViewById(R.id.callButton);
        callStatusListView = findViewById(R.id.callStatusListView);
        callStatusList = new ArrayList<>();
        retryQueue = new ArrayList<>(); // Initialize retry queue

        // Set up the adapter for the ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, callStatusList);
        callStatusListView.setAdapter(adapter);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCalling();
            }
        });
    }

    private void startCalling() {
        String phoneNumber = phoneNumberInput.getText().toString();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else {
            makeCall(phoneNumber);
        }
    }

    private void makeCall(String phoneNumber) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);
            simulateCallStatus(phoneNumber); // Simulate call status for demonstration
        } catch (Exception e) {
            Toast.makeText(this, "Error making call: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void simulateCallStatus(String phoneNumber) {
        // Simulate call status (for demonstration purposes)
        boolean isBusy = false; // Replace with actual call status check
        boolean isUnanswered = false; // Replace with actual call status check

        if (isBusy) {
            callStatusList.add(phoneNumber + " - Busy");
            addToRetryQueue(phoneNumber);
        } else if (isUnanswered) {
            callStatusList.add(phoneNumber + " - Unanswered");
            addToRetryQueue(phoneNumber);
        } else {
            callStatusList.add(phoneNumber + " - Call Completed");
        }

        updateCallStatusListView();
    }

    private void addToRetryQueue(String phoneNumber) {
        retryQueue.add(phoneNumber); // Add to retry queue
        callStatusList.add(phoneNumber + " - Added to Retry Queue");
    }

    private void updateCallStatusListView() {
        adapter.notifyDataSetChanged(); // Notify the adapter to refresh the ListView
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCalling();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
