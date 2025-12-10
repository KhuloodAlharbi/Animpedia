package com.example.animapedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull; // مهم للـ Firebase listeners

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast; // لعرض رسائل للمستخدم

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

// استدعاء مكتبات Firebase
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap; // لحفظ البيانات

public class AnimalDetailsActivity extends AppCompatActivity {

    // تعريف العناصر
    ImageView imgAnimal, btnFav; // أضفنا btnFav هنا
    TextView txtName, txtSciName, txtKingdom, txtPhylum, txtClass, txtOrder, txtFamily, txtGenus, txtDescription;
    ImageButton btnBack;

    // متغيرات Firebase والمفضلة
    DatabaseReference favRef;
    FirebaseAuth mAuth;
    boolean isFavorite = false;
    int animalId; // نحتاج الآيدي ليكون عاماً (Global)
    String animalNameStr, animalImageStr; // لحفظ البيانات في المفضلة

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_details);

        // ربط العناصر بالـ XML
        imgAnimal = findViewById(R.id.imgAnimal);
        btnFav = findViewById(R.id.btnFav); // الزر الجديد (تأكد من وجوده في XML)
        txtName = findViewById(R.id.AnimalName);
        txtSciName = findViewById(R.id.txtSciName);

        txtKingdom = findViewById(R.id.txtKingdom);
        txtPhylum = findViewById(R.id.txtPhylum);
        txtClass = findViewById(R.id.txtClass);
        txtOrder = findViewById(R.id.txtOrder);
        txtFamily = findViewById(R.id.txtFamily);
        txtGenus = findViewById(R.id.txtGenus);
        txtDescription = findViewById(R.id.txtDescription);
        btnBack = findViewById(R.id.btnback);

        // استلام البيانات من الصفحة السابقة
        animalNameStr = getIntent().getStringExtra("name");
        String sciName = getIntent().getStringExtra("scientificName");
        animalImageStr = getIntent().getStringExtra("image");
        animalId = getIntent().getIntExtra("id", 0);

        // عرض البيانات الأساسية
        txtName.setText(animalNameStr);
        txtSciName.setText("Scientific name: " + sciName);
        Glide.with(this).load(animalImageStr).into(imgAnimal);

        // ---------------------------------------------------------
        // بداية كود المفضلة (Firebase Logic)
        // ---------------------------------------------------------
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            // المسار: Favorites -> UserID -> AnimalID
            favRef = FirebaseDatabase.getInstance().getReference("Favorites")
                    .child(user.getUid())
                    .child(String.valueOf(animalId));

            // 1. التحقق: هل هذا الحيوان موجود بالفعل في مفضلة المستخدم؟
            checkFavoriteStatus();

            // 2. برمجة الضغط على زر القلب
            btnFav.setOnClickListener(view -> {
                if (isFavorite) {
                    removeFromFavorites();
                } else {
                    addToFavorites();
                }
            });
        } else {
            // إذا لم يكن مسجلاً للدخول، نخفي الزر أو نطلب منه التسجيل
            btnFav.setVisibility(View.GONE);
        }
        // ---------------------------------------------------------

        // تحميل باقي التفاصيل من API
        loadDetails(animalId);
    }

    // دالة التحقق من الحالة الحالية
    private void checkFavoriteStatus() {
        favRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // موجود في المفضلة -> لون أحمر
                    btnFav.setImageResource(R.drawable.ic_favorite_red);
                    isFavorite = true;
                } else {
                    // غير موجود -> حدود فقط
                    btnFav.setImageResource(R.drawable.ic_favorite_border);
                    isFavorite = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // دالة الإضافة
    private void addToFavorites() {
        // نجهز البيانات التي نريد حفظها في صفحة المفضلة عشان ما نضطر نحملها من النت تاني
        HashMap<String, Object> favData = new HashMap<>();
        favData.put("id", animalId);
        favData.put("name", animalNameStr);
        favData.put("image", animalImageStr);

        favRef.setValue(favData)
                .addOnSuccessListener(aVoid -> Toast.makeText(AnimalDetailsActivity.this, "Added to Favorites", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(AnimalDetailsActivity.this, "Error adding to favorites", Toast.LENGTH_SHORT).show());
    }

    // دالة الحذف
    private void removeFromFavorites() {
        favRef.removeValue()
                .addOnSuccessListener(aVoid -> Toast.makeText(AnimalDetailsActivity.this, "Removed from Favorites", Toast.LENGTH_SHORT).show());
    }

    // دالة تحميل التفاصيل (كودك الأصلي كما هو)
    private void loadDetails(int id) {

        String url = "https://api.inaturalist.org/v1/taxa/" + id;
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest req = new StringRequest(Request.Method.GET, url,
                res -> {
                    try {
                        JSONObject root = new JSONObject(res);
                        // تأكدنا من وجود results قبل القراءة لتجنب الانهيار
                        if (root.has("results") && root.getJSONArray("results").length() > 0) {
                            JSONObject obj = root.getJSONArray("results").getJSONObject(0);
                            JSONArray ancestors = obj.optJSONArray("ancestors");

                            String kingdom="", phylum="", className="", order="", family="", genus="";

                            if (ancestors != null) {
                                for (int i = 0; i < ancestors.length(); i++) {
                                    JSONObject a = ancestors.getJSONObject(i);
                                    String rank = a.optString("rank");

                                    switch (rank) {
                                        case "kingdom": kingdom = a.optString("name"); break;
                                        case "phylum": phylum = a.optString("name"); break;
                                        case "class": className = a.optString("name"); break;
                                        case "order": order = a.optString("name"); break;
                                        case "family": family = a.optString("name"); break;
                                        case "genus": genus = a.optString("name"); break;
                                    }
                                }
                            }

                            txtKingdom.setText("Kingdom: " + kingdom);
                            txtPhylum.setText("Phylum: " + phylum);
                            txtClass.setText("Class: " + className);
                            txtOrder.setText("Order: " + order);
                            txtFamily.setText("Family: " + family);
                            txtGenus.setText("Genus: " + genus);

                            String desc = obj.optString("wikipedia_summary", "");
                            if (desc.isEmpty()) desc = "No description available.";
                            txtDescription.setText(desc);
                        } else {
                            txtDescription.setText("No details found.");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        txtDescription.setText("Failed to load details.");
                    }
                },
                err -> txtDescription.setText("Network error.")
        );

        queue.add(req);
    }

    public void goBack(View view) {
        finish();
    }
}