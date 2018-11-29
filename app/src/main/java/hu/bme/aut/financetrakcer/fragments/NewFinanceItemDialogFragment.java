package hu.bme.aut.financetrakcer.fragments;

import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.Spinner;

import hu.bme.aut.financetrakcer.R;
import hu.bme.aut.financetrakcer.model.Finance;

public class NewFinanceItemDialogFragment extends DialogFragment {
    public static final String TAG = "NewFinanceItemDialogFragment";


    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText estimatedPriceEditText;
    private Spinner categorySpinner;


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
        try {
            finance.amount = Integer.parseInt(estimatedPriceEditText.getText().toString());
        } catch (NumberFormatException e) {
            finance.amount = 0;
        }
        return finance;
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_finance, null);
        nameEditText = contentView.findViewById(R.id.FinanceNameEditText);
        descriptionEditText = contentView.findViewById(R.id.FinanceDescriptionEditText);
        estimatedPriceEditText = contentView.findViewById(R.id.FinanceEstimatedPriceEditText);
        categorySpinner = contentView.findViewById(R.id.FinanceCategorySpinner);
        categorySpinner.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.category_items)));
        return contentView;
    }
}

