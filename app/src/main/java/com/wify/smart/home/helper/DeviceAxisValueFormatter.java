package com.wify.smart.home.helper;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by philipp on 02/06/16.
 */
public class DeviceAxisValueFormatter implements IAxisValueFormatter {

    public static List<String> Devices = new ArrayList<>();

    private final BarLineChartBase<?> chart;

    public DeviceAxisValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        try {

            return Devices.get((int) value);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return "";
    }

}
