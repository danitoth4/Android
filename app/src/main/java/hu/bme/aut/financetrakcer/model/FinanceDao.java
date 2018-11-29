package hu.bme.aut.financetrakcer.model;

import android.arch.persistence.room.*;

import java.util.List;

@Dao
public interface FinanceDao {
    @Query("SELECT * FROM finances")
    List<Finance> getAll();

    @Insert
    long insert(Finance finance);

    @Update
    void update(Finance finance);

    @Delete
    void deleteItem(Finance finance);
}
