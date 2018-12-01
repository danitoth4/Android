package hu.bme.aut.financetrakcer.model;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.Weeks;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static List<Finance> financeList = new ArrayList<>();

    private static String[] Categories = new String[] {
            "Food", "Electronic", "Book"
    };

    private static DataManager instance;


    public static String[] getCategories()
    {
        return Categories;
    }


    public static DataManager getInstance() {
        if(instance == null)
            instance = new DataManager();
        return instance;
    }

    public static void addItem(Finance item)
    {
        financeList.add(item);
    }

    public static void removeItem(Finance item)
    {
        financeList.remove(item);
    }

    public static void  addItems(List<Finance> list)
    {
        financeList.addAll(list);
    }

    public static List<Finance> getItems()
    {
        return financeList;
    }

    public static int allCost(DateTime startDate, DateTime endDate, List<Finance> finances)
    {
        int sum = 0;
        for(Finance f : finances)
        {
            DateTime financeDate = new DateTime(f.year, f.month, f.day, 0, 0);
            if(financeDate.compareTo(startDate) < 0)
                financeDate = startDate;
            if(financeDate.compareTo(endDate) > 0)
                continue;

            int occasions;
            switch(f.frequency.toLowerCase())
            {
                case "monthly" :
                    occasions = Months.monthsBetween(financeDate, endDate).getMonths();
                    break;
                case "weekly" :
                    occasions = Weeks.weeksBetween(financeDate, endDate).getWeeks();
                    break;
                case "daily" :
                    occasions = Days.daysBetween(financeDate, endDate).getDays();
                    break;
                default:
                    occasions = 1;
                    break;
            }


            sum += occasions * f.amount;
        }
        return sum;
    }
}
