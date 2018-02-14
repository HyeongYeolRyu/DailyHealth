package kr.co.company.dailyhealth;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static kr.co.company.dailyhealth.R.id.chart_sleep;

public class SleepbioFragment extends Fragment
    {
        DBHelper mHelper;
        SQLiteDatabase db;
        Cursor cursor;
        Cursor cursor2;

        ///현재시간 가져오기
        long now = System.currentTimeMillis();
        // Data 객체에 시간을 저장한다.
        Date date = new Date(now);
        // yyyy/MM/dd HH:mm:ss
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMdd");
        String time = sdfNow.format(new Date(System.currentTimeMillis()));

        public static SleepbioFragment newInstance()
        {
            SleepbioFragment fragment = new SleepbioFragment();
            return fragment;
        }

        public SleepbioFragment()
        {
            // Required empty public constructor
        }

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {


            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_sleepbio,container,false);


            String sleepquery = String.format(
                    "select DATA from SLEEP_DATA where date between ('%s'-4) and '%s'",time, time);


            float sleep_data = 0f;


            mHelper = new DBHelper(getActivity());
            db = mHelper.getWritableDatabase();

            LineChart lineChart = (LineChart)view.findViewById(chart_sleep);

            ArrayList<Entry> entries = new ArrayList<>();

            cursor = db.rawQuery(sleepquery, null);
            if(cursor.moveToFirst()) {
                String COUNT = String.valueOf(cursor.getCount());
                Log.d("스트링 확인", COUNT);
                if(cursor.getCount() > 4)
                {
                    for(int i = 0; i<5; i++)
                    {
                        sleep_data = cursor.getInt(cursor.getColumnIndex("DATA"));
                        entries.add(new Entry(sleep_data, i));
                        cursor.moveToNext();
                    }
                }
                else
                {
                    for(int i = 0; i<5; i++)
                    {
                        if(i<5-cursor.getCount()) {
                            entries.add(new Entry(0, i));
                        }
                        else {
                            sleep_data = cursor.getInt(cursor.getColumnIndex("DATA"));
                            entries.add(new Entry(sleep_data, i));
                            cursor.moveToNext();
                        }
                    }
                }
            }
            else {

            }

            LineDataSet dataset = new LineDataSet(entries, "");

            ArrayList<String> labels = new ArrayList<String>();
            labels.add("");
            labels.add("");
            labels.add("");
            labels.add("");
            labels.add("Today");



            LineData data = new LineData(labels, dataset);

            dataset.setDrawCubic(true);
            dataset.setDrawFilled(true);


            lineChart.setDescription("");

            YAxis rightAxis = lineChart.getAxisRight();
            rightAxis.setEnabled(false);

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            lineChart.setData(data);
            lineChart.animateY(2500);


            Button addsleepbtn = (Button) view.findViewById(R.id.addSleepBtn);
            addsleepbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getActivity(), InputSleepActivity.class);
                    startActivity(intent);
                }
            });


            //종합데이터 가져오기
            String sleepselect = String.format(
                    "select DATE, DATA FROM SLEEP_DATA where date between ('%s'-4) and '%s';",time, time);


            String[] textData = new String[5];

            cursor2 = db.rawQuery(sleepselect, null);

            if(cursor2.moveToFirst()) {

                if(cursor2.getCount() > 4)
                {
                    for(int i = 0; i<5; i++)
                    {
                        String date = cursor2.getString(cursor2.getColumnIndex("DATE"));
                        String fdate = String.format("       %s-%s-%s",date.substring(0,4), date.substring(4, 6), date.substring(6,8));
                        Integer count_t = cursor2.getInt(cursor2.getColumnIndex("DATA"));
                        Integer count_h = count_t / 60;
                        Integer count_m = count_t % 60;
                        if(count_t >= 60)
                        {
                            String count = String.format("%d시간 %d분", count_h, count_m );
                            textData[i] = fdate +"                       " +count;
                        }
                        else
                        {
                            String count = String.format("%d분", count_m );
                            textData[i] = fdate +"                       " +count;
                        }
                        cursor2.moveToNext();
                    }
                }
                else {
                    for (int i = 0; i < 5; i++) {
                        if (i < 5 - cursor2.getCount()) {
                            textData[i] = "";
                        } else {
                            String date = cursor2.getString(cursor2.getColumnIndex("DATE"));
                            String fdate = String.format("       %s-%s-%s",date.substring(0,4), date.substring(4, 6), date.substring(6,8));
                            Integer count_t = cursor2.getInt(cursor2.getColumnIndex("DATA"));
                            Integer count_h = count_t / 60;
                            Integer count_m = count_t % 60;
                            if (count_t >= 60) {
                                String count = String.format("%d시간 %d분", count_h, count_m);
                                textData[i] = fdate + "                       " + count;
                            } else {
                                String count = String.format("%d분", count_m);
                                textData[i] = fdate + "                       " + count;
                            }
                            cursor2.moveToNext();
                        }
                    }
                }
            }
            else{

            }

            TextView tvSleep1 = (TextView) view.findViewById(R.id.sleep_data1);
            TextView tvSleep2 = (TextView) view.findViewById(R.id.sleep_data2);
            TextView tvSleep3 = (TextView) view.findViewById(R.id.sleep_data3);
            TextView tvSleep4 = (TextView) view.findViewById(R.id.sleep_data4);
            TextView tvSleep5 = (TextView) view.findViewById(R.id.sleep_data5);

            tvSleep1.setText(textData[4]);
            tvSleep2.setText(textData[3]);
            tvSleep3.setText(textData[2]);
            tvSleep4.setText(textData[1]);
            tvSleep5.setText(textData[0]);

            db.close();

            return view;
        }


    }