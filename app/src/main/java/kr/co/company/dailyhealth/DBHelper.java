package kr.co.company.dailyhealth;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static kr.co.company.dailyhealth.ExerciseListActivity.EXER_TABLE_NAME;
import static kr.co.company.dailyhealth.ExerciseListActivity.KEY_ECAL;
import static kr.co.company.dailyhealth.ExerciseListActivity.KEY_EXER_NAME;
import static kr.co.company.dailyhealth.FoodListActivity.FOOD_TABLE_NAME;
import static kr.co.company.dailyhealth.FoodListActivity.KEY_FCAL;
import static kr.co.company.dailyhealth.FoodListActivity.KEY_FOOD_NAME;

/**
 * Created by Kimmyeongjun on 2016. 12. 5..
 */
class DBHelper extends SQLiteOpenHelper
{
    public static final String ROOT_DIR = "/data/data/kr.co.company.dailyhealth/databases/";  //로컬db 저장
    private static final String DATABASE_NAME = "Dailyhealth.db"; //로컬db명
    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 2);
        //setDB(context);
    }

    public void onCreate(SQLiteDatabase db)
    {
        //CREATE TABLE FOOD_LIST( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price INTEGER)
        // AUTOINCREMENT 속성 사용 시 PRIMARY KEY로 지정한다.
        String flquery = String.format( "CREATE TABLE %s ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "%s TEXT, "
                + "TYPE TEXT, "
                + "%s INTEGER );", FOOD_TABLE_NAME, KEY_FOOD_NAME, KEY_FCAL );
        db.execSQL( flquery );

        String elquery = String.format( "CREATE TABLE %s ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "%s TEXT, "
                + "TYPE TEXT, "
                + "%s INTEGER );", EXER_TABLE_NAME, KEY_EXER_NAME, KEY_ECAL );
        db.execSQL( elquery );

        String tfquery = String.format( "CREATE TABLE %s ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "DATE NUMERIC, "
                + "NAME TEXT, "
                + "CAL INTEGER, "
                + "COUNT INTEGER, "
                + "TOTAL INTEGER ); ", "TODAY_FOOD");
        db.execSQL( tfquery );
        String tefquery = String.format( "CREATE TABLE %s ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "DATE NUMERIC, "
                + "NAME TEXT, "
                + "CAL INTEGER, "
                + "COUNT INTEGER, "
                + "TOTAL INTEGER );", "TODAY_FOOD_TEST");
        db.execSQL( tefquery );

        String tequery = String.format( "CREATE TABLE %s ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "DATE NUMERIC, "
                + "NAME TEXT, "
                + "CAL INTEGER, "
                + "COUNT INTEGER, "
                + "TOTAL INTEGER ); ", "TODAY_EXER");
        db.execSQL( tequery );

        String mydataquery = String.format( "CREATE TABLE MY_DATA (" +
                " 'NAME' TEXT," +
                " 'AGE' INTEGER," +
                " 'BIRTH' TEXT," +
                " 'HEIGHT' INTEGER," +
                " 'WEIGHT' INTEGER," +
                " 'BMI' INTEGER ); ");
        db.execSQL( mydataquery );


        String todaydataquery = String.format( "CREATE TABLE TODAY_DATA (" +
                " 'DATE' NUMERIC," +
                " 'FOOD_TOTAL_CAL' INTEGER," +
                " 'EXER_TOTAL_CAL' INTEGER," +
                " PRIMARY KEY('DATE') )");
        db.execSQL( todaydataquery );


        String stepdata = String.format(
                "CREATE TABLE 'STEP_DATA' ('DATE' NUMERIC, 'DATA' INTEGER )");
        db.execSQL( stepdata );

        String sleepdata = String.format(
                "CREATE TABLE 'SLEEP_DATA' ( 'DATE' NUMERIC, 'DATA' INTEGER, PRIMARY KEY('DATE') )");
        db.execSQL( sleepdata );

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {    }

    public static void setDB(Context ctx) {
        File folder = new File(ROOT_DIR);
        if(folder.exists()) {
        } else {
            folder.mkdirs();
        }
        AssetManager assetManager = ctx.getResources().getAssets(); //ctx가 없으면 assets폴더를 찾지 못한다.
        File outfile = new File(ROOT_DIR+"Dailyhealth.db");
        InputStream is = null;
        FileOutputStream fo = null;
        long filesize = 0;
        try {
            is = assetManager.open("Dailyhealth.db");
            filesize = is.available();
            if (outfile.length() <= 0) {
                byte[] tempdata = new byte[(int) filesize];
                is.read(tempdata);
                is.close();
                outfile.createNewFile();
                fo = new FileOutputStream(outfile);
                fo.write(tempdata);
                fo.close();
            } else {}
        } catch (IOException e) {}
    }

}