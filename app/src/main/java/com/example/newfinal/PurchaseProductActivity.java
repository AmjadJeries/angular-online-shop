package com.example.newfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PurchaseProductActivity extends AppCompatActivity {

    private EditText creditCardNumber, expiryDate, cvv;
    private Button purchaseButton;
    private String productName;
    private double productPrice;
    private String userName, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_product);

        // מצא את הכפתורים והרכיבים האחרים ב-XML
        Button backButton = findViewById(R.id.backButton);  // כפתור חזרה מותאם אישית
        creditCardNumber = findViewById(R.id.creditCardNumber);
        expiryDate = findViewById(R.id.expiryDate);
        cvv = findViewById(R.id.cvv);
        purchaseButton = findViewById(R.id.purchaseButton);

        // קבלת הנתונים שהועברו מה-Intent
        productName = getIntent().getStringExtra("productName");
        productPrice = getIntent().getDoubleExtra("productPrice", -1);  // השתמש ב- -1 כדי לזהות שגיאה

        // קבלת פרטי המשתמש מה-SharedPreferences
        SharedPreferences sp = getSharedPreferences("my_prefs", MODE_PRIVATE);
        userName = sp.getString("user_name", "");  // משתנה השם שמור ב-SharedPreferences
        userId = sp.getString("user_id", "");  // משתנה ה-ID שמור ב-SharedPreferences

        // רישום הערכים לוודא שהם נכונים
        Log.d("PurchaseProductActivity", "Product name: " + productName + ", Product price: " + productPrice);
        Log.d("PurchaseProductActivity", "User name: " + userName + ", User ID: " + userId);

        // אם המחיר לא הועבר נכון, הצג הודעת שגיאה
        if (productPrice == -1) {
            Toast.makeText(this, "Failed to retrieve product price.", Toast.LENGTH_SHORT).show();
            return;
        }

        // הגדרת שם המוצר והמחיר בממשק המשתמש
        TextView productNameView = findViewById(R.id.productName);
        TextView productPriceView = findViewById(R.id.productPrice);
        productNameView.setText(productName);
        productPriceView.setText("$" + productPrice);

        // קביעת פעולה בעת לחיצה על כפתור החזרה
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // חזרה לדף הקודם או לדף מסוים אחר
                Intent intent = new Intent(PurchaseProductActivity.this, ProductsActivity.class);
                startActivity(intent);
                finish();  // סיים את הפעילות הנוכחית
            }
        });

        // פעולה בעת לחיצה על כפתור הקנייה
        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUserAndPurchaseProduct();
            }
        });
    }

    private void registerUserAndPurchaseProduct() {
        String creditCardNum = creditCardNumber.getText().toString().trim();
        String expiry = expiryDate.getText().toString().trim();
        String cvvCode = cvv.getText().toString().trim();

        if (TextUtils.isEmpty(creditCardNum) || TextUtils.isEmpty(expiry) || TextUtils.isEmpty(cvvCode)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // יצירת אובייקט רכישה ושמירתו ב-Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("purchase_history");

        String purchaseId = databaseReference.push().getKey();  // יצירת מפתח ייחודי לרכישה
        long purchaseDate = System.currentTimeMillis();  // תאריך ושעת הרכישה

        // יצירת אובייקט של הרכישה עם פרטי המשתמש שנשמרו
        Purchase purchase = new Purchase(purchaseId, userId, productName, (int) productPrice, purchaseDate);

        // שמירת הרכישה ב-Firebase
        databaseReference.child(purchaseId).setValue(purchase).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(PurchaseProductActivity.this, "Purchase saved successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PurchaseProductActivity.this, "Failed to save purchase.", Toast.LENGTH_SHORT).show();
                Log.e("Firebase", "Error saving purchase", task.getException());
            }
        });

        // מעבר חזרה לדף המוצרים
        startActivity(new Intent(PurchaseProductActivity.this, ProductsActivity.class));
        finish();  // סיים את הפעילות הנוכחית כדי למנוע חזרה
    }
}
