package com.example.zat.zoo;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.annimon.stream.Stream;
import com.example.zat.zoo.db.DbHelper;
import com.example.zat.zoo.model.Item;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


@SmallTest
@RunWith(AndroidJUnit4.class)
public class DbHelperTest {
  @Before
  public void setUp() throws Exception {
    DbHelper db = new DbHelper(InstrumentationRegistry.getTargetContext());
    db.reset();
  }

  @Test
  public void findAll() throws Exception {
    DbHelper db = getDb();

    db.insert(new Item("one", "one body"));
    db.insert(new Item("two", "two body"));

    List<Item> items = db.findAll();
    assertEquals(2, items.size());
  }

  @Test
  public void delete() throws Exception {
    DbHelper db = getDb();
    assertEquals(db.findAll().size(), 0);
    db.insert(new Item("one", "one one"));

    db.delete(1);
    assertNull(db.find(1));
  }

  @Test
  public void search() throws Exception {
    DbHelper db = getDb();
    List<Item> items = new ArrayList<Item>() {
      {
        add(new Item("one", "foo bar"));
        add(new Item("two", "two two"));
        add(new Item("three", "three three"));
      }
    };

    Stream.of(items).forEach(db::insert);

    assertEquals(db.findAll().size(), items.size());
    assertEquals(items.subList(0, 1).size(), db.search("foo").size());

    List<Item> searchedItems = db.search("foo");
    assertArrayEquals(
            Stream.of(items.subList(0, 1)).map(item -> item.body).toArray(),
            Stream.of(searchedItems).map(item -> item.body).toArray()
    );
  }

  private DbHelper getDb() {
    return new DbHelper(InstrumentationRegistry.getTargetContext());
  }
}
