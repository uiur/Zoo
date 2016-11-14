package com.example.zat.zoo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {
  public static final int DATABASE_VERSION = 2;

  public DbHelper(Context context) {
    super(context, "zoo.db", null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL("create table item ( _id INTEGER primary key, name TEXT, body TEXT )");

    ArrayList<Pair<String, String>> list = new ArrayList<Pair<String, String>>() {
      {
        add(new Pair<String, String>("nice name", "nice body"));
        add(new Pair<String, String>("bad name", "bad body"));
      }
    };

    for (Pair<String, String> pair : list) {
      ContentValues values = new ContentValues();
      values.put("name", pair.first);
      values.put("body", pair.second);
      db.insert("item", null, values);
    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    db.execSQL("drop table if exists item");
    onCreate(db);
  }

  @Override
  public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    onUpgrade(db, oldVersion, newVersion);
  }
}
