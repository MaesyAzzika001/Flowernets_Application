package com.example.ujicoba;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class EditMyshopActivity extends AppCompatActivity {

    private EditText etShopName, etShopPhone, etShopEmail, etShopAddress;
    private EditText etShopWorkDays, etShopWorkHours, etShopDescription; // <-- Field baru
    private Button btnSaveProfile;
    private DatabaseHelper dbHelper;
    private int currentUserId;
    private String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_myshop);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_edit_shop);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Inisialisasi Views
        etShopName = findViewById(R.id.et_shop_name);
        etShopPhone = findViewById(R.id.et_shop_phone);
        etShopEmail = findViewById(R.id.et_shop_email);
        etShopAddress = findViewById(R.id.et_shop_address);
        etShopWorkDays = findViewById(R.id.et_shop_work_days);
        etShopWorkHours = findViewById(R.id.et_shop_work_hours);
        etShopDescription = findViewById(R.id.et_shop_description);
        btnSaveProfile = findViewById(R.id.btn_save_profile);

        dbHelper = new DatabaseHelper(this);

        // Ambil data user yang login dari SharedPreferences
        SharedPreferences prefs = getSharedPreferences("USER_PREF", MODE_PRIVATE);
        currentUserId = prefs.getInt("LOGGED_IN_USER_ID", -1);
        currentUserEmail = prefs.getString("LOGGED_IN_EMAIL", "");

        if (currentUserId == -1) {
            Toast.makeText(this, "Sesi tidak ditemukan.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        loadExistingData();

        btnSaveProfile.setOnClickListener(v -> saveProfileChanges());
    }

    private void loadExistingData() {
        etShopEmail.setText(currentUserEmail);

        Cursor cursor = dbHelper.getUserProfile(currentUserId);
        if (cursor != null && cursor.moveToFirst()) {
            String shopName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_NAME));
            String shopPhone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PHONE));
            String shopAddress = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ADDRESS));
            String workDays = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SHOP_WORK_DAYS));
            String workHours = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SHOP_WORK_HOURS));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SHOP_DESCRIPTION));

            etShopName.setText(shopName);
            etShopPhone.setText(shopPhone);
            etShopAddress.setText(shopAddress);
            etShopWorkDays.setText(workDays);
            etShopWorkHours.setText(workHours);
            etShopDescription.setText(description);

            cursor.close();
        }
    }

    private void saveProfileChanges() {
        String newShopName = etShopName.getText().toString().trim();
        String newShopPhone = etShopPhone.getText().toString().trim();
        String newShopAddress = etShopAddress.getText().toString().trim();
        String newWorkDays = etShopWorkDays.getText().toString().trim();
        String newWorkHours = etShopWorkHours.getText().toString().trim();
        String newDescription = etShopDescription.getText().toString().trim();

        if (newShopName.isEmpty() || newShopPhone.isEmpty()) {
            Toast.makeText(this, "Nama Toko dan Nomor Telepon wajib diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isUpdated = dbHelper.updateShopProfile(
                currentUserId,
                newShopName,
                newShopPhone,
                newShopAddress,
                newWorkDays,
                newWorkHours,
                newDescription,
                null  // Photo URI tidak diubah di sini
        );

        if (isUpdated) {
            Toast.makeText(this, "Profil berhasil disimpan!", Toast.LENGTH_SHORT).show();
            finish(); // Kembali ke MyshopActivity yang akan me-refresh datanya
        } else {
            Toast.makeText(this, "Gagal menyimpan profil.", Toast.LENGTH_SHORT).show();
        }
    }
}