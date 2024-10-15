package com.example.newfinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private SharedPreferences sp;
    private Button previousOrdersButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        // Initialize SharedPreferences
        sp = getSharedPreferences("my_prefs", MODE_PRIVATE);

        // Retrieve the user's name from SharedPreferences
        String userName = sp.getString("user_name", "User");

        // Display the greeting
        TextView greetingTextView = findViewById(R.id.greetingTextView);
        greetingTextView.setText("Hi, " + userName + "!");

        // The rest of your existing code
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productAdapter = new ProductAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(productAdapter);

        loadProductsFromFirebase();

        previousOrdersButton = findViewById(R.id.previousOrdersButton);
        previousOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductsActivity.this, PurchaseHistoryActivity.class);
                String userId = getUserId();
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    private void loadProductsFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Product");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productAdapter.clearProducts(); // Clear existing products
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Product product = snapshot.getValue(Product.class);
                        if (product != null) {
                            productAdapter.addProduct(product);
                            Log.d("ProductsActivity", "Product added: " + product.getName());
                        } else {
                            Log.e("ProductsActivity", "Product data is null");
                        }
                    }
                } else {
                    Log.d("ProductsActivity", "No products available.");
                    // Optionally, show a message to the user
                }
            }


    @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ProductsActivity", "Failed to load products", databaseError.toException());
            }
        });
    }

    private void logoutUser() {
        // Clear the stored user ID
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("user_id");
        editor.apply();

        // Navigate back to the login screen (MainActivity)
        Intent intent = new Intent(ProductsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Finish current activity
    }

    private String getUserId() {
        // This method retrieves the user ID of the logged-in user from SharedPreferences
        return sp.getString("user_id", ""); // Get the user ID from SharedPreferences
    }
}
