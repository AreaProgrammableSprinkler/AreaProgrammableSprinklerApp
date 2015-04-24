package com.ensc38.areaprogrammablesprinkler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class BaseActivity extends Activity {

    TextView text;

    public static final String EXT_FOLDER = "/APSPrograms/";
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_TOAST = 5;
    public static final String TOAST = "toast";
    public BluetoothAdapter btAdapter = null;
    public BluetoothSocket btSocket = null;
    private OutputStream outStream = null;


    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_ENABLE_BT = 3;

    // Well known SPP UUID
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Insert your bluetooth devices MAC address
    public static String address = "08:FD:0E:AF:3F:C3";


    ArrayAdapter<String> mArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        text = (TextView) findViewById(R.id.textEdit);
        checkAndMakeDir("TestFile.csv");
        checkBTState();
    }

    public void openProgrammer(View V){
        Intent programIntent = new Intent(this,ProgramActivity.class);
        startActivity(programIntent);

    }

    public void openRun(View V){
        Intent runIntent = new Intent(this,RunActivity.class);
        startActivity(runIntent);

    }

    public void blueRun(View V) {
        Intent serverIntent = null;
        // Launch the DeviceListActivity to see devices and do scan
        serverIntent = new Intent(this, BlueActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);

    }

     public void connectToDevice(String adr) {
        super.onResume();

        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = btAdapter.getRemoteDevice(adr);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        try {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        // Create a data stream so we can talk to server.
        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
        }
    }


    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on

        // Emulator doesn't support Bluetooth and will return null
        if(btAdapter==null) {
            errorExit("Fatal Error", "Bluetooth Not supported. Aborting.");
        } else {
            if (btAdapter.isEnabled()) {

            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    private void errorExit(String title, String message){
        Toast msg = Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_SHORT);
        msg.show();
        finish();
    }

    private void sendData(String message) {
        byte[] msgBuffer = message.getBytes();
        try {
            outStream.write(msgBuffer);
        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            errorExit("Fatal Error", msg);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
        }
    }

    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        address = data.getExtras().getString(BlueActivity.EXTRA_DEVICE_ADDRESS);
        text.setText("Device Address: " + address);
        connectToDevice(address);
        // Get the BluetoothDevice object
        BluetoothDevice device = btAdapter.getRemoteDevice(address);
    }

    public  File checkAndMakeDir(String fileName) {
        String newPath = Environment.getExternalStorageDirectory() + EXT_FOLDER;
        Log.i("Issue", newPath);
        try {
            File f = new File(newPath);
            f.mkdirs();
        } catch (Exception e) {
            Log.i("Issue", e.toString());
        }
        return null;
    }


}