package com.example.newfinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.PurchaseViewHolder> {

    private Context context;  // הקשר של האפליקציה - נדרש לצורך גישה למשאבים כמו LayoutInflater
    private List<Purchase> purchaseList;  // רשימת הרכישות שתוצג ברשימה

    // בנאי למחלקת האדפטר, מקבל את ההקשר ורשימת הרכישות
    public PurchaseAdapter(Context context, List<Purchase> purchaseList) {
        this.context = context;
        this.purchaseList = purchaseList;
    }

    // יצירת ViewHolder חדש כאשר אין View ממוחזר זמין
    @NonNull
    @Override
    public PurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // ניפוח קובץ ה-XML של פריט הרכישה כדי ליצור את ה-View המתאים
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchase_item, parent, false);
        return new PurchaseViewHolder(view);  // יצירת והחזרת ה-ViewHolder עם ה-View החדש
    }

    // קישור הנתונים ל-ViewHolder שנוצר על ידי onCreateViewHolder
    @Override
    public void onBindViewHolder(@NonNull PurchaseViewHolder holder, int position) {
        Purchase purchase = purchaseList.get(position);  // קבלת הפריט הנוכחי מהרשימה
        holder.productName.setText(purchase.getProductName());  // הצגת שם המוצר
        holder.productPrice.setText("Price: $" + purchase.getProductPrice());  // הצגת מחיר המוצר

        // המרה של חותמת הזמן (timestamp) לתאריך קריא
        long timestamp = purchase.getPurchaseDate();  // קבלת חותמת הזמן של תאריך הרכישה
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());  // הגדרת פורמט התאריך
        String formattedDate = sdf.format(new Date(timestamp));  // המרת חותמת הזמן לתאריך בפורמט הרצוי
        holder.purchaseDate.setText("Date: " + formattedDate);  // הצגת התאריך המפורמט
    }

    // מחזירה את מספר הפריטים ברשימה
    @Override
    public int getItemCount() {
        return purchaseList.size();  // מספר הפריטים ברשימת הרכישות
    }

    // מחלקת ViewHolder פנימית, אחראית להחזיק את ה-View עבור פריט ברשימה
    public static class PurchaseViewHolder extends RecyclerView.ViewHolder {
        public TextView productName, productPrice, purchaseDate;  // שדות לתצוגת שם המוצר, מחיר ותאריך הרכישה

        public PurchaseViewHolder(View view) {
            super(view);
            productName = view.findViewById(R.id.productName);  // קישור TextView לשם המוצר מתוך ה-View
            productPrice = view.findViewById(R.id.productPrice);  // קישור TextView למחיר המוצר מתוך ה-View
            purchaseDate = view.findViewById(R.id.purchaseDate);  // קישור TextView לתאריך הרכישה מתוך ה-View
        }
    }
}
