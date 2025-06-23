// CartItem.java
package com.example.ujicoba;

import android.os.Parcel;
import android.os.Parcelable;

public class CartItem implements Parcelable { // Implement Parcelable
    private int cartId;
    private Produk produk;
    private int quantity;
    private boolean isChecked;

    public CartItem(Produk produk, int quantity) {
        this.produk = produk;
        this.quantity = quantity;
        this.isChecked = true;
    }

    public CartItem(int cartId, Produk produk, int quantity, boolean isChecked) {
        this.cartId = cartId;
        this.produk = produk;
        this.quantity = quantity;
        this.isChecked = isChecked;
    }

    // Implementasi Parcelable
    protected CartItem(Parcel in) {
        cartId = in.readInt();
        produk = in.readParcelable(Produk.class.getClassLoader());
        quantity = in.readInt();
        isChecked = in.readByte() != 0;
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cartId);
        dest.writeParcelable(produk, flags);
        dest.writeInt(quantity);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }

    // Getter dan Setter
    public int getCartId() { return cartId; }
    public void setCartId(int cartId) { this.cartId = cartId; }
    public Produk getProduk() { return produk; }
    public void setProduk(Produk produk) { this.produk = produk; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public boolean isChecked() { return isChecked; }
    public void setChecked(boolean checked) { isChecked = checked; }

    public double getTotalPrice() {
        try {
            return Double.parseDouble(produk.getHarga()) * quantity;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}