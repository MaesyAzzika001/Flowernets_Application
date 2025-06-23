package com.example.ujicoba;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);
        etEmail = findViewById(R.id.editEmail);
        etPassword = findViewById(R.id.editPassword2);
        btnLogin = findViewById(R.id.submit);
        tvRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v -> loginUser());

        tvRegister.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void loginUser() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Email dan Password harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Cek apakah user valid
        if (dbHelper.checkUser(email, password)) {
            // 2. Jika valid, ambil ID user dari database
            int userId = dbHelper.getUserId(email); // <-- PANGGIL METODE INI

            // 3. Simpan semua informasi penting ke SharedPreferences
            SharedPreferences preferences = getSharedPreferences("USER_PREF", MODE_PRIVATE); // <-- Gunakan nama yang konsisten
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("IS_LOGGED_IN", true);
            editor.putInt("LOGGED_IN_USER_ID", userId);   // <-- SIMPAN USER ID
            editor.putString("LOGGED_IN_EMAIL", email); // <-- SIMPAN EMAIL
            editor.apply();

            Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Login gagal: Email atau Password salah", Toast.LENGTH_SHORT).show();
        }
    }
}
