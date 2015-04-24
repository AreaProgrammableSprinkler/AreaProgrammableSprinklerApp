package com.ensc38.areaprogrammablesprinkler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class ProgramActivity extends BaseActivity {

    private EditText ProgramName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);
        ProgramName = (EditText) findViewById(R.id.ProgramNameField);
        ProgramName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ProgramName.setText("");
                }
            }
        });
    }

    public void openPosition(View V){
        Intent positionIntent = new Intent(this,PositionActivity.class);
        positionIntent.putExtra("ProgramName", ProgramName.getText().toString());
        startActivity(positionIntent);
        finish();

    }

    public void closeProgram(View V){
        finish();
    }

}
