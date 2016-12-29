package pl.edu.agh.kt.aradoszek.meterreader.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import pl.edu.agh.kt.aradoszek.meterreader.Data.Meter;
import pl.edu.agh.kt.aradoszek.meterreader.R;

public class AddMeterDialogFragment extends DialogFragment {

    //================================================================================
    // Properties
    //================================================================================

    public AddMeterDialogFragment.AddMeterDialogListener listener;
    private EditText nameTextView;
    private EditText descriptionTextView;
    private RadioGroup typeRadioGroup;
    private View dialogView;

    //================================================================================
    // Create view
    //================================================================================

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        dialogView = inflater.inflate(R.layout.fragment_add_meter_dialog, null);
        nameTextView = (EditText) dialogView.findViewById(R.id.meter_name);
        descriptionTextView = (EditText) dialogView.findViewById(R.id.meter_description);
        typeRadioGroup = (RadioGroup) dialogView.findViewById(R.id.meter_type);

        builder.setView(dialogView)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Meter meter = createMeterFromDialogInputData();
                        if (meter != null) {
                            listener.onDialogPositiveClick(AddMeterDialogFragment.this, meter);
                        } else {
                            Toast.makeText(getActivity(), "Error, item not added" , Toast.LENGTH_SHORT);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogNegativeClick(AddMeterDialogFragment.this);
                    }
                });
        return builder.create();
    }

    private Meter createMeterFromDialogInputData() {
        String name = nameTextView.getText().toString();
        String description = descriptionTextView.getText().toString();
        Meter.MeterType type = getTypeFromRadioGroup(dialogView, typeRadioGroup);
        if (description == null)  {
            description = "";
        }
        if (name != null) {
            return new Meter(name, description, type);
        } else {
            return null;
        }
    }

    private Meter.MeterType getTypeFromRadioGroup(View view, RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = (RadioButton) view.findViewById(selectedId);
        String selectedtext = selectedRadioButton.getText().toString();

        if (selectedtext.equals("Gas")) {
            return Meter.MeterType.GAS;
        }
        if (selectedtext.equals( "Water")) {
            return Meter.MeterType.WATER;
        }
        if (selectedtext.equals("Electricity")) {
            return Meter.MeterType.ELECTRICITY;
        }
        return null;
    }

    //================================================================================
    // Listener
    //================================================================================
    public interface AddMeterDialogListener {
        void onDialogPositiveClick(DialogFragment dialog, Meter meter);

        void onDialogNegativeClick(DialogFragment dialog);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (AddMeterDialogFragment.AddMeterDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement AddMeterDialogListener");
        }
    }
}
