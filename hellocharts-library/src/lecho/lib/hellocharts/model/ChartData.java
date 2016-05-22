package lecho.lib.hellocharts.model;

import android.graphics.Typeface;

/**
 * Base interface for all chart data models.
 */
public interface ChartData {

    /**
     * Updates data by scale during animation.
     *
     * @param scale value from 0 to 1.0
     */
    public void update(float scale);

    /**
     * Inform data that animation finished(data should be update with scale 1.0f).
     */
    public void finish();

    /**
     * @see #setAxisXBottom(Axis)
     */
    public Axis getAxisXBottom();

    /**
     * Set horizontal axis at the bottom of the chart. Pass null to remove that axis.
     *
     * @param axisX
     */
    public void setAxisXBottom(Axis axisX);

    /**
     * @see #setAxisYLeft(Axis)
     */
    public Axis getAxisYLeft();

    /**
     * Set vertical axis on the left of the chart. Pass null to remove that axis.
     *
     * @param axisY
     */
    public void setAxisYLeft(Axis axisY);

    /**
     * @see #setAxisXTop(Axis)
     */
    public Axis getAxisXTop();

    /**
     * Set horizontal axis at the top of the chart. Pass null to remove that axis.
     *
     * @param axisX
     */
    public void setAxisXTop(Axis axisX);

    /**
     * @see #setAxisYRight(Axis)
     */
    public Axis getAxisYRight();

    /**
     * Set vertical axis on the right of the chart. Pass null to remove that axis.
     *
     * @param axisY
     */
    public void setAxisYRight(Axis axisY);

    /**
     * Returns color used to draw value label text.
     */
    public int getValueLabelTextColor();

    /**
     * Set value label text color, by default Color.WHITE.
     */
    public void setValueLabelsTextColor(int labelsTextColor);

    /**
     * Returns text size for value label in SP units.
     */
    public int getValueLabelTextSize();

    /**
     * Set text size for value label in SP units.
     */
    public void setValueLabelTextSize(int labelsTextSize);

    /**
     * Returns Typeface for value labels.
     *
     * @return Typeface or null if Typeface is not set.
     */
    public Typeface getValueLabelTypeface();

    /**
     * Set Typeface for all values labels.
     *
     * @param typeface
     */
    public void setValueLabelTypeface(Typeface typeface);

    //Todo add by hiperion 20160522
    /**
     * Returns color used to draw value icon text.
     */
    public int getValueIconTextColor();

    /**
     * Set value icon text color, by default Color.WHITE.
     */
    public void setValueIconTextColor(int iconTextColor);

    /**
     * Returns text size for value icon in SP units.
     */
    public int getValueIconTextSize();

    /**
     * Set text size for value icon in SP units.
     */
    public void setValueIconTextSize(int iconTextSize);

    /**
     * Returns Typeface for value icon.
     *
     * @return Typeface or null if Typeface is not set.
     */
    public Typeface getValueIconTypeface();

    /**
     * Set Typeface for all values icon.
     *
     * @param typeface
     */
    public void setValueIconTypeface(Typeface typeface);


    /**
     * @see #setValueLabelBackgroundEnabled(boolean)
     */
    public boolean isValueLabelBackgroundEnabled();

    /**
     * Set whether labels should have rectangle background. Default is true.
     */
    public void setValueLabelBackgroundEnabled(boolean isValueLabelBackgroundEnabled);

    /**
     * @see #setValueLabelBackgroundAuto(boolean)
     */
    public boolean isValueLabelBackgroundAuto();

    /**
     * Set false if you want to set custom color for all value labels. Default is true.
     */
    public void setValueLabelBackgroundAuto(boolean isValueLabelBackgrountAuto);

    /**
     * @see #setValueLabelBackgroundColor(int)
     */
    public int getValueLabelBackgroundColor();

    /**
     * Set value labels background. This value is used only if isValueLabelBackgroundAuto returns false. Default is
     * green.
     */
    public void setValueLabelBackgroundColor(int valueLabelBackgroundColor);
}
