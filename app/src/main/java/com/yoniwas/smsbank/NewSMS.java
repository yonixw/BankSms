package com.yoniwas.smsbank;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class NewSMS extends BroadcastReceiver  {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage smsMessageObj = SmsMessage.createFromPdu((byte[]) pdusObj[i]);

                    String senderNumOrServiceName = smsMessageObj.getDisplayOriginatingAddress();
                    String smsMessageBody = smsMessageObj.getDisplayMessageBody();
                    //String senderDesc = currentMessage;

                    Log.i("SmsReceiver", "senderNum: "+ senderNumOrServiceName + "; message: " + smsMessageBody);


                    // Show Alert
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context,
                            "senderNum: "+ senderNumOrServiceName + ", message: " + smsMessageBody, duration);
                    toast.show();

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }
}
