package com.example.ujicoba;

public class Chat {
    private int profileImageResId; // ID gambar dari drawable
    private String name;
    private String lastMessage;
    private String timestamp;
    private int unreadCount;

    public Chat(int profileImageResId, String name, String lastMessage, String timestamp, int unreadCount) {
        this.profileImageResId = profileImageResId;
        this.name = name;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.unreadCount = unreadCount;
    }

    // Buat semua Getters
    public int getProfileImageResId() {
        return profileImageResId;
    }

    public String getName() {
        return name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getUnreadCount() {
        return unreadCount;
    }
}