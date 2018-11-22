package com.example.edu.ottobotcontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView textViewConnectedBT;
    Button buttonScanBT;
    int[] buttonControlId = {R.id.buttonUp, R.id.buttonL, R.id.buttonC, R.id.buttonR, R.id.buttonD,
                    R.id.buttonMoonwalk, R.id.buttonBend, R.id.buttonAscendingTurn, R.id.buttonUpdown, R.id.buttonObstacle};
    Button buttonControl[] = new Button[buttonControlId.length];

    String buttonControlStr[] = {"U", "L", "C", "R", "D", "a", "b", "c", "d", "e"};

    String address = null;
    BluetoothAdapter bluetoothAdapter = null;
    BluetoothSocket bluetoothSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewConnectedBT = findViewById(R.id.textViewConnectedBT);
        buttonScanBT = findViewById(R.id.buttonScanBT);
        buttonScanBT.setOnClickListener(this);

        for(int i=0; i<buttonControlId.length; i++) {
            buttonControl[i] = findViewById(buttonControlId[i]);
            buttonControl[i].setOnClickListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent newint = getIntent();
        address = newint.getStringExtra("device_address");
        if(address != null) {
            new ConnectBluetoothTask().execute();
            textViewConnectedBT.setText(address);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == buttonScanBT) {
            Intent intent = new Intent(this, BluetoothDeviceListActivity.class);
            startActivity(intent);
        }
        else {
            for(int i=0; i<buttonControlId.length; i++) {
                if (v == buttonControl[i])
                    btSendMessage(buttonControlStr[i]);
            }
        }
    }

    private void btSendMessage(String flag) {
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.getOutputStream().write(flag.getBytes());
            } catch (IOException e) {
//                msg("Error");
            }
        }
    }

    private class ConnectBluetoothTask extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        protected void onPreExecute() {

//            progressBarConnecting.setVisibility(View.VISIBLE);

        }

        protected Void doInBackground(Void... devices) {
            try {
                if (bluetoothSocket == null || !isBtConnected) {
                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = bluetoothAdapter.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    bluetoothSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    bluetoothSocket.connect();//start connection

                }
            } catch (IOException e) {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }

            return null;
        }
    }

}
