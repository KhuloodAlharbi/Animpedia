package com.example.animapedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View; // تأكد من وجود هذا السطر
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FavoritesAdapter adapter;
    List<FavoriteAnimal> favList;
    DatabaseReference ref;
    FirebaseAuth auth;

    // تعريف الـ Empty View
    LinearLayout emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerView = findViewById(R.id.recyclerViewFav);
        emptyView = findViewById(R.id.emptyView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        favList = new ArrayList<>();
        adapter = new FavoritesAdapter(this, favList);
        recyclerView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            // تأكد أن الرابط هذا هو الرابط الصحيح الخاص بك
            String dbUrl = "https://animapedia-ba3a5-default-rtdb.firebaseio.com/";
            ref = FirebaseDatabase.getInstance(dbUrl).getReference("Favorites").child(user.getUid());

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    favList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        FavoriteAnimal animal = ds.getValue(FavoriteAnimal.class);
                        favList.add(animal);
                    }
                    adapter.notifyDataSetChanged();

                    // التحقق لإظهار أو إخفاء التصميم الفارغ
                    if (favList.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(FavoritesActivity.this, "Error loading favorites", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // 🔥🔥 هذه هي الدالة الناقصة التي ستحل الخطأ 🔥🔥
    public void goBack(View view) {
        finish();
    }
}