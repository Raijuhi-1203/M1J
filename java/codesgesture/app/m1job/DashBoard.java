package codesgesture.app.m1job;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import codesgesture.app.m1job.Model.AdsModel;
import codesgesture.app.m1job.Services.CallJson;
import codesgesture.app.m1job.Services.JsonCallbacks;
import codesgesture.app.m1job.Services.NetParam;
import codesgesture.app.m1job.Services.UserUtil;
import codesgesture.app.m1job.Utils.CallJsonWithoutProgress;
import codesgesture.app.m1job.Utils.Constants;

public class DashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    LinearLayout videsh,desh,jobpost;
    SliderLayout slider;
//    ViewPager viewpager;
    ArrayList<AdsModel> adsModels=new ArrayList<>();
    final int PERMISSION_REQUEST_CODE =112;

//    private static final String JSON_URL = "https://m1j.codiee.co.in/M1JWebService.asmx/get_slider";
//    private RequestQueue mRequestQueue;
//    private StringRequest mStringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        FirebaseMessaging.getInstance().subscribeToTopic("jobpost_alert");

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View vw=navigationView.getHeaderView(0);

        if (Build.VERSION.SDK_INT > 32) {
            if (!shouldShowRequestPermissionRationale("112")){
                getNotificationPermission();
            }
        }

        slider=findViewById(R.id.slider);
//        viewpager=findViewById(R.id.viewpager);

        videsh=findViewById(R.id.videsh);
        jobpost=findViewById(R.id.jobpost);
        desh=findViewById(R.id.desh);

        videsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DashBoard.this,PageJob.class);
                intent.putExtra("id","2");
                startActivity(intent);
            }
        });
        desh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DashBoard.this,PageJob.class);
                intent.putExtra("id","1");
                startActivity(intent);
            }
        });
        jobpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DashBoard.this,PageJobPost.class);
                intent.putExtra("id","1");
                startActivity(intent);
            }
        });

      //  sendAndRequestResponse();
        FetchBanner();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
            else {
                // repeat the permission or open app details
            }
        }
        
    }


//    private void sendAndRequestResponse() {
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            response = response.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>"," ");
//                            response = response.replace("<string xmlns=\"http://tempuri.org/\">"," ");
//                            response = response.replace("</string>"," ");
//                            response = response.replace("["," ");
//                            response = response.replace("]"," ");
//                            response = response.replace("\r\n"," ");
//                            JSONObject finalparent = new JSONObject();
//                            finalparent.put("data", response);
//
//                            JSONObject obj = new JSONObject(finalparent.toString());
//                            adsModels = new ArrayList<>();
//                            JSONArray dataArray  = obj.getJSONArray("data");
//                            for (int i = 0; i < dataArray.length(); i++) {
//                                AdsModel adsModel = new AdsModel();
//                                JSONObject dataobj = dataArray.getJSONObject(i);
//                                String spath = dataobj.getString("slider_path");
//                                adsModel.setSlider(spath);
//                                adsModels.add(adsModel);
//
//                                DefaultSliderView defaultSliderView = new DefaultSliderView(DashBoard.this);
//                                defaultSliderView.image(Constants.BASEURI2+adsModel.getSlider_path());
//                                slider.addSlider(defaultSliderView);
//                                slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//                                slider.setCustomAnimation(new DescriptionAnimation());
//                                slider.setDuration(5000);
//
//                            }
//
//
//
//
////                            JSONArray array = new JSONArray(response);
////                            for (int i = 0; i < array.length(); i++) {
////                                JSONObject heroObject = array.getJSONObject(i);
////                                AdsModel hero = new AdsModel(heroObject.getString("slider_path"));
////                                adsModels.add(hero);
////
////                                DefaultSliderView defaultSliderView = new DefaultSliderView(DashBoard.this);
////                                defaultSliderView.image(Constants.BASEURI2+hero.getSlider_path());
////                                slider.addSlider(defaultSliderView);
////                                slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
////                                slider.setCustomAnimation(new DescriptionAnimation());
////                                slider.setDuration(5000);
////
////                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        //displaying the error in toast if occurrs
//                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//        //creating a request queue
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        //adding the string request to request queue
//        requestQueue.add(stringRequest);
//    }

    private void FetchBanner() {
        adsModels.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJsonWithoutProgress jc = new CallJsonWithoutProgress(DashBoard.this);
        jc.SendRequest("get_slider", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                if (json.length() > 0) {
                    JSONArray array = new JSONArray(json);
                    for (int s = 0; s < array.length(); s++) {
                        JSONObject obj = array.getJSONObject(s);
                        AdsModel model = new AdsModel();
                        model.setSlider(obj.getString("slider"));
                        model.setSlider_path(obj.getString("slider_path"));
                        adsModels.add(model);

                        DefaultSliderView defaultSliderView = new DefaultSliderView(DashBoard.this);
                        defaultSliderView.image(Constants.BASEURI2+model.getSlider_path());
                        defaultSliderView.setScaleType(BaseSliderView.ScaleType.FitCenterCrop);
                       // defaultSliderView.setScaleType(BaseSliderView.ScaleType.CenterCrop);
                        slider.addSlider(defaultSliderView);
                        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        slider.setCustomAnimation(new DescriptionAnimation());
                        slider.setDuration(5000);

                    }
                }
            }

            @Override
            public void onPostError(String msg) {
            }
        }, "", "Wait Loading...");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(DashBoard.this, DashBoard.class));
        }  else if (id == R.id.nav_rate) {
            rateApp();
        }else if (id == R.id.nav_share) {
            UserUtil.ShareApp(this);
        }else if (id == R.id.nav_developer) {
            startActivity(new Intent(DashBoard.this,PageDeveloper.class));
        }else if (id == R.id.nav_about) {
            startActivity(new Intent(DashBoard.this,PageAbout.class));
        }else if (id == R.id.nav_term) {
            startActivity(new Intent(DashBoard.this,PageTerm.class));
        }else if (id == R.id.nav_privacy) {
            startActivity(new Intent(DashBoard.this,PagePrivacy.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void rateApp() {
        try
        {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 26)
        {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        }
        else
        {
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        Intent intent = new Intent(DashBoard.this, DashBoard.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        finish();
    }

    public void getNotificationPermission(){
        try {
            if (Build.VERSION.SDK_INT > 32) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE);
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // allow

                }  else {
                    //deny
                }
                return;

        }

    }

}