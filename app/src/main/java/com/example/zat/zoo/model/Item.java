package com.example.zat.zoo.model;

import java.util.ArrayList;

public class Item {
  public final static ArrayList<Item> ITEMS = new ArrayList<Item>() {
    {
      add(new Item("one", "one one"));
      add(new Item("two", "two two"));
      add(new Item("three", "three three"));
      add(new Item("four", "four four"));
      add(new Item("five", "five five"));
    }
  };

  public String name;
  public String body;

  public Item(String name, String body) {
    this.name = name;
    this.body = body;
  }
}
