package com.pmanager.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PasswordDao_Impl implements PasswordDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PasswordEntry> __insertionAdapterOfPasswordEntry;

  private final EntityDeletionOrUpdateAdapter<PasswordEntry> __deletionAdapterOfPasswordEntry;

  private final EntityDeletionOrUpdateAdapter<PasswordEntry> __updateAdapterOfPasswordEntry;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public PasswordDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPasswordEntry = new EntityInsertionAdapter<PasswordEntry>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `passwords` (`id`,`title`,`username`,`encryptedPassword`,`website`,`category`,`notes`,`createdAt`,`updatedAt`,`isFavorite`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final PasswordEntry entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTitle());
        }
        if (entity.getUsername() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getUsername());
        }
        if (entity.getEncryptedPassword() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getEncryptedPassword());
        }
        if (entity.getWebsite() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getWebsite());
        }
        if (entity.getCategory() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getCategory());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getNotes());
        }
        statement.bindLong(8, entity.getCreatedAt());
        statement.bindLong(9, entity.getUpdatedAt());
        final int _tmp = entity.isFavorite() ? 1 : 0;
        statement.bindLong(10, _tmp);
      }
    };
    this.__deletionAdapterOfPasswordEntry = new EntityDeletionOrUpdateAdapter<PasswordEntry>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `passwords` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final PasswordEntry entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfPasswordEntry = new EntityDeletionOrUpdateAdapter<PasswordEntry>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `passwords` SET `id` = ?,`title` = ?,`username` = ?,`encryptedPassword` = ?,`website` = ?,`category` = ?,`notes` = ?,`createdAt` = ?,`updatedAt` = ?,`isFavorite` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final PasswordEntry entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTitle());
        }
        if (entity.getUsername() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getUsername());
        }
        if (entity.getEncryptedPassword() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getEncryptedPassword());
        }
        if (entity.getWebsite() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getWebsite());
        }
        if (entity.getCategory() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getCategory());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getNotes());
        }
        statement.bindLong(8, entity.getCreatedAt());
        statement.bindLong(9, entity.getUpdatedAt());
        final int _tmp = entity.isFavorite() ? 1 : 0;
        statement.bindLong(10, _tmp);
        statement.bindLong(11, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM passwords WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public long insert(final PasswordEntry entry) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfPasswordEntry.insertAndReturnId(entry);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final PasswordEntry entry) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfPasswordEntry.handle(entry);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final PasswordEntry entry) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfPasswordEntry.handle(entry);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteById(final long id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteById.release(_stmt);
    }
  }

  @Override
  public LiveData<List<PasswordEntry>> getAllPasswords() {
    final String _sql = "SELECT * FROM passwords ORDER BY updatedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"passwords"}, false, new Callable<List<PasswordEntry>>() {
      @Override
      @Nullable
      public List<PasswordEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfEncryptedPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "encryptedPassword");
          final int _cursorIndexOfWebsite = CursorUtil.getColumnIndexOrThrow(_cursor, "website");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final List<PasswordEntry> _result = new ArrayList<PasswordEntry>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PasswordEntry _item;
            _item = new PasswordEntry();
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item.setId(_tmpId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            _item.setTitle(_tmpTitle);
            final String _tmpUsername;
            if (_cursor.isNull(_cursorIndexOfUsername)) {
              _tmpUsername = null;
            } else {
              _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            }
            _item.setUsername(_tmpUsername);
            final String _tmpEncryptedPassword;
            if (_cursor.isNull(_cursorIndexOfEncryptedPassword)) {
              _tmpEncryptedPassword = null;
            } else {
              _tmpEncryptedPassword = _cursor.getString(_cursorIndexOfEncryptedPassword);
            }
            _item.setEncryptedPassword(_tmpEncryptedPassword);
            final String _tmpWebsite;
            if (_cursor.isNull(_cursorIndexOfWebsite)) {
              _tmpWebsite = null;
            } else {
              _tmpWebsite = _cursor.getString(_cursorIndexOfWebsite);
            }
            _item.setWebsite(_tmpWebsite);
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            _item.setCategory(_tmpCategory);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item.setNotes(_tmpNotes);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item.setCreatedAt(_tmpCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item.setUpdatedAt(_tmpUpdatedAt);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            _item.setFavorite(_tmpIsFavorite);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<PasswordEntry>> searchPasswords(final String query) {
    final String _sql = "SELECT * FROM passwords WHERE title LIKE '%' || ? || '%' OR username LIKE '%' || ? || '%' OR website LIKE '%' || ? || '%' ORDER BY updatedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    _argIndex = 2;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    _argIndex = 3;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"passwords"}, false, new Callable<List<PasswordEntry>>() {
      @Override
      @Nullable
      public List<PasswordEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfEncryptedPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "encryptedPassword");
          final int _cursorIndexOfWebsite = CursorUtil.getColumnIndexOrThrow(_cursor, "website");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final List<PasswordEntry> _result = new ArrayList<PasswordEntry>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PasswordEntry _item;
            _item = new PasswordEntry();
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item.setId(_tmpId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            _item.setTitle(_tmpTitle);
            final String _tmpUsername;
            if (_cursor.isNull(_cursorIndexOfUsername)) {
              _tmpUsername = null;
            } else {
              _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            }
            _item.setUsername(_tmpUsername);
            final String _tmpEncryptedPassword;
            if (_cursor.isNull(_cursorIndexOfEncryptedPassword)) {
              _tmpEncryptedPassword = null;
            } else {
              _tmpEncryptedPassword = _cursor.getString(_cursorIndexOfEncryptedPassword);
            }
            _item.setEncryptedPassword(_tmpEncryptedPassword);
            final String _tmpWebsite;
            if (_cursor.isNull(_cursorIndexOfWebsite)) {
              _tmpWebsite = null;
            } else {
              _tmpWebsite = _cursor.getString(_cursorIndexOfWebsite);
            }
            _item.setWebsite(_tmpWebsite);
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            _item.setCategory(_tmpCategory);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item.setNotes(_tmpNotes);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item.setCreatedAt(_tmpCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item.setUpdatedAt(_tmpUpdatedAt);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            _item.setFavorite(_tmpIsFavorite);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<PasswordEntry>> getByCategory(final String category) {
    final String _sql = "SELECT * FROM passwords WHERE category = ? ORDER BY updatedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (category == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, category);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"passwords"}, false, new Callable<List<PasswordEntry>>() {
      @Override
      @Nullable
      public List<PasswordEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfEncryptedPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "encryptedPassword");
          final int _cursorIndexOfWebsite = CursorUtil.getColumnIndexOrThrow(_cursor, "website");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final List<PasswordEntry> _result = new ArrayList<PasswordEntry>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PasswordEntry _item;
            _item = new PasswordEntry();
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item.setId(_tmpId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            _item.setTitle(_tmpTitle);
            final String _tmpUsername;
            if (_cursor.isNull(_cursorIndexOfUsername)) {
              _tmpUsername = null;
            } else {
              _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            }
            _item.setUsername(_tmpUsername);
            final String _tmpEncryptedPassword;
            if (_cursor.isNull(_cursorIndexOfEncryptedPassword)) {
              _tmpEncryptedPassword = null;
            } else {
              _tmpEncryptedPassword = _cursor.getString(_cursorIndexOfEncryptedPassword);
            }
            _item.setEncryptedPassword(_tmpEncryptedPassword);
            final String _tmpWebsite;
            if (_cursor.isNull(_cursorIndexOfWebsite)) {
              _tmpWebsite = null;
            } else {
              _tmpWebsite = _cursor.getString(_cursorIndexOfWebsite);
            }
            _item.setWebsite(_tmpWebsite);
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            _item.setCategory(_tmpCategory);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item.setNotes(_tmpNotes);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item.setCreatedAt(_tmpCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item.setUpdatedAt(_tmpUpdatedAt);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            _item.setFavorite(_tmpIsFavorite);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<PasswordEntry>> getFavorites() {
    final String _sql = "SELECT * FROM passwords WHERE isFavorite = 1 ORDER BY updatedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"passwords"}, false, new Callable<List<PasswordEntry>>() {
      @Override
      @Nullable
      public List<PasswordEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfEncryptedPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "encryptedPassword");
          final int _cursorIndexOfWebsite = CursorUtil.getColumnIndexOrThrow(_cursor, "website");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final List<PasswordEntry> _result = new ArrayList<PasswordEntry>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PasswordEntry _item;
            _item = new PasswordEntry();
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item.setId(_tmpId);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            _item.setTitle(_tmpTitle);
            final String _tmpUsername;
            if (_cursor.isNull(_cursorIndexOfUsername)) {
              _tmpUsername = null;
            } else {
              _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            }
            _item.setUsername(_tmpUsername);
            final String _tmpEncryptedPassword;
            if (_cursor.isNull(_cursorIndexOfEncryptedPassword)) {
              _tmpEncryptedPassword = null;
            } else {
              _tmpEncryptedPassword = _cursor.getString(_cursorIndexOfEncryptedPassword);
            }
            _item.setEncryptedPassword(_tmpEncryptedPassword);
            final String _tmpWebsite;
            if (_cursor.isNull(_cursorIndexOfWebsite)) {
              _tmpWebsite = null;
            } else {
              _tmpWebsite = _cursor.getString(_cursorIndexOfWebsite);
            }
            _item.setWebsite(_tmpWebsite);
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            _item.setCategory(_tmpCategory);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item.setNotes(_tmpNotes);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item.setCreatedAt(_tmpCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item.setUpdatedAt(_tmpUpdatedAt);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            _item.setFavorite(_tmpIsFavorite);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public List<PasswordEntry> getByWebsiteSync(final String domain) {
    final String _sql = "SELECT * FROM passwords WHERE website LIKE '%' || ? || '%'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (domain == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, domain);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
      final int _cursorIndexOfEncryptedPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "encryptedPassword");
      final int _cursorIndexOfWebsite = CursorUtil.getColumnIndexOrThrow(_cursor, "website");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
      final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
      final List<PasswordEntry> _result = new ArrayList<PasswordEntry>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final PasswordEntry _item;
        _item = new PasswordEntry();
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _item.setTitle(_tmpTitle);
        final String _tmpUsername;
        if (_cursor.isNull(_cursorIndexOfUsername)) {
          _tmpUsername = null;
        } else {
          _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
        }
        _item.setUsername(_tmpUsername);
        final String _tmpEncryptedPassword;
        if (_cursor.isNull(_cursorIndexOfEncryptedPassword)) {
          _tmpEncryptedPassword = null;
        } else {
          _tmpEncryptedPassword = _cursor.getString(_cursorIndexOfEncryptedPassword);
        }
        _item.setEncryptedPassword(_tmpEncryptedPassword);
        final String _tmpWebsite;
        if (_cursor.isNull(_cursorIndexOfWebsite)) {
          _tmpWebsite = null;
        } else {
          _tmpWebsite = _cursor.getString(_cursorIndexOfWebsite);
        }
        _item.setWebsite(_tmpWebsite);
        final String _tmpCategory;
        if (_cursor.isNull(_cursorIndexOfCategory)) {
          _tmpCategory = null;
        } else {
          _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        }
        _item.setCategory(_tmpCategory);
        final String _tmpNotes;
        if (_cursor.isNull(_cursorIndexOfNotes)) {
          _tmpNotes = null;
        } else {
          _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        }
        _item.setNotes(_tmpNotes);
        final long _tmpCreatedAt;
        _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
        _item.setCreatedAt(_tmpCreatedAt);
        final long _tmpUpdatedAt;
        _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
        _item.setUpdatedAt(_tmpUpdatedAt);
        final boolean _tmpIsFavorite;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
        _tmpIsFavorite = _tmp != 0;
        _item.setFavorite(_tmpIsFavorite);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public PasswordEntry getById(final long id) {
    final String _sql = "SELECT * FROM passwords WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
      final int _cursorIndexOfEncryptedPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "encryptedPassword");
      final int _cursorIndexOfWebsite = CursorUtil.getColumnIndexOrThrow(_cursor, "website");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
      final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
      final PasswordEntry _result;
      if (_cursor.moveToFirst()) {
        _result = new PasswordEntry();
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _result.setId(_tmpId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _result.setTitle(_tmpTitle);
        final String _tmpUsername;
        if (_cursor.isNull(_cursorIndexOfUsername)) {
          _tmpUsername = null;
        } else {
          _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
        }
        _result.setUsername(_tmpUsername);
        final String _tmpEncryptedPassword;
        if (_cursor.isNull(_cursorIndexOfEncryptedPassword)) {
          _tmpEncryptedPassword = null;
        } else {
          _tmpEncryptedPassword = _cursor.getString(_cursorIndexOfEncryptedPassword);
        }
        _result.setEncryptedPassword(_tmpEncryptedPassword);
        final String _tmpWebsite;
        if (_cursor.isNull(_cursorIndexOfWebsite)) {
          _tmpWebsite = null;
        } else {
          _tmpWebsite = _cursor.getString(_cursorIndexOfWebsite);
        }
        _result.setWebsite(_tmpWebsite);
        final String _tmpCategory;
        if (_cursor.isNull(_cursorIndexOfCategory)) {
          _tmpCategory = null;
        } else {
          _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        }
        _result.setCategory(_tmpCategory);
        final String _tmpNotes;
        if (_cursor.isNull(_cursorIndexOfNotes)) {
          _tmpNotes = null;
        } else {
          _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        }
        _result.setNotes(_tmpNotes);
        final long _tmpCreatedAt;
        _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
        _result.setCreatedAt(_tmpCreatedAt);
        final long _tmpUpdatedAt;
        _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
        _result.setUpdatedAt(_tmpUpdatedAt);
        final boolean _tmpIsFavorite;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
        _tmpIsFavorite = _tmp != 0;
        _result.setFavorite(_tmpIsFavorite);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getCount() {
    final String _sql = "SELECT COUNT(*) FROM passwords";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<PasswordEntry> getAllPasswordsSync() {
    final String _sql = "SELECT * FROM passwords ORDER BY updatedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
      final int _cursorIndexOfEncryptedPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "encryptedPassword");
      final int _cursorIndexOfWebsite = CursorUtil.getColumnIndexOrThrow(_cursor, "website");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
      final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
      final List<PasswordEntry> _result = new ArrayList<PasswordEntry>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final PasswordEntry _item;
        _item = new PasswordEntry();
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _item.setTitle(_tmpTitle);
        final String _tmpUsername;
        if (_cursor.isNull(_cursorIndexOfUsername)) {
          _tmpUsername = null;
        } else {
          _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
        }
        _item.setUsername(_tmpUsername);
        final String _tmpEncryptedPassword;
        if (_cursor.isNull(_cursorIndexOfEncryptedPassword)) {
          _tmpEncryptedPassword = null;
        } else {
          _tmpEncryptedPassword = _cursor.getString(_cursorIndexOfEncryptedPassword);
        }
        _item.setEncryptedPassword(_tmpEncryptedPassword);
        final String _tmpWebsite;
        if (_cursor.isNull(_cursorIndexOfWebsite)) {
          _tmpWebsite = null;
        } else {
          _tmpWebsite = _cursor.getString(_cursorIndexOfWebsite);
        }
        _item.setWebsite(_tmpWebsite);
        final String _tmpCategory;
        if (_cursor.isNull(_cursorIndexOfCategory)) {
          _tmpCategory = null;
        } else {
          _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        }
        _item.setCategory(_tmpCategory);
        final String _tmpNotes;
        if (_cursor.isNull(_cursorIndexOfNotes)) {
          _tmpNotes = null;
        } else {
          _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        }
        _item.setNotes(_tmpNotes);
        final long _tmpCreatedAt;
        _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
        _item.setCreatedAt(_tmpCreatedAt);
        final long _tmpUpdatedAt;
        _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
        _item.setUpdatedAt(_tmpUpdatedAt);
        final boolean _tmpIsFavorite;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
        _tmpIsFavorite = _tmp != 0;
        _item.setFavorite(_tmpIsFavorite);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
