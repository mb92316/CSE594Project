package com.example.android.cse594project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

public class Settings extends AppCompatActivity {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        setContentView(R.layout.activity_settings);
    }


    public void Pinpad (View view) {
        TextView textView = (TextView) findViewById(R.id.testtext);
        editor = pref.edit();
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.pinOnButton:
                if (checked) {
                    editor.putInt("pinpadInt", 1);
                    editor.commit();

                }
                    break;
            case R.id.pinOffButton:
                if (checked) {
                    editor.putInt("pinpadInt", 0);
                    editor.commit();
                }
                    break;
        }
    }
}

