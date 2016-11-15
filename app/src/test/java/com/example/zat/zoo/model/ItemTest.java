package com.example.zat.zoo.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ItemTest {
  @Test
  public void tags() throws Exception {
    Item item = new Item("title", "body #foo \n #bar bar\n #foobar2000\n #Trump");
    assertArrayEquals(new String[]{"foo", "bar", "foobar2000", "Trump"}, item.tags().toArray());
  }
}