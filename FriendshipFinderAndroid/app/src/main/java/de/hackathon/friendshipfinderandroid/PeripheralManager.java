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
    BluetoothGattServer server;
    BluetoothGattService service;
    BluetoothLeAdvertiser advertiser = BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();

    AdvertiseSettings settings = new AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
            .setConnectable(true)
            .build();
    AdvertiseData data = new AdvertiseData.Builder()
            .setIncludeDeviceName(false)
            .addServiceUuid(new ParcelUuid(UUID.fromString("0949f341-11a9-4bf9-be13-877d2fd8946e")))
            .build();
    AdvertiserCallback callback = new AdvertiserCallback();

    public PeripheralManager(Context context){
         server = ((BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE)).openGattServer(context,this);
         service = new BluetoothGattService(UUID.fromString("0949f341-11a9-4bf9-be13-877d2fd8946e"),BluetoothGattService.SERVICE_TYPE_PRIMARY);
         addCServices();
    }

    public void addCServices(){
        BluetoothGattCharacteristic char1 = new BluetoothGattCharacteristic(UUID.randomUUID(),BluetoothGattCharacteristic.PROPERTY_READ,BluetoothGattCharacteristic.PERMISSION_READ);
        service.addCharacteristic(char1);
        server.addService(service);
    }
    public void startAdvertising() {
        advertiser.startAdvertising(settings,data,callback);
    }

    class AdvertiserCallback extends AdvertiseCallback {
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
