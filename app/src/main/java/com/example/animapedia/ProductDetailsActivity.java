package com.example.animapedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    ImageView detailImage;
    TextView detailName, detailPrice, detailDescription;
    Button btnBuyNow, btnAddToCart;   // <-- ADDED THIS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // XML links
        detailImage = findViewById(R.id.detailImage);
        detailName = findViewById(R.id.detailName);
        detailPrice = findViewById(R.id.detailPrice);
        detailDescription = findViewById(R.id.detailDescription);
        btnBuyNow = findViewById(R.id.btnBuyNow);
        btnAddToCart = findViewById(R.id.btnAddToCart);   // <-- FIXED

        // Receive data from adapter
        String name = getIntent().getStringExtra("name");
        String price = getIntent().getStringExtra("price");
        String description = getIntent().getStringExtra("description");
        String imageUrl = getIntent().getStringExtra("imageUrl");

        // Display product data
        detailName.setText(name);
        detailPrice.setText("$" + price);
        detailDescription.setText(description);
        Glide.with(this).load(imageUrl).into(detailImage);

        btnBuyNow.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailsActivity.this, BillingActivity.class);

            intent.putExtra("name", name);
            intent.putExtra("price", price);

            startActivity(intent);
        });


        // ADD TO CART
        btnAddToCart.setOnClickListener(v -> {

            FirebaseAuth auth = FirebaseAuth.getInstance();
            String uid = auth.getCurrentUser().getUid(); // make sure user logged in

            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference("cart")
                    .child(uid)
                    .push();  // new item

            HashMap<String, String> item = new HashMap<>();
            item.put("name", name);
            item.put("price", price);
            item.put("imageUrl", imageUrl);

            ref.setValue(item)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(ProductDetailsActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ProductDetailsActivity.this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

    }
}
