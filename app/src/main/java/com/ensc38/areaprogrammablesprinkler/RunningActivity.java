package com.ensc38.areaprogrammablesprinkler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.File;


public class RunningActivity extends Activity {

    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            fileName = extras.getString("PathList");
        }
    }

    public void onRemove(View V){
        File file = new File(fileName);
        boolean d0 = file.delete();
        Log.i("Delete Check", "File deleted: " + fileName + "/myFile " + d0);
        finish();
    }


    public void closeRunning(View V){
        finish();
    }

}


