package kr.co.company.dailyhealth.listadapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import kr.co.company.dailyhealth.R;

/**
 * Created by Kimmyeongjun on 2016. 12. 5..
 */

public class TodayExerCursorAdapter extends CursorAdapter {

    @SuppressWarnings("deprecation")
    public TodayExerCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvName = (TextView) view.findViewById(R.id.tv_ename);
        TextView tvCount = (TextView) view.findViewById(R.id.tv_ecount);
        TextView tvCal = (TextView) view.findViewById(R.id.tv_ecal);

        String name = cursor.getString(cursor.getColumnIndex("NAME"));
        Integer count_t = cursor.getInt(cursor.getColumnIndex("COUNT"));
        Integer count_h = count_t / 60;
        Integer count_m = count_t % 60;
        if(count_t >= 60)
        {
            String count = String.format("%d시간 %d분", count_h, count_m );
            tvCount.setText(count);
        }
        else
        {
            String count = String.format("%d분", count_m );
            tvCount.setText(count);
        }

        String total = cursor.getString(cursor.getColumnIndex("TOTAL"));

        Log.d("스트링 확인", name + ", " + total);

        tvName.setText(name);
        tvCal.setText(total);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.exerciselist_item, parent, false);
        return v;
    }

}