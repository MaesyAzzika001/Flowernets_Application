// KonfirmasiPembayaranActivity.java
package com.example.ujicoba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class KonfirmasiPembayaranActivity extends AppCompatActivity {

    private TextView tvNomorPesanan, tvNamaProduk, tvWaktuPemesanan, tvWaktuPembayaran;
    private DatabaseHelper dbHelper;
    private String currentOrderNumber;
    private long currentOrderAutoId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_pembayaran);

        dbHelper = new DatabaseHelper(this);

        tvNomorPesanan = findViewById(R.id.tv_konfirmasi_nomor_pesanan);
        tvNamaProduk = findViewById(R.id.tv_konfirmasi_nama_produk);
        tvWaktuPemesanan = findViewById(R.id.tv_konfirmasi_waktu_pemesanan);
        tvWaktuPembayaran = findViewById(R.id.tv_konfirmasi_waktu_pembayaran);
        ImageButton btnBack = findViewById(R.id.btn_back_konfirmasi);
        Button btnLihatDetailFooter = findViewById(R.id.btn_lihat_detail_pesanan_footer);
        TextView tvLihatNota = findViewById(R.id.tv_lihat_nota);

        btnBack.setOnClickListener(v -> {
            // Arahkan ke MainActivity atau halaman utama setelah konfirmasi
            Intent intent = new Intent(KonfirmasiPembayaranActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        View.OnClickListener lihatDetailClickListener = v -> {
            if (currentOrderNumber != null || currentOrderAutoId != -1) {
                // Di sini Anda bisa membuat intent ke activity detail pesanan
                // dengan membawa currentOrderNumber atau currentOrderAutoId
                Toast.makeText(KonfirmasiPembayaranActivity.this, "Membuka detail pesanan: " + (currentOrderNumber != null ? currentOrderNumber : currentOrderAutoId), Toast.LENGTH_SHORT).show();
                // Contoh:
                // Intent detailIntent = new Intent(KonfirmasiPembayaranActivity.this, DetailPesananActivity.class);
                // detailIntent.putExtra("ORDER_NUMBER", currentOrderNumber);
                // detailIntent.putExtra("ORDER_AUTO_ID", currentOrderAutoId);
                // startActivity(detailIntent);
            } else {
                Toast.makeText(KonfirmasiPembayaranActivity.this, "Data pesanan tidak ditemukan.", Toast.LENGTH_SHORT).show();
            }
        };

        btnLihatDetailFooter.setOnClickListener(lihatDetailClickListener);
        tvLihatNota.setOnClickListener(lihatDetailClickListener);


        currentOrderNumber = getIntent().getStringExtra("ORDER_NUMBER");
        currentOrderAutoId = getIntent().getLongExtra("ORDER_AUTO_ID", -1);


        Order order = null;
        if (currentOrderAutoId != -1) {
            order = dbHelper.getOrderById(currentOrderAutoId);
        } else if (currentOrderNumber != null) {
            order = dbHelper.getOrderByOrderNumber(currentOrderNumber);
        }


        if (order != null) {
            tvNomorPesanan.setText(order.getOrderNumber());
            tvNamaProduk.setText(order.getProductSummary()); // Atau ambil dari itemsJson jika lebih detail
            tvWaktuPemesanan.setText(order.getOrderTimestamp());
            tvWaktuPembayaran.setText(order.getPaymentTimestamp());
        } else {
            Toast.makeText(this, "Gagal memuat detail pesanan.", Toast.LENGTH_SHORT).show();
            tvNomorPesanan.setText("-");
            tvNamaProduk.setText("-");
            tvWaktuPemesanan.setText("-");
            tvWaktuPembayaran.setText("-");
        }
    }

    @Override
    public void onBackPressed() {
        // Arahkan ke MainActivity atau halaman utama setelah konfirmasi
        Intent intent = new Intent(KonfirmasiPembayaranActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        // super.onBackPressed(); // Hapus ini jika ingin perilaku custom
    }
}