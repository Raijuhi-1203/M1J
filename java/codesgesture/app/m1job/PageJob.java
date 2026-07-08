package codesgesture.app.m1job;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import codesgesture.app.m1job.Adapter.JobAdapter;
import codesgesture.app.m1job.Model.JobModel;
import codesgesture.app.m1job.Services.CallJson;
import codesgesture.app.m1job.Services.JsonCallbacks;
import codesgesture.app.m1job.Services.NetParam;
import codesgesture.app.m1job.Services.UserUtil;

public class PageJob extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    RecyclerView recyclerview;
    LinearLayout norecord;
    JobAdapter jobAdapter;
    ArrayList<JobModel> jobModels=new ArrayList<>();
    private static final int PERMISSION_REQUEST_CODE = 200;
    String id ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jobs);
        id=getIntent().getStringExtra("id");

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View vw=navigationView.getHeaderView(0);

        recyclerview=findViewById(R.id.recyclerview);
        norecord=findViewById(R.id.norecord);





        GridLayoutManager mLayoutManager = new GridLayoutManager(PageJob.this, 1);
        recyclerview.setLayoutManager(mLayoutManager);
        jobAdapter = new JobAdapter(PageJob.this, jobModels, R.layout.item_job);
        recyclerview.setAdapter(jobAdapter);
        recyclerview.setItemViewCacheSize(jobModels.size());
//        recyclerview.smoothScrollToPosition(jobAdapter.getItemCount());
        BindData();
        callAtRuntime();

    }

    private void callAtRuntime() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

                && checkSelfPermission(Manifest.permission.CALL_PHONE)

                != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);

        }

        else {

        }
    }

    private void BindData() {
        jobModels.clear();
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson(PageJob.this);
        param.add(new NetParam("jobcat",id));
        jc.SendRequest("get_job", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                JSONArray array = new JSONArray(json);
                for (int s = 0; s < array.length(); s++) {
                    JSONObject obj = array.getJSONObject(s);
                    JobModel product = new JobModel();
                    product.setId(obj.getString("id"));
                    product.setAd_img(obj.getString("ad_img"));
                    product.setContactno(obj.getString("contactno"));
                    product.setMobile2(obj.getString("mobile2"));
                    product.setMobile3(obj.getString("mobile3"));
                    product.setJobcat(obj.getString("jobcat"));
                    jobModels.add(product);
                }
                jobAdapter.notifyDataSetChanged();
                BindDataView();
            }

            @Override
            public void onPostError(String msg) {
                BindDataView();
            }
        }, "", "Loading..");
    }

    private void BindDataView() {
        if(jobModels.size()>0){
            norecord.setVisibility(View.GONE);
        }
        else{
            norecord.setVisibility(View.VISIBLE);
        }
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
            startActivity(new Intent(PageJob.this, DashBoard.class));
        } else if (id == R.id.nav_rate) {
            rateApp();
        }else if (id == R.id.nav_share) {
            UserUtil.ShareApp(this);
        }else if (id == R.id.nav_developer) {
            startActivity(new Intent(PageJob.this,PageDeveloper.class));
        }else if (id == R.id.nav_about) {
            startActivity(new Intent(PageJob.this,PageAbout.class));
        }else if (id == R.id.nav_term) {
            startActivity(new Intent(PageJob.this,PageTerm.class));
        }else if (id == R.id.nav_privacy) {
            startActivity(new Intent(PageJob.this,PagePrivacy.class));
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

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callAtRuntime();
            } else {
                UserUtil.ShowMsg("Call Permission Denied. Try Again!",PageJob.this);
            }

        }

    }
}
