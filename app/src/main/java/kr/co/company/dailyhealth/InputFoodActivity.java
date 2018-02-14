package kr.co.company.dailyhealth;

/**
 * Created by Kimmyeongjun on 2016. 12. 6..
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import kr.co.company.dailyhealth.listadapter.FoodCursorAdapter;

public class InputFoodActivity extends Activity {
    DBHelper mHelper;
    SQLiteDatabase db;
    Cursor cursor;
    FoodCursorAdapter foodAdapter;

    ///현재시간 가져오기
    long now = System.currentTimeMillis();
    // Data 객체에 시간을 저장한다.
    Date date = new Date(now);
    // yyyy/MM/dd HH:mm:ss
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMdd");
    String time = sdfNow.format(new Date(System.currentTimeMillis()));
    SimpleDateFormat _sdfNow = new SimpleDateFormat("yyyy-MM-dd");
    String _time = _sdfNow.format(new Date(System.currentTimeMillis()));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputfood);

        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        int width = (int) (display.getWidth() * 0.8); //Display 사이즈의 80%

        int height = (int) (display.getHeight() * 0.5);  //Display 사이즈의 90%

        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

        TextView tvName = (TextView) findViewById(R.id.tv_ifname);
        TextView tvCal = (TextView) findViewById(R.id.tv_ifcal);

        Intent intent = getIntent();
        Bundle bundleData = intent.getBundleExtra("INPUT_DATA");

        if(bundleData == null) {

        }

        String FOOD_NAME = bundleData.getString("FOOD_NAME");
        String FOOD_CAL = bundleData.getString("FOOD_CAL");

        Log.d("스트링 확인", FOOD_NAME + ", " + FOOD_CAL);

        tvName.setText(FOOD_NAME);
        tvCal.setText(FOOD_CAL);



       // querySelectType = String.format("SELECT * FROM %s WHERE type = '%s'", FOOD_TABLE_NAME, FOOD_TYPE);
    }

    public void inputFoodData( View v )
    {
        TextView fName = (TextView) findViewById( R.id.tv_ifname );
        TextView fCal = (TextView) findViewById( R.id.tv_ifcal );
        EditText eCount = (EditText) findViewById( R.id.et_ifcount );

        String name = fName.getText().toString();
        int cal = Integer.parseInt( fCal.getText().toString() );

        try
        {
            int count = Integer.parseInt( eCount.getText().toString() );

            int total = cal * count;
            // 문자열은 ''로 감싸야 한다.
            String query = String.format(
                    "INSERT INTO TODAY_FOOD VALUES (null, datetime('now','+9 hours'), '%s', %d, %d, %d );", name, cal, count, total );

            mHelper = new DBHelper(this);
            db = mHelper.getWritableDatabase();

            db.execSQL( query );

            String totalupdate = String.format("UPDATE TODAY_DATA SET " +
                    "FOOD_TOTAL_CAL = (SELECT SUM(TOTAL) FROM TODAY_FOOD B" +
                    "            WHERE B.DATE like '%s" +
                    "%s') WHERE DATE = '%s';", _time, "%", time);

            db.execSQL(totalupdate);
        }
        catch( NumberFormatException e )
        {
            Toast.makeText( this, "자연수 입력해주세요.", Toast.LENGTH_LONG).show();
        }

        finish();

        Toast.makeText(this, "저장되었습니다", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    public void mOnClick(View v) {
        finish();
        Toast.makeText(this, "취소되었습니다", Toast.LENGTH_SHORT).show();
    }
}
