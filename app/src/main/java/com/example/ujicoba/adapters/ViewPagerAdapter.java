package com.example.ujicoba.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.ujicoba.fragments.KategoriFragment;
import com.example.ujicoba.fragments.ProdukFragment;
import com.example.ujicoba.fragments.TokoFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new ProdukFragment();
            case 2:
                return new KategoriFragment();
            default:
                return new TokoFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Jumlah tab
    }
}