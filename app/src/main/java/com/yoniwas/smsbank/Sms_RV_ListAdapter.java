package com.yoniwas.smsbank;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Sms_RV_ListAdapter extends
        RecyclerView.Adapter <Sms_RV_ListAdapter.Sms_RV_ViewHolder> {

    private List<Sms_RV_dataObj> items;

    Sms_RV_ListAdapter(List<Sms_RV_dataObj> modelData)
    {
        if (modelData == null)
        {
            throw new IllegalArgumentException("modelData must not be null");
        }
        this.items = modelData;
    }

    @Override
    public Sms_RV_ViewHolder onCreateViewHolder(
        ViewGroup viewGroup, int viewType)
    {
        View itemView = LayoutInflater
            .from(viewGroup.getContext())
            .inflate(
                R.layout.sms_item,
                viewGroup,
                false);
        return new Sms_RV_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(
        Sms_RV_ViewHolder viewHolder, int position)
    {
        // Get position from model:
        Sms_RV_dataObj model = items.get(position);

        // Update the view that will be recycled:
        viewHolder.lblStore.setText(model.store);
        viewHolder.lblPrice.setText(model.price);
        viewHolder.lblCurr.setText(model.currency);

        String dateStr = DateUtils.formatDateTime(
            viewHolder.lblDate.getContext(),
            model.recieved.getTime(),
            DateUtils.FORMAT_ABBREV_ALL);
        //viewHolder.lblDate.setText(dateStr);

        viewHolder.lblDate.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(model.recieved));


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //ViewHolders are basically caches of your View objects
    public final static class Sms_RV_ViewHolder extends RecyclerView.ViewHolder  implements  View.OnClickListener{
        TextView lblPrice, lblCurr, lblDate, lblStore;
        Button btnAdd, btnShow;

        public Sms_RV_ViewHolder(View itemView) {
            super(itemView);
            lblPrice = (TextView) itemView.findViewById(R.id.lblPrice);
            lblCurr = (TextView) itemView.findViewById(R.id.lblCurr);
            lblDate = (TextView) itemView.findViewById(R.id.lblDate);
            lblStore = (TextView) itemView.findViewById(R.id.lblStore);

            btnAdd = (Button) itemView.findViewById(R.id.btnAddPic);
            btnShow = (Button) itemView.findViewById(R.id.btnShowPics);

            btnAdd.setOnClickListener(this);
            btnShow.setOnClickListener(this);

            //getAdapterPosition() //<== id in items
        }



        @Override
        public void onClick(View v) {
            if (v.getId() == btnAdd.getId()) {
                //Toast.makeText(v.getContext(), "Add " + lblDate.getText().toString(), Toast.LENGTH_SHORT).show();
                String itemID = lblDate.getText().toString()
                        .replace("/","_")
                        .replace(":","_");


                File idFolder = new File(IOHelper.getStorageDir(v.getContext()), itemID);
                if (!idFolder.exists())
                    idFolder.mkdir();
                File imgFile = new File(idFolder,
                        new SimpleDateFormat("yyyy-MM-dd-HH-mm").format(new Date()) + ".png");


                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                Uri photoURI = FileProvider.getUriForFile(v.getContext(),
                        "com.example.android.fileprovider",
                        imgFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                ((Activity)v.getContext()).startActivityForResult(intent, MainActivity.REQUEST_IMAGE_CAPTURE);
            }
            else {
                Toast.makeText(v.getContext(), "Show " + lblDate.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}