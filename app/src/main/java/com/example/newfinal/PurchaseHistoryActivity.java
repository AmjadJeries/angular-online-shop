package com.example.newfinal;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PurchaseHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PurchaseAdapter purchaseAdapter;
    private List<Purchase> purchaseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_history_activity);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Purchase List and Adapter
        purchaseList = new ArrayList<>();
        purchaseAdapter = new PurchaseAdapter(this, purchaseList);
        recyclerView.setAdapter(purchaseAdapter);

        // Get the userId from the intent
        String userId = getIntent().getStringExtra("userId");
        Log.d("PurchaseHistoryActivity", "Loading purchase history for userId: " + userId);

        // Load purchase history from Firebase
        loadPurchaseHistory(userId);
    }

    private void loadPurchaseHistory(String userId) {
        DatabaseReference purchaseHistoryRef = FirebaseDatabase.getInstance().getReference("purchase_history");

        Log.d("PurchaseHistoryActivity", "Starting to load purchase history...");

        // Query the purchase history by userId
        purchaseHistoryRef.orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("PurchaseHistoryActivity", "DataSnapshot size: " + dataSnapshot.getChildrenCount());

                purchaseList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Purchase purchase = snapshot.getValue(Purchase.class);
                    if (purchase != null) {
                        Log.d("PurchaseHistoryActivity", "Purchase: " + purchase.getProductName());
                        purchaseList.add(purchase);
                    } else {
                        Log.d("PurchaseHistoryActivity", "Purchase is null");
                    }
                }
                purchaseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("PurchaseHistory", "Failed to load purchase history", databaseError.toException());
            }
        });
    }

}
