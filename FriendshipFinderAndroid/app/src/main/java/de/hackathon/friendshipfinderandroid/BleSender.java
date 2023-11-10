package de.hackathon.friendshipfinderandroid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.AdvertiseCallback;
import android.os.ParcelUuid;
import android.util.Log;

import java.util.UUID;

public class BleSender {
    BluetoothAdapter bluetoothAdapter;
    AdvertiseSettings settings;
    AdvertiseData data;
    BluetoothLeAdvertiser bluetoothLeAdvertiser;


    BleSender(BluetoothAdapter blA){
        if (blA == null) {
            Log.d("cerror","Devicr not supported");
            return;
        }
        bluetoothAdapter = blA;
    }

    void SetAdvertiseSetting(){
        settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                .setConnectable(true)
                .build();
    }

    void SetAdvertiseData(){
        data = new AdvertiseData.Builder()
                .setIncludeDeviceName(false)
                .addServiceUuid(new ParcelUuid(UUID.fromString("0949f341-11a9-4bf9-be13-877d2fd8946e")))
                // Add other data if needed
                .build();
    }

    void StartAdvertising(){
        bluetoothLeAdvertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
        if (bluetoothLeAdvertiser != null) {
            bluetoothLeAdvertiser.startAdvertising(settings, data, advertiseCallback);
        }
    }

    private final AdvertiseCallback advertiseCallback = new AdvertiseCallback() {
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            // Advertising started successfully
            Log.d("bls","Bluetoth advertising started");
        }

        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            // Handle advertising failure
            Log.e("bls","Failed to start advertising " + errorCode);
        }
    };



}