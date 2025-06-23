package com.example.ujicoba;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BestSellerAdapter extends RecyclerView.Adapter<BestSellerAdapter.ProductViewHolder> {

    private Context context;
    private List<Produk> productList;

    public BestSellerAdapter(Context context, List<Produk> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Produk product = productList.get(position);
        holder.tvProductName.setText(product.getNama());

        // Format harga
        try {
            double priceValue = Double.parseDouble(product.getHarga());
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
            holder.tvProductPrice.setText(format.format(priceValue));
        } catch (NumberFormatException e) {
            holder.tvProductPrice.setText("Rp" + product.getHarga()); // Fallback
        }


        // Set gambar dari nama drawable
        int imageId = context.getResources().getIdentifier(product.getGambar(), "drawable", context.getPackageName());
        if (imageId != 0) {
            holder.ivProductImage.setImageResource(imageId);
        } else {
            // Jika tidak ditemukan, set gambar default
            holder.ivProductImage.setImageResource(R.drawable.buxket); // Ganti dengan drawable default Anda
        }

        // Menambahkan OnClickListener pada item view
        holder.itemView.setOnClickListener(v -> {
            // Buat Intent untuk membuka DetailProductActivity
            Intent intent = new Intent(context, DetailProductActivity.class);
            // Kirim ID produk melalui intent
            intent.putExtra("PRODUCT_ID", product.getId());
            // Mulai activity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName;
        TextView tvProductPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.iv_product_image);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
        }
    }
}