package kr.co.company.dailyhealth;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import static kr.co.company.dailyhealth.MainActivity.mybirth;

public class BiorhythmFragment extends Fragment {

    public static BiorhythmFragment newInstance() {
        BiorhythmFragment fragment = new BiorhythmFragment();
        return fragment;
    }

    public BiorhythmFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_biorhythm,container,false);


        ///////////////////////////////////////////////////


        SetBiorhythm(view);

        ///////////////////////////////////////////////////
        return view;
    }

    public void SetBiorhythm(View view){
        Calendar birth = Calendar.getInstance();

        Log.d("스트링 확인", mybirth );

        int year = Integer.parseInt(mybirth.substring(0, 4));
        int month = Integer.parseInt(mybirth.substring(5, 7));
        int date = Integer.parseInt(mybirth.substring(8, 10));

//        int year = 1995;
//        int month = 12;
//        int date = 21;


        birth.set(year, month-1, date);
        Calendar now = Calendar.getInstance();

        long past = birth.getTime().getTime();
        long today = now.getTime().getTime();
        long day = (today - past)/(24*60*60*1000);

        double body = Math.sin((2 * 3.14  * day) / 23) * 100;
        double sense = Math.sin((2 * 3.14  * day) / 28) * 100;
        double brain = Math.sin((2 * 3.14  * day) / 33) * 100;

        double body2 = Double.parseDouble(String.format("%.2f",body));
        double sense2 = Double.parseDouble(String.format("%.2f",sense));
        double brain2 = Double.parseDouble(String.format("%.2f",brain));

        TextView tvbody = (TextView) view.findViewById(R.id.bioBody);
        tvbody.setText("신체 지수 : " + body2);
        TextView tvsense = (TextView) view.findViewById(R.id.bioSense);
        tvsense.setText("감성 지수 : " + sense2);
        TextView tvbrain = (TextView) view.findViewById(R.id.bioBrain);
        tvbrain.setText("지성 지수 : " + brain2);

        tvbody.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        tvsense.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        tvbrain.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);

        double maxValue = Math.max(Math.max(body2,sense2), brain2);

        TextView tvBioText = (TextView) view.findViewById(R.id.bioText);

        ImageView ivBio = (ImageView) view.findViewById(R.id.bioImage);

        if (Double.compare(maxValue,body2) == 0) //신체 지수가 가장 클 때
        {
            tvBioText.setText("오늘은 운동하기 좋은 날!");
            tvbody.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19);
            ivBio.setBackgroundResource(R.drawable.exercise);
        }
        else
            tvbody.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);

        if (Double.compare(maxValue,sense2) == 0) //감정 지수가 가장 클 때
        {
            tvBioText.setText("오늘은 달달한 영화라도 보아요!");
            tvsense.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19);
            ivBio.setBackgroundResource(R.drawable.heart);
        }
        else
            tvsense.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);

        if (Double.compare(maxValue,brain2) == 0) //지성 지수가 가장 클 때
        {
            tvBioText.setText("오늘은 책 읽기 좋은 날이네요!");
            tvbrain.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19);
            ivBio.setBackgroundResource(R.drawable.book);
        }
        else
            tvbrain.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);

    }

}