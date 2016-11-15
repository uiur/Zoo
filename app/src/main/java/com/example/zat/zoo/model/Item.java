package com.example.zat.zoo.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Item {
  public int _id;
  public String name;
  public String body;

  public Item(String name, String body) {
    this.name = name;
    this.body = body;
  }

  public List<String> tags() {
    Pattern pattern = Pattern.compile("(?:^|\\s+)#(\\w+)");
    Matcher matcher = pattern.matcher(body);

    final ArrayList<String> list = new ArrayList<>();
    while (matcher.find()) {
      String tagName = matcher.group(1);
      list.add(tagName);
    }

    return list;
  }
}
