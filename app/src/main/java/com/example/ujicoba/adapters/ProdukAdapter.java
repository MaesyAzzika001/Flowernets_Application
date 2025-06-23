package com.example.ujicoba.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide; // Pastikan Anda menambahkan dependensi Glide
import com.example.ujicoba.Produk;
import com.example.ujicoba.R;
import java.io.File;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.ProdukViewHolder> {

    private Context context;
    private List<Produk> produkList;

    public ProdukAdapter(Context context, List<Produk> produkList) {
        this.context = context;
        this.produkList = produkList;
    }

    @NonNull
    @Override
    public ProdukViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_produk, parent, false);
        return new ProdukViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdukViewHolder holder, int position) {
        Produk produk = produkList.get(position);
        holder.tvNama.setText(produk.getNama());

        // Format harga ke Rupiah
        try {
            double harga = Double.parseDouble(produk.getHarga());
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
            holder.tvHarga.setText(formatRupiah.format(harga));
        } catch (NumberFormatException e) {
            holder.tvHarga.setText(produk.getHarga()); // Tampilkan apa adanya jika format salah
        }

        // Muat gambar menggunakan Glide
        if (produk.getGambar() != null && !produk.getGambar().isEmpty()) {
            Glide.with(context)
                    .load(new File(produk.getGambar())) // Jika gambar adalah path file
                    // .load(Uri.parse(produk.getGambar())) // Jika gambar adalah URI string
                    .placeholder(R.drawable.chinese) // Gambar default
                    .error(R.drawable.board) // Gambar jika gagal load
                    .into(holder.ivGambar);
        }
    }

    @Override
    public int getItemCount() {
        return produkList.size();
    }

    static class ProdukViewHolder extends RecyclerView.ViewHolder {
        ImageView ivGambar;
        TextView tvNama, tvHarga;

        public ProdukViewHolder(@NonNull View itemView) {
            super(itemView);
            ivGambar = itemView.findViewById(R.id.iv_produk_gambar);
            tvNama = itemView.findViewById(R.id.tv_produk_nama);
            tvHarga = itemView.findViewById(R.id.tv_produk_harga);
        }
    }
}