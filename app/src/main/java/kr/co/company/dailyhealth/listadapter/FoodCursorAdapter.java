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

import static kr.co.company.dailyhealth.FoodListActivity.KEY_FCAL;
import static kr.co.company.dailyhealth.FoodListActivity.KEY_FOOD_NAME;

/**
 * Created by Kimmyeongjun on 2016. 12. 5..
 */

public class FoodCursorAdapter extends CursorAdapter {

    @SuppressWarnings("deprecation")
    public FoodCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }



    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvName = (TextView) view.findViewById(R.id.tv_fname);
        TextView tvCal = (TextView) view.findViewById(R.id.tv_fcal);

        String name = cursor.getString(cursor.getColumnIndex(KEY_FOOD_NAME));
        String cal = cursor.getString(cursor.getColumnIndex(KEY_FCAL));

        Log.d("스트링 확인", name + ", " + cal);

        tvName.setText(name);
        tvCal.setText(cal);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.foodlist_item, parent, false);
        return v;
    }

}