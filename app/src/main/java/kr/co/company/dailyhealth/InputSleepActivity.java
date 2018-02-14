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
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import kr.co.company.dailyhealth.listadapter.FoodCursorAdapter;

public class InputSleepActivity extends Activity {
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



    EditText et_SYear;
    EditText et_SMonth;
    EditText et_SDate;
    EditText et_SData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputsleep);

        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        int width = (int) (display.getWidth() * 0.8); //Display 사이즈의 70%

        int height = (int) (display.getHeight() * 0.5);  //Display 사이즈의 90%

        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

        et_SYear = (EditText) findViewById(R.id.et_sleep_year);
        et_SMonth = (EditText) findViewById(R.id.et_sleep_month);
        et_SDate = (EditText) findViewById(R.id.et_sleep_date);


        Log.d("문자열체크", time);
        et_SYear.setText(time.substring(0, 4));
        et_SMonth.setText(time.substring(4, 6));
        et_SDate.setText(time.substring(6, 8));
    }

    public void inputSleepData( View v )
    {
        et_SData = (EditText) findViewById( R.id.et_sleep_data );


        try
        {
            int sleepData = Integer.parseInt( et_SData.getText().toString() );

            String sleepDate = String.format("%s%s%s",et_SYear.getText().toString(),et_SMonth.getText().toString(),et_SDate.getText().toString());



            Log.d("문자열체크", sleepDate);

            mHelper = new DBHelper(this);

            String select = String.format("select DATE from SLEEP_DATA where DATE = '%s'", sleepDate);
            String sleepdataquery = "";

            db = mHelper.getWritableDatabase();

            cursor = db.rawQuery(select, null);

            if(cursor.moveToFirst())
            {
                sleepdataquery = String.format("UPDATE SLEEP_DATA SET DATA = '%s' WHERE DATE = '%s';", sleepData, sleepDate);

            }else
            {
                try {
                    sleepdataquery = String.format("INSERT INTO SLEEP_DATA VALUES (%s, %d );", sleepDate, sleepData);
                } catch (Exception e) {
                }
            }
            db.execSQL(sleepdataquery);
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
