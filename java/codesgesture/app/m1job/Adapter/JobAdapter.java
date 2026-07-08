package codesgesture.app.m1job.Adapter;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import codesgesture.app.m1job.BuildConfig;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import codesgesture.app.m1job.JobApply;
import codesgesture.app.m1job.Model.JobModel;
import codesgesture.app.m1job.R;
import codesgesture.app.m1job.Services.UserUtil;
import codesgesture.app.m1job.Utils.Constants;


public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {
    private ArrayList<JobModel> arrayList;
    private Context context;
    private int layout;

    public JobAdapter(Context context, ArrayList<JobModel> arrayList, int layout) {
        this.arrayList = arrayList;
        this.context = context;
        this.layout=layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {
        final JobModel data = arrayList.get(i);

        Uri uri = Uri.parse(Constants.BASEURI2+data.getAd_img());
        Glide.with(context).load(uri).into(holder.adimg);

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable bitmapDrawable=(BitmapDrawable) holder.adimg.getDrawable();
                Bitmap bitmap=bitmapDrawable.getBitmap();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                Uri uri1;
                String text ="नौकरी के लिए डाउनलोड करें M1J app" + "\n https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
                uri1=saveImage(bitmap,context.getApplicationContext());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_TEXT,text);
                intent.putExtra(Intent.EXTRA_STREAM,uri1);
                context.startActivity(Intent.createChooser(intent,"share image"));
            }
        });

        holder.num1.setText(data.getContactno());
        holder.num2.setText(data.getMobile2());
        holder.num3.setText(data.getMobile3());

        holder.call1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String contact = data.getContactno();
                OpenCallDialog(context,contact);
               // UserUtil.CallDialog(context,contact);
            }
        });

        holder.call2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String contact = data.getMobile2();
                OpenCallDialog(context,contact);
                // UserUtil.CallDialog(context,contact);
            }
        });

        holder.call3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String contact = data.getMobile3();
                OpenCallDialog(context,contact);
                // UserUtil.CallDialog(context,contact);
            }
        });

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                downloadImageNew(data.getAd_img(),Constants.BASEURI2+data.getAd_img());

              //  new DownloadImage().execute(uri);
            }
        });

        holder.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable bitmapDrawable=(BitmapDrawable) holder.adimg.getDrawable();
                Bitmap bitmap=bitmapDrawable.getBitmap();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setPackage("com.whatsapp");
                intent.setType("image/*");
                Uri uri1;
                String text ="नौकरी के लिए डाउनलोड करें M1J app" + "\n https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
                uri1=saveImage(bitmap,context.getApplicationContext());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_TEXT,text);
                intent.putExtra(Intent.EXTRA_STREAM,uri1);

                try {
                    context.startActivity(intent);
                } catch (android.content.ActivityNotFoundException ex) {
                    UserUtil.ShowMsg("Whatsapp have not been installed.",context);
                }

            }
        });

        holder.btnapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, JobApply.class);
                intent.putExtra("data",data);
                context.startActivity(intent);
            }
        });
    }

    private void OpenCallDialog(Context context,String contact) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_call);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button btcall = (Button) dialog.findViewById(R.id.btcall);
        Button btcancel = (Button) dialog.findViewById(R.id.btcancel);
        TextView txnumber=dialog.findViewById(R.id.txnumber);
        txnumber.setText("Dial : +91-"+contact);

        btcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + contact.toString().trim()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                context.startActivity(callIntent);
            }
        });
        btcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private Uri saveImage(Bitmap bitmap, Context applicationContext) {
        File file=new File(context.getCacheDir(),"images");
        Uri uri=null;
        try{
            file.mkdir();
            File file1=new File(file,"M1J.jpg");
            FileOutputStream outputStream=new FileOutputStream(file1);
            bitmap.compress(Bitmap.CompressFormat.JPEG,90,outputStream);
            outputStream.flush();
            outputStream.close();
            uri= FileProvider.getUriForFile(Objects.requireNonNull(context.getApplicationContext()), "codesgesture.app.m1job"+".provider",file1);
        }catch (IOException e){

            Log.d("TAG","Exception"+e.getMessage());
        }
return  uri;
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView adimg;
        LinearLayout share,call1,call2,call3,download,whatsapp;
        LinearLayout click2;
        Button btnapply;

        TextView num1,num2,num3;
        ViewHolder(View view) {
            super(view);

            share = view.findViewById(R.id.share);
            adimg = view.findViewById(R.id.adimg);
            call1 = view.findViewById(R.id.call1);
            call2 = view.findViewById(R.id.call2);
            call3 = view.findViewById(R.id.call3);

            num1 = view.findViewById(R.id.num1);
            num2 = view.findViewById(R.id.num2);
            num3 = view.findViewById(R.id.num3);

            click2 = view.findViewById(R.id.click2);
            whatsapp = view.findViewById(R.id.whatsapp);
            download = view.findViewById(R.id.download);
            btnapply = view.findViewById(R.id.btnapply);
        }
    }

    public void updateList(ArrayList<JobModel> list) {
        arrayList = list;
        notifyDataSetChanged();
    }

    private void downloadImageNew(String filename, String downloadUrlOfImage){

        String[] parts = filename.split("/Jobad/");
        String part1=parts[0];
        String part2=parts[1];
        String[] extension = part2.split("[.]");
        String filenm= extension[0];
        String extnsn = extension[1];


        try{
            DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(downloadUrlOfImage);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(part2)
                    .setMimeType(extnsn) // Your file type. You can use this code to download other file types also.
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,File.separator + filename + ".jpg");
            dm.enqueue(request);
            Toast.makeText(context, "Image download started.", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}