// FormProdukActivity.java
package com.example.ujicoba;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
// import android.database.Cursor; // Tidak lagi dibutuhkan untuk mengambil produk by ID
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormProdukActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;


    private EditText etNamaProduk, etHarga, etDeskripsi, etJenis, etWarna, etUkuran, etStok;
    private Spinner spinnerKategori;
    private ImageView imagePreview;
    private Uri imageUri;
    private String currentImageName = null; // Untuk menyimpan nama file gambar dari DB saat edit

    private DatabaseHelper dbHelper;
    private int idProduk = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_produk);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, // Tambahkan WRITE jika menyimpan ke external
                    PERMISSION_REQUEST_CODE);
        }

        dbHelper = new DatabaseHelper(this);

        // Inisialisasi view
        etNamaProduk = findViewById(R.id.et_nama_produk);
        etHarga = findViewById(R.id.et_harga);
        etDeskripsi = findViewById(R.id.et_deskripsi);
        etJenis = findViewById(R.id.et_jenis);
        etWarna = findViewById(R.id.et_warna);
        etUkuran = findViewById(R.id.et_ukuran);
        etStok = findViewById(R.id.et_stok);
        spinnerKategori = findViewById(R.id.spinner_kategori);
        imagePreview = findViewById(R.id.image_preview);
        Button btnPilihGambar = findViewById(R.id.btn_pilih_gambar);
        Button btnSimpan = findViewById(R.id.btn_simpan);
        ImageButton backButton = findViewById(R.id.back_button);

        // Dummy data spinner (sebaiknya diambil dari resource atau konstanta)
        String[] kategoriList = {"Pilih Kategori", "Wedding", "Event", "Gift", "Bunga", "Tanaman", "Vas", "Aksesoris"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kategoriList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKategori.setAdapter(adapter);

        btnPilihGambar.setOnClickListener(v -> openImagePicker());
        backButton.setOnClickListener(v -> finish());

        idProduk = getIntent().getIntExtra("id_produk", -1);
        if (idProduk != -1) {
            // Mengambil objek Produk, bukan Cursor
            Produk produk = dbHelper.getProductById(idProduk);
            if (produk != null) {
                etNamaProduk.setText(produk.getNama());
                etHarga.setText(produk.getHarga());
                etDeskripsi.setText(produk.getDeskripsi());
                etJenis.setText(produk.getJenis());
                etWarna.setText(produk.getWarna());
                etUkuran.setText(produk.getUkuran());
                etStok.setText(String.valueOf(produk.getStok())); // Stok adalah int

                String kategoriDb = produk.getKategori();
                for (int i = 0; i < spinnerKategori.getCount(); i++) {
                    if (spinnerKategori.getItemAtPosition(i).toString().equals(kategoriDb)) {
                        spinnerKategori.setSelection(i);
                        break;
                    }
                }

                currentImageName = produk.getGambar(); // Simpan nama gambar dari DB
                if (currentImageName != null && !currentImageName.isEmpty()) {
                    // Coba load dari internal storage jika pathnya absolut
                    // atau dari drawable jika hanya nama resource
                    if (currentImageName.contains(File.separator)) { // Anggap path absolut
                        File imgFile = new File(currentImageName);
                        if (imgFile.exists()) {
                            imagePreview.setImageURI(Uri.fromFile(imgFile));
                            imageUri = Uri.fromFile(imgFile); // Simpan URI file lokal
                        }
                    } else { // Anggap nama resource drawable
                        int imageResId = getResources().getIdentifier(currentImageName, "drawable", getPackageName());
                        if (imageResId != 0) {
                            imagePreview.setImageResource(imageResId);
                            // imageUri tidak di-set di sini kecuali Anda menyalinnya ke file lokal
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Produk tidak ditemukan.", Toast.LENGTH_SHORT).show();
                finish(); // Kembali jika produk tidak ada
            }
        }

        btnSimpan.setOnClickListener(v -> {
            String nama = etNamaProduk.getText().toString().trim();
            String harga = etHarga.getText().toString().trim();
            String deskripsi = etDeskripsi.getText().toString().trim();
            String kategori = spinnerKategori.getSelectedItem().toString();
            String jenis = etJenis.getText().toString().trim();
            String warna = etWarna.getText().toString().trim();
            String ukuran = etUkuran.getText().toString().trim();
            String stokStr = etStok.getText().toString().trim();
            int jumlah = stokStr.isEmpty() ? 0 : Integer.parseInt(stokStr);
            String tanggal = tanggalHariIni();

            // Logika penyimpanan gambar:
            // Jika imageUri baru dipilih, gunakan path-nya.
            // Jika tidak ada imageUri baru DAN ini mode edit DAN currentImageName ada, gunakan currentImageName.
            // Jika tidak, string kosong.
            String gambarPathToSave;
            if (imageUri != null && imageUri.getPath() != null && !imageUri.getPath().isEmpty() && new File(imageUri.getPath()).exists()) {
                // Jika imageUri adalah file path yang valid (setelah disimpan ke internal)
                gambarPathToSave = imageUri.getPath();
            } else if (idProduk != -1 && currentImageName != null && !currentImageName.isEmpty()) {
                // Mode edit dan gambar lama ada (dan tidak diubah)
                gambarPathToSave = currentImageName;
            } else {
                // Tidak ada gambar baru atau lama yang valid (untuk produk baru tanpa gambar, atau jika gambar lama tidak valid)
                gambarPathToSave = ""; // atau null, atau nama placeholder jika ada
            }


            if (nama.isEmpty() || harga.isEmpty() || deskripsi.isEmpty() || kategori.equals("Pilih Kategori")) {
                Toast.makeText(this, "Harap lengkapi Nama, Harga, Deskripsi, dan Kategori!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Validasi harga dan stok sebagai angka
                Double.parseDouble(harga); // Coba parse harga
                Integer.parseInt(stokStr); // Coba parse stok
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Harga dan Stok harus berupa angka.", Toast.LENGTH_SHORT).show();
                return;
            }


            boolean success;
            if (idProduk != -1) {
                // Pastikan Anda memiliki metode updateProduct di DatabaseHelper yang sesuai
                // Jika metode updateProduct belum ada atau parameternya berbeda, Anda perlu menyesuaikannya.
                // Asumsikan updateProduct sudah ada dan sesuai:
                // success = dbHelper.updateProduct(idProduk, nama, harga, deskripsi, kategori, jenis, warna, ukuran, gambarPathToSave, jumlah, tanggal);
                // Jika metode updateProduct belum dibuat di DatabaseHelper, Anda perlu menambahkannya.
                // Untuk sekarang, mari kita asumsikan DatabaseHelper Anda memiliki metode seperti ini:
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_NAMA, nama);
                values.put(DatabaseHelper.COLUMN_HARGA, harga);
                values.put(DatabaseHelper.COLUMN_DESKRIPSI, deskripsi);
                values.put(DatabaseHelper.COLUMN_KATEGORI, kategori);
                values.put(DatabaseHelper.COLUMN_JENIS, jenis);
                values.put(DatabaseHelper.COLUMN_WARNA, warna);
                values.put(DatabaseHelper.COLUMN_UKURAN, ukuran);
                values.put(DatabaseHelper.COLUMN_GAMBAR, gambarPathToSave);
                values.put(DatabaseHelper.COLUMN_STOK, jumlah);
                values.put(DatabaseHelper.COLUMN_TANGGAL, tanggal);
                success = dbHelper.getWritableDatabase().update(DatabaseHelper.TABLE_PRODUCTS, values, DatabaseHelper.COLUMN_PRODUK_ID + "=?", new String[]{String.valueOf(idProduk)}) > 0;

            } else {
                success = dbHelper.insertProduct(nama, harga, deskripsi, kategori, jenis, warna, ukuran, gambarPathToSave, jumlah, tanggal);
            }

            if (success) {
                Toast.makeText(this, "Produk berhasil " + (idProduk != -1 ? "diperbarui!" : "disimpan!"), Toast.LENGTH_SHORT).show();
                // Kembali ke KelolaProdukActivity atau MainActivity
                // Intent intent = new Intent(FormProdukActivity.this, KelolaProdukActivity.class);
                // startActivity(intent);
                setResult(RESULT_OK); // Set result agar activity sebelumnya bisa refresh jika perlu
                finish();
            } else {
                Toast.makeText(this, "Gagal " + (idProduk != -1 ? "memperbarui" : "menyimpan") + " produk.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // intent.setType("image/*"); // ACTION_PICK sudah menyiratkan ini untuk MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Metode untuk menyimpan Bitmap ke internal storage dan mengembalikan path absolutnya
    private String simpanGambarKeInternalStorage(Bitmap bitmap) {
        // Buat direktori khusus untuk gambar produk jika belum ada
        File directory = new File(getFilesDir(), "product_images");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filename = "produk_" + System.currentTimeMillis() + ".jpg";
        File file = new File(directory, filename);

        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out); // Kompresi 90%
            return file.getAbsolutePath(); // Mengembalikan path absolut dari file yang disimpan
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal menyimpan gambar", Toast.LENGTH_SHORT).show();
            return null;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData(); // Ini adalah URI dari gallery (content://)
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                imagePreview.setImageBitmap(bitmap); // Tampilkan preview

                // Simpan bitmap ke internal storage dan dapatkan path file lokalnya
                String localFilePath = simpanGambarKeInternalStorage(bitmap);
                if (localFilePath != null) {
                    imageUri = Uri.fromFile(new File(localFilePath)); // Update imageUri dengan path file lokal
                    currentImageName = localFilePath; // Juga update currentImageName agar ini yang disimpan
                } else {
                    imageUri = null; // Gagal menyimpan, set ke null
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show();
                imageUri = null;
            }
        }
    }

    private String tanggalHariIni() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin diberikan
            } else {
                Toast.makeText(this, "Izin akses penyimpanan dibutuhkan untuk memilih gambar.", Toast.LENGTH_LONG).show();
            }
        }
    }
}