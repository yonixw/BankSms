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
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText txtboxFilters;
    RecyclerView lstSms;

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
        List<Sms_RV_dataObj> dataSet = SmsReaderHelper.getAllSms(c,txtboxFilters.getText().toString().split(";"));
        lstSms.setAdapter(new Sms_RV_ListAdapter(dataSet));
    }

    public static String itemID  = null;


    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                boolean sucess = false;
                try {
                    if (itemID != null) {
                        File idFolder = new File(IOHelper.getStorageDir(this), itemID);
                        if (!idFolder.exists())
                            idFolder.mkdir();
                        File imgFile = new File(idFolder,
                                new SimpleDateFormat("yyyy-MM-dd-HH-mm").format(new Date()) + ".png");
                        //imgFile.createNewFile();

                        bitmap = (Bitmap) data.getExtras().get("data");
                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(imgFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                            // PNG is a lossless format, the compression factor (100) is ignored
                            Toast.makeText(this, "Image saved!", Toast.LENGTH_SHORT).show();
                            sucess =true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (out != null) {
                                    out.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!sucess)
                    Toast.makeText(this, "Error saving scan", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
