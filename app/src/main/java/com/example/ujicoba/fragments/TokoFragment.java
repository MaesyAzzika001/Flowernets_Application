package com.example.ujicoba.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.ujicoba.DatabaseHelper;
import com.example.ujicoba.R;

public class TokoFragment extends Fragment {

    private TextView tvShopAddress, tvShopWorkDays, tvShopWorkHours, tvShopDescription;
    private DatabaseHelper dbHelper;
    private int currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_toko, container, false);

        tvShopAddress = view.findViewById(R.id.tv_shop_address);
        tvShopWorkDays = view.findViewById(R.id.tv_shop_work_days);
        tvShopWorkHours = view.findViewById(R.id.tv_shop_work_hours);
        tvShopDescription = view.findViewById(R.id.tv_shop_description);

        dbHelper = new DatabaseHelper(getContext());

        SharedPreferences prefs = requireActivity().getSharedPreferences("USER_PREF", Context.MODE_PRIVATE);
        currentUserId = prefs.getInt("LOGGED_IN_USER_ID", -1);

        if (currentUserId != -1) {
            loadShopProfile();
        }

        return view;
    }

    private void loadShopProfile() {
        Cursor cursor = dbHelper.getUserProfile(currentUserId);

        if (cursor != null && cursor.moveToFirst()) {
            String shopAddress = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ADDRESS));
            String workDays = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SHOP_WORK_DAYS));
            String workHours = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SHOP_WORK_HOURS));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SHOP_DESCRIPTION));

            tvShopAddress.setText(shopAddress != null && !shopAddress.isEmpty() ? shopAddress : "Alamat belum diatur");
            tvShopWorkDays.setText(workDays != null && !workDays.isEmpty() ? workDays : "Hari kerja belum diatur");
            tvShopWorkHours.setText(workHours != null && !workHours.isEmpty() ? workHours : "Jam kerja belum diatur");
            tvShopDescription.setText(description != null && !description.isEmpty() ? description : "Deskripsi belum diatur.");

            cursor.close();
        } else {
            Toast.makeText(getContext(), "Gagal memuat info toko.", Toast.LENGTH_SHORT).show();
        }
    }
}