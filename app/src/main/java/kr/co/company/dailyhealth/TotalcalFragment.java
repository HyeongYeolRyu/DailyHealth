package kr.co.company.dailyhealth;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static kr.co.company.dailyhealth.MainActivity.mystandardcal;


public class TotalcalFragment extends Fragment {

    DBHelper mHelper;
    SQLiteDatabase db;
    Cursor cursor;

    ///현재시간 가져오기
    long now = System.currentTimeMillis();
    // Data 객체에 시간을 저장한다.
    Date date = new Date(now);
    // yyyy/MM/dd HH:mm:ss
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMdd");
    String time = sdfNow.format(new Date(System.currentTimeMillis()));

    public static TotalcalFragment newInstance() {
        TotalcalFragment fragment = new TotalcalFragment();
        return fragment;
    }

    public TotalcalFragment() {
        // Required empty public constructor
          }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_totalcal,container,false);

        //종합데이터 가져오기
        Float food_total=0.0f;
        Float exer_total=0.0f;
        Float standardcal = Float.parseFloat(mystandardcal);

        String query = String.format(
                "select * FROM TODAY_DATA where date = '%s'", time);

        mHelper = new DBHelper(getActivity());
        db = mHelper.getWritableDatabase();

//        cursor = db.rawQuery(querySelectAll, null);

        cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            food_total = cursor.getFloat(cursor.getColumnIndex("FOOD_TOTAL_CAL"));
            exer_total = cursor.getFloat(cursor.getColumnIndex("EXER_TOTAL_CAL"));

        }
        else {

        }


        ///////////////////////////////////////////////////////////////////////////////////////
        // 그래프
        PieChart pieChart = (PieChart)view.findViewById(R.id.totalcalchart);

        ArrayList<Entry> entries = new ArrayList<>();
        if(standardcal > food_total)
            entries.add(new Entry((standardcal-(food_total-exer_total)), 0));
        else
            entries.add(new Entry(-(standardcal-(food_total-exer_total)), 0));
        entries.add(new Entry(food_total-exer_total, 1));
        entries.add(new Entry(exer_total, 2));


        PieDataSet dataset = new PieDataSet(entries, "");

        ArrayList<String> labels = new ArrayList<String>();
        if(food_total == 0 && exer_total == 0) {
            labels.add("권장칼로리");
            labels.add("");
            labels.add("");
                    }
        else
        {
            if (standardcal > food_total)
                labels.add("부족한칼로리");
            else
                labels.add("초과한칼로리");
            labels.add("소모할칼로리");
            labels.add("운동한칼로리");
        }


        dataset.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData data = new PieData(labels, dataset);



        pieChart.setDescription("");

        pieChart.setData(data);
        pieChart.animateY(2500);

        ///////////////////////////////////////////////////////////////////////////////////////

        db.close();
          return view;
    }
}
