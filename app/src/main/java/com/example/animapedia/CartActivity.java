package com.example.animapedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    RecyclerView cartRecycler;
    CartAdapter cartAdapter;
    List<ProductModel> cartList;

    TextView totalPriceText;
    Button btnCheckout;

    double totalPrice = 0;   // <-- correct total variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // --- UI ELEMENTS ---
        cartRecycler = findViewById(R.id.cartRecycler);
        totalPriceText = findViewById(R.id.totalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);

        cartRecycler.setLayoutManager(new LinearLayoutManager(this));

        cartList = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartList);
        cartRecycler.setAdapter(cartAdapter);

        // Load cart from Firebase
        loadCartItems();

        // CHECKOUT BUTTON
        btnCheckout.setOnClickListener(v -> {
            Intent i = new Intent(CartActivity.this, BillingActivity.class);

            // ✔ Send total price
            i.putExtra("totalPrice", totalPrice);

            // ✔ Send all cart item names
            ArrayList<String> names = new ArrayList<>();
            for (ProductModel item : cartList) {
                names.add(item.getName());
            }

            i.putStringArrayListExtra("items", names);

            startActivity(i);
        });
    }

    private void loadCartItems() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("cart")
                .child(uid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                cartList.clear();
                totalPrice = 0;

                for (DataSnapshot item : snapshot.getChildren()) {

                    ProductModel model = item.getValue(ProductModel.class);

                    if (model != null) {
                        // store Firebase key inside model
                        model.setId(item.getKey());
                        cartList.add(model);

                        // TOTAL PRICE CALC
                        try {
                            totalPrice += Double.parseDouble(model.getPrice());
                        } catch (Exception ignored) {}
                    }
                }

                totalPriceText.setText("Total: $" + totalPrice);
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }
}
