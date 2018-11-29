package hu.bme.aut.financetrakcer.model;

import android.arch.persistence.room.*;
import android.support.annotation.NonNull;

@Entity(tableName = "categories")
public class Category {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @ColumnInfo(name = "name")
    @PrimaryKey
    @NonNull
    private String name;

    @ColumnInfo(name = "color")
    private int color;
}
