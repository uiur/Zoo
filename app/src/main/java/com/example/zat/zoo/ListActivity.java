package com.example.zat.zoo;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.zat.zoo.db.DbHelper;
import com.example.zat.zoo.model.Item;

import java.util.List;
import java.util.Optional;

public class ListActivity extends AppCompatActivity {
  private RecyclerView recyclerView;
  private RecyclerView.LayoutManager layoutManager;
  private RecyclerViewAdapter recyclerViewAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);

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

    recyclerViewAdapter = new RecyclerViewAdapter(this, "");
    recyclerView.setAdapter(recyclerViewAdapter);

    handleIntent();
  }

  private void handleIntent() {
    Intent intent = getIntent();
    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
      final String query = intent.getStringExtra(SearchManager.QUERY);
      recyclerViewAdapter.onQuery(query);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.search, menu);

    SearchView searchView = (SearchView) menu.findItem(R.id.search_view).getActionView();
    searchView.setIconifiedByDefault(false);

    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        recyclerViewAdapter.onQuery(query);
        return true;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        if (newText.equals("")) {
          recyclerViewAdapter.onQuery("");
          return true;
        }
        return false;
      }
    });

    View outsideFrame = findViewById(R.id.outside_frame);
    outsideFrame.setOnTouchListener((v, e) -> {
      searchView.clearFocus();
      return false;
    });

    searchView.setOnQueryTextFocusChangeListener((v, focus) -> {

      System.out.println(focus);
      if (focus) {
        outsideFrame.setVisibility(View.VISIBLE);
      } else {
        outsideFrame.setVisibility(View.INVISIBLE);
      }
    });

    return true;
  }

  void hideKeyboard(View v) {
    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    v.clearFocus();
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

    public RecyclerViewAdapter(Context context, String query) {
      dbHelper = new DbHelper(context);

      updateByQuery(query);

    }

    private void updateByQuery(String query) {
      if (query.length() > 0) {
        this.items = dbHelper.search(query);
      } else {
        this.items = dbHelper.findAll();
      }
    }

    public void onQuery(String query) {
      updateByQuery(query);
      notifyDataSetChanged();
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
