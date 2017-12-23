package com.example.rsdomi.trythreads;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;


public class SecondActivity extends AppCompatActivity {

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        double[] x;
        double[] y;
        int n;
        final String DEBUG_DATA = "DATA";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            x = bundle.getDoubleArray("xrArray");
            y = bundle.getDoubleArray("fxArray");
            n = x.length;

            DataPoint[] values = new DataPoint[n];     //creating an object of type DataPoint[] of size 'n'
            List<String[]> data = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                DataPoint v = null;
                // avoid null pointers
                if ((y != null) && (x != null)) {
                    v = new DataPoint(x[i], y[i]);
                }
                // plotted values
                values[i] = v;
                // write .csv data
                data.add(new String[]{ String.format("%.5f", x[i]) , String.format("%.5f", y[i]) });
            }

            try {
                //String csv = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/fk1d.csv";
                String csv="/storage/3336-3438/fk1d.csv";

                Toast.makeText(getApplicationContext(), "writing to " + csv, Toast.LENGTH_LONG).show();

                CSVWriter writer = new CSVWriter(new FileWriter(csv));
                //
                writer.writeAll(data);
                writer.flush();
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            //
            GraphView graph;
            PointsGraphSeries<DataPoint> series;       //an Object of the PointsGraphSeries for plotting scatter graphs
            graph = findViewById(R.id.graph);

            graph.setTitle("Feynman-Kac 1D Solution");

            graph.getGridLabelRenderer().setVerticalAxisTitle("f(x)");
            graph.getGridLabelRenderer().setHorizontalAxisTitle("x - [1,2]");
            //graph.getLegendRenderer().setVisible(true);
            // optional styles
            graph.setTitleTextSize(30);
            //graph.setTitleColor(Color.BLUE);

            graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(30);
            //graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);

            graph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(30);
            graph.getGridLabelRenderer().setTextSize(30);
            //graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLUE);
            //
            Viewport viewport = graph.getViewport();

            viewport.setYAxisBoundsManual(true);
            viewport.setXAxisBoundsManual(true);

            viewport.setMinX(1.0);
            viewport.setMaxX(2.0);

            viewport.setMinY(1.0);
            viewport.setMaxY(2.0);
            //
            series = new PointsGraphSeries<>(values);   //initializing/defining series to get the data from the method 'data()'
            graph.addSeries(series);
            //adding the series to the GraphView
            series.setShape(PointsGraphSeries.Shape.RECTANGLE);
            series.setSize(5);
        }
    }





}
