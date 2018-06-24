package com.yoniwas.smsbank;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText txtboxFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtboxFilters = (EditText)findViewById(R.id.txtFilters);
    }



    public void btnReadClick(View v) {
        SmsReaderHelper.getAllSms(v.getContext(),txtboxFilters.getText().toString().split(";"));
    }
}
