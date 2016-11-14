package com.example.zat.zoo;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.example.zat.zoo.db.DbHelper;
import com.example.zat.zoo.model.Item;

import junit.framework.TestCase;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;


@SmallTest
@RunWith(AndroidJUnit4.class)
public class DbHelperTest {
  @BeforeClass
  public static void setUp() throws Exception {
    DbHelper db = new DbHelper(InstrumentationRegistry.getTargetContext());
    db.reset();
  }

  @Test
  public void findAll() throws Exception {
    DbHelper db = new DbHelper(InstrumentationRegistry.getTargetContext());

    db.insert(new Item("one", "one body"));
    db.insert(new Item("two", "two body"));

    List<Item> items = db.findAll();
    assertEquals(2, items.size());
  }
}
