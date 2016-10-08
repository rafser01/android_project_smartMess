package com.example.hp.smartmess2.adapter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.example.hp.smartmess2.Registration;

import java.util.Calendar;

/**
 * Created by HP on 7/15/2016.
 */
public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    final Calendar calendar=Calendar.getInstance();
    int myDay=calendar.get(Calendar.DAY_OF_MONTH);
    int myMonth=calendar.get(Calendar.MONTH);
    int myYear=calendar.get(Calendar.YEAR);
    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        populateDate(year,monthOfYear,dayOfMonth);
    }

    public Dialog onCreateDialog(Bundle bundle){
        return new DatePickerDialog(getActivity(),this,myYear,myMonth,myDay);
    }

    private void populateDate( int year, int month, int day){
            Registration registration=new Registration();
            registration.setDate(getActivity(),new StringBuffer().append(year).append("-").append(month+1).append("-").append(day));
     }


}
