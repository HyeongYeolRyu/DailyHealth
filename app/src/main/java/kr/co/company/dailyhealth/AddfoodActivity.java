package kr.co.company.dailyhealth;


import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class AddfoodActivity extends AppCompatActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfood);
    }
    Intent intent = new Intent();
    ComponentName componentName = new ComponentName(
            "kr.co.company.dailyhealth",
            "kr.co.company.dailyhealth.FoodListActivity");


    public void inputDirectFood(View v){   //한식

        startActivity(new Intent(this, InputDirectFoodActivity.class));
    }

    public void onClick1(View v){   //한식

        intent.setComponent(componentName);

        Bundle bundleData = new Bundle();
        bundleData.putString("FOOD_TYPE","korean");

        intent.putExtra("TYPE_DATA", bundleData);
        startActivity(intent);
    }

    public void onClick2(View v){   //양식

        intent.setComponent(componentName);

        Bundle bundleData = new Bundle();
        bundleData.putString("FOOD_TYPE","western");

        intent.putExtra("TYPE_DATA", bundleData);
        startActivity(intent);
    }

    public void onClick3(View v){   //일식
        intent.setComponent(componentName);

        Bundle bundleData = new Bundle();
        bundleData.putString("FOOD_TYPE","japanese");

        intent.putExtra("TYPE_DATA", bundleData);
        startActivity(intent);
    }

    public void onClick4(View v){   //중식
        intent.setComponent(componentName);

        Bundle bundleData = new Bundle();
        bundleData.putString("FOOD_TYPE","chinese");

        intent.putExtra("TYPE_DATA", bundleData);
        startActivity(intent);
    }

    public void onClick5(View v){   //패스트푸드
        intent.setComponent(componentName);

        Bundle bundleData = new Bundle();
        bundleData.putString("FOOD_TYPE","fastfood");

        intent.putExtra("TYPE_DATA", bundleData);
        startActivity(intent);
    }

    public void onClick6(View v){   //빵
        intent.setComponent(componentName);

        Bundle bundleData = new Bundle();
        bundleData.putString("FOOD_TYPE","bread");

        intent.putExtra("TYPE_DATA", bundleData);
        startActivity(intent);
    }

    public void onClick7(View v){   //과일/견과류
        intent.setComponent(componentName);

        Bundle bundleData = new Bundle();
        bundleData.putString("FOOD_TYPE","fruit");

        intent.putExtra("TYPE_DATA", bundleData);
        startActivity(intent);
    }

    public void onClick8(View v){   //음료
        intent.setComponent(componentName);

        Bundle bundleData = new Bundle();
        bundleData.putString("FOOD_TYPE","drink");

        intent.putExtra("TYPE_DATA", bundleData);
        startActivity(intent);
    }

}