package codesgesture.app.m1job;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import codesgesture.app.m1job.Model.AdsModel;
import codesgesture.app.m1job.Services.CallJson;
import codesgesture.app.m1job.Services.JsonCallbacks;
import codesgesture.app.m1job.Services.NetParam;
import codesgesture.app.m1job.Utils.Constants;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  FirebaseMessaging.getInstance().subscribeToTopic("jobpost_alert");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, DashBoard.class);
                startActivity(i);
                finish();
            }
        }, 5000);



//        Button clickbt=findViewById(R.id.clickbt);
//        clickbt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this,DashBoard.class));
//            }
//        });
    }


}