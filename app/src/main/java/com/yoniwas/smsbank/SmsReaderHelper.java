package com.yoniwas.smsbank;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.Telephony;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class SmsReaderHelper {
    public  static boolean arrHas(String[] arr, String val) {
        for (String item : arr){
            if (item.equals(val.trim())) return  true;
        }
        return  false;
    }

    public static List<Sms_RV_dataObj> getAllSms(Context context, String[] names) {

        ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(Telephony.Sms.CONTENT_URI, null, null, null, null);

        int totalSMS = 0;
        int filteredSMS = 0;

        List<Sms_RV_dataObj> result = new ArrayList<>();

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

                    if (arrHas(names,number) && type == "inbox") {
                        Sms_RV_dataObj smsData = new Sms_RV_dataObj(body, dateFormat);
                        if (smsData.isValid){
                            result.add(smsData);
                            filteredSMS++;
                        }
                    }
                    c.moveToNext();
                }
            }

            c.close();
            Toast.makeText(context, "Found " + filteredSMS + " filtered sms by sender", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "No message was found at all!", Toast.LENGTH_SHORT).show();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result.sort(new Comparator<Sms_RV_dataObj>() {
                @Override
                public int compare(Sms_RV_dataObj o1, Sms_RV_dataObj o2) {
                    return -o1.recieved.compareTo(o2.recieved);
                }
            });
        }
        return  result;
    }
}

