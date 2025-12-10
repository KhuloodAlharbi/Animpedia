package com.example.animapedia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<ProductModel> cartList;

    public CartAdapter(Context context, List<ProductModel> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        ProductModel item = cartList.get(position);

        holder.cartName.setText(item.getName());
        holder.cartPrice.setText("$" + item.getPrice());
        Glide.with(context).load(item.getImageUrl()).into(holder.cartImage);

        // --------------------------------
        // DELETE ITEM FROM CART (CORRECT PLACE)
        // --------------------------------
        holder.deleteIcon.setOnClickListener(v -> {

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference("cart")
                    .child(uid)
                    .child(item.getId());  // must exist in ProductModel

            ref.removeValue();
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        TextView cartName, cartPrice;
        ImageView cartImage, deleteIcon;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            cartName = itemView.findViewById(R.id.cartName);
            cartPrice = itemView.findViewById(R.id.cartPrice);
            cartImage = itemView.findViewById(R.id.cartImage);
            deleteIcon = itemView.findViewById(R.id.deleteIcon); // MUST exist in XML
        }
    }
}
