package com.example.ujicoba;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {
    private long addressId;
    private int userId;
    private String recipientName;
    private String streetAddress;
    private String city;
    private String postalCode;
    private String details;

    public Address() {}

    public Address(long addressId, int userId, String recipientName, String streetAddress, String city, String postalCode, String details) {
        this.addressId = addressId;
        this.userId = userId;
        this.recipientName = recipientName;
        this.streetAddress = streetAddress;
        this.city = city;
        this.postalCode = postalCode;
        this.details = details;
    }

    // Getters and Setters
    public long getAddressId() { return addressId; }
    public void setAddressId(long addressId) { this.addressId = addressId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    public String getStreetAddress() { return streetAddress; }
    public void setStreetAddress(String streetAddress) { this.streetAddress = streetAddress; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    // Parcelable implementation
    protected Address(Parcel in) {
        addressId = in.readLong();
        userId = in.readInt();
        recipientName = in.readString();
        streetAddress = in.readString();
        city = in.readString();
        postalCode = in.readString();
        details = in.readString();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(addressId);
        dest.writeInt(userId);
        dest.writeString(recipientName);
        dest.writeString(streetAddress);
        dest.writeString(city);
        dest.writeString(postalCode);
        dest.writeString(details);
    }
}