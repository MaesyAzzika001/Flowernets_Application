package com.example.ujicoba.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ujicoba.DatabaseHelper;
import com.example.ujicoba.Kategori;
import com.example.ujicoba.R;
import com.example.ujicoba.adapters.KategoriAdapter;
import java.util.List;

public class KategoriFragment extends Fragment {

    private RecyclerView recyclerView;
    private KategoriAdapter kategoriAdapter;
    private List<Kategori> kategoriList;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kategori, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_kategori);
        dbHelper = new DatabaseHelper(getContext());

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load data dan set adapter
        loadKategoriData();

        return view;
    }

    private void loadKategoriData() {
        kategoriList = dbHelper.getAllCategoriesWithCount();
        kategoriAdapter = new KategoriAdapter(getContext(), kategoriList);
        recyclerView.setAdapter(kategoriAdapter);
    }
}