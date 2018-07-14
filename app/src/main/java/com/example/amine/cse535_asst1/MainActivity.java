package com.example.amine.cse535_asst1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "MainActivity";
    @BindView(R.id.et_patient_name)
    EditText et_patient_name;

    @BindView(R.id.et_age)
    EditText et_age;

    @BindView(R.id.et_patient_id)
    EditText et_patient_id;

    @BindView(R.id.bt_run)
    Button bt_run;

    @BindView(R.id.bt_stop)
    Button bt_stop;

    @BindView(R.id.rg_sex)
    RadioGroup rg_sex;

    @BindView(R.id.rb_male)
    RadioButton rb_male;

    @BindView(R.id.rb_female)
    RadioButton rb_female;

    @BindView(R.id.gv_graph)
    GraphView graph;

    //ArrayList<DataPoint> dataPoints;

    //random to generate the random data points
    final Random random = new Random();
    //x is the x axis here refers to time
    double x = 0;
    //handler to schedule an update every 1 second.
    final Handler handler = new Handler();
    //reference to the runnable that we are running every second
    Runnable run_every_interval;
    //if the heart rate monitor is running
    boolean running = false;
    //update time
    long time = 1000;
    //number of points displayed on graph
    int number_of_points = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //dataPoints = new ArrayList<DataPoint>();
        //graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        //graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling

        graph.setTitle("HeartBeat Monitor");
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(20);

        graph.getViewport().setScrollable(true);

    }
    @OnClick(R.id.bt_run)
    public void run() {
        if(et_age.getText().toString().isEmpty() || et_patient_id.getText().toString().isEmpty() ||
                et_patient_name.getText().toString().isEmpty()) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Patient Information Not available");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else{
        //if(!running) {
            Log.d(TAG, "button run");
            final LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                    new DataPoint(x, 0),

            });
            x = x + (double)(time)/1000;
            graph.addSeries(series);
            run_every_interval = new Runnable() {
                @Override
                public void run() {

                    DataPoint data = new DataPoint(x, random.nextDouble());
                    //dataPoints.add(data); //saving the data point in an array
                        series.appendData(data, true, number_of_points);
                    x = x + (double)time/1000.0;
                    handler.postDelayed(this, time);

                }
            };
            //run after time
            running = true;
            handler.postDelayed(run_every_interval, time);
        }

    }

    @OnClick(R.id.bt_stop)
    public void stop() {
        Log.d(TAG,"button stop");
        //x = 0;
        graph.removeAllSeries(); //clear graph
        //dataPoints.clear(); //clear array
        if(run_every_interval !=null) {
            handler.removeCallbacks(run_every_interval);
            running = false;
        }
    }
}
