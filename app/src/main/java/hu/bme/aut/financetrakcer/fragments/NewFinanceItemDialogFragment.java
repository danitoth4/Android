package hu.bme.aut.financetrakcer.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import hu.bme.aut.financetrakcer.R;
import hu.bme.aut.financetrakcer.model.Finance;

public class NewFinanceItemDialogFragment extends DialogFragment implements DatePickerDialogFragment.OnDateSelectedListener {
    public static final String TAG = "NewFinanceItemDialogFragment";


    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText amountEditText;
    private Spinner categorySpinner;
    private  Spinner frequencySpinner;
    private EditText firstDateEditText;
    private CheckBox isIncomeCheckBox;
    private DateTime firstDate = DateTime.now();

    @Override
    public void onDateSelected(int year, int month, int day, boolean start) {
        firstDate = new DateTime(year, month + 1, day, 0, 0);
        firstDateEditText.setText(firstDate.getYear() + "." + firstDate.getMonthOfYear() + "." + firstDate.getDayOfMonth() + ".");
    }


    public interface NewFinanceItemDialogListener {
        void onFinanceItemCreated(Finance newItem);
    }

    private NewFinanceItemDialogListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof NewFinanceItemDialogListener) {
            listener = (NewFinanceItemDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the NewFinanceItemDialogListener interface!");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.new_finance)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isValid()) {
                            listener.onFinanceItemCreated(getFinance());
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private boolean isValid() {
        return nameEditText.getText().length() > 0;
    }

    private Finance getFinance() {
        Finance finance = new Finance();
        finance.name = nameEditText.getText().toString();
        finance.description = descriptionEditText.getText().toString();
        finance.frequency = frequencySpinner.getSelectedItem().toString();
        try {
            finance.amount = Integer.parseInt(amountEditText.getText().toString());
        } catch (NumberFormatException e) {
            finance.amount = 0;
        }
        finance.year = firstDate.getYear();
        finance.month = firstDate.getMonthOfYear();
        finance.day = firstDate.getDayOfMonth();
        finance.income = isIncomeCheckBox.isChecked();
        finance.category = categorySpinner.getSelectedItem().toString();

        return finance;
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_finance, null);
        nameEditText = contentView.findViewById(R.id.FinanceNameEditText);
        descriptionEditText = contentView.findViewById(R.id.FinanceDescriptionEditText);
        amountEditText = contentView.findViewById(R.id.FinanceAmountEditText);
        categorySpinner = contentView.findViewById(R.id.FinanceCategorySpinner);
        categorySpinner.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.category_items)));
        frequencySpinner = contentView.findViewById(R.id.FinanceFrequencySpinner);
        frequencySpinner.setAdapter((new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.frequency_array))));
       firstDateEditText = contentView.findViewById(R.id.firstDateTextView);
       firstDateEditText.setText(firstDate.getYear() + "." + firstDate.getMonthOfYear() + "." + firstDate.getDayOfMonth() + ".");
       firstDateEditText.setOnClickListener(new View.OnClickListener() {
           @Override
               public void onClick(View v) {
               new DatePickerDialogFragment().show(getFragmentManager(), "START");
           }
       });
        isIncomeCheckBox = contentView.findViewById(R.id.isIncomeCheckbox);


        return contentView;
    }
}

