package com.echolocation19.listapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.echolocation19.listapp.data.DatabaseHandler;
import com.echolocation19.listapp.model.Item;
import com.echolocation19.listapp.ui.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ListActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

  private RecyclerView recyclerView;
  private RecyclerViewAdapter recyclerViewAdapter;
  private FloatingActionButton fab;
  private DatabaseHandler databaseHandler;
  private List<Item> itemList;
  private AlertDialog.Builder builder;
  private TextView title;
  private TextView description;
  private Button saveButton;
  private AlertDialog dialog;
  private View view;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);

    databaseHandler = new DatabaseHandler(this);

    view = findViewById(R.id.listActivityRoot);

    recyclerView = findViewById(R.id.recyclerView);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    fab = findViewById(R.id.fab2);
    recyclerViewChanges();
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        createPopUpDialog();
      }
    });

    setupSharedPreferences();
  }

  private void recyclerViewChanges() {
    itemList = databaseHandler.getAllItems();

    recyclerViewAdapter = new RecyclerViewAdapter(this, itemList);
    recyclerView.setAdapter(recyclerViewAdapter);
    recyclerViewAdapter.notifyDataSetChanged();
  }

  private void setupSharedPreferences() {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    setBackgroundColor(sharedPreferences.getString(getString(R.string.user_background_color_key), getString(R.string.pref_value_blue)));
    sharedPreferences.registerOnSharedPreferenceChangeListener(this);
  }

  private void createPopUpDialog() {
    builder = new AlertDialog.Builder(this);
    View view = getLayoutInflater().inflate(R.layout.popup, null);

    title = view.findViewById(R.id.titleEditText);
    description = view.findViewById(R.id.descriptionEditText);
    saveButton = view.findViewById(R.id.saveButton);
    saveButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!title.getText().toString().trim().isEmpty()
                && !description.getText().toString().trim().isEmpty()) {
          saveItem(v);
        } else {
          Snackbar.make(v, "Empty fields not allowed", Snackbar.LENGTH_SHORT).show();
        }
      }
    });
    builder.setView(view);
    dialog = builder.create();
    dialog.show();
  }

  private void saveItem(View v) {
    Item item = new Item();
    item.setTitle(title.getText().toString().trim());
    item.setDescription(description.getText().toString().trim());

    databaseHandler.addItem(item);

    Snackbar.make(v, "Item saved", Snackbar.LENGTH_SHORT).show();
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        dialog.dismiss();
        recyclerViewChanges();
      }
    }, 1200);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_settings:
        startActivity(new Intent(ListActivity.this, SettingsActivity.class));
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    switch (key) {
      case "background_color":
        loadColorFromPreference(sharedPreferences);
        break;
    }
  }

  private void loadColorFromPreference(SharedPreferences sharedPreferences) {
    setBackgroundColor(sharedPreferences.getString(getString(R.string.user_background_color_key), getString(R.string.pref_value_blue)));
  }

  private void setBackgroundColor(String color) {
    switch (color) {
      case "red":
        view.setBackgroundResource(R.color.prefRed);
        break;
      case "yellow":
        view.setBackgroundResource(R.color.prefYellow);
        break;
      case "blue":
        view.setBackgroundResource(R.color.prefBlue);
        break;
      case "purple":
        view.setBackgroundResource(R.color.prefPurple);
        break;
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
  }
}
