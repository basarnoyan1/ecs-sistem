package noyansoft.ecssistem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class SetMain extends AppCompatActivity {
    Random r = new Random();
    Button server,updatepref,devpref,update;
    MainActivity ma;
    SharedPreferences sharedPref;
    Boolean dsave,ismobile,isWifi,con,betab;
    Boolean hvupd = false;
    int net;
    ProgressBar pb;
    TextView txtup;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    public static String VERCODE = "vercode";
    public static String frcvc = "";
    String vername = BuildConfig.VERSION_NAME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_main);
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolset);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        ConstraintLayout cons = (ConstraintLayout) findViewById(R.id.conset);
        int i = r.nextInt(3-0)+0;
        switch (i){
            case 0:
                cons.setBackgroundResource(R.drawable.wall);
                break;
            case 1:
                cons.setBackgroundResource(R.drawable.wall2);
                break;
            case 2:
                cons.setBackgroundResource(R.drawable.wall3);
                break;
            default:
                cons.setBackgroundResource(R.drawable.wall);
                break;
        }
        sharedPref = this.getSharedPreferences("SQLPref", MODE_PRIVATE);
        net = sharedPref.getInt("netpref",1);
        betab = sharedPref.getBoolean("betatester",false);
        if(betab){
            VERCODE = "vercode_beta";
        }else{
            VERCODE = "vercode";
        }
        ma = new MainActivity();
        server = (Button) findViewById(R.id.serverset);
        pb = (ProgressBar) findViewById(R.id.pbupdate);
        devpref = (Button) findViewById(R.id.devset);
        updatepref = (Button) findViewById(R.id.updateset);
        update = (Button) findViewById(R.id.update);
        txtup = (TextView) findViewById(R.id.txtupdate);
        EarlyCheck();
        server.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent in = new Intent(SetMain.this, Settings.class);
                startActivity(in);
            }
        });
        devpref.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent in = new Intent(SetMain.this, DevSettings.class);
                startActivity(in);
            }
        });
        updatepref.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent in = new Intent(SetMain.this, UpdateSet.class);
                startActivity(in);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dsave = sharedPref.getBoolean("datasaver",false);
                if(dsave){
                    NetworkCheck();
                    if (con){
                    reload(true);
                    UpdaterDataSave();}}
                                else {
                    if (ma.updater) {
                        NetworkCheck();
                        if (con){
                            if(!hvupd){
                        reload(true);}
                        AlertDialog.Builder builder = new AlertDialog.Builder(SetMain.this);
                        builder.setTitle("Yeni güncelleme mevcut");
                        builder.setMessage("Şu an ECS Sistem v" + ma.vername + " sürümünü kullanıyorsunuz . Bu güncelleme ECS Sistem v"
                                + ma.frcvc + " sürümünü yükleyecektir." +
                                " Yüklemek ister misiniz?");
                        builder.setNegativeButton("İPTAL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                EarlyCheck();
                            }
                        });
                        builder.setPositiveButton("YÜKLE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DownloadFileFromDrive();
                            }
                        });
                        builder.show();
                        txtup.setText("Güncelleme mevcut");
                        update.setBackgroundResource(R.mipmap.update2);
                        update.setVisibility(View.VISIBLE);
                        pb.setVisibility(View.GONE);
                        hvupd=true;}

                    } else {
                        NetworkCheck();
                        if (con){
                            if(!hvupd){
                        reload(true);}
                        UpdaterDataSave();}
                    }
                }
            }
        });
    }
    public void UpdaterDataSave(){
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setDefaults(R.xml.remotecd);
        frcvc = mFirebaseRemoteConfig.getString(VERCODE);
        mFirebaseRemoteConfig.fetch(0).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mFirebaseRemoteConfig.activateFetched();
                    if(!frcvc.equals(vername)){
                    if (!frcvc.equals("1.3.5")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SetMain.this);
                        builder.setTitle("Yeni güncelleme mevcut");
                        builder.setMessage("Şu an ECS Sistem v" + vername + " sürümünü kullanıyorsunuz ." +
                                " Bu güncelleme ECS Sistem v" + frcvc + " sürümünü yükleyecektir." +
                                " Yüklemek ister misiniz?");
                        builder.setNegativeButton("İPTAL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                        builder.setPositiveButton("YÜKLE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DownloadFileFromDrive();
                            }
                        });
                        builder.show();
                        hvupd = true;
                        txtup.setText("Güncelleme mevcut");
                        update.setBackgroundResource(R.mipmap.update2);
                        update.setVisibility(View.VISIBLE);
                        pb.setVisibility(View.GONE);
                    } else {
                        reload(false);
                    }}else {
                        reload(false);
                    }
                }   else {
                    reload(false);}
            }
        });
    }
    public void reload(Boolean check){
        if(check){
            txtup.setText("Güncellemeler denetleniyor...");
            pb.setVisibility(View.VISIBLE);
            update.setVisibility(View.INVISIBLE);
        }
       else {
            txtup.setText("Uygulamanız güncel");
            pb.setVisibility(View.GONE);
            update.setVisibility(View.VISIBLE);
            update.setBackgroundResource(R.mipmap.reload);
        }
    }
    public void DownloadFileFromDrive(){
        txtup.setText("Yükleniyor...");
        pb.setVisibility(View.VISIBLE);
        update.setVisibility(View.INVISIBLE);
        if(betab){
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://drive.google.com/uc?export=download&id=0ByRQempmFVB9MFdDTlFLMG9jcmM")));
        }
        else {
            startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://drive.google.com/uc?export=download&id=0ByRQempmFVB9blRzQ213S1VfSFE")));
        }
    }
    public void NetworkCheck(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        ismobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .isConnectedOrConnecting();
        isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();
        switch (net){
            case 0:
               if(!isWifi) {
                   txtup.setText("Kablosuz bağlantı yok");
                   con = false;
               }else{con = true;}
                break;
            case 1:
                if(!isWifi&!ismobile){
                    txtup.setText("İnternet bağlantısı yok");
                    con = false;
                }else{con = true;}
                break;
        }
    }
    public void EarlyCheck(){
        if(ma.updater){
            txtup.setText("Güncelleme mevcut");
            update.setBackgroundResource(R.mipmap.update2);
            hvupd = true;
        }
        else {
            txtup.setText("Uygulamanız güncel");
            update.setBackgroundResource(R.mipmap.reload);
            NetworkCheck();
        }
    }
}
