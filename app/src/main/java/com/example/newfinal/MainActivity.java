package com.example.newfinal;

// יבוא ספריות ושירותים של Android ו-Firebase
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    // הכרזה על משתנים גלובליים
    private EditText idField;  // שדה קלט למזהה המשתמש
    private DatabaseReference databaseReference;  // משתנה גישה למסד הנתונים של Firebase
    private VideoView videoView;  // רכיב להצגת וידאו
    private SharedPreferences sp;  // משתנה לאחסון נתונים מקומיים באפליקציה

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // הגדרת הפריסה הראשית של הפעילות

        sp = getSharedPreferences("my_prefs", MODE_PRIVATE);  // טעינת SharedPreferences לאחסון מקומי של נתונים

        // בדיקה אם המשתמש כבר מחובר
        String userId = sp.getString("user_id", null);
        if (userId != null) {  // אם מזהה המשתמש כבר שמור ב-SharedPreferences, הוא יעבור ישר לעמוד המוצרים
            navigateToProductsActivity();
        }

        // קישור רכיבי הפריסה לקוד
        idField = findViewById(R.id.idField);
        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);
        TextView helpText = findViewById(R.id.helpText);
        videoView = findViewById(R.id.videoView);

        // הגדרת נתיב הווידאו והפעלתו
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bookvid);
        videoView.setVideoURI(videoUri);
        videoView.start();

        // לולאת הווידאו
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);  // הגדרת הווידאו לחזרה בלולאה אינסופית
            }
        });

        // הגדרת פעולת לחצן ההתחברות
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();  // קריאה לפונקציה שמבצעת התחברות
            }
        });

        // הגדרת פעולת לחצן ההרשמה
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));  // מעבר לעמוד ההרשמה
            }
        });

        // הגדרת פעולת לחצן העזרה
        helpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HelpActivity.class));  // מעבר לעמוד העזרה
            }
        });
    }

    // פונקציה לטיפול בהתחברות המשתמש
    private void loginUser() {
        //קריאת ID ושמירה ב id
        String id = idField.getText().toString().trim();

        if (TextUtils.isEmpty(id)) {
            Toast.makeText(this, "Please enter ID", Toast.LENGTH_SHORT).show();
            return;
        }
//שורה זו יוצרת הפניה למסד הנתונים של Firebase, במיוחד לענף Users.
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        //כאן ניגשים לפרטי המשתמש תחת הענף Users באמצעות מזהה המשתמש (id) כילד (child) בתוך הענף.
        //addListenerForSingleValueEvent מוסיף מאזין (Listener) שמבצע פעולה אחת בלבד: קריאת ערך פעם אחת מהמסד הנתונים.
        databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //שיטה זו מופעלת כאשר הנתונים משויכים בהצלחה למזהה המבוקש.
                if (snapshot.exists()) {
                    // Fetch the user's name
                    String userName = snapshot.child("name").getValue(String.class);

                    // Save user ID and name in SharedPreferences
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("user_id", id);
                    editor.putString("user_name", userName);  // Save the user's name
                    editor.apply();

                    navigateToProductsActivity();
                } else {
                    Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // פונקציה להעברה לעמוד המוצרים
    private void navigateToProductsActivity() {
        startActivity(new Intent(MainActivity.this, ProductsActivity.class));  // מעבר לעמוד המוצרים
        finish();  // סיום הפעילות הנוכחית כדי למנוע חזרה אחורה
    }
}
