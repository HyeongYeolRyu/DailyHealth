package kr.co.company.dailyhealth;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import static kr.co.company.dailyhealth.MainActivity.myage;
import static kr.co.company.dailyhealth.MainActivity.mybirth;
import static kr.co.company.dailyhealth.MainActivity.myheight;
import static kr.co.company.dailyhealth.MainActivity.myname;
import static kr.co.company.dailyhealth.MainActivity.mystandardcal;
import static kr.co.company.dailyhealth.MainActivity.myweight;


public class SettingActivity extends AppCompatActivity {

    DBHelper mHelper;
    SQLiteDatabase db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mHelper = new DBHelper(this);
        db = mHelper.getWritableDatabase();

        EditText name = (EditText) findViewById(R.id.Name);
        EditText Age = (EditText) findViewById(R.id.Age);
        EditText Birth = (EditText) findViewById(R.id.Birth);
        EditText Height = (EditText) findViewById(R.id.Height);
        EditText Weight = (EditText) findViewById(R.id.Weight);
        TextView Standardcal = (TextView) findViewById(R.id.Standardcal);

        name.setText(myname);
        Age.setText(myage);
        Birth.setText(mybirth);
        Height.setText(myheight);
        Weight.setText(myweight);
        Standardcal.setText(mystandardcal+" kcal");


        changeState(false);

    }

    public void onToggleClicked(View view) {
// Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();
        if (on) {

            EditText name = (EditText) findViewById(R.id.Name);
            EditText Age = (EditText) findViewById(R.id.Age);
            EditText Birth = (EditText) findViewById(R.id.Birth);
            EditText Height = (EditText) findViewById(R.id.Height);
            EditText Weight = (EditText) findViewById(R.id.Weight);

            changeState(true);

        } else {


            EditText name = (EditText) findViewById(R.id.Name);
            EditText Age = (EditText) findViewById(R.id.Age);
            EditText Birth = (EditText) findViewById(R.id.Birth);
            EditText Height = (EditText) findViewById(R.id.Height);
            EditText Weight = (EditText) findViewById(R.id.Weight);
            TextView Standardcal = (TextView) findViewById(R.id.Standardcal);

            changeState(false);

            myname = String.format("%s" ,name.getText());
            myage = String.format("%s" ,Age.getText());
            mybirth = String.format("%s" ,Birth.getText());
            myheight = String.format("%s" ,Height.getText());
            myweight = String.format("%s" ,Weight.getText());
            mystandardcal = String.format("%.2f" , (Float.parseFloat(myheight)-100)*0.9*30);
//
//            Float mystandardc = (Float.parseFloat(myheight)-100)*0.9*30;

            String query = String.format("UPDATE MY_DATA SET NAME = '%s'," +
                    "AGE = %s," +
                    "BIRTH = '%s'," +
                    "HEIGHT = %s," +
                    "WEIGHT = %s," +
                    "STANDARD_CAL = %s", myname, myage, mybirth, myheight, myweight, mystandardcal);


            db.execSQL( query );

        }
    }

    public void changeState(boolean bool){
        EditText name = (EditText) findViewById(R.id.Name);
        EditText Age = (EditText) findViewById(R.id.Age);
        EditText Birth = (EditText) findViewById(R.id.Birth);
        EditText Height = (EditText) findViewById(R.id.Height);
        EditText Weight = (EditText) findViewById(R.id.Weight);

        name.setFocusableInTouchMode(bool);
        Age.setFocusableInTouchMode(bool);
        Birth.setFocusableInTouchMode(bool);
        Height.setFocusableInTouchMode(bool);
        Weight.setFocusableInTouchMode(bool);

        name.setFocusable(bool);
        Age.setFocusable(bool);
        Birth.setFocusable(bool);
        Height.setFocusable(bool);
        Weight.setFocusable(bool);
    }

}