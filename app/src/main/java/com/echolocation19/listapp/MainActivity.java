package com.echolocation19.listapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.echolocation19.listapp.data.DatabaseHandler;
import com.echolocation19.listapp.model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

  private EditText title;
  private EditText description;
  private Button saveButton;
  private AlertDialog dialog;
  private AlertDialog.Builder builder;
  private DatabaseHandler databaseHandler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    databaseHandler = new DatabaseHandler(this);

    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        createPopUpDialog();
      }
    });

    chooseActivity();
  }

  private void chooseActivity() {
    if (databaseHandler.getItemCount() > 0) {
      startActivity(new Intent(MainActivity.this, ListActivity.class));
      finish();
    }
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
        startActivity(new Intent(MainActivity.this, ListActivity.class));
      }
    }, 1200);
  }
}
