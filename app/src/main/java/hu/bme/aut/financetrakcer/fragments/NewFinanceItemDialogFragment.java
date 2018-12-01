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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import hu.bme.aut.financetrakcer.R;
import hu.bme.aut.financetrakcer.model.Finance;

public class NewFinanceItemDialogFragment extends DialogFragment {
    public static final String TAG = "NewFinanceItemDialogFragment";


    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText amountEditText;
    private Spinner categorySpinner;
    private  Spinner frequencySpinner;
    private EditText yearEditText;
    private EditText monthEditText;
    private EditText dayEditText;
    private CheckBox isIncomeCheckBox;


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
        try {
            finance.year = Integer.parseInt(yearEditText.getText().toString());
        } catch (NumberFormatException e) {
            finance.year = Calendar.getInstance().get(Calendar.YEAR);
        }
        try {
            finance.month = Integer.parseInt(monthEditText.getText().toString());
        } catch (NumberFormatException e) {
            finance.month = Calendar.getInstance().get(Calendar.MONTH);
        }
        try {
            finance.day = Integer.parseInt(dayEditText.getText().toString());
        } catch (NumberFormatException e) {
            finance.day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        }
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
        yearEditText = contentView.findViewById(R.id.FinanceYearEditText);
        monthEditText = contentView.findViewById(R.id.FinanceMonthEditText);
        dayEditText = contentView.findViewById(R.id.FinanceDayEditText);
        isIncomeCheckBox = contentView.findViewById(R.id.isIncomeCheckbox);


        return contentView;
    }
}

