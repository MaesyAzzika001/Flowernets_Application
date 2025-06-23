package com.example.ujicoba;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ManajemenTokoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ManajemenTokoAdapter adapter;
    private List<Toko> tokoList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this); // EdgeToEdge bisa dinonaktifkan jika tidak diperlukan
        setContentView(R.layout.activity_manajemen_toko);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Inisialisasi
        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recycler_view_toko);
        tokoList = new ArrayList<>();

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ManajemenTokoAdapter(this, tokoList);
        recyclerView.setAdapter(adapter);

        // Muat data dari database
        loadTokoData();
    }

    private void loadTokoData() {
        // Hapus data lama sebelum memuat yang baru
        tokoList.clear();

        // Panggil metode dari DatabaseHelper untuk mendapatkan semua toko
        List<Toko> allToko = dbHelper.getAllToko();

        // Tambahkan semua toko ke list yang digunakan oleh adapter
        tokoList.addAll(allToko);

        // Beri tahu adapter bahwa data telah berubah
        adapter.notifyDataSetChanged();
    }
}