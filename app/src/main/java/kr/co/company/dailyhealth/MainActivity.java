package kr.co.company.dailyhealth;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    //db
    DBHelper mHelper;
    SQLiteDatabase db;
    Cursor c = null;
    String query;


    ///현재시간 가져오기
    long now = System.currentTimeMillis();
    // Data 객체에 시간을 저장한다.
    Date date = new Date(now);
    // yyyy/MM/dd HH:mm:ss
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMdd");
    String time = sdfNow.format(new Date(System.currentTimeMillis()));
    SimpleDateFormat _sdfNow = new SimpleDateFormat("yyyy-MM-dd");
    String _time = _sdfNow.format(new Date(System.currentTimeMillis()));



    ////MY_DATA cursor 개인정보
    Cursor mydatac;

    public static String myname = "";
    public static String myage = "";
    public static String mybirth = "";
    public static String myheight = "";
    public static String myweight = "";
    public static String mystandardcal = "";



    HomeFragment fragHome;
    HealthFragment fragHealth;
    ExerciseFragment fragExercise;
    StatisticsFragment fragStatistics;
    SleepbioFragment fragSleepbio;

    PedometerFragment fragPedo;
    TotalcalFragment fragTotalcal;
    BiorhythmFragment fragBio;

    ///

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ////

        try {
            boolean bResult = isCheckDB(this);	// DB가 있는지?
            Log.d("MiniApp", "DB Check="+bResult);
            if(!bResult){	// DB가 없으면 복사
                copyDB(this);
            }else{

            }
        } catch (Exception e) {

        }


        //MY_DATA

        mHelper = new DBHelper(this);
        db = mHelper.getWritableDatabase();

        String mydataquery = String.format("SELECT * FROM MY_DATA;");
        mydatac = db.rawQuery(mydataquery, null);

        if(mydatac.moveToFirst()) {
            myname = mydatac.getString(mydatac.getColumnIndex("NAME"));
            myage = mydatac.getString(mydatac.getColumnIndex("AGE"));
            mybirth = mydatac.getString(mydatac.getColumnIndex("BIRTH"));
            myheight = mydatac.getString(mydatac.getColumnIndex("HEIGHT"));
            myweight = mydatac.getString(mydatac.getColumnIndex("WEIGHT"));
            mystandardcal = mydatac.getString(mydatac.getColumnIndex("STANDARD_CAL"));
        }
        else
        {

        }

        /// TODAY_DATA 레코드 생성

        String select = String.format("select DATE from TODAY_DATA where DATE = '%s'", time);
        String todaydataquery = "";

        c = db.rawQuery(select, null);

        if(c.moveToFirst())
        {


        }else
        {
            try {
                todaydataquery = String.format("INSERT INTO TODAY_DATA VALUES ('%s', %d, %d );", time, 0, 0);
                db.execSQL(todaydataquery);
            } catch (Exception e) {
            }
        }


        //////////
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);


        fragHealth = HealthFragment.newInstance();
        fragExercise = ExerciseFragment.newInstance();
        fragStatistics = StatisticsFragment.newInstance();
        fragSleepbio = SleepbioFragment.newInstance();


        //////////////////////////

        fragHome = new HomeFragment().newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_activity_main, fragHome).commit();


         fragPedo = new PedometerFragment().newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_fragment_pedometer, fragPedo).commit();


         fragTotalcal = new TotalcalFragment().newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_fragment_totalcal, fragTotalcal).commit();


         fragBio = new BiorhythmFragment().newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_fragment_biorhythm, fragBio).commit();

        db.close();

    }

    // DB가 있나 체크하기
    public boolean isCheckDB(Context mContext){
        String filePath = "/data/data/kr.co.company.dailyhealth/databases/Dailyhealth.db";
        File file = new File(filePath);

        if (file.exists()) {
            return true;
        }

        return false;

    }
    // DB를 복사하기
    // assets의 /xxxx.db 파일을 설치된 프로그램의 내부 DB공간으로 복사하기
    public void copyDB(Context mContext){
        Log.d("MiniApp", "copyDB");
        AssetManager manager = mContext.getAssets();
        String folderPath = "/data/data/kr.co.company.dailyhealth/databases/";
        String filePath ="/data/data/kr.co.company.dailyhealth/databases/Dailyhealth.db";
        File folder = new File(folderPath);
        File file = new File(filePath);

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            InputStream is = manager.open("Dailyhealth.db");
            BufferedInputStream bis = new BufferedInputStream(is);

            if (folder.exists()) {
            }else{
                folder.mkdirs();
            }


            if (file.exists()) {
                file.delete();
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            int read = -1;
            byte[] buffer = new byte[1024];
            while ((read = bis.read(buffer, 0, 1024)) != -1) {
                bos.write(buffer, 0, read);
            }

            bos.flush();

            bos.close();
            fos.close();
            bis.close();
            is.close();

        } catch (IOException e) {
            Log.e("ErrorMessage : ", e.getMessage());
        }
    }



    //메뉴 설정
    public void onClick(View headerView){
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {


            fragHome = new HomeFragment().newInstance();


            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_activity_main, fragHome).commit();

            fragPedo = new PedometerFragment().newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_fragment_pedometer, fragPedo).commit();


            fragTotalcal = new TotalcalFragment().newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_fragment_totalcal, fragTotalcal).commit();


            fragBio = new BiorhythmFragment().newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_fragment_biorhythm, fragBio).commit();


        } else if (id == R.id.nav_health) {

            fragHealth = HealthFragment.newInstance();


            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_activity_main, fragHealth).commit();

        } else if (id == R.id.nav_exercise) {

            fragExercise = ExerciseFragment.newInstance();


            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_activity_main, fragExercise).commit();


        } else if (id == R.id.nav_statistics) {

            fragStatistics = StatisticsFragment.newInstance();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_activity_main, fragStatistics).commit();


        } else if (id == R.id.nav_sleepbio) {

            fragSleepbio = SleepbioFragment.newInstance();



            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_activity_main, fragSleepbio).commit();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





}
