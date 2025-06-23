package com.example.ujicoba.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ujicoba.DatabaseHelper;
import com.example.ujicoba.Produk;
import com.example.ujicoba.R;
import com.example.ujicoba.adapters.ProdukAdapter;
import java.util.List;

public class ProdukFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProdukAdapter produkAdapter;
    private List<Produk> produkList;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_produk, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_produk);
        dbHelper = new DatabaseHelper(getContext());

        // Setup RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Load data dan set adapter
        loadProdukData();

        return view;
    }

    private void loadProdukData() {
        produkList = dbHelper.getAllProducts();
        produkAdapter = new ProdukAdapter(getContext(), produkList);
        recyclerView.setAdapter(produkAdapter);
    }
}