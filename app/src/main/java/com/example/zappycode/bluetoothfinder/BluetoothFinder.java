package com.example.zappycode.bluetoothfinder;

import android.bluetooth.BluetoothAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class BluetoothFinder {

    private static final int RUNNING = 1;
    private static final int NOT_RUNNING = 0;

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private AtomicInteger OPERATIONAL_STATE = new AtomicInteger(NOT_RUNNING);

    public void start() {
        OPERATIONAL_STATE.set(RUNNING);
        executorService.submit(this::startFinding);
    }

    public void stop() {
        OPERATIONAL_STATE.set(NOT_RUNNING);
    }

    private void startFinding() {
        do {
            try {
                bluetoothAdapter.startDiscovery();
                Thread.sleep(1000);
                bluetoothAdapter.cancelDiscovery();
            } catch (Exception e) {
                //LOGGER ERROR
            }
        }
        while (OPERATIONAL_STATE.get() == RUNNING);
    }
}
