package com.example.zat.zoo.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Item {
  private static int count = 0;
  public final static ArrayList<Item> ITEMS = new ArrayList<Item>();

  public int id;
  public String name;
  public String body;

  public Item(String name, String body) {
    this.id = count++;
    this.name = name;
    this.body = body;
  }

  public static Item find(Context context, int id) {
    for (Item i : Item.getAll(context)) {
      if (i.id == id) {
        return i;
      }
    }

    return null;
  }

  public static List<Item> getAll(Context context) {
    SharedPreferences pref = context.getSharedPreferences("item", Context.MODE_PRIVATE);

    Gson gson = new Gson();
    ArrayList<Item> items = new ArrayList<Item>();
    Map<String, ?> map = pref.getAll();
    for (String str : (Collection<String>) map.values()) {
      items.add(gson.fromJson(str, Item.class));
    }

    return items;
  }

  public void save(Context context) {
    SharedPreferences pref = context.getSharedPreferences("item", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = pref.edit();
    Gson gson = new Gson();

    editor.putString(String.format("%d", this.id), gson.toJson(this));
    editor.commit();
  }
}
