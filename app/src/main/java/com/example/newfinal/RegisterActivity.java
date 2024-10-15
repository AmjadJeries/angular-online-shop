package com.example.newfinal;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private EditText nameField, idField;
    private Button registerButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameField = findViewById(R.id.nameField);
        idField = findViewById(R.id.idField);
        registerButton = findViewById(R.id.registerButton);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String name = nameField.getText().toString().trim();
        String id = idField.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(id)) {
            Toast.makeText(this, "Please enter both name and ID", Toast.LENGTH_SHORT).show();
            return;
        }

        Users user = new Users(name, id);
        databaseReference.child(id).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(RegisterActivity.this, "User registered", Toast.LENGTH_SHORT).show();
                navigateToMAINActivity(); // Navigate to ProductsActivity on successful registration
            } else {
                Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Registration failed: ", task.getException());
            }
        });
    }

    private void navigateToMAINActivity() {
        Intent intent = new Intent(RegisterActivity.this,  MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear back stack
        startActivity(intent);
        finish(); // Finish current activity
    }
}
