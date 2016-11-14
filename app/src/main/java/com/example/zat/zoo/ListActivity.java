package com.example.zat.zoo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zat.zoo.db.DbHelper;
import com.example.zat.zoo.model.Item;

import java.util.List;

public class ListActivity extends AppCompatActivity {
  private RecyclerView recyclerView;
  private RecyclerView.LayoutManager layoutManager;
  private RecyclerView.Adapter recyclerViewAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);

    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);

    recyclerViewAdapter = new RecyclerViewAdapter(this);
    recyclerView.setAdapter(recyclerViewAdapter);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener((View view) -> {
      startActivity(new Intent(view.getContext(), EditActivity.class));
    });

    DbHelper dbHelper = new DbHelper(this);
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    String[] columns = {"_id", "name", "body"};
    Cursor cursor = db.query("item", columns, null, null, null, null, null);

    while (cursor.moveToNext()) {
      System.out.println(cursor.getString(0));
      System.out.println(cursor.getString(1));
      System.out.println(cursor.getString(2));
    }

    cursor.close();
  }

  public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    public List<Item> items;

    public class ViewHolder extends RecyclerView.ViewHolder {
      public View view;

      public ViewHolder(View v) {
        super(v);
        view = v;
      }
    }

    public RecyclerViewAdapter(Context context) {
      items = Item.getAll(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item, parent, false);

      return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      final Item item = items.get(position);
      TextView nameTextView = (TextView) holder.view.findViewById(R.id.card_view_name);
      TextView bodyTextView = (TextView) holder.view.findViewById(R.id.card_view_body);

      nameTextView.setText(item.name);
      bodyTextView.setText(item.body);

      holder.view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent intent = new Intent(view.getContext(), EditActivity.class);
          intent.putExtra("ID", item.id);
          startActivity(intent);
        }
      });
    }

    @Override
    public int getItemCount() {
      return items.size();
    }
  }
}
