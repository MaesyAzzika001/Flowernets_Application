package com.example.ujicoba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddAddressActivity extends AppCompatActivity {

    private EditText etFullName, etStreetAddress, etCity, etPostalCode, etDetails;
    private Button btnSaveAddress;
    private ImageButton btnBack;
    private DatabaseHelper dbHelper;
    private int loggedInUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        dbHelper = new DatabaseHelper(this);

        // Ambil ID user dari SharedPreferences
        SharedPreferences prefs = getSharedPreferences("USER_PREF", MODE_PRIVATE);
        loggedInUserId = prefs.getInt("LOGGED_IN_USER_ID", -1);

        findViews();

        btnBack.setOnClickListener(v -> finish());
        btnSaveAddress.setOnClickListener(v -> saveAddress());
    }

    private void findViews() {
        etFullName = findViewById(R.id.et_full_name);
        etStreetAddress = findViewById(R.id.et_street_address);
        etCity = findViewById(R.id.et_city);
        etPostalCode = findViewById(R.id.et_postal_code);
        etDetails = findViewById(R.id.et_details);
        btnSaveAddress = findViewById(R.id.btn_save_address);
        btnBack = findViewById(R.id.btn_back_add_address);
    }

    private void saveAddress() {
        String fullName = etFullName.getText().toString().trim();
        String street = etStreetAddress.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String postalCode = etPostalCode.getText().toString().trim();
        String details = etDetails.getText().toString().trim();

        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(street) || TextUtils.isEmpty(city)) {
            Toast.makeText(this, "Nama, Alamat, dan Kota tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        if (loggedInUserId == -1) {
            Toast.makeText(this, "Sesi tidak valid, silakan login ulang", Toast.LENGTH_SHORT).show();
            return;
        }

        Address address = new Address();
        address.setUserId(loggedInUserId);
        address.setRecipientName(fullName);
        address.setStreetAddress(street);
        address.setCity(city);
        address.setPostalCode(postalCode);
        address.setDetails(details);

        long newAddressId = dbHelper.addAddress(address);

        if (newAddressId != -1) {
            address.setAddressId(newAddressId); // Set ID yang baru dibuat oleh DB
            Toast.makeText(this, "Alamat berhasil disimpan!", Toast.LENGTH_SHORT).show();

            // Kirim data alamat baru kembali ke CheckoutActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("NEW_ADDRESS", address);
            setResult(RESULT_OK, resultIntent);
            finish();
        } else {
            Toast.makeText(this, "Gagal menyimpan alamat", Toast.LENGTH_SHORT).show();
        }
    }
}