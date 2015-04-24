package com.ensc38.areaprogrammablesprinkler;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class PositionActivity extends Activity {
    int PressureArray[] = new int[200];
    int counter = 0;
    TextView Position;
    TextView Program;
    Spinner PressureSpinner;
    Integer[] PressureOptions = new Integer[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
    String ProgramName;
    String filename;
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

    public static final String EXT_FOLDER = "/APSPrograms/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position);
        Position = (TextView) findViewById(R.id.textPos);
        Position.setText(String.valueOf(counter));
        Program = (TextView) findViewById(R.id.ProgName);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, PressureOptions);
        PressureSpinner = (Spinner) findViewById(R.id.pressure);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PressureSpinner.setAdapter(adapter);
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            ProgramName = extras.getString("ProgramName");
            filename = ProgramName + ".csv";
            Program.setText(ProgramName);
        }
    }

    public void closePosition(View V){
        finish();
    }

    public void Comm(View V) {

    }
    public void errorExit(String title, String message){
        Toast msg = Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_SHORT);
        msg.show();
        finish();
    }

    public void onNextButton(View V){
        String currentVal = PressureSpinner.getSelectedItem().toString();
        PressureArray[counter] = Integer.parseInt(currentVal);
        counter++;
        if(counter == 200)
        {
            counter = 0;
        }
        Position.setText(String.valueOf(counter));
    }

    public void onPrevButton(View V){
        counter--;
        if(counter == -1)
        {
            counter = 199;
        }
        Position.setText(String.valueOf(counter));
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

    public void saveProgramToFile(View V) {
        String entry = "";
        for(int i = 0; i < PressureArray.length; i++) {
            entry += PressureArray[i] + ",";
        }

        try {
            OutputStreamWriter output = new OutputStreamWriter(openFileOutput(filename, Context.MODE_PRIVATE));
            output.write(entry);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("File", readFromFile());
        finish();
    }

    private String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = openFileInput(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
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
        connectToDevice(address);
        // Get the BluetoothDevice object
        BluetoothDevice device = btAdapter.getRemoteDevice(address);
    }

    public File checkAndMakeDir(String fileName) {
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
