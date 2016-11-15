package com.example.zat.zoo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import com.example.zat.zoo.model.Item;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
  public static final int DATABASE_VERSION = 2;
  public static final String[] COLUMNS = {"_id", "name", "body"};

  public DbHelper(Context context) {
    super(context, "zoo.db", null, DATABASE_VERSION);
  }

  public void insert(Item item) {
    SQLiteDatabase db = getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put("name", item.name);
    values.put("body", item.body);

    if (item._id > 0) {
      db.update("item", values, "_id = ?", new String[]{ String.valueOf(item._id) });
      return;
    }


    db.insert("item", null, values);
  }

  public Item find(int _id) {
    Cursor cursor = getWritableDatabase().query("item", COLUMNS, "_id = ?", new String[]{String.valueOf(_id)}, null, null, null, "1");
    if (cursor.moveToFirst()) {
      return getCurrentItemByCursor(cursor);
    }

    return null;
  }

  private Item getCurrentItemByCursor(Cursor cursor) {
    final Item item = new Item(cursor.getString(1), cursor.getString(2));
    item._id = cursor.getInt(0);

    return item;
  }

  public List<Item> findAll() {
    SQLiteDatabase db = getWritableDatabase();

    Cursor cursor = db.query("item", COLUMNS, null, null, null, null, null);
    ArrayList<Item> list = new ArrayList<>();
    while (cursor.moveToNext()) {
      list.add(getCurrentItemByCursor(cursor));
    }
    cursor.close();

    return list;
  }

  public void delete(int id) {
    getWritableDatabase().delete("item", "_id = ?", new String[]{String.valueOf(id)});
  }


  public void deleteAll() {
    SQLiteDatabase db = getWritableDatabase();
    db.execSQL("drop table if exists item");
  }

  public void reset() {
    deleteAll();
    onCreate(getWritableDatabase());
  }

  public List<Item> search(String query) {
    SQLiteDatabase db = getReadableDatabase();
    Cursor cursor = db.query(
            "item",
            COLUMNS,
            "name like ? or body LIKE ?",
            new String[] { "%" + query + "%", "%" + query + "%" },
            null,
            null,
            null
    );

    List<Item> items = new ArrayList<Item>();
    while (cursor.moveToNext()) {
      Item item = getCurrentItemByCursor(cursor);
      items.add(item);
    }

    return items;
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL("create table item ( _id INTEGER primary key, name TEXT, body TEXT )");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    deleteAll();
    onCreate(db);
  }

  @Override
  public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    onUpgrade(db, oldVersion, newVersion);
  }
}
