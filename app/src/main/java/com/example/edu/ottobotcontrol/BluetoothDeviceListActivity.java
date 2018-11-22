package com.example.edu.ottobotcontrol;


import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.bluetooth.BluetoothDevice;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothDeviceListActivity extends AppCompatActivity implements ListView.OnItemClickListener, View.OnClickListener{

    Button buttonPairedList;
    ListView listViewBluetoothList;
    private BluetoothAdapter bluetoothAdapter = null;
    Set<BluetoothDevice> pairedDeviceList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_device_list);

        listViewBluetoothList = findViewById(R.id.listViewBluetoothList);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        buttonPairedList = findViewById(R.id.buttonPairedList);
        buttonPairedList.setOnClickListener(this);

        if (bluetoothAdapter == null) {
            //Show a mensag. that the device has no bluetooth adapter
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();

            //finish apk
            finish();
        } else if (!bluetoothAdapter.isEnabled()) {
            //Ask to the user turn the bluetooth on
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
        }
    }

    public void onClick(View v) {

        pairedDeviceList = bluetoothAdapter.getBondedDevices();
        ArrayList pairedList = new ArrayList();

        if (pairedDeviceList.size() > 0) {
            for (BluetoothDevice bt : pairedDeviceList) {
                pairedList.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
            }
        } else {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, pairedList);
        listViewBluetoothList.setAdapter(adapter);
        listViewBluetoothList.setOnItemClickListener(this); //Method called when the device from the list is clicked
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String info = ((TextView) view).getText().toString();
        String address = info.substring(info.length() - 17);
//        String name;
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.putExtra("device_address", address);
//        intent.putExtra("device_name", name);
        startActivity(intent);
    }

}
