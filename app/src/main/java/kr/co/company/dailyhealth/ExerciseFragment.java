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
import android.widget.ListView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.co.company.dailyhealth.listadapter.TodayExerCursorAdapter;

import static kr.co.company.dailyhealth.R.id.exer_chart;

public class ExerciseFragment extends Fragment
{

    DBHelper mHelper;
    SQLiteDatabase db;
    Cursor cursor;
    Cursor cursor2;
    TodayExerCursorAdapter todayexerAdapter;

    ///현재시간 가져오기
    long now = System.currentTimeMillis();
    // Data 객체에 시간을 저장한다.
    Date date = new Date(now);
    // yyyy/MM/dd HH:mm:ss
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMdd");
    String time = sdfNow.format(new Date(System.currentTimeMillis()));
    SimpleDateFormat _sdfNow = new SimpleDateFormat("yyyy-MM-dd");
    String _time = _sdfNow.format(new Date(System.currentTimeMillis()));

    public static ExerciseFragment newInstance()
    {
        ExerciseFragment fragment = new ExerciseFragment();
        return fragment;
    }

    public ExerciseFragment()
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
        View view = inflater.inflate(R.layout.fragment_exercise,container,false);

        String exerquery = String.format(
                "select EXER_TOTAL_CAL FROM TODAY_DATA where date between ('%s'-4) and '%s'",time, time);


        float exer_total = 0f;


        mHelper = new DBHelper(getActivity());
        db = mHelper.getWritableDatabase();


        // 그래프2 ___EXER_TOTAL_CAL
        LineChart lineChart2 = (LineChart)view.findViewById(exer_chart);

        ArrayList<Entry> entries2 = new ArrayList<>();

        cursor2 = db.rawQuery(exerquery, null);
        if(cursor2.moveToFirst()) {
            String COUNT = String.valueOf(cursor2.getCount());
            Log.d("스트링 확인", COUNT);
            if(cursor2.getCount() > 4)
            {
                for(int i = 0; i<5; i++)
                {
                    exer_total = cursor2.getInt(cursor2.getColumnIndex("EXER_TOTAL_CAL"));
                    entries2.add(new Entry(exer_total, i));
                    cursor2.moveToNext();
                }
            }
            else
            {
                for(int i = 0; i<5; i++)
                {
                    if(i<5-cursor2.getCount()) {
                        entries2.add(new Entry(0, i));
                    }
                    else {
                        exer_total = cursor2.getInt(cursor2.getColumnIndex("EXER_TOTAL_CAL"));
                        entries2.add(new Entry(exer_total, i));
                        cursor2.moveToNext();
                    }
                }
            }
        }
        else {

        }

        LineDataSet dataset2 = new LineDataSet(entries2, "");

        ArrayList<String> labels2 = new ArrayList<String>();
        labels2.add("");
        labels2.add("");
        labels2.add("");
        labels2.add("");
        labels2.add("Today");

        LineData data2 = new LineData(labels2, dataset2);

        dataset2.setDrawCubic(true);
        dataset2.setDrawFilled(true);

        lineChart2.setDescription("");

        YAxis rightAxis = lineChart2.getAxisRight();
        rightAxis.setEnabled(false);

        XAxis xAxis2 = lineChart2.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);

        lineChart2.setData(data2);
        lineChart2.animateY(2500);
        ///////////////////////////////////////////////////////////////////////////////////////



        Button addexercisebtn = (Button) view.findViewById(R.id.addexercisebtn);
        addexercisebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ExerciseListActivity.class);
                startActivity(intent);
            }
        });

        String querySelectType = String.format(
                "select * FROM TODAY_EXER where date like '%s" +
                        "%s'", _time,"%");

        ListView list = (ListView) view.findViewById(R.id.lv_texer);

//        mHelper = new DBHelper(getActivity());
//        db = mHelper.getWritableDatabase();

//        cursor = db.rawQuery(querySelectAll, null);

        cursor = db.rawQuery(querySelectType, null);

        todayexerAdapter = new TodayExerCursorAdapter(getActivity(), cursor);

        list.setAdapter(todayexerAdapter);


        db.close();
        return view;

    }
}