package com.example.zappycode.bluetoothfinder;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BluetoothFinder bluetoothFinder = new BluetoothFinder();

    //VIEW
    private TextView statusTextView;
    private Button searchButton;
    private Button stopButton;

    //DATA
    private ArrayList<String> bluetoothDevices = new ArrayList<>();
    private ArrayAdapter arrayAdapter;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                String address = device.getAddress();
                String rssi = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));
                String deviceString = name + " " + address + " " + rssi;
                bluetoothDevices.add(deviceString);
                arrayAdapter.notifyDataSetChanged();
            }
        }
    };

    public void searchClicked(View view) {
        statusTextView.setText("Searching...");
        searchButton.setEnabled(false);
        stopButton.setEnabled(true);
        bluetoothDevices.clear();
        arrayAdapter.notifyDataSetChanged();
        bluetoothFinder.start();
    }

    public void stopClicked(View view) {
        statusTextView.setText("");
        searchButton.setEnabled(true);
        stopButton.setEnabled(false);
        bluetoothFinder.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);
        statusTextView = findViewById(R.id.statusTextView);
        searchButton = findViewById(R.id.searchButton);
        stopButton = findViewById(R.id.stopButton);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, bluetoothDevices);

        listView.setAdapter(arrayAdapter);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(broadcastReceiver, intentFilter);
    }
}
