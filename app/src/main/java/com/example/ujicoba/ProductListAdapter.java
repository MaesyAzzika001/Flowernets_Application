package com.example.ujicoba;

import android.content.Context;
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

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {

    private Context context;
    private List<Produk> productList;
    private OnItemClickListener mListener; // Listener untuk klik

    // 1. Definisikan Interface untuk Click Listener
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // 2. Buat metode untuk mengatur listener dari activity
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ProductListAdapter(Context context, List<Produk> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view, mListener); // Kirim listener ke ViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Produk produk = productList.get(position);
        holder.tvProductName.setText(produk.getNama());

        try {
            double price = Double.parseDouble(produk.getHarga());
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
            holder.tvProductPrice.setText(formatRupiah.format(price));
        } catch (NumberFormatException e) {
            holder.tvProductPrice.setText("Rp " + produk.getHarga());
        }

        int imageResId = context.getResources().getIdentifier(produk.getGambar(), "drawable", context.getPackageName());
        if (imageResId != 0) {
            holder.ivProductImage.setImageResource(imageResId);
        } else {
            holder.ivProductImage.setImageResource(R.drawable.chinese); // Gambar default
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // --- KELAS VIEWHOLDER YANG DIPERBAIKI ---
    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName, tvProductPrice;

        // 3. Modifikasi constructor ViewHolder untuk menerima listener
        public ProductViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.iv_product_image);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);

            // 4. Atur OnClickListener pada seluruh item view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // Panggil metode interface saat item diklik
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}