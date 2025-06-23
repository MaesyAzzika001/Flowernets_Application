package com.example.ujicoba;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ManajemenTokoAdapter extends RecyclerView.Adapter<ManajemenTokoAdapter.TokoViewHolder> {

    private Context context;
    private List<Toko> tokoList;

    public ManajemenTokoAdapter(Context context, List<Toko> tokoList) {
        this.context = context;
        this.tokoList = tokoList;
    }

    @NonNull
    @Override
    public TokoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_manajemen_toko, parent, false);
        return new TokoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TokoViewHolder holder, int position) {
        Toko toko = tokoList.get(position);

        holder.tvNamaToko.setText(toko.getNamaToko());
        holder.tvStatusToko.setText(toko.getStatus());
        holder.tvLokasiToko.setText(toko.getLokasi() != null ? toko.getLokasi() : "N/A");

        // Set listener untuk tombol aksi
        holder.actionDetail.setOnClickListener(v -> Toast.makeText(context, "Detail: " + toko.getNamaToko(), Toast.LENGTH_SHORT).show());
        holder.actionVerifikasi.setOnClickListener(v -> Toast.makeText(context, "Verifikasi: " + toko.getNamaToko(), Toast.LENGTH_SHORT).show());
        holder.actionBlokir.setOnClickListener(v -> Toast.makeText(context, "Blokir: " + toko.getNamaToko(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return tokoList.size();
    }

    public static class TokoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaToko, tvStatusToko, tvLokasiToko;
        // Mengubah dari ImageView ke TextView
        TextView actionDetail, actionVerifikasi, actionBlokir;

        public TokoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaToko = itemView.findViewById(R.id.tv_nama_toko);
            tvStatusToko = itemView.findViewById(R.id.tv_status_toko);
            tvLokasiToko = itemView.findViewById(R.id.tv_lokasi_toko);

            // findViewById sekarang akan mengambil TextView, sesuai dengan layout
            actionDetail = itemView.findViewById(R.id.action_detail);
            actionVerifikasi = itemView.findViewById(R.id.action_verifikasi);
            actionBlokir = itemView.findViewById(R.id.action_blokir);
        }
    }
}