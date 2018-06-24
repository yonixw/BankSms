package com.yoniwas.smsbank;

import android.util.Log;

import java.util.Date;

public class Sms_RV_dataObj {

    public static final String TAG = "SMS_MINE_DATA";

    public String store;
    public String price;
    public String currency;
    public Date recieved;

    public boolean isValid = false;

    public  Sms_RV_dataObj(String smsBody, Date dateRecv) {
        recieved = dateRecv;

        String[] words  = smsBody.split(" ");
        if (words.length < 7)  {
            Log.e(TAG,"Less than 7 words. Body: " + smsBody);
            return;
        }

        // There should always be a price and currency:
        price = words[6];
        currency = words[7];

        // Try to find if store is specifiec. Search until end of sentence (ended with dot)
        int startStoreIndex = smsBody.indexOf(price + " "+currency) ;
        if (startStoreIndex > -1){
            startStoreIndex += (price + " "+currency).length();
            int endStoreIndex = smsBody.indexOf(".", startStoreIndex);
            if (endStoreIndex > -1) {
                try {
                    store = smsBody.substring(startStoreIndex +1, endStoreIndex ); // +1 to avoid hebrew `Bet` char
                } catch (Exception ex) {
                    throw  ex;
                }
            }
        }

        isValid = true;

    }
}
