package noyansoft.ecssistem;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationChannel;
import android.provider.Settings.Secure;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.provider.FontRequest;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.FontRequestEmojiCompatConfig;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity{
    SharedPreferences sharedPref;
    ConnectionClass connectionClass;
    EditText edtuserid,edtpass;
    Button btnlogin,btnset;
    ConstraintLayout cons;
    ProgressBar pbbar;
    Snackbar tost;
    TextView txtwelcome;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    public static String VERCODE = "vercode";
    public static String frcvc = "";
    Boolean isFirstRun = true;
    String vername = BuildConfig.VERSION_NAME;
    String user;
    public static Boolean updater = false;
    Boolean dsave,con,betab,hvaccount;
    Random r = new Random();
    int net;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            sharedPref = MainActivity.this.getSharedPreferences("SQLPref", MODE_PRIVATE);
            dsave = sharedPref.getBoolean("datasaver", false);
            net = sharedPref.getInt("netpref", 1);
            betab = sharedPref.getBoolean("betatester", false);
            isFirstRun = sharedPref.getBoolean("first_run",true);
            if (betab) {
                VERCODE = "vercode_beta";
            } else {
                VERCODE = "vercode";
            }
            if(isFirstRun) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("first_run", false);
                editor.commit();
            }
            if (!dsave) {
                NetworkCheck();
                if (con) {
                    Updater(true);
                }
            }
            connectionClass = new ConnectionClass();
            cons = (ConstraintLayout) findViewById(R.id.maincon);
            int i = r.nextInt(3 - 0) + 0;
            switch (i) {
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

        edtuserid = (EditText) findViewById(R.id.edtuserid);
        edtpass = (EditText) findViewById(R.id.edtpass);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        btnset = (Button) findViewById(R.id.serv);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        txtwelcome = (TextView) findViewById(R.id.welcometxt);
            String deviceId = Secure.getString(this.getContentResolver(),
                    Secure.ANDROID_ID);
            if (deviceId.equals("43acae81bc6b1209")) {
                Alpha();
            }
        try{
            AccountManager acm = AccountManager.get(MainActivity.this);
            Account[] ac = acm.getAccountsByType("noyansoft.ecssistem.account");
                if(!ac[0].name.equals(null)){
                user = ac[0].name;}
            if (ac.length > 0) {
                hvaccount = true;
                edtuserid.setVisibility(View.GONE);
                edtpass.setVisibility(View.GONE);
                txtwelcome.setVisibility(View.VISIBLE);
                txtwelcome.setText(getString(R.string.welcome_message) + user + "!");
            } else {
                hvaccount = false;
                edtuserid.setVisibility(View.VISIBLE);
                edtpass.setVisibility(View.VISIBLE);
                txtwelcome.setVisibility(View.GONE);
            }
        }
        catch(Exception e){
            String m = e.getMessage();
        }
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtuserid.getText().toString().equals("a") &
                        edtpass.getText().toString().equals("a")) {
                    AccountManager accountManager = AccountManager.get(MainActivity.this);
                    Account account = new Account(edtuserid.getText().toString(),"noyansoft.ecssistem.account");
                    boolean success = accountManager.addAccountExplicitly(account,edtpass.getText().toString(),null);
                    if(success){
                        Log.d(TAG,"Account created");
                    }else{
                        Log.d(TAG,"Account creation failed. Look at previous logs to investigate");
                    }
                    Snackbar.make(btnset, "Giriş yapıldı.", Snackbar.LENGTH_SHORT);
                    Intent i1 = new Intent(MainActivity.this, ActSecim.class);
                    startActivity(i1);
                } else {
                    if(hvaccount){
                        Intent i = new Intent(MainActivity.this,ActSecim.class);
                        startActivity(i);
                    }
                    else {
                        DoLogin doLogin = new DoLogin();
                        doLogin.execute("");
                        Log.e("Kayıt1", connectionClass.db);
                    }
                }
            }
        });
        btnset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SetMain.class);
                startActivity(i);
            }
        });
        btnset.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(), "ECS Sistem v" + vername + " by Başar Noyan", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                if (count == 1) {
                    Intent i2 = new Intent(MainActivity.this, ActSecim.class);
                    startActivity(i2);
                }
            }
        });
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }
    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }
    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }
    public class DoLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;


        String userid = edtuserid.getText().toString();
        String password = edtpass.getText().toString();


        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
            btnlogin.setText("");
        }
        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            btnlogin.setText(getText(R.string.login));
            if (tost != null) {
                tost.dismiss();
            }
            tost = Snackbar.make(btnlogin,r,Snackbar.LENGTH_SHORT);
            tost.show();
            if(isSuccess) {
                Intent i = new Intent(MainActivity.this,ActSecim.class);
                startActivity(i);
            }
        }
        @Override
        protected String doInBackground(String... params) {
            if(userid.trim().equals("")|| password.trim().equals(""))
                z = "Lütfen kullanıcı adı ve parola giriniz.";
            else
            {
                try {
                    Connection con = connectionClass.CONN(MainActivity.this);
                    if (con == null) {
                        z = "SQL sunucuya bağlanılamadı.";

                    } else {
                        String query = "select * from Usertb where UserId='" + userid + "' and password='" + password + "'";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);

                        if(rs.next())
                        {

                            z = "Giriş yapıldı.";
                            isSuccess=true;
                        }
                        else
                        {
                            z = "Kullanıcı bulunamadı.";
                            isSuccess = false;
                        }
                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = "Kullanıcı bulunamadı.";
                }
            }
            return z;
        }
    }
    public void Updater(final boolean notif_e){
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setDefaults(R.xml.remotecd);
        frcvc = mFirebaseRemoteConfig.getString(VERCODE);
        mFirebaseRemoteConfig.fetch(0)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mFirebaseRemoteConfig.activateFetched();
                            if(!frcvc.equals("1.3.5")){
                            updater = !frcvc.equals(vername);
                            if(notif_e){
                            Notif();}
                            else{DownloadFileFromDrive();}}
                        } else {
                        }
                    }
                });
    }
    public void PermissionControl(){
        int vercode = Integer.valueOf(android.os.Build.VERSION.SDK);
        if(vercode<22){
            int permission = PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
            if (permission == PermissionChecker.PERMISSION_GRANTED) {
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setMessage("Lütfen ayarlardan uygulama izinlerini kontrol ediniz.")
                        .setTitle("Uyarı");
                AlertDialog dialog = builder.create();
            }
        }
        else{
            int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            1);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.CAMERA},
                                1);}
                }
                return;
            }
        }
    }
    public void Notif(){
        if(updater) {
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(MainActivity.this)
                            .setSmallIcon(R.mipmap.checkupdate)
                            .setContentText("ECS Sistem v"+frcvc+" güncellemesi mevcut.")
                            .setSound(soundUri)
                            .setPriority(Notification.PRIORITY_MIN)
                            .setLights(Color.BLUE,300,100);
            int vercode = Integer.valueOf(android.os.Build.VERSION.SDK);
            if(vercode>22){
                mBuilder.setColor(getColor(R.color.colorPrimaryDark));}
            if(vercode<25){
                mBuilder.setContentTitle("Uygulama Güncellemesi");
            }else{
                mBuilder.setSubText("Uygulama Güncellemesi");
            }
            mBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;
            Intent resultIntent = new Intent(Intent.ACTION_VIEW);
            if(betab){
                resultIntent.setData(Uri.parse("https://drive.google.com/uc?export=download&id=0ByRQempmFVB9MFdDTlFLMG9jcmM"));
            }else {
                resultIntent.setData(Uri.parse("https://drive.google.com/uc?export=download&id=0ByRQempmFVB9blRzQ213S1VfSFE"));
            }
            PendingIntent pending = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pending);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(001, mBuilder.build());
            updater=true;
        }else{}
    }
    public void NetworkCheck(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean ismobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .isConnectedOrConnecting();
        boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();
        switch (net){
            case 0:
                if(!isWifi) {
                    con = false;
                }else{con = true;}
                break;
            case 1:
                if(!isWifi&!ismobile){
                    con = false;
                }else{con = true;}
                break;
        }
    }
    public void DownloadFileFromDrive(){
        if(betab){
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://drive.google.com/uc?export=download&id=0ByRQempmFVB9MFdDTlFLMG9jcmM")));
        }else{
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://drive.google.com/uc?export=download&id=0ByRQempmFVB9blRzQ213S1VfSFE")));
        }
    }
    public void Alpha(){
        int vercode = Integer.valueOf(android.os.Build.VERSION.SDK);
        if(vercode<26) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.logo3_n2)
                            .setContentText("Tap to developer settings.🤔")
                            .setPriority(Notification.PRIORITY_MIN)
                            .setShowWhen(false);
            Intent notificationIntent = new Intent(this, DevSettings.class);
            PendingIntent intent = PendingIntent.getActivity(this, 0,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (vercode > 22) {
                mBuilder.setColor(getColor(R.color.colorPrimaryDark));
            }
            if (vercode < 25) {
                mBuilder.setContentTitle("Developer Mode 😜");
            } else {
                mBuilder.setSubText("Developer Mode 😀");
                mBuilder.setContentTitle("Using developer device");
            }
            mBuilder.setContentIntent(intent);
            mBuilder.build().flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
            mBuilder.setOngoing(true);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(002, mBuilder.build());
        }
        else{
            channel();
        }
    }
    @TargetApi(26)
    public void NotifChan() {
        NotificationChannel nchannel =
                new NotificationChannel(
                        "notifchannel",
                        "Bildirimler",
                        NotificationManager.IMPORTANCE_DEFAULT);
        nchannel.setLightColor(Color.BLUE);
        nchannel.setVibrationPattern(new long[]{100, 200, 300, 400});
        NotificationManager notifman =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notifman.createNotificationChannel(nchannel);
    }
    @TargetApi(26)
    public Notification.Builder channel() {
        Intent resultIntent = new Intent(Intent.ACTION_VIEW);
        if(betab){
            resultIntent.setData(Uri.parse("https://drive.google.com/uc?export=download&id=0ByRQempmFVB9MFdDTlFLMG9jcmM"));
        }else {
            resultIntent.setData(Uri.parse("https://drive.google.com/uc?export=download&id=0ByRQempmFVB9blRzQ213S1VfSFE"));
        }
        PendingIntent pending = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, channel().build());
        updater=true;
        return new Notification.Builder(getApplicationContext(),"notifchannel")
                .setSmallIcon(R.mipmap.checkupdate)
                .setContentText("ECS Sistem v"+frcvc+" güncellemesi mevcut.")
                .setContentTitle("Uygulama Güncellemesi")
                .setAutoCancel(true)
                .setContentIntent(pending)
                .setCategory("Bildirim");
    }
}

