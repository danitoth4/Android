package hu.bme.aut.financetrakcer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hu.bme.aut.financetrakcer.fragments.DatePickerDialogFragment;
import hu.bme.aut.financetrakcer.model.DataManager;
import hu.bme.aut.financetrakcer.model.Finance;

public class DiagramActivity extends AppCompatActivity
        implements DatePickerDialogFragment.OnDateSelectedListener  {

    private PieChart categoryChart;
    private Button switchButton;
    private boolean profits = false;
    private DateTime startDate = DateTime.now().minusDays(1);
    private DateTime endDate = startDate.plusMonths(1);
    private EditText startDateEditText;
    private EditText endDateEditText;
    private TextView balanceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagram);

        categoryChart = findViewById(R.id.chartCategory);
        switchButton = findViewById(R.id.btnSwitch);
        balanceTextView = findViewById(R.id.balanceTextView);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profits = !profits;
                loadData();
                switchButton.setText(
                        profits ? getString(R.string.watch_spending) : getString(R.string.watch_earning)
                );
            }
        });
        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.endDateEditText);
        endDateEditText.setText(endDate.getYear() + "." + endDate.getMonthOfYear() + "." + endDate.getDayOfMonth() + ".");
        startDateEditText.setText(startDate.getYear() + "." + startDate.getMonthOfYear() + "." + startDate.getDayOfMonth() + ".");
        startDateEditText.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialogFragment().show(getSupportFragmentManager(), "START");
                    }
                }
        );
        endDateEditText.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialogFragment().show(getSupportFragmentManager(), "END");
                    }
                }
        );
        loadData();
    }

    private void loadData()
    {
        List<PieEntry> entries = new ArrayList<>();
        List<Finance> finances = DataManager.getInstance().getItems();
        List<Finance> params = new ArrayList<>();
        for(String s : DataManager.getCategories())
        {
            for(Finance f : finances)
            {
                if(f.category.equals(s) && profits == f.income)
                    params.add(f);
            }
            if(params.size() > 0)
            {
                int value = DataManager.allCost(startDate, endDate, params);
                if(value > 0)
                entries.add(new PieEntry(value, s));
            }
            params.clear();
        }
        PieDataSet dataSet = new PieDataSet(entries, getString(R.string.Categories));
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        categoryChart.setData(data);
        categoryChart.invalidate();
        List<Finance> expenses = new ArrayList<>();
        List<Finance> profits = new ArrayList<>();
        for(Finance f : finances)
        {
            if(f.income)
                profits.add(f);
            else
                expenses.add(f);
        }
        int allcost = DataManager.allCost(startDate, endDate, profits) - DataManager.allCost(startDate, endDate, expenses);
        balanceTextView.setText("Balance: " + allcost);
    }

    @Override
    public void onDateSelected(int year, int month, int day, boolean start) {
        DateTime datetime = new DateTime(year, month + 1, day, 0, 0);
        if(start) {
            startDate = datetime;
            startDateEditText.setText(startDate.getYear() + "." + startDate.getMonthOfYear() + "." + startDate.getDayOfMonth() + ".");
        }
        else {
            endDate = datetime;
            endDateEditText.setText(endDate.getYear() + "." + endDate.getMonthOfYear() + "." + endDate.getDayOfMonth() + ".");
        }
        loadData();
    }
}
