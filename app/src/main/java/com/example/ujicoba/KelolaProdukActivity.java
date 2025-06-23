// KelolaProdukActivity.java
package com.example.ujicoba;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor; // Pastikan import ini ada
// import android.database.sqlite.SQLiteDatabase; // Tidak perlu import ini di sini
import android.graphics.Color;
import android.os.Bundle;
// import android.util.Log; // Hapus jika tidak digunakan
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.io.File;

public class KelolaProdukActivity extends AppCompatActivity {

    TableLayout tableLayout;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelola_produk);

        // ... (kode SharedPreferences dan BottomNavigationView tetap sama) ...
        SharedPreferences preferences = getSharedPreferences("USER_PREF", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("IS_LOGGED_IN", false);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(KelolaProdukActivity.this, MainActivity.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(KelolaProdukActivity.this, ProfileActivity.class));
                    return true;
                }else if (itemId == R.id.nav_scan) {
                    startActivity(new Intent(KelolaProdukActivity.this, ActivityCameraScan.class));
                    return true;
                }else if (itemId == R.id.nav_cart) {
                    startActivity(new Intent(KelolaProdukActivity.this, ActivityCart.class));
                    return true;
                }else if (itemId == R.id.nav_chat) {
                    startActivity(new Intent(KelolaProdukActivity.this, ChatActivity.class));
                    return true;
                }
                return false;
            }
        });


        dbHelper = new DatabaseHelper(this);
        tableLayout = findViewById(R.id.tableLayout);

        Button tambahProdukButton = findViewById(R.id.btn_tambahproduk);
        tambahProdukButton.setOnClickListener(v -> {
            Intent intent = new Intent(KelolaProdukActivity.this, FormProdukActivity.class);
            startActivity(intent);
        });

        displayProductsContent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Bersihkan view sebelum menambahkan lagi untuk menghindari duplikasi
        // Cek apakah tableLayout memiliki child lebih dari 1 (header) sebelum removeAllViews
        if (tableLayout.getChildCount() > 1) {
            // Simpan header
            View headerView = tableLayout.getChildAt(0);
            tableLayout.removeAllViews();
            tableLayout.addView(headerView); // Tambahkan kembali header
        } else if (tableLayout.getChildCount() == 0) { // Jika belum ada header sama sekali
            addTableHeader();
        }
        displayProductsContent(); // Ganti nama agar tidak rekursif dengan addTableHeader
    }

    private void addTableHeader() {
        // Hanya tambahkan header jika belum ada
        if (tableLayout.getChildCount() == 0) {
            TableRow header = new TableRow(this);
            header.setBackgroundColor(Color.parseColor("#A41E36")); // Sesuaikan dengan warna tema Anda
            header.setPadding(12, 12, 12, 12);

            String[] headers = {
                    "Nama", "Harga", "Deskripsi", "Kategori", "Jenis",
                    "Warna", "Ukuran", "Gambar", "Jumlah", "Tanggal", "Aksi"
            };

            for (String title : headers) {
                TextView tv = new TextView(this);
                tv.setText(title);
                tv.setTextColor(Color.WHITE);
                tv.setTextSize(12); // Ukuran font bisa disesuaikan
                tv.setPadding(8, 8, 8, 8); // Padding agar tidak terlalu mepet
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                // Tambahkan LayoutParams untuk mengatur weight jika diperlukan agar kolom rapi
                // TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                // tv.setLayoutParams(params);
                header.addView(tv);
            }
            tableLayout.addView(header);
        }
    }


    // Ubah nama metode ini agar tidak konflik jika Anda memanggil addTableHeader dari sini
    private void displayProductsContent() {
        // Panggil metode yang mengembalikan Cursor
        Cursor cursor = dbHelper.getAllProductsAsCursor(); // <--- PERUBAHAN DI SINI

        // Hapus baris data produk yang sudah ada (kecuali header)
        int childCount = tableLayout.getChildCount();
        if (childCount > 1) { // Indeks 0 adalah header
            tableLayout.removeViews(1, childCount - 1);
        }


        if (cursor != null && cursor.moveToFirst()) {
            do {
                TableRow row = new TableRow(this);
                row.setPadding(12, 12, 12, 12); // Padding untuk setiap baris

                // Ambil data berdasarkan nama kolom dari DatabaseHelper
                TextView nama = createTextView(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAMA)));
                row.addView(nama);

                TextView harga = createTextView(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HARGA)));
                row.addView(harga);

                TextView deskripsi = createTextView(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESKRIPSI)));
                deskripsi.setMaxLines(2); // Batasi deskripsi agar tidak terlalu panjang di tabel
                deskripsi.setEllipsize(android.text.TextUtils.TruncateAt.END);
                row.addView(deskripsi);

                TextView kategori = createTextView(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_KATEGORI)));
                row.addView(kategori);

                TextView jenis = createTextView(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_JENIS)));
                row.addView(jenis);

                TextView warna = createTextView(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_WARNA)));
                row.addView(warna);

                TextView ukuran = createTextView(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_UKURAN)));
                row.addView(ukuran);

                ImageView gambar = new ImageView(this);
                String path = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GAMBAR));
                // Coba muat gambar jika path adalah nama resource drawable
                int imageResId = 0;
                if (path != null && !path.isEmpty()) {
                    imageResId = getResources().getIdentifier(path, "drawable", getPackageName());
                }

                if (imageResId != 0) {
                    Glide.with(this)
                            .load(imageResId)
                            .placeholder(R.drawable.chinese) // Pastikan placeholder_image ada
                            .error(R.drawable.chinese) // Gambar jika ada error load
                            .override(100, 100) // Sesuaikan ukuran
                            .centerCrop()
                            .into(gambar);
                } else if (path != null && !path.isEmpty() && new File(path).exists()){ // Jika path adalah file path
                    Glide.with(this)
                            .load(new File(path))
                            .placeholder(R.drawable.chinese)
                            .error(R.drawable.chinese)
                            .override(100,100)
                            .centerCrop()
                            .into(gambar);
                }
                else {
                    gambar.setImageResource(R.drawable.chinese); // Gambar default
                }
                TableRow.LayoutParams imageParams = new TableRow.LayoutParams(120, 120); // Sesuaikan ukuran gambar
                imageParams.setMargins(8,0,8,0);
                gambar.setLayoutParams(imageParams);
                row.addView(gambar);


                TextView jumlah = createTextView(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STOK)));
                row.addView(jumlah);

                TextView tanggal = createTextView(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TANGGAL)));
                row.addView(tanggal);

                LinearLayout aksiLayout = new LinearLayout(this);
                aksiLayout.setOrientation(LinearLayout.HORIZONTAL);
                aksiLayout.setGravity(android.view.Gravity.CENTER);


                final int idProduk = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUK_ID));

                Button editButton = new Button(this);
                editButton.setText("Edit");
                // Styling button (opsional, bisa pakai drawable)
                // editButton.setBackgroundColor(Color.parseColor("#FFA500")); // Orange
                // editButton.setTextColor(Color.WHITE);
                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                buttonParams.setMargins(0,0,8,0); // Margin kanan untuk spasi
                editButton.setLayoutParams(buttonParams);
                editButton.setOnClickListener(v -> {
                    Intent intent = new Intent(KelolaProdukActivity.this, FormProdukActivity.class);
                    intent.putExtra("id_produk", idProduk);
                    startActivity(intent);
                });
                aksiLayout.addView(editButton);

                Button hapusButton = new Button(this);
                hapusButton.setText("Hapus");
                // Styling button (opsional)
                // hapusButton.setBackgroundColor(Color.parseColor("#FF0000")); // Red
                // hapusButton.setTextColor(Color.WHITE);
                hapusButton.setOnClickListener(v -> {
                    dbHelper.deleteProduct(idProduk);
                    // Refresh tampilan tabel setelah hapus
                    // tableLayout.removeAllViews(); // Ini akan menghapus header juga
                    // addTableHeader(); // Tambah header lagi
                    // displayProductsContent(); // Tampilkan lagi konten produk
                    onResume(); // Panggil onResume untuk refresh yang lebih aman
                });
                aksiLayout.addView(hapusButton);

                row.addView(aksiLayout);
                tableLayout.addView(row);

            } while (cursor.moveToNext());

        } else {
            // Jika tidak ada data, tambahkan satu baris pesan
            TableRow emptyRow = new TableRow(this);
            TextView emptyText = new TextView(this);
            emptyText.setText("Belum ada produk.");
            emptyText.setPadding(16, 16, 16, 16);
            TableRow.LayoutParams params = new TableRow.LayoutParams();
            params.span = headers.length; // Span sebanyak jumlah kolom header
            params.gravity = android.view.Gravity.CENTER;
            emptyText.setLayoutParams(params);
            emptyRow.addView(emptyText);
            tableLayout.addView(emptyRow);
        }
        if (cursor != null) {
            cursor.close(); // Selalu tutup cursor setelah selesai digunakan
        }
    }

    // Helper method untuk membuat TextView dengan style standar
    private TextView createTextView(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setPadding(8, 8, 8, 8); // Padding untuk sel
        tv.setTextSize(11); // Sesuaikan ukuran font
        // tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER); // Jika ingin semua text center
        return tv;
    }

    // String array header untuk digunakan di displayProductsContent juga
    private final String[] headers = {
            "Nama Produk", "Harga", "Deskripsi", "Kategori", "Jenis",
            "Warna", "Ukuran", "Gambar", "Jumlah", "Ditambahkan", "Aksi"
    };

    // displayProducts() yang lama, bisa dihapus atau di-refactor
    // private void displayProducts() {
    //     addTableHeader();
    //     displayProductsContent();
    // }
}