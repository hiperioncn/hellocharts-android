package lecho.lib.hellocharts.samples;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.animation.ChartAnimationListener;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.LineChartView;

public class LineChartActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }
    }

    /**
     * A fragment containing a line chart.
     */
    public static class PlaceholderFragment extends Fragment {

        private LineChartView chart;
        private LineChartData data;
        private int numberOfLines = 1;
        private int maxNumberOfLines = 4;
        private int numberOfPoints = 12;

        float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];

        private boolean hasAxes = true;
        private boolean hasAxesNames = true;
        private boolean hasLines = true;
        private boolean hasPoints = true;
        private ValueShape shape = ValueShape.CIRCLE;
        private boolean isFilled = false;
        private boolean hasLabels = false;
        private boolean isCubic = false;
        private boolean hasLabelForSelected = false;
        private boolean pointsHaveDifferentColor;

        public static final String WEATHER_CLEAR_DAY = "\uf00d";
        public static final String WEATHER_CLEAR_NIGHT = "\uf02e";
        private static final String WEATHER_PARTLY_SUNNY = "\uf00c";
        private static final String WEATHER_MOSTLY_CLOUDY = "\uf013";
        private static final String WEATHER_CLOUDY = "\uf013";
        private static final String WEATHER_WARNING = "";
        private static final String WEATHER_WINDY = "\uf050";
        private static final String WEATHER_FOG = "\uf014";
        private static final String WEATHER_RAINY = "\uf019";
        private static final String WEATHER_ICE = "\uf076";
        private static final String WEATHER_SMOKE = "\uf062";
        private static final String WEATHER_SNOW = "\uf01b";
        private static final String WEATHER_THUNDERSTORMS = "\uf01e";
        private static final String WEATHER_UNKNOWN = "\uf03e";

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.fragment_line_chart, container, false);

            chart = (LineChartView) rootView.findViewById(R.id.chart);
            chart.setOnValueTouchListener(new ValueTouchListener());


            // Generate some random values.
            generateValues();

            generateData();

            // Disable viewport recalculations, see toggleCubic() method for more info.
            chart.setViewportCalculationEnabled(false);

//            chart.setZoomEnabled(false);
//            chart.setInteractive(true);
//            chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
//            chart.setScrollEnabled(true);

//            chart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
//            chart.setZoomLevel(0,0,20);
//            chart.setCameraDistance(10f);
//            chart.setLabelFor();
            final Viewport v = new Viewport(chart.getMaximumViewport());
            v.top =30; //example max value
            v.bottom = 16;  //example min value
            chart.setMaximumViewport(v);
//            v.left =0; //example max value
//            v.right =6; //example max value
            v.left = 0;
            v.right =v.left+5;
            chart.setCurrentViewport(v);
            chart.setViewportCalculationEnabled(false);

