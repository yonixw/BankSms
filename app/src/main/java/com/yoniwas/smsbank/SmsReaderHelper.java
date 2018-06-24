package com.yoniwas.smsbank;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

public class SmsReaderHelper {
    public  static boolean arrHas(String[] arr, String val) {
        for (String item : arr){
            if (item.equals(val.trim())) return  true;
        }
        return  false;
    }

    public static void getAllSms(Context context, String[] names) {

        ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(Telephony.Sms.CONTENT_URI, null, null, null, null);

        int totalSMS = 0;
        int filteredSMS = 0;

        if (c != null) {
            totalSMS = c.getCount();
            if (c.moveToFirst()) {
                for (int j = 0; j < totalSMS; j++) {
                    String smsDate = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.DATE));
                    String number = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                    String body = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.BODY));
                    Date dateFormat= new Date(Long.valueOf(smsDate));

                    String type = "none";
                    switch (Integer.parseInt(c.getString(c.getColumnIndexOrThrow(Telephony.Sms.TYPE)))) {
                        case Telephony.Sms.MESSAGE_TYPE_INBOX:
                            type = "inbox";
                            break;
                        case Telephony.Sms.MESSAGE_TYPE_SENT:
                            type = "sent";
                            break;
                        case Telephony.Sms.MESSAGE_TYPE_OUTBOX:
                            type = "outbox";
                            break;
                        default:
                            break;
                    }

                    if (arrHas(names,number) && type == "inbox")
                        filteredSMS++;
                    c.moveToNext();
                }
            }

            c.close();
            Toast.makeText(context, "Found " + filteredSMS + " filtered sms by sender", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "No message was found at all!", Toast.LENGTH_SHORT).show();
        }
    }
}

