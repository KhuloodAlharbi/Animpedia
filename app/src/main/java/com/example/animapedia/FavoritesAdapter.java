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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {

    Context context;
    List<FavoriteAnimal> list;
    DatabaseReference favRef;

    public FavoritesAdapter(Context context, List<FavoriteAnimal> list) {
        this.context = context;
        this.list = list;

        // تجهيز رابط الحذف (استخدمي نفس الرابط اليدوي لضمان العمل)
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // ضعي رابط الداتابيس الخاص بك هنا كما فعلنا سابقاً
        String dbUrl = "https://animapedia-ba3a5-default-rtdb.firebaseio.com/";
        favRef = FirebaseDatabase.getInstance(dbUrl).getReference("Favorites").child(uid);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteAnimal animal = list.get(position);

        holder.txtName.setText(animal.getName());
        Glide.with(context).load(animal.getImage()).into(holder.img);

        // القلب دائماً أحمر في هذه الصفحة
        holder.btnRemove.setImageResource(R.drawable.ic_favorite_red);

        // برمجة زر الحذف
        holder.btnRemove.setOnClickListener(v -> {
            // 1. احصل على الترتيب الحالي للعنصر بدقة
            int currentPos = holder.getAdapterPosition();

            if (currentPos != RecyclerView.NO_POSITION) {
                FavoriteAnimal itemToDelete = list.get(currentPos);

                // 2. احذف من فايربيس
                favRef.child(String.valueOf(itemToDelete.getId())).removeValue();

                // 3. احذف من القائمة في الشاشة فوراً (Visual removal)
                list.remove(currentPos);
                notifyItemRemoved(currentPos);
                notifyItemRangeChanged(currentPos, list.size());

                Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show();
            }
        });

        // الانتقال للتفاصيل عند الضغط على باقي الكارت
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AnimalDetailsActivity.class);
            intent.putExtra("id", animal.getId());
            intent.putExtra("name", animal.getName());
            intent.putExtra("image", animal.getImage());
            intent.putExtra("scientificName", "");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img, btnRemove; // btnRemove هو القلب
        TextView txtName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.favImg);
            txtName = itemView.findViewById(R.id.favName);
            btnRemove = itemView.findViewById(R.id.btnRemoveFav); // ربطناه بالـ XML الجديد
        }
    }
}