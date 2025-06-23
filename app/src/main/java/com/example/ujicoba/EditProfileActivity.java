package com.example.ujicoba;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Toolbar toolbar;
    private CircleImageView ivProfilePhoto;
    private EditText etNamaToko, etNomorTelepon, etEmail, etTanggalLahir, etAlamat;
    private Spinner spinnerJenisKelamin;
    private Button btnSimpan;
    private RelativeLayout layoutFotoProfile;

    private DatabaseHelper databaseHelper;
    private int currentUserId = -1;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initViews();
        databaseHelper = new DatabaseHelper(this);

        // Ambil ID user yang sedang login
        SharedPreferences prefs = getSharedPreferences("USER_PREF", MODE_PRIVATE);

        // --- PERBAIKAN DI BARIS INI ---
        // Gunakan kunci "LOGGED_IN_USER_ID" agar sama dengan di LoginActivity
        currentUserId = prefs.getInt("LOGGED_IN_USER_ID", -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "Sesi tidak valid, silakan login kembali.", Toast.LENGTH_LONG).show();
            // Arahkan ke LoginActivity
            finish();
            return;
        }

        setupToolbar();
        setupSpinner();
        setupListeners();

        loadUserProfile();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar_edit_profile);
        ivProfilePhoto = findViewById(R.id.iv_profile_photo);
        etNamaToko = findViewById(R.id.et_nama_toko);
        etNomorTelepon = findViewById(R.id.et_nomor_telepon);
        etEmail = findViewById(R.id.et_email);
        etTanggalLahir = findViewById(R.id.et_tanggal_lahir);
        spinnerJenisKelamin = findViewById(R.id.spinner_jenis_kelamin);
        etAlamat = findViewById(R.id.et_alamat);
        btnSimpan = findViewById(R.id.btn_simpan);
        layoutFotoProfile = findViewById(R.id.layout_foto_profile);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJenisKelamin.setAdapter(adapter);
    }

    private void setupListeners() {
        etTanggalLahir.setOnClickListener(v -> showDatePickerDialog());
        layoutFotoProfile.setOnClickListener(v -> openImageChooser());
        btnSimpan.setOnClickListener(v -> saveProfile());
    }

    private void loadUserProfile() {
        Cursor cursor = databaseHelper.getUserProfile(currentUserId);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                String namaToko = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_NAME));
                String telepon = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PHONE));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL));
                String tglLahir = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_BIRTHDATE));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_GENDER));
                String alamat = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ADDRESS));
                String photoUriString = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PHOTO_URI));

                etNamaToko.setText(namaToko);
                etNomorTelepon.setText(telepon);
                etEmail.setText(email);
                etTanggalLahir.setText(tglLahir);
                etAlamat.setText(alamat);

                // Set spinner
                if (gender != null) {
                    ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerJenisKelamin.getAdapter();
                    int position = adapter.getPosition(gender);
                    spinnerJenisKelamin.setSelection(position);
                }

                // Set foto profil
                if (photoUriString != null && !photoUriString.isEmpty()) {
                    selectedImageUri = Uri.parse(photoUriString);
                    Glide.with(this).load(selectedImageUri).into(ivProfilePhoto);
                }
            } finally {
                cursor.close();
            }
        }
    }

    private void saveProfile() {
        String namaToko = etNamaToko.getText().toString().trim();
        String telepon = etNomorTelepon.getText().toString().trim();
        String tglLahir = etTanggalLahir.getText().toString().trim();
        String gender = spinnerJenisKelamin.getSelectedItem().toString();
        String alamat = etAlamat.getText().toString().trim();
        String photoUriString = (selectedImageUri != null) ? selectedImageUri.toString() : null;

        if (TextUtils.isEmpty(namaToko) || TextUtils.isEmpty(telepon) || TextUtils.isEmpty(alamat)) {
            Toast.makeText(this, "Harap isi semua kolom yang wajib.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isSuccess = databaseHelper.updateUserProfile(currentUserId, namaToko, telepon, tglLahir, gender, alamat, photoUriString);

        if (isSuccess) {
            Toast.makeText(this, "Profil berhasil diperbarui.", Toast.LENGTH_SHORT).show();
            finish(); // Kembali ke halaman sebelumnya
        } else {
            Toast.makeText(this, "Gagal memperbarui profil.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    etTanggalLahir.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            // Memberikan izin persisten untuk URI
            final int takeFlags = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            getContentResolver().takePersistableUriPermission(selectedImageUri, takeFlags);

            Glide.with(this).load(selectedImageUri).into(ivProfilePhoto);
        }
    }
}