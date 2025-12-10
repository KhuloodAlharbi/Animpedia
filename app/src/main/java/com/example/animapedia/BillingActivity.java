package com.example.animapedia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class BillingActivity extends AppCompatActivity {

    TextView orderSummary;
    EditText inputAddress, inputPhone;
    Button btnConfirm;

    String name, price;
    List<String> items;
    double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        orderSummary = findViewById(R.id.orderSummary);
        inputAddress = findViewById(R.id.inputAddress);
        inputPhone = findViewById(R.id.inputPhone);
        btnConfirm = findViewById(R.id.btnConfirm);

        // Check what type of order it is
        name = getIntent().getStringExtra("name");
        price = getIntent().getStringExtra("price");

        items = getIntent().getStringArrayListExtra("items");
        totalPrice = getIntent().getDoubleExtra("totalPrice", 0);

        if (name != null) {
            // SINGLE PRODUCT
            orderSummary.setText("Buying: " + name + "\nPrice: $" + price);
        }
        else {
            // MULTIPLE ITEMS
            orderSummary.setText("Items: " + items + "\nTotal: $" + totalPrice);
        }

        btnConfirm.setOnClickListener(v -> saveOrder());
    }


    private void saveOrder() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("orders")
                .child(uid)
                .push();

        HashMap<String, Object> order = new HashMap<>();

        order.put("address", inputAddress.getText().toString());
        order.put("phone", inputPhone.getText().toString());
        order.put("timestamp", System.currentTimeMillis());

        if (name != null) {
            // Single product order
            order.put("item", name);
            order.put("price", price);
        } else {
            // Cart order
            order.put("items", items);
            order.put("total", totalPrice);
        }

        ref.setValue(order).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Order Submitted Successfully!", Toast.LENGTH_LONG).show();
            finish();
        });
    }
}
