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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.example.zat.zoo.db.DbHelper;
import com.example.zat.zoo.model.Item;

import java.util.List;
import java.util.stream.Collectors;

public class ListActivity extends AppCompatActivity {
  private RecyclerView recyclerView;
  private RecyclerView.LayoutManager layoutManager;
  private RecyclerViewAdapter recyclerViewAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);

    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);

    recyclerViewAdapter = new RecyclerViewAdapter(this);
    recyclerView.setAdapter(recyclerViewAdapter);

    ItemTouchHelper touchHelper = new ItemTouchHelper(new  ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
      @Override
      public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
      }

      @Override
      public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        recyclerViewAdapter.onRemove(position);
      }
    });

    touchHelper.attachToRecyclerView(recyclerView);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(view -> startActivity(new Intent(view.getContext(), EditActivity.class)));
  }

  public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    public List<Item> items;
    private DbHelper dbHelper;

    public class ViewHolder extends RecyclerView.ViewHolder {
      public View view;

      public ViewHolder(View v) {
        super(v);
        view = v;
      }
    }

    public RecyclerViewAdapter(Context context) {
      dbHelper = new DbHelper(context);
      this.items = dbHelper.findAll();
    }

    public void onRemove(int position) {
      Item item = items.get(position);
      dbHelper.delete(item._id);
      items.remove(position);
      notifyItemRemoved(position);
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

      holder.view.setOnClickListener(view -> {
        Intent intent = new Intent(view.getContext(), EditActivity.class);
        intent.putExtra("ID", item._id);
        startActivity(intent);
      });
    }

    @Override
    public int getItemCount() {
      return items.size();
    }

  }
}
