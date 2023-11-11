package de.hackathon.friendshipfinderandroid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private ArrayAdapter<String> devicesArrayAdapter;
    private final Handler handler = new Handler();
    private static final long SCAN_INTERVAL = 10 * 1000; // Scan every 10 seconds
    private BleSender Bls;

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.BLUETOOTH,
                android.Manifest.permission.BLUETOOTH_ADMIN,
                android.Manifest.permission.BLUETOOTH_CONNECT,
                android.Manifest.permission.BLUETOOTH_SCAN,
                android.Manifest.permission.BLUETOOTH_ADVERTISE};
        ActivityCompat.requestPermissions(this, permissions, 12453);

        findViewById(R.id.toProfilePage).setOnClickListener(v -> {
            Intent myIntent = new Intent(MainActivity.this, ProfileActivity.class);
            MainActivity.this.startActivity(myIntent);
        });

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (bluetoothAdapter == null) {
//            return;
//        }
//        boolean succ = bluetoothAdapter.setName("E");

        //Log.d("bls", "" + succ);
//
//        if (!bluetoothAdapter.isEnabled()) {
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, 1);
//        }
//        Bls = new BleSender(bluetoothAdapter);
//        Bls.SetAdvertiseSetting();
//        Bls.SetAdvertiseData();
//        Bls.StartAdvertising();

        ListView devicesListView = findViewById(R.id.devicesListView);
        devicesArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        devicesListView.setAdapter(devicesArrayAdapter);

        // showPairedDevices();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
        startDeviceDiscovery();
        PeripheralManager m = new PeripheralManager(getBaseContext());
        m.startAdvertising();
    }

    private void startDeviceDiscovery() {
        // Start the initial discovery
        bluetoothAdapter.startDiscovery();

        // Schedule a repeated task for continuous scanning
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Restart device discovery after SCAN_INTERVAL
                bluetoothAdapter.startDiscovery();
                handler.postDelayed(this, SCAN_INTERVAL);
            }
        }, SCAN_INTERVAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
        }
        unregisterReceiver(receiver);
    }

    private void showPairedDevices() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                devicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        final Set<String> bluetoothDevices = new HashSet<>();

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!bluetoothDevices.contains(device.getAddress())) {
                    bluetoothDevices.add(device.getAddress());
                    if (device.getName() != null) {
                        devicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    }
                }
            }
        }
    };
}
