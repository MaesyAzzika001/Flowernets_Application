// CartAdapter.java
package com.example.ujicoba;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItemList;
    private DatabaseHelper databaseHelper;
    private CartInteractionListener listener; // Interface untuk callback ke Activity

    // Interface untuk komunikasi dengan ActivityCart
    public interface CartInteractionListener {
        void onCartUpdated(); // Dipanggil ketika ada perubahan (kuantitas, hapus, checkbox)
        void onItemRemoved(int position); // Dipanggil ketika item dihapus karena kuantitas 0
    }

    public CartAdapter(Context context, List<CartItem> cartItemList, CartInteractionListener listener) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.databaseHelper = new DatabaseHelper(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem currentItem = cartItemList.get(position);
        Produk produk = currentItem.getProduk();

        holder.tvProductName.setText(produk.getNama());

        // Format harga satuan
        try {
            double hargaSatuan = Double.parseDouble(produk.getHarga());
            holder.tvProductPriceUnit.setText(formatRupiah(hargaSatuan));
        } catch (NumberFormatException e) {
            holder.tvProductPriceUnit.setText("Rp -");
        }

        holder.tvQuantity.setText(String.valueOf(currentItem.getQuantity()));
        holder.tvItemTotalPrice.setText(formatRupiah(currentItem.getTotalPrice()));

        // Load gambar produk (pastikan nama gambar di DB sesuai dengan drawable)
        int imageResId = context.getResources().getIdentifier(produk.getGambar(), "drawable", context.getPackageName());
        if (imageResId != 0) {
            holder.ivProductImage.setImageResource(imageResId);
        } else {
            holder.ivProductImage.setImageResource(R.drawable.chinese); // Gambar default jika tidak ditemukan
        }

        // Checkbox state
        holder.cbItemSelect.setOnCheckedChangeListener(null); // Hapus listener sementara untuk mencegah trigger saat bind
        holder.cbItemSelect.setChecked(currentItem.isChecked());
        holder.cbItemSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            currentItem.setChecked(isChecked);
            if (listener != null) {
                listener.onCartUpdated();
            }
        });

        // Tombol Tambah Kuantitas (+)
        holder.btnIncreaseQuantity.setOnClickListener(v -> {
            int newQuantity = currentItem.getQuantity() + 1;
            // Anda mungkin ingin menambahkan validasi terhadap stok produk di sini
            // if (newQuantity <= produk.getStok()) {
            currentItem.setQuantity(newQuantity);
            databaseHelper.updateCartItemQuantity(currentItem.getCartId(), newQuantity);
            holder.tvQuantity.setText(String.valueOf(newQuantity));
            holder.tvItemTotalPrice.setText(formatRupiah(currentItem.getTotalPrice()));
            if (listener != null) {
                listener.onCartUpdated();
            }
            // } else {
            //     Toast.makeText(context, "Stok tidak mencukupi", Toast.LENGTH_SHORT).show();
            // }
        });

        // Tombol Kurang Kuantitas (-)
        holder.btnDecreaseQuantity.setOnClickListener(v -> {
            int newQuantity = currentItem.getQuantity() - 1;
            currentItem.setQuantity(newQuantity); // Update di objek dulu
            databaseHelper.updateCartItemQuantity(currentItem.getCartId(), newQuantity); // newQuantity bisa 0 atau negatif, DB akan handle hapus

            if (newQuantity <= 0) {
                // Hapus item dari list dan notify adapter
                // Biarkan Activity yang menghapus dari list agar sinkron
                if (listener != null) {
                    listener.onItemRemoved(holder.getAdapterPosition());
                }
            } else {
                holder.tvQuantity.setText(String.valueOf(newQuantity));
                holder.tvItemTotalPrice.setText(formatRupiah(currentItem.getTotalPrice()));
                if (listener != null) {
                    listener.onCartUpdated();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public void updateCartItems(List<CartItem> newItems) {
        this.cartItemList.clear();
        this.cartItemList.addAll(newItems);
        notifyDataSetChanged();
    }

    private String formatRupiah(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(amount);
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName, tvProductPriceUnit, tvQuantity, tvItemTotalPrice;
        Button btnIncreaseQuantity, btnDecreaseQuantity;
        CheckBox cbItemSelect;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.iv_product_image_cart);
            tvProductName = itemView.findViewById(R.id.tv_product_name_cart);
            tvProductPriceUnit = itemView.findViewById(R.id.tv_product_price_unit_cart);
            tvQuantity = itemView.findViewById(R.id.tv_item_quantity);
            tvItemTotalPrice = itemView.findViewById(R.id.tv_item_total_price);
            btnIncreaseQuantity = itemView.findViewById(R.id.btn_increase_quantity);
            btnDecreaseQuantity = itemView.findViewById(R.id.btn_decrease_quantity);
            cbItemSelect = itemView.findViewById(R.id.cb_item_select);
        }
    }
}