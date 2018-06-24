package com.yoniwas.smsbank;

import android.util.Log;

import java.util.Date;

public class Sms_RV_dataObj {

    public static final String TAG = "SMS_MINE_DATA";

    public String store;
    public String price;
    public String currency;
    public Date recieved;

    public  Sms_RV_dataObj(String smsBody, Date dateRecv) {
        recieved = dateRecv;

        String[] words  = smsBody.split(" ");
        if (words.length < 7)  {
            Log.e(TAG,"Less than 7 words. Body: " + smsBody);
        }

        // There should always be a price and currency:
        price = words[6];
        currency = words[7];

        // Try to find if store is specifiec. Search until end of sentence (ended with dot)
        int startStoreIndex = smsBody.indexOf(price + " "+currency) + 1;
        if (startStoreIndex > 0){
            int endStoreIndex = smsBody.indexOf(".", startStoreIndex);
            if (endStoreIndex > -1) {
                store = smsBody.substring(startStoreIndex, endStoreIndex - startStoreIndex +1);
            }
        }

    }
}
