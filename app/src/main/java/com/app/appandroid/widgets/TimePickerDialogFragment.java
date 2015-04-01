package com.app.appandroid.widgets;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;


import java.util.Calendar;

/**
 * Created by Try on 31/03/2015.
 */
public class TimePickerDialogFragment extends DialogFragment {
    private OnTimeSetListener onTimeSetListener;

    public TimePickerDialogFragment(){

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar cal = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),onTimeSetListener,cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),true);
        return timePickerDialog;
    }

    public void setOnTimeSetListener(OnTimeSetListener onTimeSetListener) {
        this.onTimeSetListener = (OnTimeSetListener)onTimeSetListener;
    }


}
