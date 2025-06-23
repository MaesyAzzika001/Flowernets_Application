package com.example.ujicoba;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

// import androidx.activity.EdgeToEdge; // Komentari jika tidak digunakan

public class ManajemenPelangganActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private LinearLayout pelangganTableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this); // Komentari jika tidak digunakan
        setContentView(R.layout.activity_manajemen_pelanggan);

        Toolbar toolbar = findViewById(R.id.toolbar_pelanggan);
        setSupportActionBar(toolbar);

        // Mengaktifkan tombol kembali di toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        // Listener untuk tombol kembali di toolbar sudah tidak diperlukan jika menggunakan onSupportNavigateUp()
        // toolbar.setNavigationOnClickListener(v -> onBackPressed());

        dbHelper = new DatabaseHelper(this);
        pelangganTableLayout = findViewById(R.id.pelanggan_table_layout);

        loadPelangganData();
    }

    private void loadPelangganData() {
        pelangganTableLayout.removeAllViews(); // Bersihkan view lama sebelum memuat data baru

        Cursor cursor = dbHelper.getAllUsers();

        if (cursor == null) {
            Log.e("ManajemenPelanggan", "Cursor is null.");
            Toast.makeText(this, "Tidak dapat mengambil data pengguna.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mendapatkan index kolom berdasarkan nama. Menggunakan string literal karena konstanta di DatabaseHelper private.
        // Akan lebih baik jika konstanta tersebut public static final String.
        int idColumnIndex = cursor.getColumnIndex("id");
        int emailColumnIndex = cursor.getColumnIndex("email");

        if (emailColumnIndex == -1) {
            Log.e("ManajemenPelanggan", "Kolom 'email' tidak ditemukan.");
            Toast.makeText(this, "Kesalahan pada struktur database.", Toast.LENGTH_SHORT).show();
            cursor.close();
            return;
        }


        if (cursor.moveToFirst()) {
            do {
                // final int userId = (idColumnIndex != -1) ? cursor.getInt(idColumnIndex) : -1; // Dapatkan ID jika diperlukan
                final String email = cursor.getString(emailColumnIndex);

                // Buat baris baru (LinearLayout horizontal)
                LinearLayout rowLayout = new LinearLayout(this);
                LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                // Margin untuk setiap baris (kiri, atas, kanan, bawah)
                rowParams.setMargins(dpToPx(4), dpToPx(1), dpToPx(4), dpToPx(1));
                rowLayout.setLayoutParams(rowParams);
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                rowLayout.setBackgroundColor(Color.WHITE);
                // Padding di dalam baris (kiri, atas, kanan, bawah)
                rowLayout.setPadding(dpToPx(0), dpToPx(4), dpToPx(0), dpToPx(4));


                // 1. TextView untuk "Nama" (menampilkan email)
                TextView namaTextView = new TextView(this);
                LinearLayout.LayoutParams namaParams = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.5f); // weight 1.5
                namaTextView.setLayoutParams(namaParams);
                namaTextView.setText(email); // Menampilkan email di kolom "Nama"
                namaTextView.setTextColor(Color.BLACK);
                namaTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
                namaTextView.setPadding(dpToPx(12), dpToPx(8), dpToPx(8), dpToPx(8));
                rowLayout.addView(namaTextView);

                // 2. TextView untuk "Email"
                TextView emailTextView = new TextView(this);
                LinearLayout.LayoutParams emailParams = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 2.5f); // weight 2.5
                emailTextView.setLayoutParams(emailParams);
                emailTextView.setText(email);
                emailTextView.setTextColor(Color.BLACK);
                emailTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
                emailTextView.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));
                rowLayout.addView(emailTextView);

                // 3. LinearLayout untuk "Aksi"
                LinearLayout aksiLayout = new LinearLayout(this);
                LinearLayout.LayoutParams aksiParams = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 2.0f); // weight 2.0
                aksiLayout.setLayoutParams(aksiParams);
                aksiLayout.setOrientation(LinearLayout.VERTICAL);
                aksiLayout.setGravity(Gravity.CENTER_HORIZONTAL); // Pusatkan konten di dalam aksiLayout
                aksiLayout.setPadding(dpToPx(8), dpToPx(4), dpToPx(8), dpToPx(4));

                // Tombol Aksi: Blokir
                TextView blokirTextView = new TextView(this);
                LinearLayout.LayoutParams actionTextParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                blokirTextView.setLayoutParams(actionTextParams);
                blokirTextView.setText("(blokir)");
                blokirTextView.setTextColor(Color.RED);
                blokirTextView.setClickable(true);
                blokirTextView.setFocusable(true);
                blokirTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                // Padding agar mirip dengan contoh XML statis
                blokirTextView.setPadding(0,0,0,dpToPx(2));
                blokirTextView.setOnClickListener(v -> {
                    Toast.makeText(ManajemenPelangganActivity.this, "Blokir: " + email, Toast.LENGTH_SHORT).show();
                    // TODO: Implementasikan logika blokir pengguna di sini
                });
                aksiLayout.addView(blokirTextView);

                // Tombol Aksi: Reset Password
                TextView resetPasswordTextView = new TextView(this);
                resetPasswordTextView.setLayoutParams(actionTextParams);
                resetPasswordTextView.setText("(reset password)");
                resetPasswordTextView.setTextColor(Color.BLUE);
                resetPasswordTextView.setClickable(true);
                resetPasswordTextView.setFocusable(true);
                resetPasswordTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                // Padding agar mirip dengan contoh XML statis
                resetPasswordTextView.setPadding(0,dpToPx(2),0,0);
                resetPasswordTextView.setOnClickListener(v -> {
                    Toast.makeText(ManajemenPelangganActivity.this, "Reset Password: " + email, Toast.LENGTH_SHORT).show();
                    // TODO: Implementasikan logika reset password di sini
                });
                aksiLayout.addView(resetPasswordTextView);

                rowLayout.addView(aksiLayout);

                // Tambahkan baris ke tabel layout
                pelangganTableLayout.addView(rowLayout);

                // Tidak perlu menambahkan separator secara manual jika menggunakan showDividers di XML

            } while (cursor.moveToNext());
        } else {
            // Tidak ada data pengguna, tampilkan pesan
            TextView noDataTextView = new TextView(this);
            noDataTextView.setText("Tidak ada data pelanggan.");
            noDataTextView.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));
            noDataTextView.setGravity(Gravity.CENTER);
            pelangganTableLayout.addView(noDataTextView);
        }

        cursor.close();
    }

    // Helper method untuk konversi dp ke pixel
    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }

    // Menangani aksi tombol kembali di toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Perilaku standar tombol kembali
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
        }
    }
}