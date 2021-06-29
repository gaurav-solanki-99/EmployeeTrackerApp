package com.example.employeetrackerapp.Parag;

import android.widget.NumberPicker;

import com.example.employeetrackerapp.Parag.PickerField;

import java.util.Calendar;

public interface IPickerView {
    void setShortMonth(String[] shortMonths);

    void setNumberOfMonth(int numberOfMonths);

    void dateUpdate(PickerField field, int max, int min, Calendar current);

    void setOnValueChanged(NumberPicker.OnValueChangeListener onValueChangeListener);

    NumberPicker getMonthSpinner();

    NumberPicker getYearSpinner();

    void monthSetValue(int month);

    void yearSetValue(int year);


}
