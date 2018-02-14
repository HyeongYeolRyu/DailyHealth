package kr.co.company.dailyhealth;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static kr.co.company.dailyhealth.R.id.chart_exercise;
import static kr.co.company.dailyhealth.R.id.chart_intake;

public class StatisticsFragment extends Fragment{

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

    public static StatisticsFragment newInstance()
    {
        StatisticsFragment fragment = new StatisticsFragment();
        return fragment;
    }

    public StatisticsFragment()
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
        View view = inflater.inflate(R.layout.fragment_statistics,container,false);


        //종합데이터 가져오기
        String foodquery = String.format(
                "select FOOD_TOTAL_CAL FROM TODAY_DATA where date between ('%s'-4) and '%s'",time, time);
        String exerquery = String.format(
                "select EXER_TOTAL_CAL FROM TODAY_DATA where date between ('%s'-4) and '%s'",time, time);


        float food_total = 0f;
        float exer_total = 0f;

        float fTotalSum = 0f;
        float eTotalSum = 0f;
//일일평균량 변수 선언
        float averFTotal;
        float averETotal;



        mHelper = new DBHelper(getActivity());
        db = mHelper.getWritableDatabase();

//        cursor = db.rawQuery(querySelectAll, null);




        ///////////////////////////////////////////////////////////////////////////////////////
        // 그래프1__FOOD_TOTAL_CAL
        LineChart lineChart = (LineChart)view.findViewById(chart_intake);

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
                fTotalSum += food_total;
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
                    fTotalSum += food_total;
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
        averFTotal = fTotalSum / cursor.getCount();





        // 그래프2 ___EXER_TOTAL_CAL
        LineChart lineChart2 = (LineChart)view.findViewById(chart_exercise);

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
                    eTotalSum += exer_total;
                    cursor2.moveToNext();
                }
            }
            else
            {
                for(int i = 0; i<5; i++)
                {
                    if(i<5-cursor2.getCount()) {
//              food_total = cursor.getInt(cursor.getColumnIndex("FOOD_TOTAL_CAL"));
                        entries2.add(new Entry(0, i));
                    }
                    else {
                        exer_total = cursor2.getInt(cursor2.getColumnIndex("EXER_TOTAL_CAL"));
                        entries2.add(new Entry(exer_total, i));
                        eTotalSum += exer_total;
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



        YAxis rightAxis2 = lineChart2.getAxisRight();
        rightAxis2.setEnabled(false);

        XAxis xAxis2 = lineChart2.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);

        lineChart2.setData(data2);
        lineChart2.animateY(2500);
        ///////////////////////////////////////////////////////////////////////////////////////

        averETotal = eTotalSum / cursor2.getCount();


        TextView tvAverFood = (TextView) view.findViewById(R.id.aver_food);
        TextView tvAverExer = (TextView) view.findViewById(R.id.aver_exer);

        tvAverFood.setText("평균 섭취량 : "+String.valueOf(averFTotal));
        tvAverExer.setText("평균 운동량 : "+String.valueOf(averETotal));

        db.close();
        return view;
    }
}