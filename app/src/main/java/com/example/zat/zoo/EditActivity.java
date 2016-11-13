package com.example.zat.zoo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.zat.zoo.model.Item;

public class EditActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.action_save) {
      EditText nameView = (EditText) findViewById(R.id.edit_text_name);
      String name = nameView.getText().toString();

      EditText bodyView = (EditText) findViewById(R.id.edit_text_body);
      String body = bodyView.getText().toString();

      Item newItem = new Item(name, body);
      Item.ITEMS.add(newItem);

      Intent intent = new Intent(this, ListActivity.class);
      navigateUpTo(intent);

      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.edit, menu);
    return true;
  }
}
