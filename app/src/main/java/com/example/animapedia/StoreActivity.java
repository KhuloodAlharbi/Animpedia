package com.example.animapedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StoreActivity extends AppCompatActivity {

    RecyclerView recyclerViewStore;
    ProductAdapter productAdapter;
    List<ProductModel> productList;

    DatabaseReference databaseReference;

    CardView cardGoToCart;
    TextView cartText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        // RecyclerView
        recyclerViewStore = findViewById(R.id.recyclerViewStore);
        recyclerViewStore.setLayoutManager(new LinearLayoutManager(this));

        // List + Adapter
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList);
        recyclerViewStore.setAdapter(productAdapter);

        // Firebase reference for products
        databaseReference = FirebaseDatabase.getInstance().getReference("products");
        loadProducts();

        // CART BAR ELEMENTS
        cardGoToCart = findViewById(R.id.cardGoToCart);
        cartText = findViewById(R.id.cartText);

        // Go to cart on click
        cardGoToCart.setOnClickListener(v -> {
            startActivity(new Intent(StoreActivity.this, CartActivity.class));
        });

        // Load cart count
        loadCartCount();
    }

    private void loadProducts() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                productList.clear();

                for (DataSnapshot item : snapshot.getChildren()) {
                    ProductModel model = item.getValue(ProductModel.class);
                    if (model != null) productList.add(model);
                }

                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    private void loadCartCount() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("cart")
                .child(uid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();
                cartText.setText("View Cart (" + count + ")");
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }
}
