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

import kr.co.company.dailyhealth.listadapter.TodayFoodCursorAdapter;

import static kr.co.company.dailyhealth.R.id.health_chart;

public class HealthFragment extends Fragment
{
    DBHelper mHelper;
    SQLiteDatabase db;
    Cursor cursor;
    TodayFoodCursorAdapter todayfoodAdapter;

///현재시간 가져오기
    long now = System.currentTimeMillis();
    // Data 객체에 시간을 저장한다.
    Date date = new Date(now);
    // yyyy/MM/dd HH:mm:ss
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMdd");
    String time = sdfNow.format(new Date(System.currentTimeMillis()));
    SimpleDateFormat _sdfNow = new SimpleDateFormat("yyyy-MM-dd");
    String _time = _sdfNow.format(new Date(System.currentTimeMillis()));

    public static HealthFragment newInstance()
    {
        HealthFragment fragment = new HealthFragment();
        return fragment;
    }

    public HealthFragment()
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
        View view = inflater.inflate(R.layout.fragment_health,container,false);

        //종합데이터 가져오기
        String foodquery = String.format(
                "select FOOD_TOTAL_CAL FROM TODAY_DATA where date between ('%s'-4) and '%s'",time, time);


        float food_total = 0f;


        mHelper = new DBHelper(getActivity());
        db = mHelper.getWritableDatabase();

        ///////////////////////////////////////////////////////////////////////////////////////
        // 그래프1__FOOD_TOTAL_CAL
        LineChart lineChart = (LineChart)view.findViewById(health_chart);

        ArrayList<Entry> entries = new ArrayList<>();

        cursor = db.rawQuery(foodquery, null);
        if(cursor.moveToFirst()) {
            String COUNT = String.valueOf(cursor.getCount());
            Log.d("스트링 확인", COUNT);
            if(cursor.getCount() > 4)
            {
                for(int i = 0; i<5; i++)
                {
                    food_total = cursor.getInt(cursor.getColumnIndex("FOOD_TOTAL_CAL"));
                    entries.add(new Entry(food_total, i));
                    cursor.moveToNext();
                }
            }
            else
            {
                for(int i = 0; i<5; i++)
                {
                    if(i<5-cursor.getCount()) {
//              food_total = cursor.getInt(cursor.getColumnIndex("FOOD_TOTAL_CAL"));
                        entries.add(new Entry(0, i));
                    }
                    else {
                        food_total = cursor.getInt(cursor.getColumnIndex("FOOD_TOTAL_CAL"));
                        entries.add(new Entry(food_total, i));
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
        ///////////////////////////////////////////////////////////////////////////////////////


        ///////////////////////////////////////////////////////////////////////////////////////


        Button addfoodbtn = (Button) view.findViewById(R.id.Addfoodbtn);
        addfoodbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), AddfoodActivity.class);
                startActivity(intent);
            }
        });


        /// select * FROM TODAY_FOOD_TEST where date like "2016-12-08%";

        String querySelectType = String.format(
                "select * FROM TODAY_FOOD where date like '%s" +
                "%s'", _time,"%");

        ListView list = (ListView) view.findViewById(R.id.lv_tfood);
//        cursor = db.rawQuery(querySelectAll, null);

        cursor = db.rawQuery(querySelectType, null);

        todayfoodAdapter = new TodayFoodCursorAdapter(getActivity(), cursor);

        list.setAdapter(todayfoodAdapter);


        db.close();
        return view;
    }


}


