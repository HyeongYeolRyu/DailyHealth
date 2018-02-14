package kr.co.company.dailyhealth;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import static android.content.Context.SENSOR_SERVICE;

public class PedometerFragment extends Fragment {

    DBHelper mHelper;
    SQLiteDatabase db;
    Cursor cursor;
    String query;

    Fragment frag = this;

    ///만보기
    SensorManager sensorManager;
    float previousY, currentY;
    int threshold;

    TextView stepsTextView;
    TextView meterTextView;
    TextView kcalTextView;
    ImageView img;

    static int steps = 0;
    static double meter = 0;
    static double kcal = 0;

    public static PedometerFragment newInstance() {
        PedometerFragment fragment = new PedometerFragment();
        return fragment;
    }

    public PedometerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_pedometer, container, false);

        stepsTextView = (TextView) view.findViewById(R.id.pedoStep);
        meterTextView = (TextView) view.findViewById(R.id.pedom);

        img = (ImageView) view.findViewById(R.id.pedoImage);
        img.setBackgroundResource(R.drawable.walk);

        ///////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////만보기///////////////////////////////////////

        Button pedoBtn = (Button) view.findViewById(R.id.pedoBtn);
        pedoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHelper = new DBHelper(getActivity());
                db = mHelper.getWritableDatabase();
                query = String.format("UPDATE STEP_DATA SET DATA = %d;", steps);

                db.execSQL(query);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(frag).attach(frag).commit();
                db.close();
            }
        });


        threshold = 10;

        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(stepDetector, sensorManager.
                getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);


        ///////////////////////////////////////////////////////////////////////////////////

        //스텝데이터 가져오기
        String query = String.format(
                "select * FROM STEP_DATA");

        mHelper = new DBHelper(getActivity());
        db = mHelper.getWritableDatabase();

        cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            steps = cursor.getInt(cursor.getColumnIndex("DATA"));
        }
        else {

        }


        meter = steps * 0.75;

        String strMeter = String.format("%.2f", meter);

        stepsTextView.setText(steps + " 걸음");
        meterTextView.setText(strMeter + " m");

        return view;
    }

    private SensorEventListener stepDetector = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {

            float y = event.values[1];

            currentY = y;

            int tmp;

            tmp = steps;

            if (Math.abs(currentY - previousY) > threshold) {
                steps++;
            }

            previousY = y;


        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}