package hu.bme.aut.financetrakcer.model;

import android.arch.persistence.room.*;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM categories")
    List<Category> getAll();

    @Insert
    long insert(Category category);

    @Update
    void update(Category category);

    @Delete
    void deleteItem(Category category);
}
