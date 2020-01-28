package com.echolocation19.listapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.echolocation19.listapp.model.Item;
import com.echolocation19.listapp.util.Constants;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

  public DatabaseHandler(@Nullable Context context) {
    super(context, Constants.DB_NAME, null, Constants.VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    String CREATE_LIST_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
            + Constants.KEY_ID + " INTEGER PRIMARY KEY,"
            + Constants.KEY_TITLE + " TEXT,"
            + Constants.KEY_DESCRIPTION + " TEXT,"
            + Constants.KEY_DATE_ADDED + " LONG)";
    db.execSQL(CREATE_LIST_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
    onCreate(db);
  }

  /**
   * CRUD OPERATIONS
  */

  public void addItem(Item item) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(Constants.KEY_TITLE, item.getTitle());
    values.put(Constants.KEY_DESCRIPTION, item.getDescription());
    values.put(Constants.KEY_DATE_ADDED, java.lang.System.currentTimeMillis());

    db.insert(Constants.TABLE_NAME, null, values);
  }

  public Item getItem(int id) {
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{
            Constants.KEY_ID,
            Constants.KEY_TITLE,
            Constants.KEY_DESCRIPTION,
            Constants.KEY_DATE_ADDED
    }, Constants.KEY_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);

    if (cursor != null) {
      cursor.moveToFirst();
    }

    Item item = new Item();
    if (cursor != null) {
      item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
      item.setTitle(cursor.getString(cursor.getColumnIndex(Constants.KEY_TITLE)));
      item.setDescription(cursor.getString(cursor.getColumnIndex(Constants.KEY_DESCRIPTION)));

      DateFormat dateFormat = DateFormat.getDateInstance();
      String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_ADDED))));
      item.setDateItemAdded(formattedDate);
    }
    return item;
  }

  public List<Item> getAllItems() {
    SQLiteDatabase db = getReadableDatabase();
    List<Item> items = new ArrayList<>();
    Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{
            Constants.KEY_ID,
            Constants.KEY_TITLE,
            Constants.KEY_DESCRIPTION,
            Constants.KEY_DATE_ADDED
    }, null, null, null, null, Constants.KEY_DATE_ADDED + " DESC");

    if (cursor.moveToFirst()) {
      do {
        Item item = new Item();
        item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
        item.setTitle(cursor.getString(cursor.getColumnIndex(Constants.KEY_TITLE)));
        item.setDescription(cursor.getString(cursor.getColumnIndex(Constants.KEY_DESCRIPTION)));

        DateFormat dateFormat = DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_ADDED))));
        item.setDateItemAdded(formattedDate);

        items.add(item);
      } while (cursor.moveToNext());
    }
    return items;
  }

  public int updateItem(Item item) {
    SQLiteDatabase db = getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(Constants.KEY_TITLE, item.getTitle());
    values.put(Constants.KEY_DESCRIPTION, item.getDescription());
    values.put(Constants.KEY_DATE_ADDED, java.lang.System.currentTimeMillis());

    return db.update(Constants.TABLE_NAME, values, Constants.KEY_ID + "=?", new String[]{
            String.valueOf(item.getId())
    });
  }

  public void deleteItem(int id) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(Constants.TABLE_NAME, Constants.KEY_ID + "=?", new String[]{String.valueOf(id)});
    db.close();
  }

  public int getItemCount() {
    SQLiteDatabase db = getReadableDatabase();
    String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;

    Cursor cursor = db.rawQuery(countQuery, null);
    return cursor.getCount();
  }
}
