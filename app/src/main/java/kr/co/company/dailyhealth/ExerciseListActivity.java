package kr.co.company.dailyhealth;

/**
 * Created by Kimmyeongjun on 2016. 12. 6..
 */

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import kr.co.company.dailyhealth.listadapter.ExerciseCursorAdapter;

public class ExerciseListActivity extends Activity {
    DBHelper mHelper;
    SQLiteDatabase db;
    Cursor cursor;
    ExerciseCursorAdapter exerAdapter;

    public final static String KEY_ID = "_id";
    public final static String KEY_EXER_NAME = "NAME";
    public final static String KEY_ECAL = "CAL";
    public final static String EXER_TABLE_NAME = "EXER_TABLE";

    final static String querySelectAll = String.format("SELECT * FROM %s", EXER_TABLE_NAME);
//    String querySelectType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exerciselist);

//        querySelectType = String.format("SELECT * FROM %s", EXER_TABLE_NAME);

        ListView list = (ListView) findViewById(R.id.lv_exer);

        mHelper = new DBHelper(this);
        db = mHelper.getWritableDatabase();

//        cursor = db.rawQuery(querySelectAll, null);

        cursor = db.rawQuery(querySelectAll, null);

        exerAdapter = new ExerciseCursorAdapter(this, cursor);

        list.setAdapter(exerAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                cursor.moveToPosition(position);
                String exername = cursor.getString(cursor.getColumnIndex(KEY_EXER_NAME));
                float exercal = cursor.getFloat(cursor.getColumnIndex(KEY_ECAL));

                //Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();

                Intent _intent = new Intent();
                ComponentName componentName = new ComponentName (
                        "kr.co.company.dailyhealth",
                        "kr.co.company.dailyhealth.InputExerActivity");
                _intent.setComponent(componentName);

                Bundle _bundleData = new Bundle();
                _bundleData.putString("EXER_NAME", exername);
                _bundleData.putFloat("EXER_CAL", exercal);

                _intent.putExtra("INPUT_DATA", _bundleData);
                startActivity(_intent);
            }
        });

    }
    public void inputDirectExer(View v){   //한식

        startActivity(new Intent(this, InputDirectExerActivity.class));
    }
}
