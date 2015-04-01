package com.app.appandroid.widgets;

import android.app.DialogFragment;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Try on 31/03/2015.
 */
public class DatePickerDialogFragment extends DialogFragment{
    private OnDateSetListener mDateSetListener;

    public DatePickerDialogFragment() {
        // nothing to see here, move along
    }

    public void setOnDateSetListener(OnDateSetListener callback) {
        mDateSetListener = (OnDateSetListener) callback;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar cal = Calendar.getInstance();

        return new DatePickerDialog(getActivity(),
                mDateSetListener, cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
    }


}
