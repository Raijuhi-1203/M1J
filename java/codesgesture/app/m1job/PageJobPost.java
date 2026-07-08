package codesgesture.app.m1job;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import codesgesture.app.m1job.Services.CallJson;
import codesgesture.app.m1job.Services.JsonCallbacks;
import codesgesture.app.m1job.Services.NetParam;
import codesgesture.app.m1job.Services.UserUtil;
import codesgesture.app.m1job.Services.Utility;

public class PageJobPost extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Context context;
    EditText mob1,mob2,mob3;
    Spinner spnrcat;
    String spnrid;

    TextView filename;
    String base64;
    Button btnpost,choosefile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_post);
        context=this;
        requestMultiplePermissions();
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View vw=navigationView.getHeaderView(0);

        btnpost=findViewById(R.id.btnpost);
        choosefile=findViewById(R.id.choosefile);
        filename=findViewById(R.id.filename);
        mob1=findViewById(R.id.mob1);
        mob2=findViewById(R.id.mob2);
        mob3=findViewById(R.id.mob3);
        spnrcat=findViewById(R.id.spnrcat);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrcat.setAdapter(adapter);

        spnrcat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(spnrcat.getSelectedItem().toString().equals("देश में नौकरी")){
                    spnrid ="1";
                }else if(spnrcat.getSelectedItem().toString().equals("विदेश में नौकरी")){
                    spnrid = "2";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        btnpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mob1.getText().length()==0){
                    mob1.setError("Please enter mobile no.");
                }else {
                    JobPost();
                }
            }
        });

        choosefile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });

    }

    private void JobPost() {
        ArrayList<NetParam> param;
        param = new ArrayList<NetParam>();
        CallJson jc = new CallJson((Activity) context);
        param.add(new NetParam("mobile1",mob1.getText().toString()));
        param.add(new NetParam("mobile2",mob2.getText().toString()));
        param.add(new NetParam("mobile3",mob3.getText().toString()));
        param.add(new NetParam("jobcat",spnrid));
        param.add(new NetParam("base64",base64));
        param.add(new NetParam("filenm",filename.getText().toString()));
        jc.SendRequest("post_job", param, new JsonCallbacks() {
            @Override
            public void onPostSuceess(String json, String method) throws JSONException {
                UserUtil.ShowMsg("Thank you! Job is posting succesfully after approval seeing this !!",context);
                finish();
            }
            @Override
            public void onPostError(String msg) {
            }
        }, " ", "Loading..");
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
            startActivity(new Intent(PageJobPost.this, DashBoard.class));
        } else if (id == R.id.nav_rate) {
            rateApp();
        }else if (id == R.id.nav_share) {
            UserUtil.ShareApp(this);
        }else if (id == R.id.nav_developer) {
            startActivity(new Intent(PageJobPost.this,PageDeveloper.class));
        }else if (id == R.id.nav_about) {
            startActivity(new Intent(PageJobPost.this,PageAbout.class));
        }else if (id == R.id.nav_term) {
            startActivity(new Intent(PageJobPost.this,PageTerm.class));
        }else if (id == R.id.nav_privacy) {
            startActivity(new Intent(PageJobPost.this,PagePrivacy.class));
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

    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String fileName = getFileName(uri);
            try {
                base64 = Base64.encodeToString(getBytesFromUri(uri, context), Base64.DEFAULT);
                String cls=spnrid;
                filename.setText(Utility.ImageName(cls));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public static byte[] getBytesFromUri(Uri uri, Context context) throws IOException {
        InputStream iStream = context.getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = iStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }



}