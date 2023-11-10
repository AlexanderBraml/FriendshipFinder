package de.hackathon.friendshipfinderandroid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;

import java.util.UUID;

public class PeripheralManager extends BluetoothGattServerCallback {
    BluetoothGattService BGS = new BluetoothGattService(UUID.fromString("0949f341-11a9-4bf9-be13-877d2fd8946e"),BluetoothGattService.SERVICE_TYPE_PRIMARY);
    BluetoothGattServer server;
    BluetoothLeAdvertiser BLA = BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();

    public PeripheralManager(Context context){
         server = ((BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE)).openGattServer(context,this);
    }


    public void addCharacteristic(){
        BluetoothGattCharacteristic BGC = new BluetoothGattCharacteristic(UUID.randomUUID(),BluetoothGattCharacteristic.PROPERTY_READ,BluetoothGattCharacteristic.PERMISSION_READ);
        BGS.addCharacteristic(BGC);
        server.addService(BGS);
    }
    public void startAdvertising() {

        addCharacteristic();

       AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                .setConnectable(true)
                .build();
       AdvertiseData data = new AdvertiseData.Builder()
                .setIncludeDeviceName(false)
                .addServiceUuid(new ParcelUuid(UUID.fromString("0949f341-11a9-4bf9-be13-877d2fd8946e")))
                // Add other data if needed
                .build();

        AdvertiseCallback AC = new AdvertiseCallback() {
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

        BLA.startAdvertising(settings,data,AC);
    }

}
