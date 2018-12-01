package hu.bme.aut.financetrakcer.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerDialogFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private OnDateSelectedListener onDateSelectedListener;

    private boolean start = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(context instanceof OnDateSelectedListener)) {
            throw new RuntimeException("The activity does not implement the" +
                    "OnDateSelectedListener interface");
        }

        onDateSelectedListener = (OnDateSelectedListener) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String tag = this.getTag();
        if(!tag.equals("START"))
            start = false;

        return new DatePickerDialog(getActivity(),this,
                year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        onDateSelectedListener.onDateSelected(year, month, day, start);
    }

    public interface OnDateSelectedListener {
        void onDateSelected(int year, int month, int day, boolean start);
    }
}