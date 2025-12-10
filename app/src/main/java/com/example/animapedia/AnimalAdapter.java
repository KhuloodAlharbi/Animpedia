package com.example.animapedia;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.ViewHolder> {

    Context context;
    ArrayList<AnimalModel> list;

    // متغيرات Firebase
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference favRef;

    public AnimalAdapter(Context context, ArrayList<AnimalModel> list) {
        this.context = context;
        this.list = list;

        // تجهيز الاتصال بقاعدة البيانات مرة واحدة
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null) {
            favRef = FirebaseDatabase.getInstance().getReference("Favorites").child(user.getUid());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // ربط التصميم بملف animal_item.xml
        View v = LayoutInflater.from(context).inflate(R.layout.animal_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AnimalModel item = list.get(position);

        // عرض المعلومات الأساسية
        holder.txtName.setText(item.getName());
        Glide.with(context).load(item.getImage()).into(holder.imgAnimal);

        // ---------------------------------------------------------------
        //  الجزء الخاص بالقلب (يعتمد على رد الداتابيس)
        // ---------------------------------------------------------------

        // استخدام مصفوفة لتخزين الحالة (هل هو مفضل أم لا)
        final boolean[] isFavorite = {false};

        if (user != null) {
            DatabaseReference itemRef = favRef.child(String.valueOf(item.getId()));

            // 1. مراقبة الداتابيس: أي تغيير هناك سينعكس هنا فوراً
            itemRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // الداتابيس تقول: هذا الحيوان موجود -> حول القلب للأحمر
                        holder.favBtn.setImageResource(R.drawable.ic_favorite_red);
                        isFavorite[0] = true;
                    } else {
                        // الداتابيس تقول: غير موجود -> حول القلب للفارغ
                        holder.favBtn.setImageResource(R.drawable.ic_favorite_border);
                        isFavorite[0] = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            // 2. عند الضغط: نرسل الأمر للداتابيس وننتظر التغيير
            holder.favBtn.setOnClickListener(v -> {
                if (isFavorite[0]) {
                    // حذف
                    itemRef.removeValue();
                    // ملاحظة: لن نغير الأيقونة هنا يدوياً، سننتظر onDataChange يغيرها
                } else {
                    // إضافة
                    HashMap<String, Object> favData = new HashMap<>();
                    favData.put("id", item.getId());
                    favData.put("name", item.getName());
                    favData.put("image", item.getImage());
                    itemRef.setValue(favData);
                    // ملاحظة: لن نغير الأيقونة هنا يدوياً، سننتظر onDataChange يغيرها
                }
            });
        }

        // الانتقال للتفاصيل
        holder.card.setOnClickListener(v -> {
            Intent i = new Intent(context, AnimalDetailsActivity.class);
            i.putExtra("name", item.getName());
            i.putExtra("scientificName", item.getScientificName());
            i.putExtra("image", item.getImage());
            i.putExtra("id", item.getId());
            context.startActivity(i);
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAnimal, favBtn; // أضفنا favBtn
        TextView txtName;
        CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAnimal = itemView.findViewById(R.id.imgAnimalItem);
            txtName = itemView.findViewById(R.id.txtAnimalNameItem);
            card = itemView.findViewById(R.id.cardAnimal);

            // ربط زر القلب الجديد الموجود في ملف XML
            favBtn = itemView.findViewById(R.id.imgFavItem);
        }
    }
}