package com.example.appfond;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class MyAxisValueFormatter extends ValueFormatter implements IAxisValueFormatter {
    private String[] mValues;

    public MyAxisValueFormatter(String[] values) {
        this.mValues = values;
    }


    public String getFormattedValue(float value, AxisBase axis) {
        return mValues[(int) value];
    }

    public int getDecimalDigits() {
        return 0;
    }
}
