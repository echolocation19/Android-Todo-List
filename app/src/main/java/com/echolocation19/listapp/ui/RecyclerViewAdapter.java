package com.echolocation19.listapp.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.echolocation19.listapp.R;
import com.echolocation19.listapp.data.DatabaseHandler;
import com.echolocation19.listapp.model.Item;
import com.google.android.material.snackbar.Snackbar;

import java.text.MessageFormat;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

  private Context context;
  private List<Item> itemList;
  private AlertDialog.Builder builder;
  private AlertDialog dialog;

  public RecyclerViewAdapter(Context context, List<Item> itemList) {
    this.context = context;
    this.itemList = itemList;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
    return new ViewHolder(view, context);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Item item = itemList.get(position);
    holder.title.setText(item.getTitle());
    holder.description.setText(item.getDescription());
    holder.dateAdded.setText(MessageFormat.format("Added on: {0}", item.getDateItemAdded()));
  }

  @Override
  public int getItemCount() {
    return itemList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView title;
    public TextView description;
    public TextView dateAdded;
    public Button editButton;
    public Button deleteButton;

    public ViewHolder(@NonNull View itemView, Context ctx) {
      super(itemView);
      context = ctx;

      title = itemView.findViewById(R.id.titleTextView);
      description = itemView.findViewById(R.id.descriptionTextView);
      dateAdded = itemView.findViewById(R.id.dateAddedTextView);

      editButton = itemView.findViewById(R.id.editButton);
      deleteButton = itemView.findViewById(R.id.deleteButton);

      editButton.setOnClickListener(this);
      deleteButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      int position;
      position = getAdapterPosition();
      Item item = itemList.get(position);

      switch (v.getId()) {
        case R.id.editButton:
          editItem(item);
          break;
        case R.id.deleteButton:
          deleteItem(item.getId());
          break;
      }
    }

  private void deleteItem(final int id) {
    builder = new AlertDialog.Builder(context);
    View view = LayoutInflater.from(context).inflate(R.layout.confirmation_pop, null);
    Button yesButton = view.findViewById(R.id.yesButton);
    Button noButton = view.findViewById(R.id.noButton);
    builder.setView(view);
    dialog = builder.create();
    dialog.show();

    yesButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DatabaseHandler db = new DatabaseHandler(context);
        db.deleteItem(id);
        itemList.remove(getAdapterPosition());
        notifyItemRemoved(getAdapterPosition());
        dialog.dismiss();
      }
    });

    noButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });
  }

  private void editItem(final Item item) {
    builder = new AlertDialog.Builder(context);
    final View view = LayoutInflater.from(context).inflate(R.layout.popup, null);

    TextView title = view.findViewById(R.id.title);
    final TextView titleName = view.findViewById(R.id.titleEditText);
    final TextView description = view.findViewById(R.id.descriptionEditText);
    Button saveButton = view.findViewById(R.id.saveButton);

    saveButton.setText("update");
    title.setText("Edit item");
    titleName.setText(item.getTitle());
    description.setText(item.getDescription());

    builder.setView(view);
    dialog = builder.create();
    dialog.show();

    saveButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        item.setTitle(titleName.getText().toString().trim());
        item.setDescription(description.getText().toString().trim());

        if (!titleName.getText().toString().trim().isEmpty()
        && !description.getText().toString().trim().isEmpty()) {
          databaseHandler.updateItem(item);
          notifyItemChanged(getAdapterPosition(), item);
        } else {
          Snackbar.make(view, "Empty fields not allowed", Snackbar.LENGTH_SHORT).show();
        }
        dialog.dismiss();
      }
    });
  }
}
}
