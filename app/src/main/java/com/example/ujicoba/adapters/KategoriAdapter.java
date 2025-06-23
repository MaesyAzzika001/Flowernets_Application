package com.example.ujicoba.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ujicoba.Kategori;
import com.example.ujicoba.R;
import java.util.List;
import java.util.Locale;

public class KategoriAdapter extends RecyclerView.Adapter<KategoriAdapter.KategoriViewHolder> {

    private Context context;
    private List<Kategori> kategoriList;

    public KategoriAdapter(Context context, List<Kategori> kategoriList) {
        this.context = context;
        this.kategoriList = kategoriList;
    }

    @NonNull
    @Override
    public KategoriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_kategori, parent, false);
        return new KategoriViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KategoriViewHolder holder, int position) {
        Kategori kategori = kategoriList.get(position);
        holder.tvNama.setText(kategori.getNama());
        holder.tvJumlah.setText(String.format(Locale.getDefault(), "(%d)", kategori.getJumlahProduk()));
    }

    @Override
    public int getItemCount() {
        return kategoriList.size();
    }

    static class KategoriViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvJumlah;

        public KategoriViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_kategori_nama);
            tvJumlah = itemView.findViewById(R.id.tv_kategori_jumlah);
        }
    }
}