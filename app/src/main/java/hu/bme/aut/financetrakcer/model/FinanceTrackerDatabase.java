package hu.bme.aut.financetrakcer.model;

import android.arch.persistence.room.*;

import java.util.Date;
import java.util.List;

@Database(
        entities = {Finance.class, Category.class},
        version = 3,
        exportSchema = false
)
public abstract class FinanceTrackerDatabase extends RoomDatabase {
    public abstract FinanceDao FinanceDao();

    public abstract  CategoryDao CategoryDao();

}
