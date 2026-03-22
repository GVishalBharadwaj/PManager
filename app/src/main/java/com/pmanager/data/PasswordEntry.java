package com.pmanager.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Room entity representing a saved password/credential.
 * The password field is stored encrypted (AES-256-GCM).
 */
@Entity(tableName = "passwords")
public class PasswordEntry {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String title;
    private String username;
    private String encryptedPassword;
    private String website;
    private String category;
    private String notes;
    private long createdAt;
    private long updatedAt;
    private boolean isFavorite;

    // --- Getters ---

    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getUsername() { return username; }
    public String getEncryptedPassword() { return encryptedPassword; }
    public String getWebsite() { return website; }
    public String getCategory() { return category; }
    public String getNotes() { return notes; }
    public long getCreatedAt() { return createdAt; }
    public long getUpdatedAt() { return updatedAt; }
    public boolean isFavorite() { return isFavorite; }

    // --- Setters ---

    public void setId(long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setUsername(String username) { this.username = username; }
    public void setEncryptedPassword(String encryptedPassword) { this.encryptedPassword = encryptedPassword; }
    public void setWebsite(String website) { this.website = website; }
    public void setCategory(String category) { this.category = category; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}
