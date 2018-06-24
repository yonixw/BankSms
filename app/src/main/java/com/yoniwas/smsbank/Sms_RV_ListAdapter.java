package com.yoniwas.smsbank;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
        viewHolder.lblDate.setText(dateStr);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //ViewHolders are basically caches of your View objects
    public final static class Sms_RV_ViewHolder extends RecyclerView.ViewHolder {
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

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"Add pic: " + lblDate.getText().toString(), Toast.LENGTH_SHORT);
                }
            });

            btnShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"Show pics: " + lblDate.getText().toString(), Toast.LENGTH_SHORT);
                }
            });
        }
    }
}