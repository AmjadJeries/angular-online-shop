package com.example.newfinal;

import android.content.Intent;  // יבוא מחלקת Intent לצורך ביצוע פעולות מעבר בין פעילויות ואפליקציות אחרות
import android.net.Uri;  // יבוא מחלקת Uri לטיפול בכתובות (URL)
import android.os.Bundle;  // יבוא מחלקת Bundle לניהול נתונים שמועברים בין פעילויות
import android.view.View;  // יבוא מחלקת View לניהול ממשק משתמש
import android.widget.Button;  // יבוא מחלקת Button להצגת כפתורים

import androidx.appcompat.app.AppCompatActivity;  // יבוא מחלקת AppCompatActivity לניהול פעילות עם תאימות לאחור

public class HelpActivity extends AppCompatActivity implements View.OnClickListener {
    private Button call, sendSms, openInternet;  // הכרזה על כפתורים שנמצאים בממשק המשתמש

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);  // קביעת הפריסה (layout) של הפעילות

        // קישור הכפתורים ב-XML לקוד
        call = findViewById(R.id.call);
        sendSms = findViewById(R.id.send_sms);
        openInternet = findViewById(R.id.open_internet);

        // קביעת מאזינים לכפתורים כדי לטפל בלחיצות
        call.setOnClickListener(this);
        sendSms.setOnClickListener(this);
        openInternet.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == call) {  // אם המשתמש לחץ על כפתור ה-"Call"
            // יצירת Intent למעבר לאפליקציית הטלפון עם המספר הרצוי
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:0545404006"));  // קביעת מספר הטלפון לחיוג
            startActivity(callIntent);  // הפעלת ה-Intent למעבר לאפליקציית החיוג
        }
        if (view == sendSms) {  // אם המשתמש לחץ על כפתור ה-"Send SMS"
            // יצירת Intent למעבר לאפליקציית ההודעות עם טקסט מוגדר מראש
            String message = "i want to help my for";  // הגדרת הודעת SMS שתישלח
            Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:04-9565505"));
            smsIntent.putExtra("sms_body", message);  // העברת ההודעה ל-Intent
            startActivity(smsIntent);  // הפעלת ה-Intent למעבר לאפליקציית ה-SMS
        }
        if (view == openInternet) {  // אם המשתמש לחץ על כפתור ה-"Open Internet"
            // יצירת Intent למעבר לדפדפן עם כתובת URL מוגדרת
            String url = "https://www.stematsky.com";  // הגדרת כתובת ה-URL
            Intent internetIntent = new Intent(Intent.ACTION_VIEW);
            internetIntent.setData(Uri.parse(url));  // קביעת ה-URL ב-Intent
            startActivity(internetIntent);  // הפעלת ה-Intent למעבר לדפדפן
        }
    }
}