//            final Viewport vmax = new Viewport(chart.getMaximumViewport());
//            vmax.top =30; //example max value
//            vmax.bottom = 20;  //example min value
//            vmax.left =0; //example max value
//            vmax.right =11; //example max value
//            chart.setMaximumViewport(vmax);
//            resetViewport();

            return rootView;
        }

        // MENU
        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.line_chart, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_reset) {
                reset();
                generateData();
                return true;
            }
            if (id == R.id.action_add_line) {
                addLineToData();
                return true;
            }
            if (id == R.id.action_toggle_lines) {
                toggleLines();
                return true;
            }
            if (id == R.id.action_toggle_points) {
                togglePoints();
                return true;
            }
            if (id == R.id.action_toggle_cubic) {
                toggleCubic();
                return true;
            }
            if (id == R.id.action_toggle_area) {
                toggleFilled();
                return true;
            }
            if (id == R.id.action_point_color) {
                togglePointColor();
                return true;
            }
            if (id == R.id.action_shape_circles) {
                setCircles();
                return true;
            }
            if (id == R.id.action_shape_square) {
                setSquares();
                return true;
            }
            if (id == R.id.action_shape_diamond) {
                setDiamonds();
                return true;
            }
            if (id == R.id.action_toggle_labels) {
                toggleLabels();
                return true;
            }
            if (id == R.id.action_toggle_axes) {
                toggleAxes();
                return true;
            }
            if (id == R.id.action_toggle_axes_names) {
                toggleAxesNames();
                return true;
            }
            if (id == R.id.action_animate) {
                prepareDataAnimation();
                chart.startDataAnimation();
                return true;
            }
            if (id == R.id.action_toggle_selection_mode) {
                toggleLabelForSelected();

                Toast.makeText(getActivity(),
                        "Selection mode set to " + chart.isValueSelectionEnabled() + " select any point.",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
            if (id == R.id.action_toggle_touch_zoom) {
                chart.setZoomEnabled(!chart.isZoomEnabled());
                Toast.makeText(getActivity(), "IsZoomEnabled " + chart.isZoomEnabled(), Toast.LENGTH_SHORT).show();
                return true;
            }
            if (id == R.id.action_zoom_both) {
                chart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
                return true;
            }
            if (id == R.id.action_zoom_horizontal) {
                chart.setZoomType(ZoomType.HORIZONTAL);
                return true;
            }
            if (id == R.id.action_zoom_vertical) {
                chart.setZoomType(ZoomType.VERTICAL);
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        private void generateValues() {
            for (int i = 0; i < maxNumberOfLines; ++i) {
                for (int j = 0; j < numberOfPoints; ++j) {
                    int selectIndex = (int)((float)Math.random() * 2f);
                    int delta = (int)((float)Math.random() * 3f);
                    if(selectIndex==1){
                        delta*= -1;
                    }
                    randomNumbersTab[i][j] = 25+delta;
                }
            }
        }

        private void reset() {
            numberOfLines = 1;

            hasAxes = true;
            hasAxesNames = true;
            hasLines = true;
            hasPoints = true;
            shape = ValueShape.CIRCLE;
            isFilled = false;
            hasLabels = false;
            isCubic = false;
            hasLabelForSelected = false;
            pointsHaveDifferentColor = false;

            chart.setValueSelectionEnabled(hasLabelForSelected);
            resetViewport();
        }

        private void resetViewport() {
            // Reset viewport height range to (0,100)
            final Viewport v = new Viewport(chart.getMaximumViewport());
            v.bottom = 0;
            v.top = 100;
            v.left = 0;
            v.right = numberOfPoints - 1;
            chart.setMaximumViewport(v);
            chart.setCurrentViewport(v);
        }

        private void generateData() {

            Bitmap labelBitmap1 = BitmapFactory.decodeResource(getResources(),R.drawable.weather_cloud_sun_rain);
            Bitmap labelBitmap2 = BitmapFactory.decodeResource(getResources(),R.drawable.weather_cloud_more_snow);
            Bitmap labelBitmap3 = BitmapFactory.decodeResource(getResources(),R.drawable.weather_cloud_sun);
            Bitmap labelBitmap4 = BitmapFactory.decodeResource(getResources(),R.drawable.weather_storm);
            List<Line> lines = new ArrayList<Line>();
            for (int i = 0; i < numberOfLines; ++i) {

                List<PointValue> values = new ArrayList<PointValue>();
                for (int j = 0; j < numberOfPoints; ++j) {
                    int selectIndex = (int)((float)Math.random() * 5f);
                    int labelTemperature =(int) randomNumbersTab[i][j];
                    String labelText = labelTemperature + "℃";
                    if(selectIndex == 1) {
                        values.add(new PointValue(j, randomNumbersTab[i][j]).setLabel(labelText));
                    } else if(selectIndex == 2) {
                        values.add(new PointValue(j, randomNumbersTab[i][j]).setLabel(labelText));
                    } else if(selectIndex == 3) {
                        values.add(new PointValue(j, randomNumbersTab[i][j]).setLabel(labelText));
                    } else if(selectIndex == 4) {
                        values.add(new PointValue(j, randomNumbersTab[i][j]).setLabel(labelText));
                    } else {
                        values.add(new PointValue(j, randomNumbersTab[i][j]).setLabel(labelText));
                    }

                }

                Line line = new Line(values);
                line.setColor(Color.parseColor("#D3D3D3"));//ChartUtils.COLORS[i]
                line.setShape(shape);
                line.setCubic(true);
                line.setFilled(true);
                line.setHasLabels(true);
                line.setHasLabelsOnlyForSelected(hasLabelForSelected);
                line.setHasLines(hasLines);
                line.setHasPoints(hasPoints);
                if (pointsHaveDifferentColor){
                    line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
                }
                line.setPointColor(Color.parseColor("#51b6f4"));
                line.setPointRadius(4);
                line.setColor(Color.parseColor("#ffffff"));
                line.setAreaTransparency(230);
                line.setStrokeWidth(4);
                lines.add(line);
            }

            lines.add(genTimeLine());
            data = new LineChartData(lines);

            List<AxisValue> axisValueList = new ArrayList<>();
            for(int i=0 ;i<12;i++){
                char[] labelCharArr = (i+":00").toCharArray();
                if(i==0){
                    labelCharArr = "Now".toCharArray();
                }
                axisValueList.add(new AxisValue(i,labelCharArr));
            }

            if (hasAxes) {
                Axis axisX = new Axis().setHasLines(false).setHasSeparationLine(false);
                Axis axisY = new Axis().setHasLines(false);

                axisX.setValues(axisValueList);
                axisX.setTextColor(Color.GRAY);
                axisX.setTextSize(18);
                if (hasAxesNames) {
//                    axisX.setName("Axis X");
//                    axisY.setName("Axis Y");
                }
//                data.setAxisXBottom(axisX);
                data.setAxisYLeft(null);
            } else {
                data.setAxisXBottom(null);
                data.setAxisYLeft(null);
            }
            data.setValueLabelBackgroundEnabled(false);
            data.setValueLabelsTextColor(getResources().getColor(android.R.color.darker_gray));
            data.setValueLabelTextSize(20);
            data.setBaseValue(Float.NEGATIVE_INFINITY);
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "weathericons-regular-webfont.ttf");
            data.setValueIconTextColor(Color.parseColor("#9E9FA0"));
//            data.setValueIconTextColor(Color.GRAY);
            data.setValueIconTextSize(30);
            data.setValueIconTypeface(tf);
            chart.setLineChartData(data);
        }

        private Line genTimeLine(){
            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < numberOfPoints; ++j) {
                String labelText = j + ":00";
                values.add(new PointValue(j, 21).setLabel(labelText));
            }

            Line line = new Line(values);
            line.setColor(Color.TRANSPARENT);//ChartUtils.COLORS[i]
            line.setShape(shape);
            line.setCubic(false);
            line.setFilled(false);
            line.setHasLabels(true);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(false);
            line.setHasPoints(true);
//            if (pointsHaveDifferentColor){
//                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
//            }
            line.setPointColor(Color.TRANSPARENT);
            line.setPointRadius(4);
            line.setAreaTransparency(230);
            line.setStrokeWidth(4);
            return line;
        }
        /**
         * Adds lines to data, after that data should be set again with
         * {@link LineChartView#setLineChartData(LineChartData)}. Last 4th line has non-monotonically x values.
         */
        private void addLineToData() {
            if (data.getLines().size() >= maxNumberOfLines) {
                Toast.makeText(getActivity(), "Samples app uses max 4 lines!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                ++numberOfLines;
            }

            generateData();
        }

        private void toggleLines() {
            hasLines = !hasLines;

            generateData();
        }

        private void togglePoints() {
            hasPoints = !hasPoints;

            generateData();
        }

        private void toggleCubic() {
            isCubic = !isCubic;

            generateData();

            if (isCubic) {
                // It is good idea to manually set a little higher max viewport for cubic lines because sometimes line
                // go above or below max/min. To do that use Viewport.inest() method and pass negative value as dy
                // parameter or just set top and bottom values manually.
                // In this example I know that Y values are within (0,100) range so I set viewport height range manually
                // to (-5, 105).
                // To make this works during animations you should use Chart.setViewportCalculationEnabled(false) before
                // modifying viewport.
                // Remember to set viewport after you call setLineChartData().
                final Viewport v = new Viewport(chart.getMaximumViewport());
                v.bottom = -5;
                v.top = 105;
                // You have to set max and current viewports separately.
                chart.setMaximumViewport(v);
                // I changing current viewport with animation in this case.
                chart.setCurrentViewportWithAnimation(v);
            } else {
                // If not cubic restore viewport to (0,100) range.
                final Viewport v = new Viewport(chart.getMaximumViewport());
                v.bottom = 0;
                v.top = 100;

                // You have to set max and current viewports separately.
                // In this case, if I want animation I have to set current viewport first and use animation listener.
                // Max viewport will be set in onAnimationFinished method.
                chart.setViewportAnimationListener(new ChartAnimationListener() {

                    @Override
                    public void onAnimationStarted() {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationFinished() {
                        // Set max viewpirt and remove listener.
                        chart.setMaximumViewport(v);
                        chart.setViewportAnimationListener(null);

                    }
                });
                // Set current viewpirt with animation;
                chart.setCurrentViewportWithAnimation(v);
            }

        }

        private void toggleFilled() {
            isFilled = !isFilled;

            generateData();
        }

        private void togglePointColor() {
            pointsHaveDifferentColor = !pointsHaveDifferentColor;

            generateData();
        }

        private void setCircles() {
            shape = ValueShape.CIRCLE;

            generateData();
        }

        private void setSquares() {
            shape = ValueShape.SQUARE;

            generateData();
        }

        private void setDiamonds() {
            shape = ValueShape.DIAMOND;

            generateData();
        }

        private void toggleLabels() {
            hasLabels = !hasLabels;

            if (hasLabels) {
                hasLabelForSelected = false;
                chart.setValueSelectionEnabled(hasLabelForSelected);
            }

            generateData();
        }

        private void toggleLabelForSelected() {
            hasLabelForSelected = !hasLabelForSelected;

            chart.setValueSelectionEnabled(hasLabelForSelected);

            if (hasLabelForSelected) {
                hasLabels = false;
            }

            generateData();
        }

        private void toggleAxes() {
            hasAxes = !hasAxes;

            generateData();
        }

        private void toggleAxesNames() {
            hasAxesNames = !hasAxesNames;

            generateData();
        }

        /**
         * To animate values you have to change targets values and then call {@link Chart#startDataAnimation()}
         * method(don't confuse with View.animate()). If you operate on data that was set before you don't have to call
         * {@link LineChartView#setLineChartData(LineChartData)} again.
         */
        private void prepareDataAnimation() {
            for (Line line : data.getLines()) {
                for (PointValue value : line.getValues()) {
                    // Here I modify target only for Y values but it is OK to modify X targets as well.
                    value.setTarget(value.getX(), (float) Math.random() * 100);
                }
            }
        }

        private class ValueTouchListener implements LineChartOnValueSelectListener {

            @Override
            public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
                Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {
                // TODO Auto-generated method stub

            }

        }
    }
}
