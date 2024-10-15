package com.example.newfinal;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context; // הקשר של האפליקציה - נדרש לצורך גישה למשאבים כמו LayoutInflater ו-Glide
    private List<Product> productList; // רשימת המוצרים שתוצג ברשימה

    // בנאי למחלקת האדפטר, מקבל את ההקשר ורשימת המוצרים
    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList != null ? productList : new ArrayList<>(); // אם הרשימה ריקה, יוצרים רשימה חדשה
    }

    // יצירת ViewHolder חדש כאשר אין View ממוחזר זמין
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // ניפוח קובץ ה-XML של פריט המוצר כדי ליצור את ה-View המתאים
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view); // יצירת והחזרת ה-ViewHolder עם ה-View החדש
    }

    // קישור הנתונים ל-ViewHolder שנוצר על ידי onCreateViewHolder
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position); // קבלת הפריט הנוכחי מהרשימה
        holder.productName.setText(product.getName()); // הצגת שם המוצר
        holder.productCategory.setText(product.getCategory()); // הצגת קטגוריית המוצר
        holder.productLanguage.setText(product.getLanguage()); // הצגת השפה של המוצר
        holder.productPrice.setText("$" + product.getPrice()); // הצגת מחיר המוצר

        // שימוש ב-Glide כדי לטעון את התמונה מה-URL לתוך ImageView
        Glide.with(context)
                .load(product.getImageUrl())
                .into(holder.productImage);

        // קביעת פעולה בעת לחיצה על כפתור הקנייה
        holder.purchaseButton.setOnClickListener(v -> {
            double price = product.getPrice();
            String name = product.getName();

            Log.d("ProductAdapter", "Product Name: " + name + ", Product Price: " + price); // רישום המידע ביומן לצורך דיבוג

            // יצירת Intent ומעבר לפעילות רכישת המוצר
            Intent intent = new Intent(context, PurchaseProductActivity.class);
            intent.putExtra("productName", name); // העברת שם המוצר לפעילות הרכישה
            intent.putExtra("productPrice", price); // העברת מחיר המוצר לפעילות הרכישה
            context.startActivity(intent); // התחלת פעילות רכישת המוצר
        });

    }

    // מחזירה את מספר הפריטים ברשימה
    @Override
    public int getItemCount() {
        return productList.size(); // מספר הפריטים ברשימת המוצרים
    }

    // פונקציה להוספת מוצר לרשימה ולעדכון ה-RecyclerView
    public void addProduct(Product product) {
        productList.add(product); // הוספת המוצר לרשימה
        notifyItemInserted(productList.size() - 1); // עדכון ה-RecyclerView לגבי פריט חדש שנוסף
    }

    // פונקציה לניקוי כל המוצרים מהרשימה ולעדכון ה-RecyclerView
    public void clearProducts() {
        productList.clear(); // ניקוי כל הפריטים מהרשימה
        notifyDataSetChanged(); // עדכון ה-RecyclerView לגבי השינויים
    }

    // מחלקת ViewHolder פנימית, אחראית להחזיק את ה-View עבור פריט ברשימה
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView productName, productLanguage, productCategory, productPrice; // שדות לתצוגת שם המוצר, שפה, קטגוריה ומחיר
        public ImageView productImage; // תצוגה לתמונה של המוצר
        public Button purchaseButton; // כפתור לרכישת המוצר

        public ProductViewHolder(View view) {
            super(view);
            productName = view.findViewById(R.id.productName); // קישור TextView לשם המוצר מתוך ה-View
            productLanguage = view.findViewById(R.id.productLanguage); // קישור TextView לשפה מתוך ה-View
            productCategory = view.findViewById(R.id.productCategory); // קישור TextView לקטגוריה מתוך ה-View
            productPrice = view.findViewById(R.id.productPrice); // קישור TextView למחיר מתוך ה-View
            productImage = view.findViewById(R.id.productImage); // קישור ImageView לתמונה מתוך ה-View
            purchaseButton = view.findViewById(R.id.purchaseButton); // קישור Button לכפתור הרכישה מתוך ה-View
        }
    }
}
