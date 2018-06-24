package com.yoniwas.smsbank;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText txtboxFilters;
    RecyclerView lstSms;
    List<Sms_RV_dataObj> dataSet = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtboxFilters = (EditText)findViewById(R.id.txtFilters);
        lstSms = (RecyclerView)findViewById(R.id.lstSMS);

        InitRecycleView(this);
    }



    public void btnReadClick(View v) {
        FillRecycleView(v.getContext());
    }

    public void btnExport(View v) {
        if(dataSet == null || dataSet.size() == 0) {
            Toast.makeText(this, "No data to export!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean sucess = false;
        String csvID = new SimpleDateFormat("dd_MM_yyyy_HH_mm").format(new Date()) + "_ALL_SMS.csv";
        File idFolder = new File(IOHelper.getStorageDir(this), csvID);
        FileWriter outputStreamWriter = null;
        try {
            //outputStreamWriter = new OutputStreamWriter(openFileOutput(idFolder.getAbsolutePath(),MODE_APPEND));
            outputStreamWriter = new FileWriter(idFolder.getAbsolutePath());

            //outputStreamWriter.write(data);
            for (Sms_RV_dataObj obj : dataSet) {
                outputStreamWriter.write(
                        obj.store +"|"+obj.price+"|"+obj.currency+"|"+ obj.recieved.toString()
                        +"\r\n");
            }
            outputStreamWriter.close();
            sucess =true;
            Toast.makeText(this, "Export done!", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        finally {
            try {
                if (outputStreamWriter != null) outputStreamWriter.close();
            }  catch (IOException e) {
                Log.e("Exception", "Can't close file: " + e.toString());
            }
        }

        if(!sucess)
            Toast.makeText(this, "Can't export. Error occured.", Toast.LENGTH_SHORT).show();
    }


    public void InitRecycleView(Context c) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        lstSms.setLayoutManager(layoutManager);

        // allows for optimizations if all item views are of the same size:
        lstSms.setHasFixedSize(true);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        lstSms.addItemDecoration(itemDecoration);

        // this is the default;
        // this call is actually only necessary with custom ItemAnimators
        lstSms.setItemAnimator(new DefaultItemAnimator());

        FillRecycleView(c);
    }

    public void FillRecycleView(Context c){
        dataSet = SmsReaderHelper.getAllSms(c,txtboxFilters.getText().toString().split(";"));
        lstSms.setAdapter(new Sms_RV_ListAdapter(dataSet));
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Image was saved!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Error saving scan", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
