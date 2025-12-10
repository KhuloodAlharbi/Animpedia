package com.example.animapedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    // Category buttons
    Button btnMammals, btnBirds, btnReptiles, btnFish, btnInsects,
            btnAmphibians, btnInvertebrates, btnChatbot;

    // Store card
    CardView cardStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // --- 1. Connect Category Buttons ---
        btnMammals = findViewById(R.id.btnMammals);
        btnBirds = findViewById(R.id.btnBirds);
        btnReptiles = findViewById(R.id.btnReptiles);
        btnFish = findViewById(R.id.btnFish);
        btnInsects = findViewById(R.id.btnInsects);
        btnAmphibians = findViewById(R.id.btnAmphibians);
        btnInvertebrates = findViewById(R.id.btnInvertebrates);

        // --- 2. Connect Feature Buttons ---
        // The "Chat" button inside the blue card
        btnChatbot = findViewById(R.id.btnChatbot);
        // The "Store" card in the middle of the page
        cardStore = findViewById(R.id.cardStore);

        // --- 3. Set Category Clicks ---
        btnMammals.setOnClickListener(v -> startActivity(new Intent(this, MammalsActivity.class)));
        btnBirds.setOnClickListener(v -> startActivity(new Intent(this, BirdsActivity.class)));
        btnReptiles.setOnClickListener(v -> startActivity(new Intent(this, ReptilesActivity.class)));
        btnFish.setOnClickListener(v -> startActivity(new Intent(this, Fish.class)));
        btnInvertebrates.setOnClickListener(v -> startActivity(new Intent(this, Invertebrates.class)));
        btnAmphibians.setOnClickListener(v -> startActivity(new Intent(this, Amphibians.class)));
        btnInsects.setOnClickListener(v -> startActivity(new Intent(this, InsectsActivity.class)));

        // --- 4. Set Feature Clicks ---
        btnChatbot.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, ChatbotActivity.class)));

        cardStore.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, StoreActivity.class)));

        // ===========================
        // 5. BOTTOM NAVIGATION BAR
        // ===========================
        // We handle the navigation clicks here using the new IDs (navHome, navStore, etc.)

        findViewById(R.id.navHome).setOnClickListener(v -> {
            // Already on Home, do nothing or refresh
        });

        findViewById(R.id.navStore).setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, StoreActivity.class)));

        findViewById(R.id.navChat).setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, ChatbotActivity.class)));

        findViewById(R.id.navFav).setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, FavoritesActivity.class)));

        findViewById(R.id.navCart).setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, CartActivity.class)));
    }
}
