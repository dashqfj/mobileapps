package ru.mirea.borodkinada.mireaproject;

import com.google.gson.annotations.SerializedName;

public class DataModel {
    @SerializedName("name")
    private String name;

    @SerializedName("height")
    private int height;

    @SerializedName("weight")
    private int weight;

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }
}
