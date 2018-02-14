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

import static kr.co.company.dailyhealth.ExerciseListActivity.KEY_ECAL;
import static kr.co.company.dailyhealth.ExerciseListActivity.KEY_EXER_NAME;

/**
 * Created by Kimmyeongjun on 2016. 12. 5..
 */

public class ExerciseCursorAdapter extends CursorAdapter {

    @SuppressWarnings("deprecation")
    public ExerciseCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvName = (TextView) view.findViewById(R.id.tv_ename);
        TextView tvCal = (TextView) view.findViewById(R.id.tv_ecal);

        String name = cursor.getString(cursor.getColumnIndex(KEY_EXER_NAME));
        float cal = cursor.getFloat(cursor.getColumnIndex(KEY_ECAL));

        Log.d("스트링 확인", name + ", " + cal);

        //1시간 소모칼로리
        cal = cal * 60.0f;

        tvName.setText(name);
        tvCal.setText(String.valueOf(cal));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.exerciselist_item2, parent, false);
        return v;
    }

}