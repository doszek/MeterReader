package pl.edu.agh.kt.aradoszek.meterreader.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.kt.aradoszek.meterreader.Data.Meter;
import pl.edu.agh.kt.aradoszek.meterreader.Data.Place;
import pl.edu.agh.kt.aradoszek.meterreader.R;

public class AddPlaceDialogFragment extends DialogFragment {

    //================================================================================
    // Properties
    //================================================================================

    public AddPlaceDialogListener listener;
    private AlertDialog dialog;
    private TextView nameTextView;
    private TextView descriptionTextView;
    private View dialogView;

    //================================================================================
    // Create view
    //================================================================================

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        dialogView = inflater.inflate(R.layout.fragment_add_place_dialog, null);
        nameTextView = (TextView) dialogView.findViewById(R.id.place_name);
        descriptionTextView = (TextView) dialogView.findViewById(R.id.place_description);

        builder.setView(dialogView)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Place place = createPlaceFromDialogInputData();
                        if (place != null) {
                            listener.onDialogPositiveClick(AddPlaceDialogFragment.this, place);
                        } else {
                            Toast.makeText(getActivity(), "Error, item not added" , Toast.LENGTH_SHORT);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogNegativeClick(AddPlaceDialogFragment.this);
                    }
                });
        dialog = builder.create();
        return dialog;
    }

    private Place createPlaceFromDialogInputData() {
        String name = nameTextView.getText().toString();
        String description = descriptionTextView.getText().toString();
        List<Meter> meterList = new ArrayList<Meter>();
        if (description == null) {
            description = "";
        }
        if (name != null) {
            return new Place(name, description, meterList);
        } else {
            return null;
        }
    }

    //================================================================================
    // Text Watcher - TODO
    //================================================================================

    private void setTextWatchers(final AlertDialog dialog) {
        dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
        nameTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });

        descriptionTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    //================================================================================
    // Listener
    //================================================================================

    public interface AddPlaceDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, Place place);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (AddPlaceDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement AddPlaceDialogListener");
        }
    }
}
