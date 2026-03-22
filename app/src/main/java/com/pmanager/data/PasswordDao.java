package com.pmanager.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data Access Object for password CRUD operations.
 */
@Dao
public interface PasswordDao {

    @Query("SELECT * FROM passwords ORDER BY updatedAt DESC")
    LiveData<List<PasswordEntry>> getAllPasswords();

    @Query("SELECT * FROM passwords WHERE title LIKE '%' || :query || '%' OR username LIKE '%' || :query || '%' OR website LIKE '%' || :query || '%' ORDER BY updatedAt DESC")
    LiveData<List<PasswordEntry>> searchPasswords(String query);

    @Query("SELECT * FROM passwords WHERE category = :category ORDER BY updatedAt DESC")
    LiveData<List<PasswordEntry>> getByCategory(String category);

    @Query("SELECT * FROM passwords WHERE isFavorite = 1 ORDER BY updatedAt DESC")
    LiveData<List<PasswordEntry>> getFavorites();

    @Query("SELECT * FROM passwords WHERE website LIKE '%' || :domain || '%'")
    List<PasswordEntry> getByWebsiteSync(String domain);

    @Query("SELECT * FROM passwords WHERE id = :id")
    PasswordEntry getById(long id);

    @Insert
    long insert(PasswordEntry entry);

    @Update
    void update(PasswordEntry entry);

    @Delete
    void delete(PasswordEntry entry);

    @Query("DELETE FROM passwords WHERE id = :id")
    void deleteById(long id);

    @Query("SELECT COUNT(*) FROM passwords")
    int getCount();

    @Query("SELECT * FROM passwords ORDER BY updatedAt DESC")
    List<PasswordEntry> getAllPasswordsSync();
}
