package com.ensc38.areaprogrammablesprinkler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.Arrays;


public class RunActivity extends BaseActivity {

    String [] PathList = new String[10];
    String[] RunList = new String[10];
    String[] temps;
    TextView textArea,textArea1,textArea2,textArea3,textArea4,
            textArea5,textArea6,textArea7,textArea8,textArea9,
            textArea10;
    Integer flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        File dir = getFilesDir();
        String temp;
        textArea = (TextView) findViewById(R.id.textArea);
        textArea2 = (TextView) findViewById(R.id.textArea2);
        textArea3 = (TextView) findViewById(R.id.textArea3);
        textArea4 = (TextView) findViewById(R.id.textArea4);
        textArea5 = (TextView) findViewById(R.id.textArea5);
        textArea6 = (TextView) findViewById(R.id.textArea6);
        textArea7 = (TextView) findViewById(R.id.textArea7);
        textArea8 = (TextView) findViewById(R.id.textArea8);
        textArea9 = (TextView) findViewById(R.id.textArea9);
        textArea10 = (TextView) findViewById(R.id.textArea10);



        for(int i = 0; i < dir.listFiles().length && dir.listFiles().length < 10; i++)
        {
            temp = dir.listFiles()[i].toString();
            PathList[i] = temp;
            Log.i("Files", temp);
            temps = temp.split("/|\\.");
            Log.i("Split 1", temps[temps.length-2]);
            RunList[i] = temps[temps.length-2];
        }

        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();
        StringBuilder sb4 = new StringBuilder();
        StringBuilder sb5 = new StringBuilder();
        StringBuilder sb6 = new StringBuilder();
        StringBuilder sb7 = new StringBuilder();
        StringBuilder sb8 = new StringBuilder();
        StringBuilder sb9 = new StringBuilder();
        StringBuilder sb10 = new StringBuilder();
        int size = RunList.length;
        for(int y=0; y < size; y++){
            if(RunList[y] != null) {
                if(y == 0) {
                    sb.append(RunList[y]);
                }
                else if(y == 1){
                    sb2.append(RunList[y]);
                }
                else if(y == 2){
                    sb3.append(RunList[y]);
                }
                else if(y == 3){
                    sb4.append(RunList[y]);
                }
                else if(y == 4){
                    sb5.append(RunList[y]);
                }
                else if(y == 5){
                    sb6.append(RunList[y]);
                }
                else if(y == 6){
                    sb7.append(RunList[y]);
                }
                else if(y == 7){
                    sb8.append(RunList[y]);
                }
                else if(y == 8){
                    sb9.append(RunList[y]);
                }
                else if(y == 9){
                    sb10.append(RunList[y]);
                }

            }
        }
        textArea.setText(sb.toString());
        textArea2.setText(sb2.toString());
        textArea3.setText(sb3.toString());
        textArea4.setText(sb4.toString());
        textArea5.setText(sb5.toString());
        textArea6.setText(sb6.toString());
        textArea7.setText(sb7.toString());
        textArea8.setText(sb8.toString());
        textArea9.setText(sb9.toString());
        textArea10.setText(sb10.toString());

    }



    public void openRunning(View V,int path){
        Intent runningIntent = new Intent(this,RunningActivity.class);
        runningIntent.putExtra("PathList",PathList[path]);
        startActivity(runningIntent);

    }

    public void onTextClick(View V){
        if(V == textArea)
        {
            flag = 0;
            openRunning(V,flag);
        }
        else if(V == textArea2){
            flag = 1;
            openRunning(V,flag);
        }
        else if(V == textArea3){
            flag = 2;
            openRunning(V,flag);
        }
        else if(V == textArea4){
            flag = 3;
            openRunning(V,flag);
        }
        else if(V == textArea5){
            flag = 4;
            openRunning(V,flag);
        }
        else if(V == textArea6){
            flag = 5;
            openRunning(V,flag);
        }
        else if(V == textArea7){
            flag = 6;
            openRunning(V,flag);
        }
        else if(V == textArea8){
            flag = 7;
            openRunning(V,flag);
        }
        else if(V == textArea9){
            flag = 8;
            openRunning(V,flag);
        }
        else if(V == textArea10){
            flag = 9;
            openRunning(V,flag);
        }

    }

    public void closeRun(View V){
        finish();
    }

}
