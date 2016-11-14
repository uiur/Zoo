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
  public final static ArrayList<Item> ITEMS = new ArrayList<>();

  public int _id;
  public String name;
  public String body;

  public Item(String name, String body) {
    this.name = name;
    this.body = body;
  }
}
