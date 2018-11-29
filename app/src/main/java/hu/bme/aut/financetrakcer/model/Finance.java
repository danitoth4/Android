package hu.bme.aut.financetrakcer.model;

import android.arch.persistence.room.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.*;
import java.util.*;

@Entity(tableName = "finances")
public class Finance {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "amount")
    public int amount;

    @ColumnInfo(name = "income")
    public boolean income;

    @ColumnInfo(name = "dates")
    public List<Date> dates;

    @ColumnInfo(name = "description")
    public String description;

    @TypeConverter
    public static String DateListToString(List<Date> dates)
    {
        Gson gson = new Gson();
        return gson.toJson(dates);
    }

    @TypeConverter
    public static List<Date> StringToDateList(String data)
    {
        Gson gson = new Gson();

        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Date>>() {}.getType();

        return gson.fromJson(data, listType);
    }

}

