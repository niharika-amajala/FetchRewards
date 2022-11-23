package com.example.fetchrewards;

import androidx.annotation.NonNull;

public class JsonItem {

    private final int id;
    private final String name;
    private final int listId;

    public JsonItem(int id, String name, int listId) {

        if (name == null || "".equals(name) || listId < 0 || id < 0) {
            throw new IllegalArgumentException("'Illegal Argument Exception");
        }
        this.id = id;
        this.name = name;
        this.listId = listId;
    }

    @NonNull
    @Override
    public String toString() {
        return "JsonItem{" + "id=" + id + ", name='" + name + '\'' + ", listId=" + listId + '}';
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getListId() {
        return listId;
    }
}



