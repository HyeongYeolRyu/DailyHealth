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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import kr.co.company.dailyhealth.listadapter.FoodCursorAdapter;

public class FoodListActivity extends Activity {
    DBHelper mHelper;
    SQLiteDatabase db;
    Cursor cursor;
    FoodCursorAdapter foodAdapter;

    public final static String KEY_ID = "_id";
    public final static String KEY_FOOD_NAME = "NAME";
    public final static String KEY_FCAL = "CAL";
    public final static String FOOD_TABLE_NAME = "FOOD_TABLE";

    final static String querySelectAll = String.format("SELECT * FROM %s", FOOD_TABLE_NAME);
    String querySelectType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodlist);

        Intent intent = getIntent();
        Bundle bundleData = intent.getBundleExtra("TYPE_DATA");

        if(bundleData == null) {

        }

        String FOOD_TYPE = bundleData.getString("FOOD_TYPE");

        Log.d("스트링 확인--", FOOD_TYPE);
        querySelectType = String.format("SELECT * FROM %s WHERE type = '%s'", FOOD_TABLE_NAME, FOOD_TYPE);

        ListView list = (ListView) findViewById(R.id.lv_food);

        mHelper = new DBHelper(this);
        db = mHelper.getWritableDatabase();

//        cursor = db.rawQuery(querySelectAll, null);

        cursor = db.rawQuery(querySelectType, null);

        foodAdapter = new FoodCursorAdapter(this, cursor);

        list.setAdapter(foodAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                cursor.moveToPosition(position);
                String foodname = cursor.getString(cursor.getColumnIndex(KEY_FOOD_NAME));
                String foodcal = cursor.getString(cursor.getColumnIndex(KEY_FCAL));

                Intent _intent = new Intent();
                ComponentName componentName = new ComponentName (
                        "kr.co.company.dailyhealth",
                        "kr.co.company.dailyhealth.InputFoodActivity");
                _intent.setComponent(componentName);

                Bundle _bundleData = new Bundle();
                _bundleData.putString("FOOD_NAME", foodname);
                _bundleData.putString("FOOD_CAL", foodcal);

                _intent.putExtra("INPUT_DATA", _bundleData);
                startActivity(_intent);

                //Toast.makeText(getApplicationContext(),foodname,Toast.LENGTH_SHORT).show();
            }
        });

    }



//    public void mOnClick( View v )
//    {
//        EditText eName = (EditText) findViewById( R.id.et_name );
//        EditText eAge = (EditText) findViewById( R.id.et_age );
//
//        String name = eName.getText().toString();
//        try
//        {
//            int age = Integer.parseInt( eAge.getText().toString() );
//
//            // 문자열은 ''로 감싸야 한다.
//            String query = String.format(
//                    "INSERT INTO %s VALUES ( null, '%s', %d );", TABLE_NAME, name, age );
//            db.execSQL( query );
//
//            // 아래 메서드를 실행하면 리스트가 갱신된다. 하지만 구글은 이 메서드를 deprecate한다. 고로 다른 방법으로 해보자.
//            // cursor.requery();
//            cursor = db.rawQuery( querySelectAll, null );
//            foodAdapter.changeCursor( cursor );
//        }
//        catch( NumberFormatException e )
//        {
//            Toast.makeText( this, "나이는 정수를 입력해야 합니다", Toast.LENGTH_LONG).show();
//        }
//
//        eName.setText( "" );
//        eAge.setText( "" );
//
//        // 저장 버튼 누른 후 키보드 안보이게 하기
//        InputMethodManager imm =
//                (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE );
//        imm.hideSoftInputFromWindow( eAge.getWindowToken(), 0 );
//    }
}
