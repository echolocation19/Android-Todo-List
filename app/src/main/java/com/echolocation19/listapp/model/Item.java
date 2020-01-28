package com.echolocation19.listapp.model;

public class Item {
  private int id;
  private String title;
  private String description;
  private String dateItemAdded;

  public Item() {
  }

  public Item(int id, String title, String description, String dateItemAdded) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.dateItemAdded = dateItemAdded;
  }

  public Item(String title, String description, String dateItemAdded) {
    this.title = title;
    this.description = description;
    this.dateItemAdded = dateItemAdded;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDateItemAdded() {
    return dateItemAdded;
  }

  public void setDateItemAdded(String dateItemAdded) {
    this.dateItemAdded = dateItemAdded;
  }
}
