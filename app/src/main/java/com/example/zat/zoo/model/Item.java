package com.example.zat.zoo.model;

import java.util.ArrayList;
import java.util.Iterator;

public class Item {
  private static int count = 0;
  public final static ArrayList<Item> ITEMS = new ArrayList<Item>() {
    {
      add(new Item("one", "one one"));
      add(new Item("two", "two two"));
      add(new Item("three", "three three"));
      add(new Item("four", "four four"));
      add(new Item("five", "five five"));
    }
  };

  public int id;
  public String name;
  public String body;

  public Item(String name, String body) {
    this.id = count++;
    this.name = name;
    this.body = body;
  }

  public static Item find(int id) {
    for (Item i : ITEMS) {
      if (i.id == id) {
        return i;
      }
    }

    return null;
  }
}
